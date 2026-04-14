// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.io.File;
import java.util.Set;
import java.util.function.Supplier;

import frc.robot.commands.DeployIntakeCommand;
import frc.robot.commands.RunConveyanceCommand;
import frc.robot.commands.RunIntakeCommand;
import frc.robot.commands.RunShooterManualCommand;
import frc.robot.commands.RunStagingCommand;
import frc.robot.commands.SetHoodAngleManualCommand;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Conveyance;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Staging;
import frc.robot.subsystems.SwerveSubsystem;
import swervelib.SwerveInputStream;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.math.util.Units;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // Define Swerve file
  // private File file = new File(Filesystem.getDeployDirectory(), "swerve");
  // The robot's subsystems and commands are defined here
  private final Intake intakeSubsystem = new Intake();
  private final Conveyance conveyanceSubsystem = new Conveyance();
  private final Shooter shooterSubsystem = new Shooter();
  private final Staging stagingSubsystem = new Staging();

  private final SendableChooser<Command> autoChooser;
  {
    // Add commands to the autonomous command chooser

    // Registers Commands for PathPlanner
    // Subsystem initialization
    // conveyanceSubsystem = new Conveyance();
    // shooterSubsystem = new Shooter();

    // Register Named Commands
    // NamedCommands.registerCommand("RunIntakeCommand", new
    // RunIntakeCommand(intakeSubsystem));
    // // CF Note: Changed this from a Shooter method call to `new
    // RunShooterManualCommand` to stop the runtime errors
    // NamedCommands.registerCommand("RunShooterManualCommand", new
    // RunShooterManualCommand(shooterSubsystem,0.8));
    // NamedCommands.registerCommand("RunStagingCommand",
    // Staging.RunStagingCommand());
    // NamedCommands.registerCommand("someOtherCommand", new SomeOtherCommand());

    // Do all other initialization
    // configureButtonBindings();
  }
  // ...

  // Controllers
  private final CommandXboxController driverController = new CommandXboxController(
      Constants.Controller.kDriverControllerPort);
  private final CommandXboxController operatorController = new CommandXboxController(
      Constants.Controller.kOperatorControllerPort);

  private double opInput_leftY = 0;
  private double opInput_rightY = 0;
  private double opInput_leftTrigger = 0;
  private double opInput_rightTrigger = 0;

  // The robot's subsystems and commands are defined here...
  private final SwerveSubsystem drivebase = new SwerveSubsystem(
      new File(Filesystem.getDeployDirectory(), "swerve/neo"));

  // This class stores certian settings that modify driving, such as slow mode. It
  // is done as a class so that java can access it as a reference and be mutated
  // by controller buttons.
  class ModdedDriveState {
    public boolean slowmode = false;
  }

  ModdedDriveState driveStateMods = new ModdedDriveState();

  /**
   * Converts driver input into a field-relative ChassisSpeeds that is controlled
   * by angular velocity.
   */
  SwerveInputStream driveAngularVelocity = SwerveInputStream.of(
      drivebase.getSwerveDrive(),
      () -> (driverController.getLeftY() * -1 * (driveStateMods.slowmode ? Constants.Swerve.slowModeModifier : 1.0)),
      () -> driverController.getLeftX() * -1 * (driveStateMods.slowmode ? Constants.Swerve.slowModeModifier : 1.0))
      .withControllerRotationAxis(
          () -> driverController.getRightX() * -1 * (driveStateMods.slowmode ? Constants.Swerve.slowModeModifier : 1.0))
      .deadband(Constants.Controller.kThreshold)
      .scaleTranslation(0.8)
      .allianceRelativeControl(true);

  /**
   * Clone's the angular velocity input stream and converts it to a fieldRelative
   * input stream.
   */
  SwerveInputStream driveDirectAngle = driveAngularVelocity.copy()
      .withControllerHeadingAxis(driverController::getRightX, driverController::getRightY)
      .headingWhile(true);

  /**
   * Clone's the angular velocity input stream and converts it to a robotRelative
   * input stream.
   */
  SwerveInputStream driveRobotOriented = driveAngularVelocity.copy()
      .robotRelative(true)
      .allianceRelativeControl(false);

  SwerveInputStream driveAngularVelocityKeyboard = SwerveInputStream.of(drivebase.getSwerveDrive(),
      () -> -driverController.getLeftY(),
      () -> -driverController.getLeftX())
      .withControllerRotationAxis(
          () -> driverController.getRawAxis(2))
      .deadband(Constants.Controller.kThreshold)
      .scaleTranslation(0.8)
      .allianceRelativeControl(true);

  // Derive the heading axis with math!
  SwerveInputStream driveDirectAngleKeyboard = driveAngularVelocityKeyboard.copy()
      .withControllerHeadingAxis(
          () -> Math.sin(
              driverController.getRawAxis(2) * Math.PI) * (Math.PI * 2),
          () -> Math.cos(
              driverController.getRawAxis(2) * Math.PI) * (Math.PI * 2))
      .headingWhile(true)
      .translationHeadingOffset(true)
      .translationHeadingOffset(Rotation2d.fromDegrees(0));

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    NamedCommands.registerCommand("RunIntakeCommand", new RunIntakeCommand(intakeSubsystem));
    NamedCommands.registerCommand("RunStagingCommand", new RunStagingCommand(stagingSubsystem));
    NamedCommands.registerCommand("RunConveyanceCommand", new RunConveyanceCommand(conveyanceSubsystem));
    NamedCommands.registerCommand("RunDeployCommand", new DeployIntakeCommand(intakeSubsystem, -0.25).withTimeout(1.0));
    NamedCommands.registerCommand("RunShooterManualCommand", new RunShooterManualCommand(shooterSubsystem, 0.8));
    configureBindings();

    drivebase.setupPathPlanner();

    autoChooser = AutoBuilder.buildAutoChooser();

    // autoChooser.addOption("Drive Back", DriveBackwardTimed());

    // Set the default auto (do nothing)

    // Put the autoChooser on the SmartDashboard
    SmartDashboard.putData("Auto Chooser", autoChooser);
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be
   * created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with
   * an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for
   * {@link
   * CommandXboxController
   * Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or
   * {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    SmartDashboard.putNumber("shooterspeed", 0.9);
    // EXAMPLES
    // trigger_leftBumper.onTrue(new InstantCommand(() ->
    // intakeSubsystem.runIntake(Constants.IntakeConstants.runIntakeReverse)));
    // Schedule `exampleMethodCommand` when the Xbox controller's B button is
    // pressed,
    // cancelling on release.
    // m_driverController.b().whileTrue(m_exampleSubsystem.exampleMethodCommand());

    /*******************************
     * INTAKE TRIGGERS
     *******************************/
    // Run Intake
    Trigger opTrigger_leftTrigger = operatorController.leftTrigger(Constants.Controller.kMinThreshold);
    // Trigger opTrigger_leftTrigger = new Trigger(() ->
    // Math.abs(opInput_leftTrigger) > Constants.Controller.kMinThreshold);
    // Run Intake Reverse
    Trigger opTrigger_leftBumper = new Trigger(() -> operatorController.leftBumper().getAsBoolean());
    // Deploy Intake
    Trigger opTrigger_up = new Trigger(() -> operatorController.povUp().getAsBoolean());
    Trigger opTrigger_down = new Trigger(() -> operatorController.povDown().getAsBoolean());

    /*******************************
     * INTAKE BINDINGS
     *******************************/
    // Run Intake
    opTrigger_leftTrigger.whileTrue(new RunIntakeCommand(intakeSubsystem));
    // Run Intake Reverse
    opTrigger_leftBumper.whileTrue(new RunIntakeCommand(intakeSubsystem, 0.75));
    // Deploy Intake - OUT
    opTrigger_up.whileTrue(new DeployIntakeCommand(intakeSubsystem, 0.25));
    // Deploy Intake - IN
    opTrigger_down.whileTrue(new DeployIntakeCommand(intakeSubsystem, -0.25));

    /*******************************
     * CONVEYANCE TRIGGERS
     *******************************/
    // Run Conveyance (fwd & rev)
    Trigger opTrigger_leftY = operatorController.axisMagnitudeGreaterThan(1, Constants.Controller.kThreshold);

    /*******************************
     * CONVEYANCE BINDINGS
     *******************************/
    // Run Conveyance (fwd & rev)
    opTrigger_leftY.whileTrue(new RunConveyanceCommand(conveyanceSubsystem));

    /*******************************
     * SHOOTER TRIGGERS
     *******************************/
    // Run Staging Motor
    Trigger opTrigger_rightY = operatorController.axisMagnitudeGreaterThan(5, Constants.Controller.kThreshold);

    opTrigger_rightY.whileTrue(new RunConveyanceCommand(conveyanceSubsystem));
    // Reverse Shooter
    // Trigger opTrigger_rightBumper = new Trigger(() ->
    // operatorController.rightBumper().getAsBoolean());
    // Run Shooter
    Trigger opTrigger_rightTrigger = operatorController.rightTrigger(Constants.Controller.kMinThreshold);
    /*******************************
     * SHOOTER BINDINGS
     *******************************/
    // Run Staging Motor
    // FIXME: add target velocity conditional
    opTrigger_rightY.whileTrue(new RunStagingCommand(stagingSubsystem));
    // Run Shooter
    opTrigger_rightTrigger.toggleOnTrue(Commands.defer(
        new Supplier<Command>() {
          double ll_val = 0.0;

          @Override
          public Command get() {
            if (LimelightHelpers.getTA("limelight") != 0.0) {
              ll_val = LimelightHelpers.getTA("limelight")
                  * drivebase.getPose().getRotation()
                  //.plus(Rotation2d.k180deg)
                  .getCos();
            }
            System.out.println(ll_val);

            return new RunShooterManualCommand(shooterSubsystem, Math.max(ll_val * -0.937 + 0.807, 0.62));
          }
        },
        Set.of(shooterSubsystem)));
    // Reverse Shooter
    operatorController.rightBumper().whileTrue(new RunShooterManualCommand(shooterSubsystem, -0.5));
    // Change Angle - Long Shot
    operatorController.b().whileTrue(new SetHoodAngleManualCommand(shooterSubsystem, 0.15));
    // Change Angle - Close Shot
    operatorController.a().whileTrue(new SetHoodAngleManualCommand(shooterSubsystem, -0.15));

    /*******************************
     * Swerve BINDINGS
     *******************************/
    Command driveFieldOrientedDirectAngle = drivebase.driveFieldOriented(driveDirectAngle);
    Command driveFieldOrientedAnglularVelocity = drivebase.driveFieldOriented(driveAngularVelocity);
    Command driveRobotOrientedAngularVelocity = drivebase.driveFieldOriented(driveRobotOriented);
    Command driveFieldOrientedDirectAngleKeyboard = drivebase.driveFieldOriented(driveDirectAngleKeyboard);
    Command driveFieldOrientedAnglularVelocityKeyboard = drivebase.driveFieldOriented(driveAngularVelocityKeyboard);
    driverController.y()
        .toggleOnTrue(Commands.runEnd(() -> driveStateMods.slowmode = true, () -> driveStateMods.slowmode = false));

    if (RobotBase.isSimulation()) {
      drivebase.setDefaultCommand(driveFieldOrientedDirectAngleKeyboard);
    } else {
      drivebase.setDefaultCommand(driveFieldOrientedAnglularVelocity);
    }

    if (Robot.isSimulation()) {
      Pose2d target = new Pose2d(new Translation2d(1, 4),
          Rotation2d.fromDegrees(90));
      // drivebase.getSwerveDrive().field.getObject("targetPose").setPose(target);
      driveDirectAngleKeyboard.driveToPose(() -> target,
          new ProfiledPIDController(5,
              0,
              0,
              new Constraints(5, 2)),
          new ProfiledPIDController(5,
              0,
              0,
              new Constraints(Units.degreesToRadians(360),
                  Units.degreesToRadians(180))));
      driverController.start().onTrue(Commands.runOnce(
          () -> drivebase.resetOdometry(new Pose2d(3, 3, new Rotation2d()))));
      driverController.button(1).whileTrue(drivebase.sysIdDriveMotorCommand());
      driverController.button(2).whileTrue(Commands.runEnd(
          () -> driveDirectAngleKeyboard.driveToPoseEnabled(true),
          () -> driveDirectAngleKeyboard.driveToPoseEnabled(false)));
    }
    if (DriverStation.isTest()) {
      drivebase.setDefaultCommand(driveFieldOrientedAnglularVelocity); // Overrides drive command above!

      driverController.x().whileTrue(Commands.runOnce(drivebase::lock, drivebase).repeatedly());
      driverController.start().onTrue((Commands.runOnce(drivebase::zeroGyro)));
      driverController.back().whileTrue(drivebase.centerModulesCommand());
      driverController.leftBumper().onTrue(Commands.none());
      driverController.rightBumper().onTrue(Commands.none());
    } else {
      driverController.a().onTrue((Commands.runOnce(drivebase::zeroGyro)));
      driverController.start().whileTrue(Commands.none());
      driverController.back().whileTrue(Commands.none());
      driverController.leftBumper().whileTrue(Commands.runOnce(drivebase::lock, drivebase).repeatedly());
      driverController.rightBumper().onTrue(Commands.none());
    }

    // /**
    // * Use this to pass the autonomous command to the main {@link Robot} class.
    // *
    // * @return the command to run in autonomous
    // */
    // public Command getAutonomousCommand()
    // {
    // // Pass in the selected auto from the SmartDashboard as our desired autnomous
    // commmand
    // return autoChooser.getSelected();
    // }

    // public void setMotorBrake(boolean brake)
    // {
    // drivebase.setMotorBrake(brake);
    // }
    // // SwerveInputStream angularVelocity;

  }

  public void periodic() {
    opInput_leftY = operatorController.getLeftY();
    opInput_rightY = operatorController.getRightY();
    opInput_leftTrigger = operatorController.getLeftTriggerAxis();
    opInput_rightTrigger = operatorController.getRightTriggerAxis();

    shooterSubsystem.setRightTrigger(opInput_rightTrigger);
    intakeSubsystem.setLeftTrigger(opInput_leftTrigger * Constants.Intake.intakeSpeedModifier);
    // intakeSubsystem.setLeftY(opInput_leftY);
    conveyanceSubsystem.setRightY(opInput_rightY * Constants.Conveyance.conveyanceSpeedModifier);
    stagingSubsystem.setRightY(opInput_rightY * Constants.Shooter.stagingSpeedModifier);
    conveyanceSubsystem.setLeftY(opInput_leftY);
    // stagingSubsystem.setRightY(opInput_rightY);
  }

  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
    // return new PathPlannerAuto("Test Auto");
  }
}
