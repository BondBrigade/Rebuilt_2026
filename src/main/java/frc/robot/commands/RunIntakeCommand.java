// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Intake;
import edu.wpi.first.wpilibj2.command.Command;

/** RunIntakeCommand command that uses the Intake subsystem. */
public class RunIntakeCommand extends Command {
  @SuppressWarnings("PMD.UnusedPrivateField")
  private final Intake intakeSubsystem;
  private double inputSpeed;
  private boolean sourceIsTrigger = false;

  /**
   * Creates a new RunIntakeCommand.
   *
   * @param subsystem  The intake subsystem
   * @param inputSpeed The speed to run the intake, as a double
   */
  public RunIntakeCommand(Intake subsystem, double inputSpeed) {
    intakeSubsystem = subsystem;
    this.inputSpeed = inputSpeed;
    sourceIsTrigger = false;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  public RunIntakeCommand(Intake subsystem) {
    intakeSubsystem = subsystem;
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
      inputSpeed = intakeSubsystem.opInput_leftTrigger * -0.95;

    intakeSubsystem.runIntake(inputSpeed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intakeSubsystem.runIntake(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
