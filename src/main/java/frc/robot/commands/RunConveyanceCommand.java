// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.subsystems.Conveyance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

/** RunConveyanceCommand that uses the Conveyance subsystem. */
public class RunConveyanceCommand extends Command {
  @SuppressWarnings("PMD.UnusedPrivateField")
  private final Conveyance conveyanceSubsystem;
  private double inputSpeed;

  /**
   * Creates a new RunConveyanceCommand.
   *
   * @param subsystem The subsystem used by this command.
   * @param inputSpeed The speed to run the conveyance, as a double
   */
  public RunConveyanceCommand(Conveyance subsystem) {
    conveyanceSubsystem = subsystem;
    // this.inputSpeed = inputSpeed;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    double right = conveyanceSubsystem.opInput_RightY;
    // double left = conveyanceSubsystem.opInput_LeftY;

    if (right > 0.1 || right < -0.1) inputSpeed = right; 
    else                             inputSpeed = conveyanceSubsystem.opInput_LeftY;
    // if (left > 0.1 || left < -0.1) inputSpeed = left; 

    // if (Math.abs(right) > 0.1)
    // {
    //   inputSpeed = right;
    // }
    // else if (Math.abs(left) > 0.1)
    // {
    //   inputSpeed = left;
    // }
    // else 
    // {
    //   inputSpeed = 0;
    // }

    conveyanceSubsystem.runConveyance(inputSpeed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    conveyanceSubsystem.runConveyance(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
