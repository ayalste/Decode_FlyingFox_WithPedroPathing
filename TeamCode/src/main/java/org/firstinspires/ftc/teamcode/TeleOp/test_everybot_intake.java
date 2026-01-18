package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(name="test_everybot_intake", group="Teleop")
public class test_everybot_intake extends OpMode {
    // Declare OpMode members
    private ElapsedTime runtime = new ElapsedTime();

    // Drive motors
    private DcMotor intakeMotor = null;

    @Override
    public void init() {

        // Initialize hardware
        intakeMotor = hardwareMap.get(DcMotor.class, "intake_Motor");


        // Set motor directions
        intakeMotor.setDirection(DcMotor.Direction.FORWARD);




        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void start() {
        runtime.reset();
    }

    @Override
    public void loop() {


        // Gamepad 2: intake control
        if(gamepad2.a){
            intakeMotor.setPower(1.0);
        }
        else if (gamepad2.b){
            intakeMotor.setPower(-1.0);
        }
        else{
            intakeMotor.setPower(0);
        }



        // Telemetry
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("power:","volt:"+intakeMotor.getPower());



        telemetry.update();
    }

    @Override
    public void stop() {
    }
}