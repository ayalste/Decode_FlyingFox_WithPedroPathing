package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="test_everybot_launcher", group="Teleop")
public class test_everybot_launcher extends OpMode {
    // Declare OpMode members
    private ElapsedTime runtime = new ElapsedTime();

    // Drive motors
    private DcMotor catapult1 = null;
    private DcMotor catapult2 = null;


    private enum CatapultModes {UP, DOWN, HOLD}
    private CatapultModes pivotMode;


    @Override
    public void init() {
        telemetry.addData("Status", "Initializing");

        // Initialize hardware
        catapult1 = hardwareMap.get(DcMotor.class, "catapult_motor1");
        catapult2 = hardwareMap.get(DcMotor.class, "catapult_motor2");


        // Set motor directions
        catapult1.setDirection(DcMotor.Direction.REVERSE);
        catapult2.setDirection(DcMotor.Direction.FORWARD);




        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void start() {
        runtime.reset();
    }

    @Override
    public void loop() {


        // Gamepad 2: Launcher control

        boolean catapultUpButton = gamepad2.right_bumper;
        boolean catapultDownButton = gamepad2.right_trigger > 0.2;
        if (catapultUpButton && catapultDownButton) {
            catapultUpButton = false;
        }

        if (catapultUpButton) {
            pivotMode = CatapultModes.UP;
            catapult1.setPower(-1.0);
            catapult2.setPower(-1.0);
        } else if (catapultDownButton) {
            pivotMode = CatapultModes.DOWN;
            catapult1.setPower(1);
            catapult2.setPower(1);
        } else {
            pivotMode = CatapultModes.HOLD;
            catapult1.setPower(0.2);
            catapult2.setPower(0.2);
            //Slight feed forward to keep catapult down while driving
        }




        // Telemetry
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("power:","volt:"+catapult1.getPower());



        telemetry.update();
    }

    @Override
    public void stop() {}
}