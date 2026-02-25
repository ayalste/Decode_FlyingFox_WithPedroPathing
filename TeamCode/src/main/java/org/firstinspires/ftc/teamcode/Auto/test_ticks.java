package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Autonomous(name="Measure Ticks Per Rev", group="Test")
public class test_ticks extends LinearOpMode {

    @Override
    public void runOpMode() {

        // בחר מנוע לבדיקה
        DcMotorEx testMotor = hardwareMap.get(DcMotorEx.class, "left_front_drive");

        // איפוס אנקודר
        testMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        testMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addLine("Ready to measure ticks.");
        telemetry.addLine("Rotate the wheel EXACTLY one full revolution manually.");
        telemetry.addLine("Press A when done.");
        telemetry.update();

        waitForStart();

        // מחכה ללחיצה על כפתור A
        while(opModeIsActive() && !gamepad1.a) {
            telemetry.addData("Current ticks", testMotor.getCurrentPosition());
            telemetry.update();
        }

        int ticks = testMotor.getCurrentPosition();

        telemetry.addLine("RESULT:");
        telemetry.addData("Ticks per revolution", ticks);
        telemetry.update();

        sleep(10000); // כדי שתראה את התוצאה על המסך
    }
}