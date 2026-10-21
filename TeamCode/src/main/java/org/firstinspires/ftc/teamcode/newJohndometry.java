package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.pedropathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;

@Autonomous
public class newJohndometry extends LinearOpMode {

    // Driving motor objects
    // The names refer to the location of the motor, and which wheel it controls
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    // Servo object
    // This servo changes the pitch of the ramp, hence the name
    private Servo ramp;

    // Pedropathing objects.  I do not have a good explanation other than "this is odometry."
    private Follower follower;
    private final PoseFactory p = PoseFactory.degrees();

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


        follower = Constants.create(hardwareMap);

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

        waitForStart();

        while (opModeIsActive()) {


        }
    }
}
