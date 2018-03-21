package org.usfirst.frc.team558.robot.autocommands;

import org.usfirst.frc.team558.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;



public class DriveWithEncoder extends Command {

	private double mTime;
	private double mSpeed;
	private double mDistance; // inches
	private double mKp;
	
	private double error;
	private double tolerance = .5;
	private double pidSpeed;
	
	
	 /**
     * Drive robot based off set encoder distance
     * 
     * @param aDistance
     *            Distance for robot to travel in inches
     * @param aSpeed
     *            Max speed for robot to travel -1 to 1
     * @param aTime
     *            Max time till timed out in seconds
     * @param aKp
     * 			  Proportional constant for desired distance
     */
    
    public DriveWithEncoder(double aDistance, double aSpeed, double aTime, double aKp) {
        
    	requires(Robot.Drivetrain);
        
        this.mDistance = aDistance;
        this.mSpeed = aSpeed;
        this.mTime = aTime;
        this.mKp = aKp;
        
        setTimeout(mTime);
        
    }
    

    
    protected void initialize() {
    	
    }

    
    
    protected void execute() {
    	
    	error = Math.abs(mDistance - Robot.Drivetrain.GetAverageEncoderDistance());
    	
    	if (mKp * error >= mSpeed){
    		pidSpeed = mSpeed;
    	}
    	else {
    		pidSpeed = mKp * error;
    	}

  
    	if (Robot.Drivetrain.GetAverageEncoderDistance() < mDistance)
        {
    		Robot.Drivetrain.drive(pidSpeed, -pidSpeed);
        }
        else
        {
        	Robot.Drivetrain.drive(-pidSpeed, pidSpeed);
        }
    	
    }

    
    protected boolean isFinished() {
    	return ((Math.abs(error) <= tolerance) || isTimedOut());
    }

    
    
    protected void end() {
    	
    	Robot.Drivetrain.drive(0, 0);
    	Robot.Drivetrain.resetEncoders();
    	
    }

    
    protected void interrupted() {
    }
}
