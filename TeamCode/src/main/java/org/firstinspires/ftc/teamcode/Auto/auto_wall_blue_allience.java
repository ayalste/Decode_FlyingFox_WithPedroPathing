package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@Autonomous(name = "Auto_Wall_Blue_Timer_Everybot", group = "FTC")
public class auto_wall_blue_allience extends LinearOpMode {

    DcMotor leftFrontDrive, leftBackDrive, rightFrontDrive, rightBackDrive;
    IMU imu;
    VoltageSensor battery;

    double baseHeading;

    @Override
    public void runOpMode() {

        leftFrontDrive  = hardwareMap.get(DcMotor.class, "left_front_drive");
        leftBackDrive   = hardwareMap.get(DcMotor.class, "left_back_drive");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
        rightBackDrive  = hardwareMap.get(DcMotor.class, "right_back_drive");

        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

        imu = hardwareMap.get(IMU.class, "imu");
        imu.resetYaw();

        battery = hardwareMap.voltageSensor.iterator().next();

        waitForStart();

        baseHeading = getHeading();

        // 1️⃣ 255 ס"מ קדימה
        driveStraightTimed(0.35, 2864);

        // 2️⃣ סיבוב 52°
        turnToAngle(baseHeading + 52);

        baseHeading = getHeading(); // חשוב!

        // 3️⃣ 86 ס"מ קדימה
        driveStraightTimed(0.35, 1945);

        stopAll();
    }

    /* =================== DRIVE STRAIGHT WITH IMU =================== */
    void driveStraightTimed(double speed, long timeMs) {

        double voltage = battery.getVoltage();
        double power = speed * (12.5 / voltage);

        long start = System.currentTimeMillis();

        while (opModeIsActive() && System.currentTimeMillis() - start < timeMs) {

            double error = angleError(baseHeading, getHeading());
            double correction = error * 0.01;

            correction = Math.max(-0.2, Math.min(0.2, correction));

            drive(power, correction);
        }

        stopAll();
    }

    /* =================== TURN WITH IMU =================== */
    void turnToAngle(double targetAngle) {

        double error;

        do {
            error = angleError(targetAngle, getHeading());
            double turnPower = error * 0.01;

            turnPower = Math.max(-0.4, Math.min(0.4, turnPower));

            drive(0, turnPower);

        } while (opModeIsActive() && Math.abs(error) > 1.0);

        stopAll();
    }

    /* =================== DRIVE FORMULA (UNCHANGED) =================== */
    void drive(double speed, double turn) {

        double lf = speed + turn;
        double rf = speed - turn;
        double lb = speed - turn;
        double rb = speed + turn;

        double max = Math.max(Math.max(Math.abs(lf), Math.abs(rf)),
                Math.max(Math.abs(lb), Math.abs(rb)));

        if (max > 1.0) {
            lf /= max;
            rf /= max;
            lb /= max;
            rb /= max;
        }

        leftFrontDrive.setPower(lf);
        rightFrontDrive.setPower(rf);
        leftBackDrive.setPower(lb);
        rightBackDrive.setPower(rb);
    }

    void stopAll() {
        leftFrontDrive.setPower(0);
        rightFrontDrive.setPower(0);
        leftBackDrive.setPower(0);
        rightBackDrive.setPower(0);
        sleep(150);
    }

    double getHeading() {
        YawPitchRollAngles a = imu.getRobotYawPitchRollAngles();
        return a.getYaw(AngleUnit.DEGREES);
    }

    double angleError(double target, double current) {
        double error = target - current;
        while (error > 180) error -= 360;
        while (error < -180) error += 360;
        return error;
    }
}