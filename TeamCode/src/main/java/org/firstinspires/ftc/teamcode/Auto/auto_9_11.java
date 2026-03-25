package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
@Autonomous(name = "Competition Auto Final", group = "Auto")
public class auto_9_11 extends LinearOpMode {

    DcMotor lf, lb, rf, rb;
    DcMotor X_DeadWheel, Y_DeadWheel;
    IMU imu;

    static final double TICKS_PER_CM = 35.6;
    static final double HEADING_KP = 0.02;

    @Override
    public void runOpMode() {

        // Drive motors
        lf = hardwareMap.get(DcMotor.class, "left_front_drive");
        lb = hardwareMap.get(DcMotor.class, "left_back_drive");
        rf = hardwareMap.get(DcMotor.class, "right_front_drive");
        rb = hardwareMap.get(DcMotor.class, "right_back_drive");

        lf.setDirection(DcMotor.Direction.REVERSE);
        lb.setDirection(DcMotor.Direction.REVERSE);

        // Dead wheels
        X_DeadWheel = hardwareMap.get(DcMotor.class, "CL_DWX");
        Y_DeadWheel = hardwareMap.get(DcMotor.class, "CR_DWY");

        // IMU
        imu = hardwareMap.get(IMU.class, "imu");
        imu.initialize(new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
                )
        ));

        waitForStart();

        imu.resetYaw();

        driveForwardCM(100, 0.5);
        strafeLeftCM(100, 0.5);
        turnLeft(45, 0.4);
        driveForwardCM(50, 0.5);

        stopDrive();
    }

    // ================= FORWARD WITH HEADING HOLD =================

    void driveForwardCM(double cm, double power) {

        resetY();
        double target = cm * TICKS_PER_CM;

        while(opModeIsActive()) {

            double current = Math.abs(Y_DeadWheel.getCurrentPosition());
            if(current >= Math.abs(target)) break;

            double headingError = -getYaw();
            double correction = headingError * HEADING_KP;

            lf.setPower(power - correction);
            lb.setPower(power - correction);
            rf.setPower(power + correction);
            rb.setPower(power + correction);
        }

        stopDrive();
        sleep(200);
    }

    // ================= STRAFE LEFT =================

    void strafeLeftCM(double cm, double power) {

        resetX();
        double target = cm * TICKS_PER_CM;

        while(opModeIsActive()) {

            double current = Math.abs(X_DeadWheel.getCurrentPosition());
            if(current >= Math.abs(target)) break;

            lf.setPower(-power);
            rf.setPower(power);
            lb.setPower(power);
            rb.setPower(-power);
        }

        stopDrive();
        sleep(200);
    }

    // ================= TURN LEFT =================

    void turnLeft(double degrees, double power) {

        imu.resetYaw();
        sleep(100);

        while(opModeIsActive()) {

            double yaw = getYaw();
            if(yaw >= degrees - 1) break;

            lf.setPower(-power);
            lb.setPower(-power);
            rf.setPower(power);
            rb.setPower(power);
        }

        stopDrive();
        sleep(200);
    }

    // ================= HELPERS =================

    double getYaw() {
        return imu.getRobotYawPitchRollAngles()
                .getYaw(AngleUnit.DEGREES);
    }

    void stopDrive() {
        lf.setPower(0);
        lb.setPower(0);
        rf.setPower(0);
        rb.setPower(0);
    }

    void resetX() {
        X_DeadWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        X_DeadWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    void resetY() {
        Y_DeadWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Y_DeadWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}