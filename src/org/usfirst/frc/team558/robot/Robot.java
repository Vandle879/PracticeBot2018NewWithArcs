/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team558.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team558.robot.autocommands.*;
import org.usfirst.frc.team558.robot.subsystems.*;
import org.usfirst.frc.team558.robot.util.gameState;


public class Robot extends TimedRobot {
	
	
	public static DriveTrain Drivetrain = new DriveTrain();
	public static rollerclaw rollerClaw = new rollerclaw();
	public static ShoulderArm Arm =new ShoulderArm();
	public static SuckerPunch punch = new SuckerPunch();
	public static GripperSolenoid gripper = new GripperSolenoid();
	public static HighPressureGrip highGrip = new HighPressureGrip();
	public static DetentSolenoid detent = new DetentSolenoid();
	public static CubeDetector cubeDetector = new CubeDetector();
	public static LineSensor lineSensor = new LineSensor();
	public static Gyro gyro = new Gyro();
	public gameState GameState;
	
	public static Compressor pcm = new Compressor();
	//public static Relay compressor = new Relay(0);
	
	
	public static OI m_oi;

	Command m_autonomousCommand;
	SendableChooser<Command> m_chooser = new SendableChooser<>();

	
	@Override
	public void robotInit() {
		m_oi = new OI();
		
		CameraServer.getInstance().startAutomaticCapture();
		
		// chooser.addObject("My Auto", new MyAutoCommand());
		m_chooser.addDefault("Do Nothing", new DoNothing());
		
		SmartDashboard.putData("Auto mode", m_chooser);
	}

	
	@Override
	public void disabledInit() {
		Robot.detent.RetractDetent();
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
		
		this.GameState = new gameState(DriverStation.getInstance().getGameSpecificMessage());

	}

	
	@Override
	public void autonomousInit() {
		
		Robot.Arm.resetEncoder();
		Robot.detent.FireDetent();

		m_autonomousCommand = m_chooser.getSelected();

		
		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */

		// schedule the autonomous command (example)
		if (m_autonomousCommand != null) {
			m_autonomousCommand.start();
		}
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();

		Robot.detent.FireDetent();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		
		Robot.Drivetrain.setRampRate();
		
//		Robot.Arm.resetEncoder();
	
		
		if (m_autonomousCommand != null) {
			m_autonomousCommand.cancel();
		}
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		
		Scheduler.getInstance().run();
		Robot.detent.FireDetent();
		
		SmartDashboard.putNumber("Arm Encoder", Robot.Arm.readEncoder());
		SmartDashboard.putNumber("Left Encoder", Robot.Drivetrain.readLeftEncoder());
		SmartDashboard.putNumber("Right encoder", Robot.Drivetrain.readRightEncoder());
		
		SmartDashboard.putBoolean("Line Sensor 1", Robot.lineSensor.readLineSensor1());
		SmartDashboard.putBoolean("Line Sensor 2", Robot.lineSensor.readLineSensor2());
		SmartDashboard.putBoolean("Cube Detector", Robot.cubeDetector.readCubeSensor());
		
		
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
