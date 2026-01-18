package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

public class TestBenchColor {

    NormalizedColorSensor leftColor, rightColor;

    public enum DetectedColor {
        GREEN,
        PURPLE,
        UNKNOWN
    }

    private int greenCount = 0;
    private int purpleCount = 0;

    public void init(HardwareMap hwMap) {
        leftColor  = hwMap.get(NormalizedColorSensor.class, "color_left");
        rightColor = hwMap.get(NormalizedColorSensor.class, "color_right");


    }

    private DetectedColor detectFromSensor(NormalizedColorSensor sensor) {
        NormalizedRGBA c = sensor.getNormalizedColors();

        float r = c.red;
        float g = c.green;
        float b = c.blue;

        // סף מינימלי – מסנן רעש מוחלט
        if (r < 0.0005 && g < 0.0005 && b < 0.0005) {
            return DetectedColor.UNKNOWN;
        }

        // 🟢 GREEN – ירוק דומיננטי
        if (g > r && g > b && g > 0.0015) {
            return DetectedColor.GREEN;
        }

        // 🟣 PURPLE – אדום וכחול דומים וגבוהים מ־ירוק
        if (r > g && b > g && Math.abs(r - b) < 0.001) {
            return DetectedColor.PURPLE;
        }

        return DetectedColor.UNKNOWN;
    }

    public DetectedColor getStableColor() {
        DetectedColor left  = detectFromSensor(leftColor);
        DetectedColor right = detectFromSensor(rightColor);

        // OR – אחד מספיק
        DetectedColor current =
                (left != DetectedColor.UNKNOWN) ? left : right;

        if (current == DetectedColor.GREEN) {
            greenCount++;
            purpleCount = 0;
        } else if (current == DetectedColor.PURPLE) {
            purpleCount++;
            greenCount = 0;
        } else {
            greenCount = 0;
            purpleCount = 0;
        }

        // 3 לופים רצופים = החלטה
        if (greenCount >= 3) return DetectedColor.GREEN;
        if (purpleCount >= 3) return DetectedColor.PURPLE;

        return DetectedColor.UNKNOWN;
    }
}

