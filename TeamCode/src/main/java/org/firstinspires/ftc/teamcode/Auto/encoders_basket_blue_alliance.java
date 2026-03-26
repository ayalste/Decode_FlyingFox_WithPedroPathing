package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name = "Encoders Basket Blue", group = "Auto")
public class encoders_basket_blue_alliance extends LinearOpMode {

    DcMotor lf, lb, rf, rb;
    DcMotor catapult1, catapult2;
    DcMotor intakeMotor;
    ColorSensor color1, color2;
    DistanceSensor dist1, dist2;

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

        color1 = hardwareMap.get(ColorSensor.class, "right");
        color2 = hardwareMap.get(ColorSensor.class, "left");

        dist1 = hardwareMap.get(DistanceSensor.class, "right");
        dist2 = hardwareMap.get(DistanceSensor.class, "left");

        lf.setDirection(DcMotor.Direction.REVERSE);
        lb.setDirection(DcMotor.Direction.REVERSE);
        rf.setDirection(DcMotor.Direction.FORWARD);
        rb.setDirection(DcMotor.Direction.FORWARD);

        catapult1.setDirection(DcMotor.Direction.REVERSE);
        catapult2.setDirection(DcMotor.Direction.FORWARD);


        resetEncoders();


        waitForStart();
        holdCatapult();

        sleep(200);

        catapult1.setPower(0.4);
        catapult2.setPower(0.4);

//ירייה ראשונה
        shootCatapult();
        //הולך אחורה
        driveBackward(146, 0.5);
        //עשה סיבוב של כ 45 מעלטת שמאלה
        turn(-37,0.3);

//הפעלת אינטייק
        intakeMotor.setPower(-1);
        //ניסעה קדימה לצורך איסוף כדורים
        driveForward(135, 0.5);
//נסיעה אחורה חזרה
        driveBackward(125, 0.5);
//סיבוב כ45 מעלטת ימינה
        turn(37, 0.3);
        //עצירת אינטייק
        intakeMotor.setPower(0);




//נסיעה קדימה עד לסל לצורך קליעה
        driveForward(150, 0.5);
//סידור כדורים
        intakeMotor.setPower(1);
        sleep(200);
        intakeMotor.setPower(-1);
        sleep(200);
//עצירת אינטייק
        intakeMotor.setPower(0);
        //סליפ לפני זריקה ראשונה
        sleep(1200);
        //זריקת כדורים
        shootCatapult();

//נסיעה אחרוה לצורך איסוף עוד 3 כדורים
        driveBackward(10, 0.5);
//סיבוב כ20 מעלות ימינה
        turn(20, 0.3);
//נסיעה אחרוה של כ150 סמ
        driveBackward(155, 1);
//סיבוב שמאלה כ 60 מעלות
        turn(-50,0.3);
//הפעלת אינטייק
        intakeMotor.setPower(-1);

//נסיעה קדימה כ 85 סמ
        driveForward(94,0.5);
//חזרה מאיסוף של כדורים אחרוה
        driveBackward(99, 1);
//סיבוב של כ 53 מעלות ימינה
        turn(55,0.4);
//נסיעה קדימה 173 סמ קדימה
        driveForward(170,0.9);
        //סידור כדורים
        intakeMotor.setPower(1);
        sleep(200);
        intakeMotor.setPower(-1);
        sleep(200);
//כיבוי אינטייק
        intakeMotor.setPower(0);
        // שינה לפני זריקה
        sleep(1200);
        //ירייה של כדורים
        shootCatapult();




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

        // ירי
        catapult1.setPower(-1);
        catapult2.setPower(-1);
        sleep(700);

        // החזרה למטה
        catapult1.setPower(1);
        catapult2.setPower(1);
        sleep(400);

        // 🔥 חוזר להחזקה קבועה
        holdCatapult();
    }
    void holdCatapult(){
        catapult1.setPower(0.4);
        catapult2.setPower(0.4);
    }
    boolean isGreen(ColorSensor c){
        return c.green() > c.red() && c.green() > c.blue();
    }

    boolean isPurple(ColorSensor c){
        return c.red() > 100 && c.blue() > 100;
    }
    boolean isTargetDetected(){

        double d1 = dist1.getDistance(DistanceUnit.CM);
        double d2 = dist2.getDistance(DistanceUnit.CM);

        boolean close = (d1 < 5 || d2 < 5); // מרחק

        if(!close) return false;

        return isGreen(color1) || isGreen(color2)
                || isPurple(color1) || isPurple(color2);
    }
    void runIntakeIfDetected(){

        if(isTargetDetected()){
            intakeMotor.setPower(-1);

            sleep(5000);

            intakeMotor.setPower(0);
        }
    }
    void intakeShake(){

        intakeMotor.setPower(1);
        sleep(1000);

        intakeMotor.setPower(-1);
        sleep(1000);

        intakeMotor.setPower(0);
    }


}