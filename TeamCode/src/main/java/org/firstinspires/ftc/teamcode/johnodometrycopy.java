package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.pedropathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import static com.pedropathing.api.Paths.*;
import com.pedropathing.paths.Path;

import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import com.pedropathing.ivy.Command;

@Autonomous(name = "John Odometry Copy", group = "Autonomous")
public class johnodometrycopy extends LinearOpMode {

    private Servo ramp;
    private Follower follower;
    private final PoseFactory pfac = PoseFactory.degrees();

    // 1. COORDINATES: X is forward/backward, Y is left/right
    private final Pose startPose = pfac.of(0, 0, 0);
    private final Pose forwardPose = pfac.of(90, 0, 0); // 30 inches forward
    private final Pose strafeLeftPose = pfac.of(90, 15, 0); // Kept at X=30 forward, shifted 6 inches left (Y+)

    // 2. PATH METHODS: Defining each single movement step cleanly
    private Path driveForward() {
        return line(startPose, forwardPose).linear(startPose, forwardPose);
    }

    private Path strafeLeft() {
        // Starts from the forward position and moves laterally to the left position
        return line(forwardPose, strafeLeftPose).linear(forwardPose, strafeLeftPose);
    }

    // 3. PROCEDURE: Groups paths sequentially (Forward -> Pause -> Strafe)
    private Command johndometryProcedure() {

        return sequential(

                follow(follower, driveForward()),
                waitMs(350), // Pauses briefly after driving forward
                follow(follower, strafeLeft())
        );
    }

    @Override
    public void runOpMode() throws InterruptedException {

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        ramp = hardwareMap.get(Servo.class, "ramp");

        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
        follower.update();

        waitForStart();

        // 4. SCHEDULING & EXECUTION: schedule the routine (Forward -> Pause -> Strafe)
        schedule(johndometryProcedure());

        // 5. MAIN EXECUTION LOOP
        while (opModeIsActive()) {
            follower.update();
            Scheduler.execute();

            telemetry.addData("X Position", follower.pose().x());
            telemetry.addData("Y Position", follower.pose().y());
            telemetry.addData("Heading", Math.toDegrees(follower.pose().heading()));
            telemetry.addData("Follower Mode", follower.mode());
            telemetry.update();
        }
    }
}