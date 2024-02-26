// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.RobotContainer;

public class Elevator extends SubsystemBase {
  CANSparkMax elevatorMotor;
  CANSparkMax rollerIntakeMotor;
  DigitalInput climbLimitSwitch;
  XboxController controller;
  int dpad;

  /** Creates a new Elevator. */
  public Elevator() {
    elevatorMotor = new CANSparkMax(Constants.ClimbConstants.elevatorMotorID, CANSparkLowLevel.MotorType.kBrushed);
    rollerIntakeMotor = new CANSparkMax(Constants.ClimbConstants.rollerIntakeMotorID, CANSparkLowLevel.MotorType.kBrushless);
    elevatorMotor.setIdleMode(IdleMode.kBrake);
    controller = RobotContainer.mainController;

    // climbLimitSwitch = new DigitalInput(1);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    dpad = controller.getPOV();
    // System.out.println(dpad);

    if (dpad == 45 || dpad == 315 || dpad == 0) {
      elevatorMotorForward();
    } else if (dpad == 135 || dpad == 225 || dpad == 180) {
      elevatorMotorReverse();
    } else {
      elevatorMotorStop();
    }
  }

  public void elevatorMotorForward() {
    elevatorMotor.set(-Constants.ClimbConstants.elevatorSpeed);
  }
    
  public void elevatorMotorReverse() {
    // if (!climbLimitSwitch.get()) {
      elevatorMotor.set(Constants.ClimbConstants.elevatorSpeed);
    // }
  }

  public void runRollerIntake() {
    rollerIntakeMotor.set(Constants.ClimbConstants.intakeSpeed);
  }

  public void stopRollerIntake() {
    rollerIntakeMotor.set(0);
  }

  public void elevatorMotorStop() {
    elevatorMotor.set(0);
  }

  public Command runIntake() {
    return runEnd(this::runRollerIntake, this::stopRollerIntake);
  }

  public Command elevatorUp() {
    return runEnd(this::elevatorMotorForward, this::elevatorMotorStop);
  }

  public Command elevatorDown() {
    return runEnd(this::elevatorMotorReverse, this::elevatorMotorStop);
  }


}
