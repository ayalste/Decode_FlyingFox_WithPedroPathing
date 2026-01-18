package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "SimpleAuto_NoIMU WALL ", group = "FTC")
public class auto_wall_blue_allience extends LinearOpMode {

    DcMotor fl, fr, bl, br;
    DcMotor motorA, motorB;

    static final double TICKS_PER_CM = 17.82;
    static final int TURN_52_TICKS = 306; // אפשר לכייל

    @Override
    public void runOpMode() {

        fl = hardwareMap.dcMotor.get("fl");
        fr = hardwareMap.dcMotor.get("fr");
        bl = hardwareMap.dcMotor.get("bl");
        br = hardwareMap.dcMotor.get("br");

        motorA = hardwareMap.dcMotor.get("motorA");
        motorB = hardwareMap.dcMotor.get("motorB");

        fr.setDirection(DcMotor.Direction.REVERSE);
        br.setDirection(DcMotor.Direction.REVERSE);

        resetEncoders();

        waitForStart();

        // 1. קדימה 255 ס"מ
        driveStraight(255, 0.5);

        // 2. סיבוב 52 מעלות (בלי IMU)
        turnInPlace(TURN_52_TICKS, 0.35);

        // 3. קדימה 86 ס"מ
        driveStraight(86, 0.4);

        // 4. הפעלת שני מנועים
        motorA.setPower(1);
        motorB.setPower(1);
        sleep(1500);
        motorA.setPower(0);
        motorB.setPower(0);
    }

    void driveStraight(double cm, double power) {
        int ticks = (int) (cm * TICKS_PER_CM);

        setTarget(ticks, ticks, ticks, ticks);
        runToPosition(power);
    }

    void turnInPlace(int ticks, double power) {
        setTarget(ticks, -ticks, ticks, -ticks);
        runToPosition(power);
    }

    void setTarget(int flT, int frT, int blT, int brT) {
        fl.setTargetPosition(fl.getCurrentPosition() + flT);
        fr.setTargetPosition(fr.getCurrentPosition() + frT);
        bl.setTargetPosition(bl.getCurrentPosition() + blT);
        br.setTargetPosition(br.getCurrentPosition() + brT);
    }

    void runToPosition(double power) {
        fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        br.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        fl.setPower(power);
        fr.setPower(power);
        bl.setPower(power);
        br.setPower(power);

        while (opModeIsActive() &&
                (fl.isBusy() || fr.isBusy() || bl.isBusy() || br.isBusy())) {
            idle();
        }

        stopAll();
        resetEncoders();
    }

    void resetEncoders() {
        fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    void stopAll() {
        fl.setPower(0);
        fr.setPower(0);
        bl.setPower(0);
        br.setPower(0);
    }
}
