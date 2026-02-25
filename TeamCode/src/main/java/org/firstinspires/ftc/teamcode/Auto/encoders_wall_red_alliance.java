package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Encodes Wall Red", group = "Auto")
public class encoders_wall_red_alliance extends LinearOpMode {

    DcMotor lf, lb, rf, rb;
    DcMotor catapult1, catapult2;
    DcMotor intakeMotor;

    // YOUR CONSTANTS
    static final double TICKS_PER_REV = 537.7;
    static final double WHEEL_DIAMETER_CM = 9.5;
    static final double TRACK_WIDTH_CM = 40.64;

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

        lf.setDirection(DcMotor.Direction.REVERSE);
        lb.setDirection(DcMotor.Direction.REVERSE);
        rf.setDirection(DcMotor.Direction.FORWARD);
        rb.setDirection(DcMotor.Direction.FORWARD);

        catapult1.setDirection(DcMotor.Direction.REVERSE);
        catapult2.setDirection(DcMotor.Direction.FORWARD);


        resetEncoders();

        waitForStart();

        sleep(200);

        catapult1.setPower(0.2);
        catapult2.setPower(0.2);


        driveForward(220, 0.7);

        turn(47, 0.4);

        driveForward(112, 0.7);

        shootCatapult();

        driveBackward(133, 0.7);

        turn(35,0.4);

        intakeMotor.setPower(-1);

        driveForward(125, 0.7);

        driveBackward(115, 0.7);

        turn(-43, 0.4);

        driveForward(130, 0.7);

        intakeMotor.setPower(0);

        shootCatapult();

        driveBackward(190,1);

        turn(45,0.4);

        intakeMotor.setPower(-1);

        driveForward(180,1);

        driveBackward(50, 1);

        intakeMotor.setPower(0);



        stopMotors();
    }


    // FORWARD

    void driveForward(double cm, double power){

        int ticks = (int)(cm * TICKS_PER_CM);

        setTarget(ticks, ticks, ticks, ticks);

        runToPosition(power);
    }


    // BACKWARD

    void driveBackward(double cm, double power){

        driveForward(-cm, power);

    }


    // STRAFE RIGHT

    void strafeRight(double cm, double power){

        int ticks = (int)(cm * TICKS_PER_CM);

        setTarget(
                ticks,
                -ticks,
                -ticks,
                ticks
        );

        runToPosition(power);
    }


    // STRAFE LEFT

    void strafeLeft(double cm, double power){

        strafeRight(-cm, power);

    }


    // TURN

    void turn(double angleDeg, double power){

        double turnCircumference = Math.PI * TRACK_WIDTH_CM;

        double distance = turnCircumference * (angleDeg / 360.0) * -1.8;

        int ticks = (int)(distance * TICKS_PER_CM);


        setTarget(
                -ticks,
                -ticks,
                ticks,
                ticks
        );

        runToPosition(power);
    }



    // CORE FUNCTIONS


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


        while(opModeIsActive() && lf.isBusy()){

            telemetry.addData("LF", lf.getCurrentPosition());
            telemetry.update();
        }


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

    void shootCatapult(){
        sleep(1000);
        catapult1.setPower(-1.0);
        catapult2.setPower(-1.0);
        sleep(700);
        catapult1.setPower(1);
        catapult2.setPower(1);
        sleep(300);
        catapult1.setPower(0.2);
        catapult2.setPower(0.2);

    }

}