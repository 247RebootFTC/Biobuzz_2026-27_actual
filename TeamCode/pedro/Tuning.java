package org.firstinspires.ftc.teamcode.pedro;

public class Tuning {
    // Tuners go here
}

@Tuner
public static Procedure mecanumTuner() {

    return new MecanumTuner();
}

/*@Tuner
public static Procedure pinpointTuner() {
    return new PinpointTuner();
}*/

/*@Tuner
public static Procedure foresightTuner() {
    return new ForesightTuner((hardwareMap) -> new PinpointLocalizer(hardwareMap, Constants.localizerConfig), (hardwareMap) -> new Mecanum(hardwareMap, Constants.drivetrainConfig));
}*/

/*@Tuner
    public static Procedure tests() {
        return new Tests(hardwareMap -> new Mecanum(hardwareMap, Constants.drivetrainConfig), (hardwareMap -> new PinpointLocalizer(hardwareMap, Constants.localizerConfig)), () -> new Foresight(Constants.foresightConfig));
    }*/