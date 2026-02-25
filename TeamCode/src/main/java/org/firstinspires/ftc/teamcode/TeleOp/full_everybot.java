package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.VoltageSensor;

@TeleOp(name = "full_everybot", group = "Teleop")
public class full_everybot extends OpMode {

    private VoltageSensor batteryVoltageSensor;

    private ElapsedTime runtime = new ElapsedTime();

    // Drive motors
    private DcMotor leftFrontDrive;
    private DcMotor leftBackDrive;
    private DcMotor rightFrontDrive;
    private DcMotor rightBackDrive;

    // Catapult motors
    private DcMotor catapult1;
    private DcMotor catapult2;

    // Intake motor
    private DcMotor intakeMotor;

    // Foot motor
    private DcMotor foot;

    // encoder foot
    static final int MOVE_TICKS = 2300;   // כמה שצריך להסתובב
    static final double MOVE_POWER = 1;
    boolean lastA = false;
    boolean lastB = false;
    boolean isActive = false;

    private enum CatapultModes { UP, DOWN, HOLD }
    private CatapultModes pivotMode;

    private enum FootModes { UP, DOWN, BRAKE }
    private FootModes footMode;

    private boolean shaking = false;
    private int shakeStep = 0;
    private ElapsedTime shakeTimer = new ElapsedTime();

    @Override
    public void init() {
        telemetry.addData("Status", "Initializing");

        // Drive motors
        leftFrontDrive = hardwareMap.get(DcMotor.class, "left_front_drive");
        leftBackDrive = hardwareMap.get(DcMotor.class, "left_back_drive");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
        rightBackDrive = hardwareMap.get(DcMotor.class, "right_back_drive");

        // Catapult motors
        catapult1 = hardwareMap.get(DcMotor.class, "catapult_motor1");
        catapult2 = hardwareMap.get(DcMotor.class, "catapult_motor2");

        // Intake
        intakeMotor = hardwareMap.get(DcMotor.class, "intake_Motor");

        // Foot
        foot = hardwareMap.get(DcMotor.class, "foot");
        foot.setDirection(DcMotor.Direction.REVERSE);
        foot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //encoder
        foot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        foot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        foot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        foot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Drive directions
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

        // Catapult directions
        catapult1.setDirection(DcMotor.Direction.REVERSE);
        catapult2.setDirection(DcMotor.Direction.FORWARD);

        intakeMotor.setDirection(DcMotor.Direction.FORWARD);

        // Battery sensor
        batteryVoltageSensor = hardwareMap.voltageSensor.iterator().next();

        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void start() {
        runtime.reset();


        //setting the endgame
        int newTarget = foot.getCurrentPosition() + MOVE_TICKS;
        foot.setTargetPosition(newTarget);
        foot.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        foot.setPower(0.1);
    }

    @Override
    public void loop() {

        /* =========================
           DRIVE (gamepad1)
        ========================= */
        double speed = -gamepad1.left_stick_y;
        double turn = gamepad1.right_stick_x;
        double rotation = gamepad1.left_stick_x;

        leftFrontDrive.setPower(speed + turn + rotation);
        rightFrontDrive.setPower(speed - turn - rotation);
        leftBackDrive.setPower(speed - turn + rotation);
        rightBackDrive.setPower(speed + turn - rotation);

        /* =========================
           FOOT (gamepad1)
        ========================= */
//
//        boolean footDownButton = gamepad1.a;
//        boolean footUpButton = gamepad1.b;
//
//        if (footDownButton && footUpButton) {
//            footDownButton = false;
//        }
//
//        if (footDownButton) {
//            footMode = FootModes.DOWN;
//            foot.setPower(FOOT_DOWN_POWER);
//        }
//        else if (footUpButton) {
//            footMode = FootModes.UP;
//            foot.setPower(FOOT_UP_POWER);
//        }
//        else {
//            footMode = FootModes.BRAKE;
//            foot.setPower(FOOT_OFF_POWER);
//        }

        boolean currentA = gamepad1.a;
        boolean currentB = gamepad1.b;
        boolean aPressed = currentA && !lastA;
        boolean bPressed = currentB && !lastB;

        if (aPressed && !foot.isBusy() && !isActive) {

            int newTarget = foot.getCurrentPosition() - MOVE_TICKS;

            foot.setTargetPosition(newTarget);
            foot.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            foot.setPower(MOVE_POWER);

            isActive = !isActive;
        }

        if (bPressed && !foot.isBusy() && isActive) {

            int newTarget = foot.getCurrentPosition() + MOVE_TICKS;

            foot.setTargetPosition(newTarget);
            foot.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            foot.setPower(MOVE_POWER);

            isActive = !isActive;
        }

        telemetry.addData("Position", foot.getCurrentPosition());
        telemetry.update();

        lastA = currentA;
        lastB = currentB;


        /* =========================
           CATAPULT (gamepad2)
           UP   - right trigger
           DOWN - right bumper
        ========================= */
        // CATAPULT
        boolean catapultUp = gamepad2.right_trigger > 0.2;
        boolean catapultDown = gamepad2.right_bumper;
        boolean shake = gamepad2.dpad_right;

        if (catapultUp && catapultDown) {
            catapultUp = false;
        }

        if (catapultUp) {
            pivotMode = CatapultModes.UP;
            catapult1.setPower(-1.0);
            catapult2.setPower(-1.0);
            shaking = false;
        }
        else if (catapultDown) {
            pivotMode = CatapultModes.DOWN;
            catapult1.setPower(1.0);
            catapult2.setPower(1.0);
            shaking = false;
        }
        else if (shake && !shaking) {
            shaking = true;
            shakeStep = 0;
            shakeTimer.reset();
        }
        else if (!shaking) {
            pivotMode = CatapultModes.HOLD;
            catapult1.setPower(0.2);
            catapult2.setPower(0.2);
        }

        if (shaking) {

            if (shakeTimer.milliseconds() > 80) {

                shakeTimer.reset();
                shakeStep++;

                if (shakeStep % 2 == 0) {
                    catapult1.setPower(-0.2);
                    catapult2.setPower(-0.2);
                } else {
                    catapult1.setPower(0.2);
                    catapult2.setPower(0.2);
                }

                if (shakeStep >= 6) {
                    shaking = false;
                    catapult1.setPower(0);
                    catapult2.setPower(0);
                    pivotMode = CatapultModes.DOWN;
                }
            }
        }

        /* =========================
           INTAKE (gamepad2)
           IN  - left trigger
           OUT - left bumper
        ========================= */
        if (gamepad2.a) {
            intakeMotor.setPower(1.0);
        }
        else if (gamepad2.b) {
            intakeMotor.setPower(-1.0);
        }
        else {
            intakeMotor.setPower(0);
        }

        /* =========================
           TELEMETRY
        ========================= */
        telemetry.addData("Run Time", runtime.toString());
        telemetry.addData("Catapult mode", pivotMode);
        telemetry.addData("Intake power", intakeMotor.getPower());
        telemetry.addData("Foot mode", footMode);

        telemetry.addData("Battery Voltage", batteryVoltageSensor.getVoltage());
        telemetry.addData("Battery %", "%.1f", getBatteryPercent());

        telemetry.update();
    }

    double getBatteryPercent() {

        double voltage = batteryVoltageSensor.getVoltage();

        double minVoltage = 9.6;
        double maxVoltage = 12.6;

        double percent = (voltage - minVoltage) / (maxVoltage - minVoltage) * 100.0;

        percent = Math.max(0, Math.min(100, percent));

        return percent;
    }

    @Override
    public void stop() {
        // optional stop logic
    }
}
