// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

/** RunShooterManualCommand that uses the Shooter subsystem. */
public class RunShooterManualCommand extends Command {
  @SuppressWarnings("PMD.UnusedPrivateField")
  private final Shooter shooterSubsystem;
  private double inputSpeed;

  private boolean sourceIsTrigger = false;

  /**
   * Creates a new RunShooterManualCommand.
   *
   * @param subsystem The subsystem used by this command.
   * @param speed     The speed to run the motor, in reverse as a percentage
   */
  public RunShooterManualCommand(Shooter subsystem, double inputSpeed) {
    shooterSubsystem = subsystem;
    this.inputSpeed = inputSpeed;
    sourceIsTrigger = false;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  public RunShooterManualCommand(Shooter subsystem) {
    shooterSubsystem = subsystem;
    sourceIsTrigger = true;
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
    if (sourceIsTrigger)
      inputSpeed = shooterSubsystem.opInput_rightTrigger * 0.95;

    shooterSubsystem.runShooterManual(inputSpeed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooterSubsystem.runShooterManual(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
