package org.firstinspires.ftc.teamcode;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCompatibilityManager;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagSingleDetection;

import java.util.List;
import java.util.Objects;
import java.util.Locale;

import com.pedropathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import static com.pedropathing.api.Paths.*;
import com.pedropathing.paths.Path;

import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import com.pedropathing.ivy.Command;

/* README:
 * The coordinates are according to the bottom left corner of the robot.
 * This starts at the coordinates (125.5, 48) with a heading of 90 (away from audience)
 * The robot should be directly on the far side of the loading zone from the audience
 */

@Autonomous
public class japrildometryBlueMid extends LinearOpMode {

    private Follower follower;
    private final PoseFactory pfac = PoseFactory.degrees();

    // 1. COORDINATES: X is forward/backward, Y is left/right
    private final Pose startPose = pfac.of(125.5, 48, 90);
    private final Pose strafeLeftIntermediate = pfac.of(101.5, 24, 90);
    private Pose aprilPose;

    private Path driveToShooting() {

        return line(strafeLeftIntermediate, aprilPose).linear(strafeLeftIntermediate, aprilPose);
    }

    // 2. PATH METHODS: Defining each single movement step cleanly
    private Path driveToIntermediate() {

        return line(startPose, strafeLeftIntermediate).linear(startPose, strafeLeftIntermediate);
    }

    // 3. PROCEDURE: Groups paths sequentially (Forward -> Pause -> Strafe)
    private Command johndometryProcedure() {

        return sequential(
                follow(follower, driveToIntermediate())
        );
    }

    private Command inOpModeProc() {

        return sequential(
                follow(follower, driveToShooting())
        );
    }

    public void runOpMode() throws InterruptedException {

        CameraCompatibilityManager
                .getInstance()
                .addQuirk(0x046d, 0x0825, CameraCompatibilityManager.Quirk.AVOID_LIB_USB_RESET_DEVICE);

        // This has something to do with initializing the april tag pipeline
        AprilTagProcessor april = new AprilTagProcessor.Builder()
                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .setDrawAxes(true)
                .setLensIntrinsics(1399.68, 1399.68, 678.647, 334.736)
                .build();

        VisionPortal vision = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .setCameraResolution(new Size(1280, 720))
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .addProcessor(april)
                .enableLiveView(true)
                .build();

        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
        follower.update();

        waitForStart();

        schedule(johndometryProcedure());

        while (opModeIsActive()) {

            List<AprilTagDetection> ATDs = april.getDetections();
            telemetry.addData("# AprilTags Detected", ATDs.size());

            // Step through the list of detections and display info for each one.
            for (AprilTagDetection detection : ATDs) {

                if (detection instanceof AprilTagSingleDetection) {

                    AprilTagSingleDetection singleDet = (AprilTagSingleDetection) detection;

                    if (singleDet.metadata != null) {

                        telemetry.addLine(String.format(Locale.US, "\n==== (ID %d) %s", singleDet.id, singleDet.metadata.name));
                        telemetry.addLine(String.format(Locale.US, "XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                        telemetry.addLine(String.format(Locale.US, "PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                        telemetry.addLine(String.format(Locale.US,"RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));

                    } else {

                        telemetry.addLine(String.format(Locale.US, "\n==== (ID %d) Unknown", singleDet.id));
                        telemetry.addLine(String.format(Locale.US, "Center %6.0f %6.0f   (pixels)", singleDet.center.x, singleDet.center.y));
                    }

                } else {

                    AprilTagClusterDetection clusterDet = (AprilTagClusterDetection) detection;
                    telemetry.addLine(String.format(Locale.US, "\n==== Tag Cluster (%s)", clusterDet.metadata.name));
                    telemetry.addLine(String.format(Locale.US, "Percent tags found: %d", clusterDet.percentClusterFound));
                    telemetry.addLine(String.format(Locale.US, "XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                    telemetry.addLine(String.format(Locale.US, "PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                    telemetry.addLine(String.format(Locale.US, "RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));

                    if (Objects.equals(clusterDet.metadata.name, "BLUE AUDIENCE") && aprilPose != null && !Scheduler.isRunning(johndometryProcedure())) {

                        /// Old, keep for reference
                        /*aprilPose = new Pose3D(
                            new Position(
                                DistanceUnit.INCH,
                                detection.ftcPose.x,
                                detection.ftcPose.y,
                                detection.ftcPose.z,
                                0),
                            new YawPitchRollAngles(
                                AngleUnit.DEGREES,
                                detection.ftcPose.yaw,
                                detection.ftcPose.pitch,
                                detection.ftcPose.roll,
                                0)
                        );*/

                        /// The offsets will need modification.
                        /// This currently points to the exact position of the april tag
                        aprilPose = pfac.of(
                                follower.pose().x() + detection.ftcPose.y + 72,
                                follower.pose().y() + detection.ftcPose.x - 72 - 36,
                                follower.pose().heading() + detection.ftcPose.bearing
                        );
                        follower.setPose(strafeLeftIntermediate);
                        follower.update();
                        schedule(inOpModeProc());
                    }
                }
            }

            // Add "key" information to telemetry
            telemetry.addLine("\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.");
            telemetry.addLine("PRY = Pitch, Roll & Yaw (XYZ Rotation)");
            telemetry.addLine("RBE = Range, Bearing & Elevation");

            telemetry.update();

            follower.update();
            Scheduler.execute();
        }

        vision.close();
    }
}
