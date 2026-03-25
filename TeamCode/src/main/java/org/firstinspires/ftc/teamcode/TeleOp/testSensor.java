package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name = "Ball Detection Calibration", group = "Test")
public class testSensor extends LinearOpMode {

    ColorSensor color1, color2;
    DistanceSensor dist1, dist2;

    @Override
    public void runOpMode() {

        color1 = hardwareMap.get(ColorSensor.class, "color1");
        color2 = hardwareMap.get(ColorSensor.class, "color2");

        dist1 = hardwareMap.get(DistanceSensor.class, "color1");
        dist2 = hardwareMap.get(DistanceSensor.class, "color2");

        // מדליק LED (חשוב!)
        color1.enableLed(true);
        color2.enableLed(true);

        waitForStart();

        while (opModeIsActive()) {

            double d1 = dist1.getDistance(DistanceUnit.CM);
            double d2 = dist2.getDistance(DistanceUnit.CM);

            // טווח זיהוי כדור
            boolean close1 = d1 < 5;
            boolean close2 = d2 < 5;

            telemetry.addLine("==== SENSOR 1 ====");
            telemetry.addData("Distance", d1);
            telemetry.addData("R", color1.red());
            telemetry.addData("G", color1.green());
            telemetry.addData("B", color1.blue());

            if(close1){
                telemetry.addData("Ball Detected", "YES");
                telemetry.addData("Color", detectColor(color1));
            } else {
                telemetry.addData("Ball Detected", "NO");
            }

            telemetry.addLine("==== SENSOR 2 ====");
            telemetry.addData("Distance", d2);
            telemetry.addData("R", color2.red());
            telemetry.addData("G", color2.green());
            telemetry.addData("B", color2.blue());

            if(close2){
                telemetry.addData("Ball Detected", "YES");
                telemetry.addData("Color", detectColor(color2));
            } else {
                telemetry.addData("Ball Detected", "NO");
            }

            telemetry.update();

            sleep(100);
        }
    }

    // ===== זיהוי צבע =====

    String detectColor(ColorSensor c){

        // סגול (מותאם לנתונים שלך)
        if(c.red() > 50 && c.blue() > 60){
            return "PURPLE";
        }

        // ירוק
        if(c.green() > c.red() + 15 && c.green() > c.blue() + 15){
            return "GREEN";
        }

        return "UNKNOWN";
    }
}