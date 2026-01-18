package org.firstinspires.ftc.teamcode.TeleOp;

class ArtifactColorMatcher {

    public ArtifactColor matchColor(NormalizedRGBA color) {
        double r = color.red;
        double b = color.blue;

        // זיהוי סגול: הערכים הגבוהים ביותר שנמדדו (מעל הרעש של האוויר)
        if (r >= 118 && b >= 176) {
            return ArtifactColor.PURPLE;
        }

        // זיהוי ירוק: הערכים נמוכים מהרעש של האוויר (בגלל בליעת אור של הכדור)
        if (r <= 110 && b <= 170) {
            return ArtifactColor.GREEN;
        }

        // כל מה שבאמצע (סביב RED:112, BLUE:173) נחשב לאוויר
        return ArtifactColor.NONE;
    }
}
