// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import swervelib.math.Matter;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean
 * constants. This class should not be used for any other purpose. All constants
 * should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static class Controller {
    public static final int kDriverControllerPort = 0;
    public static final int kOperatorControllerPort = 1;
    public static final double kThreshold = 0.15;
    public static final double kMinThreshold = 0.01;
    public static final double turnConstant = 6;
  }

  public static class Intake {
    public static final int intakeMotorId = 21;
    public static final int deployIntakeMotorId = 22;
    public static final double deploySpeed = 0.6;
    public static final double runIntakeReverse = 0.55;
    public static final double intakeSpeedModifier = 0.75;
  }

  public static class Conveyance {
    public static final int conveyanceMotorId = 23;
    public static final double conveyanceSpeedModifier = 0.8;
    public static final double conveyanceAutoSpeed = -0.8;
  }

  public static class Shooter {
    public static final int stagingMotorId = 31;
    public static final int hoodAngleMotorId = 32;
    public static final int shooterMotorId = 33;
    public static final int digitalInputChannel = 0;
    public static final double shooter_kP = 1.0 / 6784.0;
    public static final double shooter_kI = 0;
    public static final double shooter_kD = 0;
    public static final double angle_kP = 0.1;
    public static final double angle_kI = 0.0001;
    public static final double angle_kD = 0;
    public static final double stagingSpeedModifier = 0.5;
    public static final int shooterMaxVelocity = 5676;
    public static final int shooterVelocity = 3000;
    public static final int shooterCoasting = 1500;
    public static final double longShotAngle = 0.075; // FIXME
    public static final double closeShotAngle = 0.6; // FIXME
    public static final double autoStagingSpeed=  -0.5;
  }

  public static class Swerve {
    public static final double deadband = 0.1;
    public static final double maxSpeed = Units.feetToMeters(10);
    public static final double robotMass = 137.0 * 0.453592; // 32lbs * kg per pound
    public static final double loopTime = 0.13; // s,20ms + 110ms sprk max velocity lag
    public static final Matter chassis = new Matter(new Translation3d(0, 0, Units.inchesToMeters(8)), robotMass);
    public static final double slowModeModifier=0.45;
  }
  // "p": 0.003575,

}
