package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@Autonomous(name = "TEST_ENCODER_IMU_FIXED", group = "TEST")
public class test_enco_red_wall extends LinearOpMode {

    DcMotor lf, lb, rf, rb;
    IMU imu;

    static final double TICKS_PER_REV = 537.7;
    static final double WHEEL_DIAMETER_CM = 9.6;
    static final double TICKS_PER_CM =
            TICKS_PER_REV / (Math.PI * WHEEL_DIAMETER_CM);

    static final double DRIVE_POWER = 0.4;
    static final double TURN_KP = 0.01;

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

        RevHubOrientationOnRobot orientation =
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
                );

        imu.initialize(new IMU.Parameters(orientation));

        resetEncoders();
        setRunUsingEncoder();

        telemetry.addLine("READY – encoders + IMU test");
        telemetry.update();

        waitForStart();

        /* =====================
           1️⃣ DRIVE 1 METER
        ===================== */
        driveStraightCM(100);

        sleep(500);

        /* =====================
           2️⃣ TURN LEFT 45°
        ===================== */
//        turnLeft45();
        imu.resetYaw();

        while (opModeIsActive()){
            setDrivePower(-0.2, 0.2);
            double yaw = getHeading();
            telemetry.addData("Yaw", yaw);
            telemetry.update();
        }



//        stopDrive();
    }

    /* =====================
       DRIVE STRAIGHT – ENCODER ONLY
    ===================== */
    void driveStraightCM(double cm) {

        int targetTicks = (int)(cm * TICKS_PER_CM);
        int startTicks = lf.getCurrentPosition();

        while (opModeIsActive() &&
                Math.abs(lf.getCurrentPosition() - startTicks) < targetTicks) {

            setDrivePower(DRIVE_POWER, DRIVE_POWER);

            telemetry.addData("LF", lf.getCurrentPosition());
            telemetry.addData("RF", rf.getCurrentPosition());
            telemetry.update();
        }

        stopDrive();
    }

    /* =====================
       TURN LEFT 45° – IMU RELATIVE
    ===================== */
    void turnLeft45() {

        imu.resetYaw(); // ⭐ קריטי
        double target = 45;

        while (opModeIsActive()) {

            double yaw = getHeading();
            double error = target - yaw;

            if (Math.abs(error) < 1.0) break;

            double power = error * TURN_KP;
            power = Math.max(0.15, Math.min(0.4, power));

            setDrivePower(-power, power);

            telemetry.addData("Yaw", yaw);
            telemetry.addData("Error", error);
            telemetry.update();
        }

        stopDrive();
    }

    /* =====================
       HELPERS
    ===================== */
    void resetEncoders() {
        lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    void setRunUsingEncoder() {
        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    void setDrivePower(double left, double right) {
        lf.setPower(left);
        lb.setPower(left);
        rf.setPower(right);
        rb.setPower(right);
    }

    void stopDrive() {
        setDrivePower(0, 0);
        sleep(200);
    }

    double getHeading() {
        YawPitchRollAngles a = imu.getRobotYawPitchRollAngles();
        return a.getYaw(AngleUnit.DEGREES);
    }
}
