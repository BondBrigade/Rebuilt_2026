// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkLowLevel.MotorType;

public class Shooter extends SubsystemBase {
  private SparkMax shooterMotor;
  private SparkMax hoodAngleMotor;
  private DutyCycleEncoder hoodAngleEncoder;

  private SparkMaxConfig config;

  private SparkClosedLoopController shooter_pidController;
  private final PIDController angle_pidController;

  public double opInput_rightTrigger = 0;

  /** Creates a new Shooter Subsystem. */
  public Shooter() {
    shooterMotor = new SparkMax(Constants.Shooter.shooterMotorId, MotorType.kBrushless);
    hoodAngleMotor = new SparkMax(Constants.Shooter.hoodAngleMotorId, MotorType.kBrushless);
    hoodAngleEncoder = new DutyCycleEncoder(new DigitalInput(Constants.Shooter.digitalInputChannel));

    // Angle PID
    angle_pidController = 
      new PIDController(Constants.Shooter.angle_kP, Constants.Shooter.angle_kI, Constants.Shooter.angle_kD);

    // Shooter PID
    shooter_pidController = shooterMotor.getClosedLoopController();
    config = new SparkMaxConfig();
    config.closedLoop
      .p(Constants.Shooter.shooter_kP)
      .i(Constants.Shooter.shooter_kI)
      .d(Constants.Shooter.shooter_kD)
      .outputRange(-1, 1);
  }


  public void runShooterManual(double speed)
  {
    shooterMotor.set(speed);
  }

  public void setHoodAngle(double targetAngle)
  {
    // Calculate PID output using encoder (current angle) and target angle
    double output = angle_pidController.calculate(hoodAngleEncoder.get(), targetAngle);
    hoodAngleMotor.set(output);
  }

  public void setHoodAngleManual(double speed){
    hoodAngleMotor.set(speed);
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
   * An example method querying a boolean state of the subsystem (for example, a digital sensor).
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

  public void setRightTrigger(double arg)
  {
    opInput_rightTrigger = arg;
  }
    public Runnable autoRun(double arg)
  {
    opInput_rightTrigger = arg;
    return null;
  }

}
