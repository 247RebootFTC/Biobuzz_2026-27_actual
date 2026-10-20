package org.firstinspires.ftc.teamcode;

import com.pedropathing.tuning.autotune.Procedure;
import com.pedropathing.tuning.autotune.Tuner;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class newTeleOp extends LinearOpMode {

    // Driving motor objects
    // The names refer to the location of the motor, and which wheel it controls
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

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
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        // Makes sure the robot doesn't move after initialized, so we don't get penalized
        waitForStart();

        while (opModeIsActive()) {

            // Reset the motor speed variables to 0
            // This is in the event that one isn't set to 0, so we don't get penalized
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

            if (gamepad1.a) {

                ramp.setPosition(0.0);

            } else if (gamepad1.b) {

                ramp.setPosition(1.0);
            }
        }
    }
}