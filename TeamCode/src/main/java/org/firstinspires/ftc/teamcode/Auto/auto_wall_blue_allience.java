package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "auto blue wall", group = "FTC")
public class auto_wall_blue_allience extends LinearOpMode {

    // Drive motors
    DcMotor leftFrontDrive;
    DcMotor leftBackDrive;
    DcMotor rightFrontDrive;
    DcMotor rightBackDrive;

    // Catapult motors
    DcMotor catapult1;
    DcMotor catapult2;

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
           1️⃣ ישר 255 ס״מ
        ===================== */
        drive(0.35, 0, 0);
        sleep(3800);
        stopDrive();

        /* =====================
           2️⃣ סיבוב שמאלה ~52°
        ===================== */
        drive(0, 0.56, 0);   // turn שמאלה
        sleep(1657);         // תכווני אם צריך
        stopDrive();

        /* =====================
           3️⃣ ישר 86 ס״מ
        ===================== */
        drive(0.35, 0, 0);
        sleep(3200);
        stopDrive();

        /* =====================
           4️⃣ יריית קטפולטה
        ===================== */
        fireCatapult();

        stopDrive();
    }

    /* =====================
       DRIVE – אותו חישוב כמו TeleOp
    ===================== */
    void drive(double speed, double turn, double rotation) {
        leftFrontDrive.setPower(speed + turn + rotation);
        rightFrontDrive.setPower(speed - turn - rotation);
        leftBackDrive.setPower(speed - turn + rotation);
        rightBackDrive.setPower(speed + turn - rotation);
    }

    void stopDrive() {
        leftFrontDrive.setPower(0);
        rightFrontDrive.setPower(0);
        leftBackDrive.setPower(0);
        rightBackDrive.setPower(0);
        sleep(100);
    }

    /* =====================
       CATAPULT – כמו TeleOp
    ===================== */
    void fireCatapult() {
        catapult1.setPower(-1.0);
        catapult2.setPower(-1.0);
        sleep(700);          // זמן ירי
        catapult1.setPower(0);
        catapult2.setPower(0);
    }
}
