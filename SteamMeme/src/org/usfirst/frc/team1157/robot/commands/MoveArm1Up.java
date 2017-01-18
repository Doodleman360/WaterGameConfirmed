package org.usfirst.frc.team1157.robot.commands;


import org.usfirst.frc.team1157.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class MoveArm1Up extends Command {

    public MoveArm1Up() {
    	requires(Robot.arm1);
    	setTimeout(.9);
   	}
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
 

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.arm1.up();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.arm1.stop();
    }
 

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
