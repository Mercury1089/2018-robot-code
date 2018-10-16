package org.usfirst.frc.team1089.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import org.usfirst.frc.team1089.robot.subsystems.Elevator;

public class DelayableElevator extends CommandGroup {
    public DelayableElevator(double delay, Elevator.ElevatorPosition elevatorPosition, boolean goToDriveHeight) {
        if (goToDriveHeight) {
            addParallel(new UseElevator(Elevator.ElevatorPosition.DRIVE_CUBE));
        }
        addSequential(new WaitCommand(delay));
        addSequential(new UseElevator(elevatorPosition));
    }
}
