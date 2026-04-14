// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.subsystems.Staging;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;

/** RunStagingCommand that uses the Conveyance subsystem. */
public class RunStagingCommand extends Command {
  @SuppressWarnings("PMD.UnusedPrivateField")
  private final Staging stagingSubsystem;
  private double inputSpeed;

  /**
   * Creates a new RunStagingCommand.
   *
   * @param subsystem  The subsystem used by this command.
   * @param inputSpeed The speed to run the conveyance, as a double
   */
  public RunStagingCommand(Staging subsystem) {
    stagingSubsystem = subsystem;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    inputSpeed = stagingSubsystem.opInput_RightY;
    if (DriverStation.isAutonomous()) {
      inputSpeed = Constants.Shooter.autoStagingSpeed;
    }
    stagingSubsystem.runStagingMotor(inputSpeed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    stagingSubsystem.runStagingMotor(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
