// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.ClimbConstants;

public class Climb extends SubsystemBase {
  private CANSparkMax leftClimb;
  private CANSparkMax rightClimb;
  private RelativeEncoder leftClimbEncoder;
  private SparkPIDController pidController;
  private double elevatorRotations;


  /** Creates a new Climb. */
  public Climb() {

    leftClimb = new CANSparkMax(ClimbConstants.leftClimbMotorID, CANSparkLowLevel.MotorType.kBrushless);
    rightClimb = new CANSparkMax(ClimbConstants.rightClimbMotorID, CANSparkLowLevel.MotorType.kBrushless);

    pidController = leftClimb.getPIDController();

    pidController.setP(0.02);
    leftClimbEncoder = leftClimb.getEncoder();
    leftClimbEncoder.setPositionConversionFactor(0.50);


    leftClimb.setIdleMode(IdleMode.kBrake);
    rightClimb.follow(leftClimb);
  }
  
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("elevator rotations", this.getElevatorRotation());
    elevatorRotations = leftClimbEncoder.getPosition();
    
  }

  public double getElevatorRotation() {
    return leftClimbEncoder.getPosition();
  }

  public void climbMotorForward() {
      // pidController.setReference(
      //   ClimbConstants.rotations, 
      //   CANSparkBase.ControlType.kPosition);
    
    leftClimb.set(Constants.ClimbConstants.climbSpeed);
  }
    
  public void climbMotorReverse() {
      // pidController.setReference(
      //   -ClimbConstants.rotations, 
      //   CANSparkBase.ControlType.kPosition);

    leftClimb.set(-Constants.ClimbConstants.climbSpeed);
  }

  public void climbMotorStop() {
    leftClimb.set(0);
  }

  public Command climbUp() {
    return runEnd(this::climbMotorForward, this::climbMotorStop);
  }

  public Command climbDown() {
    return runEnd(this::climbMotorReverse, this::climbMotorStop);
  }
}