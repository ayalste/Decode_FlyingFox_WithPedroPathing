package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name = "!!! SENSOR DIAGNOSTIC !!!", group = "Test")
public class test_servo extends OpMode {

    private ColorSensor colorLeft;
    private DistanceSensor distanceLeft;

    @Override
    public void init() {
        colorLeft = hardwareMap.get(ColorSensor.class, "colorLeft");
        distanceLeft = hardwareMap.get(DistanceSensor.class, "colorLeft");

        // נתחיל עם Gain גבוה כדי לראות אם זה עוזר
        if (colorLeft instanceof NormalizedColorSensor) {
            ((NormalizedColorSensor) colorLeft).setGain(15.0f);
        }
    }

    @Override
    public void loop() {
        // ערכים גולמיים
        double r = colorLeft.red();
        double g = colorLeft.green();
        double b = colorLeft.blue();
        double dist = distanceLeft.getDistance(DistanceUnit.CM);

        telemetry.addLine("--- נתוני חיישן בזמן אמת ---");
        telemetry.addData("DISTANCE", "%.2f cm", dist);
        telemetry.addLine();
        telemetry.addData("RED", "%.5f", r);
        telemetry.addData("GREEN", "%.5f", g);
        telemetry.addData("BLUE", "%.5f", b);
        telemetry.addLine();

        // חישוב יחסים כדי לראות אם יש דומיננטיות
        if (g > 0) telemetry.addData("Ratio G/R", "%.2f", g/r);
        if (g > 0) telemetry.addData("Ratio G/B", "%.2f", g/b);

        telemetry.update();
    }
}