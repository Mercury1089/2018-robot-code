package org.usfirst.frc.team1089.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usfirst.frc.team1089.robot.Robot;
import org.usfirst.frc.team1089.robot.RobotMap;
import org.usfirst.frc.team1089.robot.subsystems.DriveTrain;
import org.usfirst.frc.team1089.robot.subsystems.Elevator;
import org.usfirst.frc.team1089.util.DelayableLogger;

import java.util.concurrent.TimeUnit;

/**
 * Command that constantly updates the height of the elevator based
 * on the elevator's current target position.
 */
public class UseElevator extends Command {
    private final Logger LOG = LogManager.getLogger(UseElevator.class);
    private final DelayableLogger SLOW_LOG = new DelayableLogger(LOG, 1, TimeUnit.SECONDS);
    private final int ELEVATOR_THRESHOLD = 500;

    private Elevator.ElevatorPosition targetPos;

    private int counter = 0;

    private boolean endable;

    /**
     * Constructs this command with the specified position to move to
     *
     * @param pos the elevator position to move to
     */
    public UseElevator(Elevator.ElevatorPosition pos) {
        requires(Robot.elevator);
        targetPos = pos;
        endable = false;

        setName("UseElevator (" + pos + ")");
        LOG.info(getName() + " Constructed");
    }

    public UseElevator(Elevator.ElevatorPosition pos, boolean canEnd) {
        this(pos);
        endable = canEnd;
    }

    @Override
    protected void initialize() {
        if(targetPos == Elevator.ElevatorPosition.CLIMB)
            Robot.elevator.toggleClimbState();

        Robot.elevator.getElevatorTalon().set(ControlMode.Position, targetPos.encPos);
        Robot.elevator.setPosition(targetPos);

        LOG.info(getName() + " initialized");
    }

    @Override
    protected void execute() {
        SLOW_LOG.run(log -> log.debug(getName() + " executing"));
    }

    @Override
    protected void interrupted() {
        LOG.info(getName() + " interrupted");
    }

    @Override
    protected void end() {
        LOG.info(getName() + "elevator ended");
    }

    @Override
    protected boolean isFinished() {
        if (endable && ELEVATOR_THRESHOLD >= Math.abs(targetPos.encPos - Robot.elevator.getCurrentHeight())) {
            LOG.info("Reached " + targetPos.toString());
            return true;
        }
        if (targetPos == Elevator.ElevatorPosition.FLOOR) {
            if (Robot.elevator.isLimitSwitchClosed()) {
                Robot.elevator.getElevatorTalon().set(ControlMode.Position, targetPos.encPos);
                LOG.info("Reached!");
                return true;
            }
        }
        return false;
    }
}
