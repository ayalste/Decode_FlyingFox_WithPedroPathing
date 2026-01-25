package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(name="full_everybot", group="Teleop")
public class full_everybot extends OpMode {
    // Declare OpMode members
    private ElapsedTime runtime = new ElapsedTime();

    // Drive motors
    private DcMotor leftFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor rightBackDrive = null;

    // Launcher motors
    private DcMotor catapult1 = null;
    private DcMotor catapult2 = null;

    // Intake motors
    private DcMotor intakeMotor = null;

    private enum CatapultModes {UP, DOWN, HOLD}
    private full_everybot.CatapultModes pivotMode;
    private DcMotor foot = null;

    private enum FootModes {UP, DOWN, BRAKE}
    private FootModes footMode;

    // Foot power values
    private final double FOOT_UP_POWER = 0.2;
    private final double FOOT_DOWN_POWER = -1;
    private final double FOOT_OFF_POWER = 0.0;


    @Override
    public void init() {
        telemetry.addData("Status", "Initializing");

        // Initialize hardware
        leftFrontDrive = hardwareMap.get(DcMotor.class, "left_front_drive");
        leftBackDrive = hardwareMap.get(DcMotor.class, "left_back_drive");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
        rightBackDrive = hardwareMap.get(DcMotor.class, "right_back_drive");

        catapult1 = hardwareMap.get(DcMotor.class, "catapult_motor1");
        catapult2 = hardwareMap.get(DcMotor.class, "catapult_motor2");

        intakeMotor = hardwareMap.get(DcMotor.class, "intake_Motor");

        // foot:
        foot = hardwareMap.get(DcMotor.class, "foot");

        foot.setDirection(DcMotor.Direction.REVERSE);
        foot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);



        // Set motor directions
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

        catapult1.setDirection(DcMotor.Direction.REVERSE);
        catapult2.setDirection(DcMotor.Direction.FORWARD);

        intakeMotor.setDirection(DcMotor.Direction.FORWARD);


        // Telementry

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
        boolean footDownButton = gamepad1.a;
        boolean footUpButton = gamepad1.b;

        if (footDownButton && footUpButton) {
            footDownButton = false;
        }

        if (footDownButton) {
            footMode = FootModes.DOWN;
            foot.setPower(FOOT_DOWN_POWER);
        }
        else if (footUpButton) {
            footMode = FootModes.UP;
            foot.setPower(FOOT_UP_POWER);
        }
        else {
            footMode = FootModes.BRAKE;
            foot.setPower(FOOT_OFF_POWER);
        }



        // Gamepad 2: Launcher control

        boolean catapultUpButton = gamepad2.right_bumper;
        boolean catapultDownButton = gamepad2.right_trigger > 0.2;
        if (catapultUpButton && catapultDownButton) {
            catapultUpButton = false;
        }

        if (catapultUpButton) {
            pivotMode = full_everybot.CatapultModes.UP;
            catapult1.setPower(-1.0);
            catapult2.setPower(-1.0);
        } else if (catapultDownButton) {
            pivotMode = full_everybot.CatapultModes.DOWN;
            catapult1.setPower(1);
            catapult2.setPower(1);
        } else {
            pivotMode = full_everybot.CatapultModes.HOLD;
            catapult1.setPower(0.2);
            catapult2.setPower(0.2);
            //Slight feed forward to keep catapult down while driving
        }


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
        telemetry.addData("Drive power:","volt:"+ (Math.abs(leftBackDrive.getPower())+Math.abs(leftFrontDrive.getPower())+Math.abs(rightBackDrive.getPower())+Math.abs(rightFrontDrive.getPower()))/4);
        telemetry.addData("Catapult power:","volt:"+catapult1.getPower());
        telemetry.addData("Intake power:","volt:"+intakeMotor.getPower());
        telemetry.addData("Foot power", foot.getPower());
        telemetry.addData("Foot mode", footMode);


        telemetry.update();
    }
    @Override
    public void stop() {
        // Add stop logic if needed
    }
}