package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name = "BallDetector_Final_V2", group = "Main")
public class BallDetectorMotor extends OpMode {

    private ColorSensor colorLeft;
    private DistanceSensor distanceLeft;
    private DcMotor intakeMotor;
    private ArtifactColorMatcher matcher;

    @Override
    public void init() {
        colorLeft = hardwareMap.get(ColorSensor.class, "colorLeft");
        distanceLeft = hardwareMap.get(DistanceSensor.class, "colorLeft");
        intakeMotor = hardwareMap.get(DcMotor.class, "intake_Motor");
        matcher = new ArtifactColorMatcher();

        // וודא שה-Gain מוגדר בדיוק כפי שהיה בזמן המדידה בתמונות
        if (colorLeft instanceof com.qualcomm.robotcore.hardware.NormalizedColorSensor) {
            ((com.qualcomm.robotcore.hardware.NormalizedColorSensor) colorLeft).setGain(15.0f);
        }
    }

    @Override
    public void loop() {
        NormalizedRGBA currentRGBA = new NormalizedRGBA(
                colorLeft.red(), colorLeft.green(), colorLeft.blue());

        ArtifactColor result = matcher.matchColor(currentRGBA);
        double dist = distanceLeft.getDistance(DistanceUnit.CM);

        // הפעלה רק אם המרחק קטן מ-6 ס"מ כדי למנוע זיהוי שגוי של קירות/רצפה
        if (result != ArtifactColor.NONE && dist < 6.0) {
            intakeMotor.setPower(1.0);
        } else {
            intakeMotor.setPower(0.0);
        }

        telemetry.addData("COLOR", result);
        telemetry.addData("DIST", "%.2f", dist);
        telemetry.addData("R", "%.1f", currentRGBA.red);
        telemetry.addData("B", "%.1f", currentRGBA.blue);
        telemetry.update();
    }
}

enum ArtifactColor { GREEN, PURPLE, NONE }
class NormalizedRGBA {
    public double red, green, blue;
    public NormalizedRGBA(double r, double g, double b) { this.red = r; this.green = g; this.blue = b; }
}