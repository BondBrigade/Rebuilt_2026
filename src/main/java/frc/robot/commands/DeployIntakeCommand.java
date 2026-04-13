// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.subsystems.Intake;
import edu.wpi.first.wpilibj2.command.Command;

/** DeployIntakeCommand command that uses the Intake subsystem. */
public class DeployIntakeCommand extends Command {
  @SuppressWarnings("PMD.UnusedPrivateField")
  private final Intake intakeSubsystem;
  private final double speed;

  /**
   * Creates a new DeployIntakeCommand.
   *
   * @param subsystem The intake subsystem
   * @param speed     as a percentage of power to set motor speed to
   */
  public DeployIntakeCommand(Intake subsystem, double speed) {
    intakeSubsystem = subsystem;
    this.speed = speed;
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
    intakeSubsystem.deployIntake(speed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intakeSubsystem.deployIntake(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
