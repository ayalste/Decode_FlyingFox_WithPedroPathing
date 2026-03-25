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
     DcMotor intakeMotor;

    static final double TICKS_PER_REV = 537.7;
    static final double WHEEL_DIAMETER_CM = 9.5;

    static final double TICKS_PER_CM =
            TICKS_PER_REV / (Math.PI * WHEEL_DIAMETER_CM);

    static final double DRIVE_POWER = 0.6;
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

        // Catapult
        catapult1 = hardwareMap.get(DcMotor.class, "CR_DWY");
        catapult2 = hardwareMap.get(DcMotor.class, "CL_DWX");
        catapult1.setDirection(DcMotor.Direction.REVERSE);
        catapult2.setDirection(DcMotor.Direction.FORWARD);

        // Intake
        intakeMotor = hardwareMap.get(DcMotor.class, "intake_Motor");

        // IMU
        imu = hardwareMap.get(IMU.class, "imu");
        RevHubOrientationOnRobot orientation =
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
                );
        imu.initialize(new IMU.Parameters(orientation));

        waitForStart();

        imu.resetYaw();
        resetEncoders();

        catapult1.setPower(0.2);
        catapult2.setPower(0.2);

        // 1) קדימה 220
        resetEncoders();
        driveCM(220, 0.7);

        // 2) שמאלה 45°
        turnLeftSecond(45, 0.35);

        // 3) שמאלה 112 (סטראף)
        resetEncoders();
        driveCM(112, 0.6);

        // 4) עצירה מוחלטת לשנייה
        stopMotors();
        sleep(1000);

        // 5) ירייה
        shootCatapult();
        catapult1.setPower(0.2);
        catapult2.setPower(0.2);

        sleep(200);
        imu.resetYaw();

        // 6) אחורה 133
        resetEncoders();
        driveCM(-133, 0.6);

        // 7) שמאלה 45°
        turnLeftSecond(45, 0.35);

        // 8) אינטייק פועל ונסיעה 76
        intakeMotor.setPower(-1);
        resetEncoders();
        driveCM(76, 0.6);
        intakeMotor.setPower(0);

        // 9) אחורה 76
        resetEncoders();
        driveCM(-76, 0.6);

        // 10) ימינה 45°
        turnRightSecond(45, 0.35);

        // 11) קדימה 133
        resetEncoders();
        driveCM(133, 0.6);

        // 12) עצירה מוחלטת לשנייה לפני ירייה
        stopMotors();
        sleep(1000);

        // 13) ירייה שנייה
        shootCatapult();

        stopMotors();
    }


    // DRIVE USING ENCODERS


    void driveCM(double cm, double drivingSpeed) {
        resetEncoders();

        int ticks = (int)(cm * TICKS_PER_CM);

        leftFrontDrive.setTargetPosition(leftFrontDrive.getCurrentPosition() + ticks);
        rightFrontDrive.setTargetPosition(rightFrontDrive.getCurrentPosition() + ticks);
        leftBackDrive.setTargetPosition(leftBackDrive.getCurrentPosition() + ticks);
        rightBackDrive.setTargetPosition(rightBackDrive.getCurrentPosition() + ticks);


        setRunToPosition();


        driveRobot(drivingSpeed);


        while(opModeIsActive() &&
                (leftFrontDrive.isBusy())) {

            telemetry.addData("Driving","Running");
            telemetry.update();
        }

        stopMotors();

        setRunUsingEncoder();

        sleep(100);
    }







    void driveRobot(double speed) {

        leftFrontDrive.setPower(speed);

        rightFrontDrive.setPower(speed);

        leftBackDrive.setPower(speed);

        rightBackDrive.setPower(speed);

        YawPitchRollAngles angles =
                imu.getRobotYawPitchRollAngles();

    }




    void turnLeft(double targetAngle, double turningSpeed){
        setRunWithoutEncoder();
//        imu.resetYaw();
        double startYaw = getYaw();
        double targetYaw = startYaw + targetAngle;
        double currentYaw = getYaw();
        double yawDiff = targetYaw - currentYaw;
        double speedMulti = yawDiff / targetAngle;

        while(yawDiff > 0) {
            currentYaw = getYaw();
            yawDiff = startYaw + targetAngle - getYaw();
            speedMulti = yawDiff / targetAngle;

            leftFrontDrive.setPower(-turningSpeed*speedMulti -0.07);
            rightFrontDrive.setPower(turningSpeed*speedMulti+ 0.07);
            leftBackDrive.setPower(-turningSpeed*speedMulti -0.07);
            rightBackDrive.setPower(turningSpeed*speedMulti+ 0.07);

            telemetry.addData("Starting Yaw", startYaw);
            telemetry.addData("Target Yaw", targetYaw);
            telemetry.addData("Current Yaw", currentYaw);
            telemetry.addData("Yaw Diff", yawDiff);
            telemetry.addData("Yaw Diff", speedMulti);
            telemetry.update();
        }

        stopMotors();
        sleep(100);

    }


    void turnRight(double targetAngle, double turningSpeed){
        setRunWithoutEncoder();
//        imu.resetYaw();
        double startYaw = getYaw();
        double targetYaw = startYaw - targetAngle;
        double currentYaw = getYaw();
        double yawDiff = targetYaw - getYaw();
        double speedMulti = yawDiff / Math.abs(targetAngle);

        while(yawDiff < 0) {
            currentYaw = getYaw();
            yawDiff = Math.abs(targetYaw) - getYaw();
            speedMulti = yawDiff / Math.abs(targetAngle);

            leftFrontDrive.setPower(turningSpeed*speedMulti+ 0.07);
            rightFrontDrive.setPower(-turningSpeed*speedMulti- 0.07);
            leftBackDrive.setPower(turningSpeed*speedMulti+ 0.07);
            rightBackDrive.setPower(-turningSpeed*speedMulti- 0.07);

            telemetry.addData("Starting Yaw", startYaw);
            telemetry.addData("Target Yaw", targetYaw);
            telemetry.addData("Current Yaw", currentYaw);
            telemetry.addData("Yaw Diff", yawDiff);
            telemetry.addData("Yaw Diff", speedMulti);
            telemetry.update();
        }

        stopMotors();
        sleep(100);

    }


    //shit test ONLY !!---------------------------------------------------------------->
    //LEFT TEST WITH ANGLE
    void turnLeftSecond(double targetAngle, double turningSpeed){

        if(targetAngle == 0) return;

        imu.resetYaw();
        sleep(80);

        setRunWithoutEncoder();

        while(opModeIsActive()) {

            double currentYaw = getYaw();

            if(currentYaw >= targetAngle - 1)
                break;

            leftFrontDrive.setPower(-turningSpeed);
            rightFrontDrive.setPower(turningSpeed);
            leftBackDrive.setPower(-turningSpeed);
            rightBackDrive.setPower(turningSpeed);
        }

        stopMotors();
        sleep(120);
    }
    //RIGHT TEST WITH ANGLE
    void turnRightSecond(double targetAngle, double turningSpeed){

        if(targetAngle == 0) return;
        imu.resetYaw();
        sleep(80);


        setRunWithoutEncoder();

        double startYaw = getYaw();
        double targetYaw = startYaw - targetAngle;

        while(opModeIsActive()) {

            double currentYaw = getYaw();
            double yawDiff = targetYaw - currentYaw;

            if(yawDiff >= 0) break;   // עצירה כשהגענו או עברנו

            double speedMulti = Math.abs(yawDiff) / targetAngle;

            // הגבלת תחום 0–1
            speedMulti = Math.max(0.0, Math.min(1.0, speedMulti));

            double power = turningSpeed * speedMulti;

            // מינימום כוח שלא ייתקע
            if(power < 0.07)
                power = 0.07;

            leftFrontDrive.setPower(power);
            rightFrontDrive.setPower(-power);
            leftBackDrive.setPower(power);
            rightBackDrive.setPower(-power);

            telemetry.addData("Starting Yaw", startYaw);
            telemetry.addData("Target Yaw", targetYaw);
            telemetry.addData("Current Yaw", currentYaw);
            telemetry.addData("Yaw Diff", yawDiff);
            telemetry.addData("Speed Multi", speedMulti);
            telemetry.update();
        }

        stopMotors();
        sleep(100);
    }
    // DRIVE STRIGHT NO ANGLE LEFT /RIGHT (RIGHT +) (LEFT -)
    void strafeCM(double cm, double speed) {

        int ticks = (int)(cm * TICKS_PER_CM);

        leftFrontDrive.setTargetPosition(leftFrontDrive.getCurrentPosition() - ticks);
        rightFrontDrive.setTargetPosition(rightFrontDrive.getCurrentPosition() + ticks);
        leftBackDrive.setTargetPosition(leftBackDrive.getCurrentPosition() + ticks);
        rightBackDrive.setTargetPosition(rightBackDrive.getCurrentPosition() - ticks);

        setRunToPosition();

        leftFrontDrive.setPower(speed);
        rightFrontDrive.setPower(speed);
        leftBackDrive.setPower(speed);
        rightBackDrive.setPower(speed);

        while(opModeIsActive() && leftFrontDrive.isBusy()) {
            telemetry.addData("Strafing", "Running");
            telemetry.update();
        }

        stopMotors();
        setRunUsingEncoder();
    }
    //shit test ONLY !!---------------------------------------------------------------->
    void turnLeftSecond_2(double targetAngle, double turningSpeed){

        if(targetAngle == 0) return;

        imu.resetYaw();          // 🔥 איפוס YAW לפני פנייה
        sleep(80);

        setRunWithoutEncoder();

        while(opModeIsActive()) {

            double currentYaw = getYaw();

            if(currentYaw >= targetAngle - 0.5)
                break;

            leftFrontDrive.setPower(-turningSpeed);
            rightFrontDrive.setPower(turningSpeed);
            leftBackDrive.setPower(-turningSpeed);
            rightBackDrive.setPower(turningSpeed);

            telemetry.addData("Yaw", currentYaw);
            telemetry.update();
        }

        stopMotors();
        sleep(120);
    }
    void turnRightSecond_2(double targetAngle, double turningSpeed){

        if(targetAngle == 0) return;

        imu.resetYaw();          // 🔥 איפוס YAW לפני פנייה
        sleep(80);

        setRunWithoutEncoder();

        while(opModeIsActive()) {

            double currentYaw = getYaw();

            if(currentYaw <= -targetAngle + 0.5)
                break;

            leftFrontDrive.setPower(turningSpeed);
            rightFrontDrive.setPower(-turningSpeed);
            leftBackDrive.setPower(turningSpeed);
            rightBackDrive.setPower(-turningSpeed);

            telemetry.addData("Yaw", currentYaw);
            telemetry.update();
        }

        stopMotors();
        sleep(120);
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
