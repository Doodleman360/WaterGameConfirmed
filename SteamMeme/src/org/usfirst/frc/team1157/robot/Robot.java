package org.usfirst.frc.team1157.robot;
import edu.wpi.first.wpilibj.Utility;

import org.usfirst.frc.team1157.robot.commands.AutoDriveStraight;
import org.usfirst.frc.team1157.robot.commands.AutoHangGearWithTurn;
import org.usfirst.frc.team1157.robot.commands.AutoLazer;
import org.usfirst.frc.team1157.robot.commands.AutoTurnAngle;
import org.usfirst.frc.team1157.robot.commands.AutoVistion;
import org.usfirst.frc.team1157.robot.commands.DTJoystickDrive;
import org.usfirst.frc.team1157.robot.commands.DTRumblePadDrive;
import org.usfirst.frc.team1157.robot.commands.GOCRAZYANDDESTRYSTUFF;
import org.usfirst.frc.team1157.robot.subsystems.Roller;
import org.usfirst.frc.team1157.robot.subsystems.DriveTrain;
import org.usfirst.frc.team1157.robot.subsystems.GearManipSubsys;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */


public class Robot extends IterativeRobot {
	
    DigitalInput limitSwitch;
	
    boolean userButton;
    public static NetworkTable table;
    boolean ran = false;
    
    public Robot() {
	table = NetworkTable.getTable("/vision");
    }

    public static final Roller roller = new Roller();
    public static OI oi;
    Command autonomousCommand;

    SendableChooser<Command> chooser = new SendableChooser<>();

    private static final int IMG_WIDTH = 640;
    private static final int IMG_HEIGHT = 360;

    double centerX = 0.0;

    public static final DriveTrain driveTrain = new DriveTrain();
    public static final GearManipSubsys hangGearSubsys = new GearManipSubsys();
    public static final ADXRS450_Gyro gyro = new ADXRS450_Gyro();
    
    public long millis = 0; // _TIMER_
    public long timer = 0; // _TIMER_
    
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.	
     */
    @Override
    public void robotInit() {
    	
    	userButton = false;
    	
	oi = new OI();

	chooser.addObject("auto drive forward", new AutoDriveStraight (1.0, 3));
	//chooser.addObject("turn to 60", new AutoTurnAngle(60));
	chooser.addObject("Gear From Left", new AutoHangGearWithTurn(false));
	chooser.addObject("Gear From Right", new AutoHangGearWithTurn(true));
	chooser.addDefault("Test Vision", new AutoVistion(0));
	//chooser.addObject("Lazer tracking (0)", new AutoLazer(0));
	//chooser.addObject("NEVER EVER USE THIS EVER IN NOT JOKING!!!!!!", new GOCRAZYANDDESTRYSTUFF());

	SmartDashboard.putData("Auto mode", chooser);
	SmartDashboard.putNumber("Forward Speed", 500);
	SmartDashboard.putNumber("Backward Speed", -500);
	SmartDashboard.putBoolean("KeepAlive", true);
	SmartDashboard.putBoolean("rumblePad?", false);
	
	LiveWindow.addSensor("Gyro", 0, gyro);
	
	UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
	camera.setResolution(IMG_WIDTH, IMG_HEIGHT);
    }

    /**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
     * the robot is disabled.
     */
    @Override
    public void disabledInit() {

    }

    @Override
    public void disabledPeriodic() {
	Scheduler.getInstance().run();
	userButton = Utility.getUserButton();
	if(userButton == true) {
		SmartDashboard.putBoolean("KeepAlive", false);
	}
    }

    /**
     * This autonomous (along with the chooser code above) shows how to select
     * between different autonomous modes using the dashboard. The sendable
     * chooser code works with the Java SmartDashboard. If you prefer the
     * LabVIEW Dashboard, remove all of the chooser code and uncomment the
     * getString code to get the auto name from the text box below the Gyro
     *
     * You can add additional auto modes by adding additional commands to the
     * chooser code above (like the commented example) or additional comparisons
     * to the switch structure below with additional strings & commands.
     */
    @Override
    public void autonomousInit() {
	autonomousCommand = chooser.getSelected();
	

	/*
	 * String autoSelected = SmartDashboard.getString("Auto Selector",
	 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
	 * = new MyAutoCommand(); break; case "Default Auto": default:
	 * autonomousCommand = new ExampleCommand(); break; }
	 */

	// schedule the autonomous command (example)
	if (autonomousCommand != null)
	    autonomousCommand.start();
	
	millis = System.currentTimeMillis();  // _TIMER_
    }

    /**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic() {
	Scheduler.getInstance().run();
	
	timerPeriodic(); // _TIMER_
    }

    @Override
    public void teleopInit() {
	if(SmartDashboard.getBoolean("rumblePad?", false)) {
	    driveTrain.setDefaultCommand(new DTRumblePadDrive());
	} else {
	    driveTrain.setDefaultCommand(new DTJoystickDrive());
	}
	// This makes sure that the autonomous stops running when
	// teleop starts running. If you want the autonomous to
	// continue until interrupted by another command, remove
	// this line or comment it out.
    	
	if (autonomousCommand != null)
	    autonomousCommand.cancel();
    }

    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic() {
    	//table.putNumber("testANewNetworkVar2", 66);
    	//table.putNumber("asdf", 77);
    	//SmartDashboard.putNumber("testANewNetworkVar", x);
    	double gyroAngle = gyro.getAngle();
    	SmartDashboard.putNumber("gyroAngle",gyroAngle);
    	Scheduler.getInstance().run();
    	
    	timerPeriodic(); // _TIMER_
    }

    /**
     * This function is called periodically during test mode
     */
    @Override
    public void testPeriodic() {
	LiveWindow.run();
    }
    
    // User defined
    // _TIMER_
    public void timerPeriodic() { 
	long millistemp = System.currentTimeMillis();
	timer += millistemp - millis;
	millis = millistemp;
	SmartDashboard.putNumber("Timer", timer);
	SmartDashboard.putNumber("Random", Math.random());
    }
    
//    public void operatorControl() {
//    	while(limitSwitch.get()) {
//    		Timer.delay(10);
//    	}
//    }
}

