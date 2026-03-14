package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Staging extends SubsystemBase
{

  private SparkMax stagingMotor;
  public double opInput_rightY = 0;

  public Staging()
  {
    stagingMotor = new SparkMax(Constants.Shooter.stagingMotorId, MotorType.kBrushless);
  }

  public void runStagingMotor(double speed)
  {
    stagingMotor.set(speed);
  }

  public void setRightY(double arg)
  {
    opInput_rightY = arg;
  }

}
