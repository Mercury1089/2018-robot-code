package org.usfirst.frc.team1089.robot.commands;

import com.ctre.phoenix.CANifier;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1089.robot.OI;
import org.usfirst.frc.team1089.robot.Robot;

public class GetLEDStatus extends Command {
    public final double MIN_INCHES = 8.0;

    public GetLEDStatus() {
        requires(Robot.ledIndicators);
    }

    @Override
    protected void execute () {
        if (Robot.vision.getPixyCam().inRange()) { //Cube is in range to auto pickup
            //set green (R: 28.2%, G: 91%, B: 14.9%)
            Robot.claw.getCanifier().setLEDOutput(28.2, CANifier.LEDChannel.LEDChannelA);
            Robot.claw.getCanifier().setLEDOutput(91, CANifier.LEDChannel.LEDChannelB);
            Robot.claw.getCanifier().setLEDOutput(14.9, CANifier.LEDChannel.LEDChannelC);

            Robot.oi.rumbleController(true);
        } else if (Robot.claw.getLidar().getDistance() <= MIN_INCHES) { //Have cube?
            //set orange (R: 100%, G: 74.5%, B: 3.9%)
            Robot.claw.getCanifier().setLEDOutput(100, CANifier.LEDChannel.LEDChannelA);
            Robot.claw.getCanifier().setLEDOutput(74.5, CANifier.LEDChannel.LEDChannelB);
            Robot.claw.getCanifier().setLEDOutput(3.9, CANifier.LEDChannel.LEDChannelC);
            Robot.oi.rumbleController(false);
        } else if (Robot.claw.getEjecting()) { //Robo is ejecting cube
            //set red (R: 100%, G: 3.9%, B: 3.9%)
            Robot.claw.getCanifier().setLEDOutput(100, CANifier.LEDChannel.LEDChannelA);
            Robot.claw.getCanifier().setLEDOutput(3.9, CANifier.LEDChannel.LEDChannelB);
            Robot.claw.getCanifier().setLEDOutput(3.9, CANifier.LEDChannel.LEDChannelC);
            Robot.oi.rumbleController(false);
        } else { //default case
            //set white (All values max power, maximum)
            Robot.claw.getCanifier().setLEDOutput(100, CANifier.LEDChannel.LEDChannelA);
            Robot.claw.getCanifier().setLEDOutput(100, CANifier.LEDChannel.LEDChannelB);
            Robot.claw.getCanifier().setLEDOutput(100, CANifier.LEDChannel.LEDChannelC);
            Robot.oi.rumbleController(false);
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
