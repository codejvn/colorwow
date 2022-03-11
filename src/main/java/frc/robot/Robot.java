// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color8Bit;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.Timer;
import java.util.Random;

/**
 * import edu.wpi.first.wpilibj.Joystick;

public class Main{
    static Joystick joystick = new Joystick(0);
    public static void main(String[] args){
        analogWrite(0, 0);
        if (joystick.getTrigger()){
        setColorBrightness('r', 200);
        }
    }
}

 */

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  Joystick joystick = new Joystick(0);
  AddressableLED led = new AddressableLED(1);
  public AddressableLEDBuffer buffer = new AddressableLEDBuffer(60);
  private int m_rainbowFirstPixelHue = 0;
  public Timer timer = new Timer();
  private boolean onlyOnce;
  private boolean stop;
  private boolean once;
  private double curtime;
  private double lasttime;
  private double m_interval;
  private double lastChange;
  private boolean on;
  private Random random;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    led.setLength(buffer.getLength());
    // led.setData(buffer);
    // led.start();
    // timer = new Timer();
    timer.reset();
    // timer.start();
    onlyOnce = false;
    stop = false;
    once = false;
    curtime = 0;
    lasttime = 0;
    on = true;
    m_interval = 0.1;
    random = new Random();
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    timer.reset();
    timer.start();
  }

  public void rgb(int red, int blue, int green){
      for (int i = 0; i < buffer.getLength(); i++){
        // System.out.println("changing buffer: " + i);
        buffer.setRGB(i, red, blue, green);
      }
      led.setData(buffer);
      led.start();
  }

  public void rainbow() {
    // For every pixel
    for (var i = 0; i < buffer.getLength(); i++) {
      // Calculate the hue - hue is easier for rainbows because the color
      // shape is a circle so only one value needs to precess
      final var hue = (m_rainbowFirstPixelHue + (i * 180 / buffer.getLength())) % 180;
      // Set the value
      buffer.setHSV(i, hue, 255, 128);
    }
    // Increase by to make the rainbow "move"
    m_rainbowFirstPixelHue += 3;
    // Check bounds
    m_rainbowFirstPixelHue %= 180;
    led.setData(buffer);
  } 

  public void oneatatime(int red, int blue, int green, double time){ //this is the only method that doesn't work as of 3/9/22.
    rgb(0,0,0);
    // for (int x = 0; x < buffer.getLength(); x++){ //make a bool to go under this statement to make the below run only once per iteration
    //   if (timer.get()% time < 0.001){
    //     onlyOnce = false;
    //     System.out.println("the current one being on is " + x);
    //     if (!onlyOnce){
    //     System.out.println("current time is: " + timer.get());
    //     buffer.setRGB(x, red, green, blue);
    //     led.setData(buffer);
    //     onlyOnce = true;
    //     if (joystick.getPOV() > 1){
    //       System.out.println("stopping?");
    //       stop = true;
    //     }
    //     }
    //   }
    // }

    //attempt #2:
    // for (int x = 0; x < buffer.getLength(); x++){
    //   if (timer.get() % time < 0.1){
    //     curtime = timer.get();
    //     System.out.println("current time is " + curtime + " last time was " + lasttime);
    //     System.out.println("current time - last time = " + (curtime-lasttime));
    //     if (curtime-lasttime < time){
    //       System.out.println("idk if this was print");
    //     buffer.setRGB(x, red, green, blue);
    //     led.setData(buffer);
    //     System.out.println("last time should be " + timer.get());
    //     lasttime = timer.get();
    //     if (joystick.getPOV() != -1){
    //       System.out.println("stopping?");
    //       stop = true;
    //     }
    //     }
    //     if (joystick.getPOV() != -1){
    //       System.out.println("stopping?");
    //       stop = true;
    //     }
     // }
   // }
   
  }

  public void alternate(int red1, int blue1, int green1, int red2, int blue2, int green2){
    double timestamp = Timer.getFPGATimestamp();
		if (timestamp- lastChange > m_interval){ //warning, i dont see where this changes on back to true...
			on = !on;
			lastChange = timestamp;
		}
		if (on){
			// m_onPattern.setLEDs(buffer);
      rgb(red1,green1,blue1);
		} else {
			// m_offPattern.setLEDs(buffer);
      rgb(red2,green2,blue2);
		}
  }

  public void nemesis(){
    for (int x = 0; x < buffer.getLength(); x++){
    if (x % 10 < 7){
      buffer.setRGB(x, 255, 0, 0);
    }
    else{
      buffer.setHSV(x, 120, 0, 100);
    }
    led.setData(buffer);
    }
  }

  public void nemessialternate(){ //nemesis() with alternating colors
    double timestamp = Timer.getFPGATimestamp();
		if (timestamp - lastChange > m_interval){ //warning, i dont see where this changes on back to true...
			on = !on;
			lastChange = timestamp;
		}
		if (on){
			// m_onPattern.setLEDs(buffer);
      for (int x = 0; x < buffer.getLength(); x++){
        if (x % 10 < 7){
          buffer.setRGB(x, 255, 0, 0);
        }
        else{
          buffer.setHSV(x, 120, 0, 100);
        }
		}} else { 
			// m_offPattern.setLEDs(buffer);
      for (int x = 0; x < buffer.getLength(); x++){
        if (x % 10 < 7){
          buffer.setHSV(x, 120, 0, 100);
        }
        else{
          buffer.setRGB(x, 255, 0, 0);
        }
		}
  }
  led.setData(buffer);
  }

  public void ukraine(){
    for (int x = 0; x < buffer.getLength(); x++){
    if (x % 10 < 5){
      buffer.setRGB(x, 255, 255, 0);
    }
    else{
      buffer.setRGB(x, 0, 0, 255);
    }
    led.setData(buffer);
    }
  }
  
  public void ukrainealternate(){ //ukraine() but they change colors
    double timestamp = Timer.getFPGATimestamp();
		if (timestamp- lastChange > m_interval){ //warning, i dont see where this changes on back to true...
			on = !on;
			lastChange = timestamp;
		}
		if (on){
			// m_onPattern.setLEDs(buffer);
      for (int x = 0; x < buffer.getLength(); x++){
        if (x % 10 < 7){
          buffer.setRGB(x, 255, 255, 0);
        }
        else{
          buffer.setRGB(x, 0, 0, 255);
        }
        led.setData(buffer);
        }
		} else {
			// m_offPattern.setLEDs(buffer);
      for (int x = 0; x < buffer.getLength(); x++){
        if (x % 10 < 5){
          buffer.setRGB(x, 0, 0, 255);
        }
        else{
          buffer.setRGB(x, 255, 255, 0);
        }
        led.setData(buffer);
        }
		}
  }


    public void literallyRandom(){
      for (int i = 0; i < buffer.getLength(); i++){
      double timestamp = Timer.getFPGATimestamp();
      System.out.println(timestamp + " - " + lastChange + " is greater than " + m_interval);
		if (timestamp- lastChange > m_interval){ //warning, i dont see where this changes on back to true...
			on = !on;
			lastChange = timestamp;
      onlyOnce = false;
		}
    if (!onlyOnce){
		if (on){
      System.out.println("light being changed " + i);
      System.out.println("leds turning on");
			// m_onPattern.setLEDs(buffer);

      buffer.setRGB(i, random.nextInt(255), random.nextInt(255), random.nextInt(255));
      led.setData(buffer);
      onlyOnce = true;
		} else {
			// m_offPattern.setLEDs(buffer);
      System.out.println("light being changed " + i);
      System.out.println("led turning off");
      buffer.setRGB(i, 0, 0, 0);
      led.setData(buffer);
		}
  }
  }
    }

  public void disco(int button, Double speed){
    while (!joystick.getRawButton(button)){
      rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
      try {
        Thread.sleep((long) (speed * 1000));
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  public void oneeach(int r, int g, int b){
    for (int i = 0; i < buffer.getLength(); i++){
      if (i > 0){
        buffer.setRGB(i-1, 0, 0, 0);
        led.setData(buffer);
      }
      buffer.setRGB(i, r, g, b);
      led.setData(buffer);
      try {
        Thread.sleep((long) (m_interval * 1000));
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    rgb(0,0,0);
  }



  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    if (joystick.getTrigger()){
      System.out.println("trigger pressed");
     rgb(255, 0, 0);
    }
    if (joystick.getRawButton(3)){
      rgb(0,255,0);
    }
    if (joystick.getRawButton(4)){
      rgb(0,0, 255);
    }
    if (joystick.getRawButton(14)){
      System.out.println("time rn: " + timer.get());
      // while (!stop){
      oneatatime(120, 200, 30, 1);
      // }
    }
    if (joystick.getRawButton(7)){
      System.out.println("button 2");
      rainbow();
      led.setData(buffer);
    }
    if (joystick.getRawButton(6)){
      System.out.println("trying to stop.");
      // led.stop();
      // led.close();
      rgb(0, 0, 0);
    }
    if (joystick.getRawButton(12)){
      nemesis();
    }
    if (joystick.getRawButton(15)){
      ukraine();
    }
    if (joystick.getRawButton(5)){
      System.out.println("button dos");
      nemessialternate();
    }
    if (joystick.getRawButton(16)){
      ukrainealternate();
    }
    if (joystick.getRawButton(17)){
      literallyRandom();
    }
    if (joystick.getRawButton(18)){
      oneeach(120, 120, 120);
    }
    if (joystick.getRawButton(2)){
      disco(1, 0.1);
    }
  }


  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {
    led.stop();
    // led.close();
    timer.stop();
    timer.reset();
  }

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {
    led.stop();
  }

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
