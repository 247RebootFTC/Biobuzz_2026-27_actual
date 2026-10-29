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
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.REVERSE);
    });

    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("odo");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_SWINGARM_POD);
        //c.xPodOffset.set(-3.0604430071012247);
        //c.yPodOffset.set(-3.775276124008059);
        c.xPodOffset.set(-3.5153856052188424);
        c.yPodOffset.set(-5.215880026028851);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

    /*public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(2.398817661754544);
                Controller secondaryTranslationalForward = Controller.proportional(0.8862990284298796);
                Controller primaryTranslationalLateral = Controller.proportional(1.7756044017902015);
                Controller secondaryTranslationalLateral = Controller.proportional(0.6560383814380562);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.02189244028705782));
                c.brake.set(Controller.proportionalFeedforward(0.018608574243999147));

                c.headingFeedback.set(Controller.proportional(5.013275433518399));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.05088874496993908, 0.005263408744165265));

                c.linearBrakeCoefficients.set(Matrix.diag(0.06624969472196371, 0.04320013462411325));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0018902827668816047, 0.002250737649254237));

                c.maxAchievableForwardVelocity.set(48.05897034398198);
                c.maxAchievableStrafeVelocity.set(42.06720947611923);
                c.naturalForwardDeceleration.set(38.55236299190259);
                c.naturalStrafeDeceleration.set(55.49186877730452);
            }
    );*/

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.4241423226602652);
                Controller secondaryTranslationalForward = Controller.proportional(0.1567092549313783);
                Controller primaryTranslationalLateral = Controller.proportional(0.2964877744672423);
                Controller secondaryTranslationalLateral = Controller.proportional(0.10954431036640518);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.016513966789279716));
                c.brake.set(Controller.proportionalFeedforward(0.014036871770887758));

                c.headingFeedback.set(Controller.proportional(2.5038254203134707));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.04200578795628257, 0.00808172701829989));

                c.linearBrakeCoefficients.set(Matrix.diag(0.02245701045106749, 0.029973411341470396));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.002900427144088262, 0.0027328643610889367));

                c.maxAchievableForwardVelocity.set(62.99285536070221);
                c.maxAchievableStrafeVelocity.set(54.62903730602858);
                c.naturalForwardDeceleration.set(38.11120388973891);
                c.naturalStrafeDeceleration.set(54.83799209806073);
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

