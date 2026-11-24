package org.firstinspires.ftc.teamcode;

import android.util.Size;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCompatibilityManager;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagSingleDetection;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

import java.util.List;

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

@Autonomous
public class japrildometry extends LinearOpMode {

    private static final boolean USE_WEBCAM = true;

    private AprilTagProcessor april;

    private VisionPortal vision;

    static final int VID = 0x046d;
    static final int PID = 0x0825;

    private Pose2D aprilPose;

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
    // april tag data -> pedro pose
    /// From John:
    /// I don't even know what I'm looking at.
    /*{
      detection.ftcPose.t;
      detection.ftcPose.y;
      detection.ftcPose.bearing;
      //pedro
      follower.pose().x();
      follower.pose().y();
      follower.pose().heading();
      //conversion
      ftcPose.t +  =follower.pose().x();
    }*/
    // 3. PROCEDURE: Groups paths sequentially (Forward -> Pause -> Strafe)
    private Command johndometryProcedure() {

        return sequential(
                follow(follower, driveForward()),
                waitMs(350), // Pauses briefly after driving forward
                follow(follower, strafeLeft())
        );
    }

    public void runOpMode() throws InterruptedException {

        CameraCompatibilityManager
                .getInstance()
                .addQuirk(VID, PID, CameraCompatibilityManager.Quirk.AVOID_LIB_USB_RESET_DEVICE);

        // This has something to do with initializing the april tag pipeline
        april = new AprilTagProcessor.Builder()
                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .setDrawAxes(true)
                .build();

        /// This is an older implementation of the next block.
        /// Keep in case something breaks with the newer one.
        /*VisionPortal.Builder vbuilder = new VisionPortal.Builder();
        vbuilder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        vbuilder.setCameraResolution(new Size(1280, 720));
        vbuilder.setStreamFormat(VisionPortal.StreamFormat.MJPEG);
        vbuilder.addProcessor(april);
        vision = vbuilder.build();*/

        vision = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .setCameraResolution(new Size(640, 480))
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .addProcessor(april)
                .enableLiveView(true)
                .build();

        waitForStart();

        while (opModeIsActive()) {

            List<AprilTagDetection> ATDs = april.getDetections();
            telemetry.addData("# AprilTags Detected", ATDs.size());

            // Step through the list of detections and display info for each one.
            for (AprilTagDetection detection : ATDs) {
                if (detection instanceof AprilTagSingleDetection) {
                    AprilTagSingleDetection singleDet = (AprilTagSingleDetection) detection;

                    if (singleDet.metadata != null) {
                        telemetry.addLine(String.format("\n==== (ID %d) %s", singleDet.id, singleDet.metadata.name));
                        telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                        telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                        telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
                    } else {
                        telemetry.addLine(String.format("\n==== (ID %d) Unknown", singleDet.id));
                        telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", singleDet.center.x, singleDet.center.y));
                    }

                } else {
                    AprilTagClusterDetection clusterDet = (AprilTagClusterDetection) detection;
                    telemetry.addLine(String.format("\n==== Tag Cluster (%s)", clusterDet.metadata.name));
                    telemetry.addLine(String.format("Percent tags found: %d", clusterDet.percentClusterFound));
                    telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                    telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                    telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
                }
            }   // end for() loop

            // Add "key" information to telemetry
            telemetry.addLine("\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.");
            telemetry.addLine("PRY = Pitch, Roll & Yaw (XYZ Rotation)");
            telemetry.addLine("RBE = Range, Bearing & Elevation");

        }

        vision.close();
    }
}
