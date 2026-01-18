package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Autonomous(name = "Simple Decode Auto", group = "Auto")
public class basic_auto_under_goal extends LinearOpMode {

    DcMotorEx leftFront, leftBack, rightFront, rightBack;

    @Override
    public void runOpMode() {

        // חיבור מנועים
        leftFront  = hardwareMap.get(DcMotorEx.class, "left_front_drive");
        leftBack   = hardwareMap.get(DcMotorEx.class, "left_back_drive");
        rightFront = hardwareMap.get(DcMotorEx.class, "right_front_drive");
        rightBack  = hardwareMap.get(DcMotorEx.class, "right_back_drive");

        // כיוונים - ייתכן שתצטרך להפוך צד אחד
        leftFront.setDirection(DcMotor.Direction.FORWARD);
        leftBack.setDirection(DcMotor.Direction.FORWARD);
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addLine("Ready for START");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        // ========= שלב 1: נסיעה ישר =========
        setAllPower(0.4);
        sleep(1500); // נוסע 1.5 שניות קדימה
        stopAll();

        sleep(300);

        // ========= שלב 2: סיבוב 360 =========
        // צד שמאל קדימה, צד ימין אחורה
        leftFront.setPower(0.4);
        leftBack.setPower(0.4);
        rightFront.setPower(-0.4);
        rightBack.setPower(-0.4);

        sleep(550); // זמן משוער ל-360°, תוכל לכוון
        stopAll();

        // ========= סיום =========
        telemetry.addLine("Auto Finished");
        telemetry.update();

        sleep(10000); // נשאר במקום
    }

    private void setAllPower(double p) {
        leftFront.setPower(p);
        leftBack.setPower(p);
        rightFront.setPower(p);
        rightBack.setPower(p);
    }

    private void stopAll() {
        setAllPower(0);
    }
}
