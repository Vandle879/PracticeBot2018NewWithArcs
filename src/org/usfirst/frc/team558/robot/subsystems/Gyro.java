package org.usfirst.frc.team558.robot.subsystems;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Subsystem;
import com.kauailabs.navx.frc.*;


/**
 *
 */
public class Gyro extends Subsystem {

    AHRS gyro = new AHRS(SPI.Port.kMXP);

    public double getGyro(){
    	
    	return gyro.getAngle();
    	
    }
    
    public void resetGyro(){
    	
    	gyro.reset();
    	
    }
    
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

