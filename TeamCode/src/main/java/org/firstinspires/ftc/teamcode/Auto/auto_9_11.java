package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@Autonomous(name = "cleannnnnnnnn", group = "Auto")
public class auto_9_11 extends LinearOpMode {

    DcMotor lf, lb, rf, rb;
    DcMotor catapult1, catapult2;
    DcMotor intakeMotor;

    IMU imu;

    static final double TICKS_PER_REV = 537.7;
    static final double WHEEL_DIAMETER_CM = 9.5;

    static final double TICKS_PER_CM =
            TICKS_PER_REV / (Math.PI * WHEEL_DIAMETER_CM);

    @Override
    public void runOpMode() {

        lf = hardwareMap.get(DcMotor.class, "left_front_drive");
        lb = hardwareMap.get(DcMotor.class, "left_back_drive");
        rf = hardwareMap.get(DcMotor.class, "right_front_drive");
        rb = hardwareMap.get(DcMotor.class, "right_back_drive");

        catapult1 = hardwareMap.get(DcMotor.class, "CR_DWY");
        catapult2 = hardwareMap.get(DcMotor.class, "CL_DWX");

        intakeMotor = hardwareMap.get(DcMotor.class, "intake_Motor");

        imu = hardwareMap.get(IMU.class, "imu");

        lf.setDirection(DcMotor.Direction.REVERSE);
        lb.setDirection(DcMotor.Direction.REVERSE);
        rf.setDirection(DcMotor.Direction.FORWARD);
        rb.setDirection(DcMotor.Direction.FORWARD);

        catapult1.setDirection(DcMotor.Direction.REVERSE);
        catapult2.setDirection(DcMotor.Direction.FORWARD);

        resetEncoders();

        waitForStart();

        // 🔥 קטפולט פשוט
        shootCatapult();

        driveBackward(143, 0.7);

        turn(-45, 0.5);

        driveForward(110, 0.7);

        driveBackward(105, 0.7);

        turn(45, 0.5);

        driveForward(130, 0.7);

        shootCatapult();

        driveBackward(220, 1);

        turn(-45, 0.5);

        driveForward(170, 1);

        driveBackward(170, 1);

        stopMotors();
    }

    // 🚗 DRIVE

    void driveForward(double cm, double power){
        int ticks = (int)(cm * TICKS_PER_CM);
        setTarget(ticks, ticks, ticks, ticks);
        runToPosition(power);
    }

    void driveBackward(double cm, double power){
        driveForward(-cm, power);
    }

    // 🔄 TURN WITH IMU

    void turn(double targetAngle, double power){

        imu.resetYaw();

        while(opModeIsActive()){

            YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
            double yaw = Math.toDegrees(orientation.getYaw());

            double error = targetAngle - yaw;

            if(Math.abs(error) < 1.5){
                break;
            }

            double turnPower = power * (error / Math.abs(targetAngle));

            if(Math.abs(turnPower) < 0.1){
                turnPower = 0.1 * Math.signum(turnPower);
            }

            lf.setPower(-turnPower);
            lb.setPower(-turnPower);
            rf.setPower(turnPower);
            rb.setPower(turnPower);
        }

        stopMotors();
        sleep(100);
    }

    // ⚙️ CORE

    void setTarget(int lfTicks, int lbTicks, int rfTicks, int rbTicks){
        lf.setTargetPosition(lf.getCurrentPosition() + lfTicks);
        lb.setTargetPosition(lb.getCurrentPosition() + lbTicks);
        rf.setTargetPosition(rf.getCurrentPosition() + rfTicks);
        rb.setTargetPosition(rb.getCurrentPosition() + rbTicks);
    }

    void runToPosition(double power){

        lf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rb.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        lf.setPower(power);
        lb.setPower(power);
        rf.setPower(power);
        rb.setPower(power);

        while(opModeIsActive() && lf.isBusy()){}

        stopMotors();
        setRunUsingEncoder();
        sleep(200);
    }

    void stopMotors(){
        lf.setPower(0);
        lb.setPower(0);
        rf.setPower(0);
        rb.setPower(0);
    }

    void resetEncoders(){
        lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setRunUsingEncoder();
    }

    void setRunUsingEncoder(){
        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    // 🎯 CATAPULT SIMPLE

    void shootCatapult(){
        catapult1.setPower(-1);
        catapult2.setPower(-1);
        sleep(800);
        catapult1.setPower(0);
        catapult2.setPower(0);
    }
}