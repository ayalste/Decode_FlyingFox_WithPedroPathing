package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@Autonomous(name = "Forward1m_Turn53", group = "Test")
public class test_enco_red_wall extends LinearOpMode {

    IMU imu;

    DcMotor leftFront, leftBack, rightFront, rightBack;

    // התאמה לגלגל 96mm
    static final double COUNTS_PER_REV = 537.6; // goBilda 312rpm
    static final double WHEEL_DIAMETER_CM = 9.6;
    static final double COUNTS_PER_CM =
            COUNTS_PER_REV / (Math.PI * WHEEL_DIAMETER_CM);

    @Override
    public void runOpMode() {

        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");

        imu = hardwareMap.get(IMU.class, "imu");

        RevHubOrientationOnRobot orientation =
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.FORWARD);

        imu.initialize(new IMU.Parameters(orientation));

        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addLine("Ready");
        telemetry.update();

        waitForStart();

        imu.resetYaw();

// 🚗 נוסע ישר 100 ס"מ
        driveForwardCM(100, 0.4);

        sleep(300);

// 🔄 סיבוב שמאלה 53 מעלות
        turnLeftToAngle(-53);

        stopMotors();
    }

    // ===== נסיעה ישר =====
    void driveForwardCM(double cm, double power) {

        int move = (int)(cm * COUNTS_PER_CM);

        leftFront.setTargetPosition(move);
        leftBack.setTargetPosition(move);
        rightFront.setTargetPosition(move);
        rightBack.setTargetPosition(move);

        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        setPower(power);

        while(opModeIsActive() && leftFront.isBusy()) {
            idle();
        }

        stopMotors();

        setRunUsingEncoder();
    }

    // ===== סיבוב לפי IMU =====
    void turnLeftToAngle(double target) {

        while(opModeIsActive() &&
                imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES) > target) {

            leftFront.setPower(-0.3);
            leftBack.setPower(-0.3);
            rightFront.setPower(0.3);
            rightBack.setPower(0.3);
        }

        stopMotors();
    }

    void setPower(double p) {
        leftFront.setPower(p);
        leftBack.setPower(p);
        rightFront.setPower(p);
        rightBack.setPower(p);
    }

    void stopMotors() {
        setPower(0);
    }

    void setRunUsingEncoder() {
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}