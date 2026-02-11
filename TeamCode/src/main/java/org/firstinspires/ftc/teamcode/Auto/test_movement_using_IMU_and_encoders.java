package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@Autonomous(name = "Simple Encoder + IMU Auto", group = "Auto")
public class test_movement_using_IMU_and_encoders extends LinearOpMode {

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

        // Motors
        lf = hardwareMap.get(DcMotor.class, "left_front_drive");
        lb = hardwareMap.get(DcMotor.class, "left_back_drive");
        rf = hardwareMap.get(DcMotor.class, "right_front_drive");
        rb = hardwareMap.get(DcMotor.class, "right_back_drive");

        lf.setDirection(DcMotor.Direction.REVERSE);
        lb.setDirection(DcMotor.Direction.REVERSE);
        rf.setDirection(DcMotor.Direction.FORWARD);
        rb.setDirection(DcMotor.Direction.FORWARD);

        lf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Encoders reset
        resetEncoders();

        // IMU
        imu = hardwareMap.get(IMU.class, "imu");

        RevHubOrientationOnRobot orientation =
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
                );

        imu.initialize(new IMU.Parameters(orientation));

        telemetry.addLine("Ready");
        telemetry.update();

        waitForStart();

        imu.resetYaw();

        // 1️⃣ Forward 250 cm
        driveCM(250);

        // 2️⃣ Turn left 50°
        turnToAngle(50);

//        // 3️⃣ Backward 100 cm
//        driveCM(-100);
//
//        // 4️⃣ Turn right 70°
//        turnToAngle(-70);

        stopMotors();
    }

    /* -------------------- DRIVE USING ENCODERS -------------------- */

    void driveCM(double cm) {

        int ticks = (int) (cm * TICKS_PER_CM);

        lf.setTargetPosition(lf.getCurrentPosition() + ticks);
        lb.setTargetPosition(lb.getCurrentPosition() + ticks);
        rf.setTargetPosition(rf.getCurrentPosition() + ticks);
        rb.setTargetPosition(rb.getCurrentPosition() + ticks);

        setRunToPosition();

        setMotorPower(DRIVE_POWER);

        while (opModeIsActive() &&
                (lf.isBusy() || lb.isBusy() || rf.isBusy() || rb.isBusy())) {

            telemetry.addData("Driving (cm)", cm);
            telemetry.update();
        }

        stopMotors();
        setRunUsingEncoder();
        sleep(200);
    }

    /* -------------------- TURN USING IMU -------------------- */

    void turnToAngle(double targetAngle) {

        while (opModeIsActive()) {

            double currentYaw = getYaw();
            double error = targetAngle - currentYaw;

            if (Math.abs(error) < 1.0) break;

            double turnPower = error * TURN_KP;
            turnPower = Math.max(-0.4, Math.min(0.4, turnPower));

            lf.setPower(-turnPower);
            lb.setPower(-turnPower);
            rf.setPower(turnPower);
            rb.setPower(turnPower);

            telemetry.addData("Target", targetAngle);
            telemetry.addData("Yaw", currentYaw);
            telemetry.update();
        }

        stopMotors();
        sleep(200);
    }

    double getYaw() {
        YawPitchRollAngles angles = imu.getRobotYawPitchRollAngles();
        return angles.getYaw(AngleUnit.DEGREES);
    }

    /* -------------------- HELPERS -------------------- */

    void resetEncoders() {
        lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setRunUsingEncoder();
    }

    void setRunToPosition() {
        lf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    void setRunUsingEncoder() {
        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    void setMotorPower(double power) {
        lf.setPower(power);
        lb.setPower(power);
        rf.setPower(power);
        rb.setPower(power);
    }

    void stopMotors() {
        setMotorPower(0);
    }
}
