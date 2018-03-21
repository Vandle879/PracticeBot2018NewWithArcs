package org.usfirst.frc.team558.robot.subsystems;

import org.usfirst.frc.team558.robot.commands.rollerClaw;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class rollerclaw extends Subsystem {

    TalonSRX rollerMotor1 = new TalonSRX(4);
    TalonSRX rollerMotor2 = new TalonSRX(5);
    
    
    public void setMotors(double rollerStick){
    	
    	rollerMotor1.set(ControlMode.PercentOutput, -rollerStick);
    	rollerMotor2.set(ControlMode.PercentOutput, rollerStick);
    	
    }
    
    
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new rollerClaw());
    }
}

