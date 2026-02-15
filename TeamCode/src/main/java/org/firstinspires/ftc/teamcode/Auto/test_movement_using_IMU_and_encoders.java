package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;


@Autonomous(name = "Auto Everybot IMU", group = "Auto")
public class test_movement_using_IMU_and_encoders extends LinearOpMode {

    private DcMotor leftFrontDrive;
    private DcMotor leftBackDrive;
    private DcMotor rightFrontDrive;
    private DcMotor rightBackDrive;

    IMU imu;

    static final double TICKS_PER_REV = 537.7;
    static final double WHEEL_DIAMETER_CM = 9.6;

    static final double TICKS_PER_CM =
            TICKS_PER_REV / (Math.PI * WHEEL_DIAMETER_CM);

    static final double DRIVE_POWER = 0.4;
    static final double TURN_KP = 0.008;


    @Override
    public void runOpMode() {

        leftFrontDrive = hardwareMap.get(DcMotor.class, "left_front_drive");
        leftBackDrive = hardwareMap.get(DcMotor.class, "left_back_drive");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
        rightBackDrive = hardwareMap.get(DcMotor.class, "right_back_drive");

        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);


        imu = hardwareMap.get(IMU.class, "imu");

        RevHubOrientationOnRobot orientation =
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
                );

        imu.initialize(new IMU.Parameters(orientation));

        resetEncoders();

        waitForStart();

        imu.resetYaw();


        driveCM(250);

        turnToAngle(50);

//        driveCM(-100);
//
//        turnToAngle(-70);


        stopMotors();
    }


    // DRIVE USING ENCODERS


    void driveCM(double cm) {

        int ticks = (int)(cm * TICKS_PER_CM);

        leftFrontDrive.setTargetPosition(leftFrontDrive.getCurrentPosition() + ticks);
        rightFrontDrive.setTargetPosition(rightFrontDrive.getCurrentPosition() + ticks);
        leftBackDrive.setTargetPosition(leftBackDrive.getCurrentPosition() + ticks);
        rightBackDrive.setTargetPosition(rightBackDrive.getCurrentPosition() + ticks);


        setRunToPosition();


        driveRobot(DRIVE_POWER,0,0);


        while(opModeIsActive() &&
                (leftFrontDrive.isBusy())) {

            telemetry.addData("Driving","Running");
            telemetry.update();
        }

        stopMotors();

        setRunUsingEncoder();

        sleep(200);
    }



    // TURN USING SAME LOGIC AS TELEOP


    void turnToAngle(double targetAngle) {

        setRunWithoutEncoder();

        while(opModeIsActive()) {

            double yaw = getYaw();

            double error = yaw - targetAngle;


            // Normalize error to -180 to +180
            error = (error + 180) % 360 - 180;

            if(Math.abs(error) < 2) break;   // דיוק יותר טוב

            double turn = error * TURN_KP;

            turn = Math.max(-0.4, Math.min(0.4, turn));

            driveRobot(0, 0, turn);

            telemetry.addData("Target", targetAngle);
            telemetry.addData("Yaw", yaw);
            telemetry.addData("Error", error);
            telemetry.update();
        }

        stopMotors();
        sleep(200);
    }




    // SAME DRIVE FUNCTION AS TELEOP


    void driveRobot(double speed, double turn, double rotation) {

        leftFrontDrive.setPower(speed + turn + rotation);

        rightFrontDrive.setPower(speed - turn - rotation);

        leftBackDrive.setPower(speed - turn + rotation);

        rightBackDrive.setPower(speed + turn - rotation);

        YawPitchRollAngles angles =
                imu.getRobotYawPitchRollAngles();

        double yaw = angles.getYaw(AngleUnit.DEGREES);
        double pitch = angles.getPitch(AngleUnit.DEGREES);
        double roll = angles.getRoll(AngleUnit.DEGREES);

        telemetry.addData("Yaw", yaw);
        telemetry.addData("Pitch", pitch);
        telemetry.addData("Roll", roll);

        telemetry.update();

    }



    double getYaw() {

        YawPitchRollAngles angles =
                imu.getRobotYawPitchRollAngles();

        return angles.getYaw(AngleUnit.DEGREES);
    }



    void stopMotors(){

        driveRobot(0,0,0);

    }



    void resetEncoders(){

        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        setRunUsingEncoder();

    }



    void setRunToPosition(){

        leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }


    void setRunUsingEncoder(){

        leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }


    void setRunWithoutEncoder(){

        leftFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

}
