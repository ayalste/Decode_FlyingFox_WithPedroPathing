package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Auto blue basket", group = "FTC")
public class auto_basket_blue_allience extends LinearOpMode {

    DcMotor leftFrontDrive, leftBackDrive, rightFrontDrive, rightBackDrive;
    DcMotor catapult1, catapult2;

    @Override
    public void runOpMode() {

        // Drive motors
        leftFrontDrive  = hardwareMap.get(DcMotor.class, "left_front_drive");
        leftBackDrive   = hardwareMap.get(DcMotor.class, "left_back_drive");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
        rightBackDrive  = hardwareMap.get(DcMotor.class, "right_back_drive");

        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

        // Catapult
        catapult1 = hardwareMap.get(DcMotor.class, "CR_DWY");
        catapult2 = hardwareMap.get(DcMotor.class, "CL_DWX");

        catapult1.setDirection(DcMotor.Direction.REVERSE);
        catapult2.setDirection(DcMotor.Direction.FORWARD);

        waitForStart();

        /* =====================
           1️⃣ ירייה – שנייה ראשונה
        ===================== */
        catapult1.setPower(-1.0);
        catapult2.setPower(-1.0);
        sleep(1000);          // שנייה
        catapult1.setPower(0);
        catapult2.setPower(0);

        /* =====================
           2️⃣ סיבוב ימינה ~45°
        ===================== */
        leftFrontDrive.setPower(0.4);
        leftBackDrive.setPower(0.4);
        rightFrontDrive.setPower(-0.4);
        rightBackDrive.setPower(-0.4);
        sleep(750);           // 45° בערך – אפשר לכוון ±50
        stopDrive();

        /* =====================
           3️⃣ נסיעה אחורה ישר
        ===================== */
        leftFrontDrive.setPower(-0.35);
        leftBackDrive.setPower(-0.35);
        rightFrontDrive.setPower(-0.35);
        rightBackDrive.setPower(-0.35);
        sleep(2500);          // אחורה – תכווני לפי המגרש
        stopDrive();
    }

    void stopDrive() {
        leftFrontDrive.setPower(0);
        rightFrontDrive.setPower(0);
        leftBackDrive.setPower(0);
        rightBackDrive.setPower(0);
        sleep(100);
    }
}
