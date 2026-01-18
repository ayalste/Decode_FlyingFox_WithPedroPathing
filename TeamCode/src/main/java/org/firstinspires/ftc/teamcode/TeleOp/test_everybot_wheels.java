package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(name="test_everybot_wheels", group="Teleop")
public class test_everybot_wheels extends OpMode {
    // Declare OpMode members
    private ElapsedTime runtime = new ElapsedTime();

    // Drive motors
    private DcMotor leftFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor rightBackDrive = null;


    @Override
    public void init() {

        // Initialize hardware
        leftFrontDrive = hardwareMap.get(DcMotor.class, "left_front_drive");
        leftBackDrive = hardwareMap.get(DcMotor.class, "left_back_drive");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
        rightBackDrive = hardwareMap.get(DcMotor.class, "right_back_drive");


        // Set motor directions
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);



        // Set motor behaviors


        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void start() {
        runtime.reset();
    }

    @Override
    public void loop() {

        // Gamepad 1: Drive control
        double speed = -gamepad1.left_stick_y;
        double turn = gamepad1.right_stick_x;
        double rotation = gamepad1.left_stick_x;
        double wheelsPower = 1.0; // Default to full power, can add toggle if needed

        leftFrontDrive.setPower((speed + turn + rotation) * wheelsPower);
        rightFrontDrive.setPower((speed - turn - rotation) * wheelsPower);
        leftBackDrive.setPower((speed - turn + rotation) * wheelsPower);
        rightBackDrive.setPower((speed + turn - rotation) * wheelsPower);


        // Telemetry
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("power:","volt:"+leftBackDrive.getPower()+leftFrontDrive.getPower()+rightBackDrive.getPower()+rightFrontDrive.getPower());



        telemetry.update();
    }

    @Override
    public void stop() {}
}