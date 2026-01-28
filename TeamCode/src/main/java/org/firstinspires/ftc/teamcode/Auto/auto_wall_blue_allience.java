package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Auto_Wall_Blue_Timer_Everybot_SIMPLE", group = "FTC")
public class auto_wall_blue_allience extends LinearOpMode {

    DcMotor leftFrontDrive, leftBackDrive, rightFrontDrive, rightBackDrive;
    DcMotor catapult1, catapult2;

    @Override
    public void runOpMode() {

        // Drive
        leftFrontDrive  = hardwareMap.get(DcMotor.class, "left_front_drive");
        leftBackDrive   = hardwareMap.get(DcMotor.class, "left_back_drive");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
        rightBackDrive  = hardwareMap.get(DcMotor.class, "right_back_drive");

        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

        // Catapult
        catapult1 = hardwareMap.get(DcMotor.class, "catapult_motor1");
        catapult2 = hardwareMap.get(DcMotor.class, "catapult_motor2");

        catapult1.setDirection(DcMotor.Direction.REVERSE);
        catapult2.setDirection(DcMotor.Direction.FORWARD);

        waitForStart();

        /* =====================
           1️⃣ ישר 255 ס"מ
        ===================== */
        driveStraight(0.35, 2864); // אותו זמן שהיה לך

        /* =====================
           2️⃣ סיבוב שמאלה 52°
        ===================== */
        turnLeft(0.3, 850); // זמן ניסוי – תכוונן אם צריך

        /* =====================
           3️⃣ ישר 86 ס"מ
        ===================== */
        driveStraight(0.35, 1945);

        /* =====================
           4️⃣ קאטפולטה בסוף
        ===================== */
        fireCatapult();

        stopAll();
    }

    /* ===================== FUNCTIONS ===================== */

    void driveStraight(double power, long timeMs) {
        leftFrontDrive.setPower(power);
        rightFrontDrive.setPower(power);
        leftBackDrive.setPower(power);
        rightBackDrive.setPower(power);
        sleep(timeMs);
        stopAll();
    }

    void turnLeft(double power, long timeMs) {
        leftFrontDrive.setPower(-power);
        leftBackDrive.setPower(-power);
        rightFrontDrive.setPower(power);
        rightBackDrive.setPower(power);
        sleep(timeMs);
        stopAll();
    }

    void fireCatapult() {
        catapult1.setPower(1.0);
        catapult2.setPower(1.0);
        sleep(700);   // זמן ירי
        catapult1.setPower(0);
        catapult2.setPower(0);
    }

    void stopAll() {
        leftFrontDrive.setPower(0);
        rightFrontDrive.setPower(0);
        leftBackDrive.setPower(0);
        rightBackDrive.setPower(0);
        sleep(100);
    }
}
