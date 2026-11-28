package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.math.Vector2D;
import com.pedropathing.math.Matrix;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.algorithm.Foresight;

public class Constants {

    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("frontLeft");
        c.frontRightName.set("frontRight");
        c.backLeftName.set("backLeft");
        c.backRightName.set("backRight");
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backRightDirection.set(DcMotorSimple.Direction.REVERSE);
    });

    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("odo");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(-4.543572373277559);
        c.yPodOffset.set(-1.6410057188019038);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.24083000077200548);
                Controller secondaryTranslationalForward = Controller.proportional(0.08898025018911854);
                Controller primaryTranslationalLateral = Controller.proportional(0.4292736098766498);
                Controller secondaryTranslationalLateral = Controller.proportional(0.15860512844731275);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.02479317260502404));
                c.brake.set(Controller.proportionalFeedforward(0.021074196714270434));

                c.headingFeedback.set(Controller.proportional(1.935622576817103));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.04021635562304475, 0.008585457686599587));

                c.linearBrakeCoefficients.set(Matrix.diag(0.06770559339671736, 0.06789669359155395));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.002449600096934954, 0.0024402518220625383));

                c.maxAchievableForwardVelocity.set(41.48012640827032);
                c.maxAchievableStrafeVelocity.set(36.38605825532221);
                c.naturalForwardDeceleration.set(28.048887184382245);
                c.naturalStrafeDeceleration.set(37.06181779578513);
            }
    );

    public static Follower create(HardwareMap h) {
        return new Follower(
                new PinpointLocalizer(h, localizerConfig),
                new Mecanum(h, drivetrainConfig),
                new Foresight(foresightConfig)
        );
    }
}

