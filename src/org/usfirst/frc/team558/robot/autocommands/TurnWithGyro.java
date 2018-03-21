package org.usfirst.frc.team558.robot.autocommands;

import org.usfirst.frc.team558.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;


//This class still needs work, proper proportional gains were not found to accurately use
// a tolerance to end the command and instead a set time was used. 


public class TurnWithGyro extends Command {

	private double desiredAngle;
	private double error;
	private double mKp;
	private double tolerance = 1;
	private double pidSpeed;
	private double mMaxSpeed;
	
    public TurnWithGyro(double aAngle, double aMaxSpeed, double aKp) {
        requires(Robot.Drivetrain);
        requires(Robot.gyro);
        this.desiredAngle = aAngle;
        this.mMaxSpeed = aMaxSpeed;
        this.mKp = aKp;
        setTimeout(2);
        
    }

    protected void initialize() {
    	
    
    }


    protected void execute() {
    	error = Math.abs(Math.abs(desiredAngle) - Math.abs(Robot.gyro.getGyro()));
    	
    	if (mKp * error >= mMaxSpeed){
    		pidSpeed = mMaxSpeed;
    	}
    	else {
    		pidSpeed = mKp * error;
    	}

  
    	if (Robot.gyro.getGyro() > desiredAngle)
        {
    		Robot.Drivetrain.drive(pidSpeed, pidSpeed);
        }
        else
        {
        	Robot.Drivetrain.drive(-pidSpeed, -pidSpeed);
        }
    	
    	
    }


    protected boolean isFinished() {
    	return isTimedOut(); //((Math.abs(error) <= tolerance) || isTimedOut());
    }


    protected void end() {
    	Robot.Drivetrain.drive(0, 0);
    }


    protected void interrupted() {
    }
}