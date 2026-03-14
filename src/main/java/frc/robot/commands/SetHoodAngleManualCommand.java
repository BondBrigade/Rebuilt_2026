// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj2.command.Command;

/** SetHoodAngleCommand that uses the Conveyance subsystem. */
public class SetHoodAngleManualCommand extends Command {
  @SuppressWarnings("PMD.UnusedPrivateField")
  private final Shooter shooterSubsystem;
  private double speed;

  /**
   * Creates a new SetHoodAngleCommand.
   *
   * @param subsystem The subsystem used by this command.
   * @param targetAngle The speed to run the conveyance, as a double
   */
  public SetHoodAngleManualCommand(Shooter subsystem, double speed) {
    shooterSubsystem = subsystem;
    this.speed = speed;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    shooterSubsystem.setHoodAngleManual(speed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
     shooterSubsystem.setHoodAngleManual(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
