package org.usfirst.frc.team1157.robot.commands;

import org.usfirst.frc.team1157.robot.Robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class RollerMove extends Command {

	Joystick stick;

	/*
	 * @param time the amount of time the roller should move
	 * 
	 * @param speed the speed from -1 to 1
	 */
	public RollerMove(Joystick stick) {
		this.stick = stick;
		requires(Robot.roller);
	}
	// Use requires() here to declare subsystem dependencies
	// eg. requires(chassis);

	// Called just before this Command runs the first time
	protected void initialize() {
		
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		Robot.roller.move(stick.getZ());
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return isTimedOut();
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.roller.stop();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
