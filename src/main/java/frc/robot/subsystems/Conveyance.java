// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

public class Conveyance extends SubsystemBase {
  private SparkMax conveyanceMotor;
  public double opInput_RightY = 0;
  public double opInput_LeftY = 0;

  /** Creates a new Conveyance Subsystem. */
  public Conveyance() {
    conveyanceMotor = new SparkMax(Constants.Conveyance.conveyanceMotorId, MotorType.kBrushless);
  }

  public void runConveyance(double speed) {
    this.conveyanceMotor.set(speed);
  }

  /**
   * Example command factory method.
   *
   * @return a command
   */
  public Command exampleMethodCommand() {
    // Inline construction of command goes here.
    // Subsystem::RunOnce implicitly requires `this` subsystem.
    return runOnce(
        () -> {
          /* one-time action goes here */
        });
  }

  /**
   * An example method querying a boolean state of the subsystem (for example, a
   * digital sensor).
   *
   * @return value of some boolean subsystem state, such as a digital sensor.
   */
  public boolean exampleCondition() {
    // Query some boolean state, such as a digital sensor.
    return false;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }

  public void setLeftY(double arg) {
    opInput_LeftY = arg;
  }

  public void setRightY(double arg) {
    opInput_RightY = arg;
  }

  public Runnable autoRun(double arg) {
    opInput_LeftY = arg;
    opInput_RightY = arg * Constants.Conveyance.conveyanceSpeedModifier;
    return null;
  }

  public static Command RunConveyanceCommand() {
    // TODO Auto-generated method stub
    return Commands.print("Unimplemented method 'RunConveyanceCommand'. I will be merciful and not crash your code. ").repeatedly();
  }

}
