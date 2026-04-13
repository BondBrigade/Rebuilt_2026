// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;

public class Intake extends SubsystemBase {
  private SparkMax deployIntakeMotor;
  private SparkMax intakeMotor;

  public double opInput_leftTrigger = 0;
  public double opInput_leftY = 0;

  /** Creates a new Intake Subsystem. */
  public Intake() {
    deployIntakeMotor = new SparkMax(Constants.Intake.deployIntakeMotorId, MotorType.kBrushless);
    intakeMotor = new SparkMax(Constants.Intake.intakeMotorId, MotorType.kBrushless);
  }

  public void deployIntake(double speed) {
    this.deployIntakeMotor.set(speed);
  }

  public void runIntake(double inputSpeed) {
    this.intakeMotor.set(-inputSpeed);
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

  public void setLeftTrigger(double arg) {
    opInput_leftTrigger = arg;
  }

  public void setLeftY(double arg) {
    opInput_leftY = arg;
  }

}
