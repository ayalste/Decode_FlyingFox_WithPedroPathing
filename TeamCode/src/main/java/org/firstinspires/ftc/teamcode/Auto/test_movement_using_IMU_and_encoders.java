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

    DcMotor leftFrontDrive, leftBackDrive, rightFrontDrive, rightBackDrive;
    DcMotor catapult1, catapult2;
    IMU imu;

    static final double TICKS_PER_REV = 537.7;
    static final double WHEEL_DIAMETER_CM = 9.6;

    static final double TICKS_PER_CM =
            TICKS_PER_REV / (Math.PI * WHEEL_DIAMETER_CM);

    static final double DRIVE_POWER = 0.4;
    static final double TURN_KP = 0.01;


    @Override
    public void runOpMode() {

        // Wheels Motors
        leftFrontDrive = hardwareMap.get(DcMotor.class, "left_front_drive");
        leftBackDrive = hardwareMap.get(DcMotor.class, "left_back_drive");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
        rightBackDrive = hardwareMap.get(DcMotor.class, "right_back_drive");

        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

        // Catapult Motors
        catapult1 = hardwareMap.get(DcMotor.class, "catapult_motor1");
        catapult2 = hardwareMap.get(DcMotor.class, "catapult_motor2");

        catapult1.setDirection(DcMotor.Direction.REVERSE);
        catapult2.setDirection(DcMotor.Direction.FORWARD);

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
        sleep(100);


        driveCM(250);

        turnLeft(43, 0.3);

        driveCM(92);

        shootCatapult();


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

        sleep(100);
    }



//    // TURN USING SAME LOGIC AS TELEOP
//
//
//    void turnToAngle(double targetAngle) {
//
//        setRunWithoutEncoder();
//
//        while(opModeIsActive()) {
//
//            double yaw = getYaw();
//
//            double error = targetAngle - yaw;
//
//            if(Math.abs(error) < 10) break;
//
//
//            double turn = error * TURN_KP;
//
//            turn = Math.max(-0.4, Math.min(0.4, turn));
//
//
//            driveRobot(0, 0, turn);
//
//
//            telemetry.addData("Yaw", yaw);
//            telemetry.update();
//
//        }
//
//        stopMotors();
//
//        sleep(100);
//    }



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

    void turnLeft(double targetAngle, double turningSpeed){
        setRunWithoutEncoder();
//        imu.resetYaw();
        double startYaw = getYaw();

        while(getYaw() < startYaw + targetAngle) {
            leftFrontDrive.setPower(-turningSpeed);
            rightFrontDrive.setPower(turningSpeed);
            leftBackDrive.setPower(-turningSpeed);
            rightBackDrive.setPower(turningSpeed);
            telementaryYawUpdate(startYaw,targetAngle);
        }

        stopMotors();
        telementaryYawUpdate(startYaw,targetAngle);
        sleep(100);

    }

    void shootCatapult(){
        catapult1.setPower(-1.0);
        catapult2.setPower(-1.0);
        sleep(1000);
    }



    double getYaw() {

        YawPitchRollAngles angles =
                imu.getRobotYawPitchRollAngles();

        return angles.getYaw(AngleUnit.DEGREES);
    }

    void telementaryYawUpdate(double startYaw, double targetAngle){
        telemetry.addData("Starting Yaw", startYaw);
        telemetry.addData("Target Yaw", startYaw + targetAngle);
        telemetry.addData("Current Yaw", getYaw());
        telemetry.addData("Yaw Diff", startYaw + targetAngle - getYaw());
        telemetry.update();
    }



    void stopMotors(){
        leftFrontDrive.setPower(0);
        rightFrontDrive.setPower(0);
        leftBackDrive.setPower(0);
        rightBackDrive.setPower(0);

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
