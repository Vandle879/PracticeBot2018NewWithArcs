package org.usfirst.frc.team558.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.RemoteSensorSource;
import com.ctre.phoenix.motorcontrol.SensorTerm;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.sensors.PigeonIMU;

import org.usfirst.frc.team558.robot.commands.*;

public class DriveTrain extends Subsystem {

	public TalonSRX leftDriveMaster = new TalonSRX(15);
	TalonSRX leftDriveSlave1 = new TalonSRX(14);
	TalonSRX leftDriveSlave2 = new TalonSRX(13);
	public TalonSRX rightDriveMaster = new TalonSRX(16);
	TalonSRX rightDriveSlave1 = new TalonSRX(1);
	TalonSRX rightDriveSlave2 = new TalonSRX(2);
	
	private PigeonIMU pidgey = new PigeonIMU(leftDriveSlave1);
	
	public static final int DRIVE_PROFILE = 0;
	public static final int ROTATION_PROFILE = 1;
	

	
	public DriveTrain(){
		
		// DriveTrain Master
		this.leftDriveMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		this.leftDriveSlave1.follow(leftDriveMaster);
		this.leftDriveSlave2.follow(leftDriveMaster);
		
		this.rightDriveMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		this.rightDriveSlave1.follow(rightDriveMaster);
		this.rightDriveSlave2.follow(rightDriveMaster);
		
		this.rightDriveMaster.setInverted(true);
		this.rightDriveSlave1.setInverted(true);
		this.rightDriveSlave2.setInverted(true);
		
		this.configPIDF(DRIVE_PROFILE, 3.6, 0.0, 0.0, 2.74, 0);
		this.configPIDF(ROTATION_PROFILE, 2.5, .005, 25.0, 0, 20);

		// configure distance sensor
		// Remote 0 will be the other side's Talon
		rightDriveMaster.configRemoteFeedbackFilter(leftDriveMaster.getDeviceID(), RemoteSensorSource.TalonSRX_SelectedSensor, 0, 0);
		rightDriveMaster.configSensorTerm(SensorTerm.Sum0, FeedbackDevice.RemoteSensor0, 0);
		rightDriveMaster.configSensorTerm(SensorTerm.Sum1, FeedbackDevice.CTRE_MagEncoder_Relative, 0);
		
		 // distances from left and right are
		 // summed, so average them
		rightDriveMaster.configSelectedFeedbackSensor(FeedbackDevice.SensorSum, 0, 0);
		rightDriveMaster.configSelectedFeedbackCoefficient(.5, 0, 0);
		
		
		rightDriveMaster.configMaxIntegralAccumulator(ROTATION_PROFILE, 3000, 0);
		

		// configure angle sensor
		// Remote 1 will be a pigeon
//*** THIS MIGHT NEED TO BE CHANGED TO A DIFFERENT SRX****
		rightDriveMaster.configRemoteFeedbackFilter(leftDriveSlave1.getDeviceID(), RemoteSensorSource.GadgeteerPigeon_Yaw, 1, 0);
//*** THIS MIGHT NEED TO BE CHANGED TO A DIFFERENT SRX****
		
		rightDriveMaster.configSelectedFeedbackSensor(FeedbackDevice.RemoteSensor1, 1, 0);
		rightDriveMaster.configSelectedFeedbackCoefficient((3600.0 / 8192.0), 0, 0);
		// Coefficient for																								
		// Pigeon to
		// convert to 360
		

		leftDriveMaster.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 5, 0);
		rightDriveMaster.configAuxPIDPolarity(false, 0);		
		
	}
	
	public void drive(double leftPower, double rightPower){
		this.leftDriveMaster.set(ControlMode.PercentOutput,leftPower);
		this.rightDriveMaster.set(ControlMode.PercentOutput,-rightPower);
	}
	
	public double getAngle() {
		double[] ypr = new double[3];
		pidgey.getYawPitchRoll(ypr);
		return ypr[0];
	}

	public double getDistance() {
		return rightDriveMaster.getSelectedSensorPosition(0);
	}
	
	public double readLeftEncoder() {
		
		return this.leftDriveMaster.getSelectedSensorPosition(0);
		
		
	}
	
	public double readRightEncoder() {
		
		return this.rightDriveMaster.getSelectedSensorPosition(0);
		
		
	}
	
	public double GetAverageEncoderDistance(){
    	return ((this.leftDriveMaster.getSelectedSensorPosition(0) + this.rightDriveMaster.getSelectedSensorPosition(0))/2);
    }
	
	public void setRampRate() {
		
		//this.leftDriveMaster.configClosedloopRamp(1000, 0);
		//this.rightDriveMaster.configClosedloopRamp(1000, 0);
		this.leftDriveMaster.configOpenloopRamp(.4, 0);
		this.rightDriveMaster.configOpenloopRamp(.4, 0);
		
	}
	
	public ErrorCode configPIDF(int slotIdx, double P, double I, double D, double F, int iZone ){
		ErrorCode errorCode = ErrorCode.OK;
		
		errorCode = this.leftDriveMaster.config_kP(slotIdx, P, 0);
		if (errorCode != ErrorCode.OK) {
			return errorCode;
		}
		errorCode = this.rightDriveMaster.config_kP(slotIdx, P, 0);
		if (errorCode != ErrorCode.OK) {
			return errorCode;
		}
		
		errorCode = this.leftDriveMaster.config_kI(slotIdx, I, 0);
		if (errorCode != ErrorCode.OK) {
			return errorCode;
		}
		errorCode = this.rightDriveMaster.config_kI(slotIdx, I, 0);
		if (errorCode != ErrorCode.OK) {
			return errorCode;
		}
		
		errorCode = this.leftDriveMaster.config_kD(slotIdx, D, 0);
		if (errorCode != ErrorCode.OK) {
			return errorCode;
		}
		errorCode = this.rightDriveMaster.config_kD(slotIdx, D, 0);
		if (errorCode != ErrorCode.OK) {
			return errorCode;
		}
		
		errorCode = this.leftDriveMaster.config_kF(slotIdx, F, 0);
		if(errorCode != ErrorCode.OK) {
			return errorCode;
		}

		
		errorCode = this.rightDriveMaster.config_kF(slotIdx, F, 0);
		if(errorCode != ErrorCode.OK) {
			return errorCode;
		}
		
		errorCode = this.leftDriveMaster.config_IntegralZone(slotIdx, iZone, 0);
		if(errorCode != ErrorCode.OK) {
			return errorCode;
		}

		
		errorCode = this.rightDriveMaster.config_IntegralZone(slotIdx, iZone, 0);
		return errorCode;
		
		
	}
	
	public void resetEncoders(){
		
		this.rightDriveMaster.setSelectedSensorPosition(0, 0, 0);
		this.leftDriveMaster.setSelectedSensorPosition(0, 0, 0);
		
	}
	

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new Elmcitydrive());
    }
}

