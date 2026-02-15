package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot.LogoFacingDirection;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot.UsbFacingDirection;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@TeleOp(name = "IMU_ONLY_TEST_FINAL", group = "TEST")
public class test_imu extends LinearOpMode {

    DcMotor lf, lb, rf, rb;
    IMU imu;

    @Override
    public void runOpMode() {

        lf = hardwareMap.get(DcMotor.class, "left_front_drive");
        lb = hardwareMap.get(DcMotor.class, "left_back_drive");
        rf = hardwareMap.get(DcMotor.class, "right_front_drive");
        rb = hardwareMap.get(DcMotor.class, "right_back_drive");

        lf.setDirection(DcMotor.Direction.REVERSE);
        lb.setDirection(DcMotor.Direction.REVERSE);
        rf.setDirection(DcMotor.Direction.FORWARD);
        rb.setDirection(DcMotor.Direction.FORWARD);

        imu = hardwareMap.get(IMU.class, "imu");

        /* =====================
           IMU ORIENTATION – MATCHES YOUR DRAWING
           Hub upside down, logo facing DOWN,
           USB facing BACK of robot
        ===================== */
        RevHubOrientationOnRobot orientation =
                new RevHubOrientationOnRobot(
                        LogoFacingDirection.BACKWARD,
                        UsbFacingDirection.UP
                );

        imu.initialize(new IMU.Parameters(orientation));

        telemetry.addLine("IMU READY");
        telemetry.addLine("Left stick X = rotate robot");
        telemetry.addLine("A = reset yaw");
        telemetry.update();

        waitForStart();

        imu.resetYaw(); // ⭐ תמיד אחרי START

        while (opModeIsActive()) {

            double turn = gamepad1.left_stick_x * 0.3;

            lf.setPower(-turn);
            lb.setPower(-turn);
            rf.setPower(turn);
            rb.setPower(turn);

            YawPitchRollAngles a = imu.getRobotYawPitchRollAngles();

            telemetry.addData("Yaw", a.getYaw(AngleUnit.DEGREES));
            telemetry.addData("Pitch", a.getPitch(AngleUnit.DEGREES));
            telemetry.addData("Roll", a.getRoll(AngleUnit.DEGREES));
            telemetry.update();

            if (gamepad1.a) {
                imu.resetYaw();
            }
        }
    }
}
