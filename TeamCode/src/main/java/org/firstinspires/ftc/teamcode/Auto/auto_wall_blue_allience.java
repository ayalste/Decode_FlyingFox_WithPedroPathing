package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Auto_Wall_Blue_Timer_Everybot", group = "FTC")
public class auto_wall_blue_allience extends LinearOpMode {

    DcMotor leftFrontDrive;
    DcMotor leftBackDrive;
    DcMotor rightFrontDrive;
    DcMotor rightBackDrive;

    @Override
    public void runOpMode() {

        leftFrontDrive  = hardwareMap.get(DcMotor.class, "left_front_drive");
        leftBackDrive   = hardwareMap.get(DcMotor.class, "left_back_drive");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
        rightBackDrive  = hardwareMap.get(DcMotor.class, "right_back_drive");

        // בדיוק כמו ב-TeleOp
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

        waitForStart();

        // 1. קדימה ~255 ס"מ
        drive(0.7, 0.0, 3100);

        // 2. סיבוב ~52 מעלות
        drive(0.0, 0.60, 450);

        // 3. קדימה ~86 ס"מ
        drive(0.6, 0.0, 1945);

        stopAll();
    }

    /**
     * תנועה לפי המודל של TeleOp
     * speed = קדימה/אחורה
     * turn  = סיבוב
     */
    void drive(double speed, double turn, long timeMs) {

        double lf = speed + turn;
        double rf = speed - turn;
        double lb = speed - turn;
        double rb = speed + turn;

        leftFrontDrive.setPower(lf);
        rightFrontDrive.setPower(rf);
        leftBackDrive.setPower(lb);
        rightBackDrive.setPower(rb);

        sleep(timeMs);
        stopAll();
    }

    void stopAll() {
        leftFrontDrive.setPower(0);
        rightFrontDrive.setPower(0);
        leftBackDrive.setPower(0);
        rightBackDrive.setPower(0);
        sleep(150);
    }
}
