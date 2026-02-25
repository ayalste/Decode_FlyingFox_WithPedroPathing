package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.VoltageSensor;

@TeleOp(name = "\\o/ TOTO_Full :)", group = "Teleop")
public class full_everybot extends OpMode {

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Declarations ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    //////////////////////////
    //////// Battery ////////
    //////////////////////////
    private VoltageSensor batteryVoltageSensor;

    //////////////////////////
    ///////// Timer //////////
    //////////////////////////
    private ElapsedTime runtime = new ElapsedTime();

    //////////////////////////
    ///// Driving system /////
    //////////////////////////
        private DcMotor leftFrontDrive;
        private DcMotor leftBackDrive;
        private DcMotor rightFrontDrive;
        private DcMotor rightBackDrive;

    //////////////////////////
    //////// Launcher ////////
    //////////////////////////
        private DcMotor catapult1;
        private DcMotor catapult2;

        private enum CatapultModes { UP, DOWN, HOLD }
        private CatapultModes pivotMode;

        private boolean shaking = false;
        private int shakeStep = 0;
        private ElapsedTime shakeTimer = new ElapsedTime();

    //////////////////////////
    ///////// Intake /////////
    //////////////////////////
        private DcMotor intakeMotor;

    //////////////////////////
    //////// End game ////////
    //////////////////////////
    private DcMotor foot;

    // Encoder
    static final int MOVE_TICKS = 2320;   // כמה שצריך להסתובב
    static final double MOVE_POWER = 1;
    boolean lastA = false;
    boolean lastB = false;
    boolean isActive = false;

    // setting the foot to up (homing)
    boolean homing = true;
    int lastEncoderPosition = 0;
    ElapsedTime stallTimer = new ElapsedTime();
    static final double HOMING_POWER = 0.2;
    static final int MOVEMENT_THRESHOLD = 5;
    static final int STALL_TIME_MS = 300;

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Actual Code ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void init() {
        //////////////////////////
        /// Updating telemetry ///
        //////////////////////////
        telemetry.addData("Status", "Initializing");

        //////////////////////////
        //////// Battery /////////
        //////////////////////////
        batteryVoltageSensor = hardwareMap.voltageSensor.iterator().next();

        //////////////////////////
        ///// Driving system /////
        //////////////////////////
        leftFrontDrive = hardwareMap.get(DcMotor.class, "left_front_drive");
        leftBackDrive = hardwareMap.get(DcMotor.class, "left_back_drive");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
        rightBackDrive = hardwareMap.get(DcMotor.class, "right_back_drive");

        // Drive directions
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

        //////////////////////////
        //////// Launcher ////////
        //////////////////////////
        catapult1 = hardwareMap.get(DcMotor.class, "CR_DWY");
        catapult2 = hardwareMap.get(DcMotor.class, "CL_DWX");


        // Catapult directions
        catapult1.setDirection(DcMotor.Direction.REVERSE);
        catapult2.setDirection(DcMotor.Direction.FORWARD);

        //////////////////////////
        ///////// Intake /////////
        //////////////////////////
        intakeMotor = hardwareMap.get(DcMotor.class, "intake_Motor");

        intakeMotor.setDirection(DcMotor.Direction.FORWARD);

        //////////////////////////
        //////// End game ////////
        //////////////////////////
        foot = hardwareMap.get(DcMotor.class, "foot");
        foot.setDirection(DcMotor.Direction.REVERSE);
        foot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //encoder
        foot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        foot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //////////////////////////
        /// Updating telemetry ///
        //////////////////////////
        telemetry.addData("Status", "Initialized & Ready ;)");
        telemetry.update();
    }

    @Override
    public void start() {
        runtime.reset();


        //setting the endgame
        homing = true;
        stallTimer.reset();
        lastEncoderPosition = foot.getCurrentPosition();

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

        // =========================
        //      FOOT (gamepad1)
        // =========================
        boolean currentA = gamepad1.a;
        boolean currentB = gamepad1.b;
        boolean aPressed = currentA && !lastA;
        boolean bPressed = currentB && !lastB;

        // HOMING LOGIC
        if (homing) {

            foot.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            foot.setPower(HOMING_POWER);

            int currentPosition = foot.getCurrentPosition();

            if (Math.abs(currentPosition - lastEncoderPosition) > MOVEMENT_THRESHOLD) {
                stallTimer.reset();
                lastEncoderPosition = currentPosition;
            }

            if (stallTimer.milliseconds() > STALL_TIME_MS) {

                foot.setPower(0);

                foot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                foot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                homing = false;
            }

            telemetry.addData("HOMING", "Running...");
            telemetry.update();
        }
        else
        {
            if (aPressed && !foot.isBusy() && !isActive) {

                int newTarget = foot.getCurrentPosition() - MOVE_TICKS;

                foot.setTargetPosition(newTarget);
                foot.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                foot.setPower(MOVE_POWER);

                isActive = true;
            }

            if (bPressed && !foot.isBusy() && isActive) {

                int newTarget = foot.getCurrentPosition() + MOVE_TICKS;

                foot.setTargetPosition(newTarget);
                foot.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                foot.setPower(MOVE_POWER);

                isActive = false;
            }

            telemetry.addData("Position", foot.getCurrentPosition());
            telemetry.update();

            lastA = currentA;
            lastB = currentB;
        }



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
        telemetry.addData("Foot mode (down)", isActive);

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
