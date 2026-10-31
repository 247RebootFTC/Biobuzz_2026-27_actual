package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class newTeleOp extends LinearOpMode {

    // Driving motor objects
    // The names refer to the location of the motor, and which wheel it controls
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    private DcMotor shoter;
    private DcMotor intake;

    // Servo object
    // This servo changes the pitch of the ramp, hence the name
    private Servo ramp;

    // Driving motor speed variables
    double FL;
    double FR;
    double BL;
    double BR;

    // Joystick analog value variables.
    // The "X" refers to the x-axis movement of the joystick.  "Y" follow the same logic.
    // The "Left" or "Right" refers to whether is it the left or right joystick.
    double XLeft;
    double YLeft;
    double XRight;
    double YRight;

    // Joystick sensitivity variables.  Linearly changes the speed the motors move.
    // orthogonalScale is speed of forwards, backwards, and strafing movement.
    //turningScale is speed of rotation
    double orthogonalScale = 1;
    double turningScale = 0.65;

    @Override
    public void runOpMode() throws InterruptedException {

        //if something is going wrong check here first cause normal programmer didn't code this
        // I don't know what I'm doing and I want and explanation once I'm done
        telemetry.addData("Status", "Initialized ");
        telemetry.update();

        // Initializes the motors
        // In other words, it assigns the actual motors to the motor objects
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");

        //shoter = hardwareMap.dcMotor.get("shoter");
        //intake = hardwareMap.dcMotor.get("intake");

        // Initializes the servos
        ramp = hardwareMap.get(Servo.class, "ramp");

        // Assigns directions to the motors so that they all spin the same direction
        /* the directions that the motors should go as long as the encoders aren't broken
        if they are the front right goes into reverse too
        frontLeft: reverse
        frontRight: forward
        backLeft: reverse
        backRight: forward
         */
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        /// These directions are subject to change
        //shoter.setDirection(DcMotor.Direction.FORWARD);
        //intake.setDirection(DcMotor.Direction.FORWARD);

        // Makes sure the robot doesn't move after initialized, so we don't get penalized
        waitForStart();

        while (opModeIsActive()) {

            // Reset the motor speed variables to 0
            // This is in the event that one isn't set to 0, so we don't get penalized
            // This also resets the motor speeds if there are no inputs
            FL = 0.0;
            FR = 0.0;
            BL = 0.0;
            BR = 0.0;

            // This inputs the joystick analog values to the joystick variables.
            // YRight is not used at the moment.
            XLeft = gamepad1.left_stick_x * orthogonalScale;
            YLeft = gamepad1.left_stick_y * orthogonalScale;
            XRight = gamepad1.right_stick_x * turningScale;
            YRight = gamepad1.right_stick_y * orthogonalScale;

            // This is the mecanum drive mix
            // This layout of results in the ability to strafe, as well as forwards and turning.

            //forward and back
            FL += YLeft;
            FR += YLeft;
            BL += YLeft;
            BR += YLeft;

            //strafing (sideways)
            FL -= XLeft;
            FR += XLeft;
            BL += XLeft;
            BR -= XLeft;

            // Turning
            FL -=XRight;
            FR +=XRight;
            BL -=XRight;
            BR +=XRight;

            //Set Motor Speeds
            frontLeft.setPower(FL);
            frontRight.setPower(FR);
            backLeft.setPower(BL);
            backRight.setPower(BR);

            // If Gunner presses RT, the shooter goes
            // Otherwise, it is off
            if (gamepad2.right_trigger >= 0.2) {

                /// This value may change
                shoter.setPower(1);

            } else {

                shoter.setPower(0);
            }

            // If the Gunner presses LT, the intake goes
            // If the Gunner presses LB, the intake goes backwards
            // If the Gunner presses B, the intake goes slowly
            if (gamepad2.left_trigger >= 0.2) {

                intake.setPower(1.0);

            } else if (gamepad2.left_bumper) {

                intake.setPower(-0.5);

            } else if (gamepad2.b) {

                intake.setPower(0.2);

            } else {

                intake.setPower(0.0);
            }

            // If the Gunner presses A, the ramp changes pitch
            if (gamepad2.a) {

                ramp.setPosition(0.1);

            } else {

                ramp.setPosition(0.0);
            }
        }
    }
}