package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@TeleOp(name = "IMU Test gg", group = "Test")
public class test_imu extends LinearOpMode {

    IMU imu;

    @Override
    public void runOpMode() {

        // Get IMU from Control Hub
        imu = hardwareMap.get(IMU.class, "imu");

        // Set hub orientation (IMPORTANT)
        RevHubOrientationOnRobot.LogoFacingDirection logo =
                RevHubOrientationOnRobot.LogoFacingDirection.BACKWARD;

        RevHubOrientationOnRobot.UsbFacingDirection usb =
                RevHubOrientationOnRobot.UsbFacingDirection.UP;

        RevHubOrientationOnRobot orientation =
                new RevHubOrientationOnRobot(logo, usb);

        imu.initialize(new IMU.Parameters(orientation));

        telemetry.addLine("IMU Initialized");
        telemetry.addLine("Rotate robot by hand");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            YawPitchRollAngles angles =
                    imu.getRobotYawPitchRollAngles();

            double yaw = angles.getYaw(AngleUnit.DEGREES);
            double pitch = angles.getPitch(AngleUnit.DEGREES);
            double roll = angles.getRoll(AngleUnit.DEGREES);

            telemetry.addData("Yaw", yaw);
            telemetry.addData("Pitch", pitch);
            telemetry.addData("Roll", roll);

            telemetry.update();
        }
    }
}