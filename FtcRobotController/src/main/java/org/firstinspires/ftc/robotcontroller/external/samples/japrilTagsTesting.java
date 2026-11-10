package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCompatibilityManager;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.usb.UsbConstants;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagSingleDetection;


import java.util.List;

@Autonomous
public class japrilTagsTesting extends LinearOpMode {

    private static final boolean USE_WEBCAM = true;

    private AprilTagProcessor april;

    private VisionPortal vision;

    static final int VID = 0x046d;
    static final int PID = 0x0825;

    public void runOpMode() throws InterruptedException {

        CameraCompatibilityManager
                .getInstance()
                .addQuirk(VID, PID, CameraCompatibilityManager.Quirk.AVOID_LIB_USB_RESET_DEVICE);

        // This has something to do with initializing the april tag pipeline
        april = new AprilTagProcessor.Builder().build();
        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        builder.addProcessor(april);
        vision = builder.build();

        while (opModeIsActive()) {



        }

        vision.close();
    }
}
