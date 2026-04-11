package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Staging extends SubsystemBase
{

  private SparkMax stagingMotor;
  public double opInput_RightY = 0;

  public Staging()
  {
    stagingMotor = new SparkMax(Constants.Shooter.stagingMotorId, MotorType.kBrushless);
  }

  public void runStagingMotor(double speed)
  {
    this.stagingMotor.set(speed);
  }

  public void setRightY(double arg)
  {
    opInput_RightY = arg;
  }

  public static Command RunStagingCommand() {
    // TODO Auto-generated method stub
//    throw new UnsupportedOperationException("Unimplemented method 'RunStagingCommand'");
// CF Note: Changed to Commands.none() to stop runtime errors. 
return Commands.none(); }
  

}
