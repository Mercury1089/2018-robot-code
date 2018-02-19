package org.usfirst.frc.team1089.robot.auton;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.modifiers.TankModifier;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class AutonTrajectoryGenerator {

    private static final double TIME_STEP = 0.02;

    public static Map<String, TrajectoryPair> generateTrajectories() {

        Map<String, TrajectoryPair> trajectories = new HashMap<String, TrajectoryPair>();

        trajectories.put("CubePickupSetupLeft", generatePair(
                 4.0, 3.0, 60.0, 3.4, new Waypoint[]{
                        new Waypoint(20.65, 7.10, Pathfinder.d2r(90)),
                        new Waypoint(14.00, 3.00, Pathfinder.d2r(90))
                }
        ));
        trajectories.put("CubePickupSetupRight", generatePair(
                 4.0, 3.0, 60.0, 3.4, new Waypoint[]{
                        new Waypoint(20.65, 7.10, Pathfinder.d2r(-90)),
                        new Waypoint(14.00, 3.00, Pathfinder.d2r(-90))
                }
        ));
        trajectories.put("CubeSetupPickupOppLeft", generatePair(
                 4.0, 3.0, 60.0, 3.4, new Waypoint[]{
                        new Waypoint(20.65, 24.00, Pathfinder.d2r(90.00)),
                        new Waypoint(20.65, 23.21, Pathfinder.d2r(90.00)),
                        new Waypoint(20.65, 19.90, Pathfinder.d2r(90.00)),
                        new Waypoint(20.65, 11.70, Pathfinder.d2r(90.00)),
                        new Waypoint(17.42, 8.85, Pathfinder.d2r(180.00))
                }
        ));
        trajectories.put("CubeSetupPickupOppRight", generatePair(
                 4.0, 3.0, 60.0, 3.4, new Waypoint[]{
                        new Waypoint(20.65, 3.00, Pathfinder.d2r(90.00)),
                        new Waypoint(20.65, 3.79, Pathfinder.d2r(90.00)),
                        new Waypoint(20.65, 7.10, Pathfinder.d2r(90.00)),
                        new Waypoint(20.65, 15.30, Pathfinder.d2r(90.00)),
                        new Waypoint(17.42, 18.15, Pathfinder.d2r(180.00))
                }
        ));
        trajectories.put("InitialScaleFrontLeft", generatePair(
                 5.0, 8.0, 30.0, 2.0, new Waypoint[]{
                        new Waypoint(3.00, 23.21, Pathfinder.d2r(0.00)),
                        new Waypoint(8.50, 23.21, Pathfinder.d2r(0.00)),
                        new Waypoint(23.75, 16.00, Pathfinder.d2r(10.00))
                }
        ));
        trajectories.put("InitialScaleFrontRight", generatePair(
                 5.0, 8.0, 30.0, 2.0, new Waypoint[]{
                        new Waypoint(3.00, 3.79, Pathfinder.d2r(0.00)),
                        new Waypoint(8.50, 3.79, Pathfinder.d2r(0.00)),
                        new Waypoint(23.75, 11.00, Pathfinder.d2r(10.00))
                }
        ));
        trajectories.put("InitialSwitchBackLeft", generatePair(
                 4.0, 3.0, 60.0, 3.4, new Waypoint[]{
                        new Waypoint(3.00, 23.21, Pathfinder.d2r(0.00)),
                        new Waypoint(18.60, 23.21, Pathfinder.d2r(0.00)),
                        new Waypoint(23.00, 20.50, Pathfinder.d2r(90.00)),
                        new Waypoint(18.60, 17.00, Pathfinder.d2r(170.00))
                }
        ));
        trajectories.put("InitialSwitchBackRight", generatePair(
                 4.0, 3.0, 60.0, 3.4, new Waypoint[]{
                        new Waypoint(3.00, 3.79, Pathfinder.d2r(0.00)),
                        new Waypoint(18.60, 3.79, Pathfinder.d2r(0.00)),
                        new Waypoint(23.00, 6.50, Pathfinder.d2r(90.00)),
                        new Waypoint(18.60, 10.00, Pathfinder.d2r(190.00))
                }
        ));
        trajectories.put("ScaleFrontLeft", generatePair(
                 4.0, 3.0, 60.0, 3.4, new Waypoint[]{
                        new Waypoint(20.65, 24.00, Pathfinder.d2r(-90.00)),
                        new Waypoint(24.97, 19.90, Pathfinder.d2r(0.00))
                }
        ));
        trajectories.put("ScaleFrontRight", generatePair(
                 4.0, 3.0, 60.0, 3.4, new Waypoint[]{
                        new Waypoint(20.65, 3.00, Pathfinder.d2r(-90.00)),
                        new Waypoint(24.97, 7.10, Pathfinder.d2r(0.00))
                }
        ));
        trajectories.put("ScaleSideLeft", generatePair(
                 4.0, 3.0, 60.0, 3.4, new Waypoint[]{
                        new Waypoint(20.65, 24.00, Pathfinder.d2r(90.00)),
                        new Waypoint(23.50, 22.50, Pathfinder.d2r(15.00)),
                        new Waypoint(25.69, 21.04, Pathfinder.d2r(90.00))
                }
        ));
        trajectories.put("ScaleSideRight", generatePair(
                 4.0, 3.0, 60.0, 3.4, new Waypoint[]{
                        new Waypoint(20.65, 3.00, Pathfinder.d2r(90.00)),
                        new Waypoint(23.50, 4.50, Pathfinder.d2r(-15.00)),
                        new Waypoint(25.69, 5.96, Pathfinder.d2r(90.00))
                }
        ));
        trajectories.put("SwitchBackLeft", generatePair(
                 4.0, 3.0, 60.0, 3.4, new Waypoint[]{
                        new Waypoint(20.65, 24.00, Pathfinder.d2r(-90.00)),
                        new Waypoint(20.65, 21.00, Pathfinder.d2r(-90.00)),
                        new Waypoint(17.42, 18.15, Pathfinder.d2r(-180.00))
                }
        ));
        trajectories.put("SwitchBackOppLeft",generatePair(
                 4.0, 3.0, 60.0, 3.4, new Waypoint[]{
                        new Waypoint(3.00, 23.21, Pathfinder.d2r(0.00)),
                        new Waypoint(17.00, 23.21, Pathfinder.d2r(0.00)),
                        new Waypoint(20.65, 19.90, Pathfinder.d2r(90.00)),
                        new Waypoint(20.65, 12.70, Pathfinder.d2r(90.00)),
                        new Waypoint(17.42, 8.85, Pathfinder.d2r(180.00))
                }
        ));
        trajectories.put("SwitchBackOppRight",generatePair(
                 4.0, 3.0, 60.0, 3.4, new Waypoint[]{
                        new Waypoint(3.00, 3.79, Pathfinder.d2r(0.00)),
                        new Waypoint(17.00, 3.79, Pathfinder.d2r(0.00)),
                        new Waypoint(20.65, 7.10, Pathfinder.d2r(90.00)),
                        new Waypoint(20.65, 14.30, Pathfinder.d2r(90.00)),
                        new Waypoint(17.42, 18.15, Pathfinder.d2r(180.00))
                }
        ));
        trajectories.put("SwitchBackRight",generatePair(
                 4.0, 3.0, 60.0, 3.4, new Waypoint[]{
                        new Waypoint(20.65, 3.00, Pathfinder.d2r(-90.00)),
                        new Waypoint(20.65, 6.00, Pathfinder.d2r(-90.00)),
                        new Waypoint(17.42, 8.855, Pathfinder.d2r(-180.00))
                }
        ));
        trajectories.put("SwitchFrontLeft",generatePair(
                 4.0, 3.0, 60.0, 3.4, new Waypoint[]{
                        new Waypoint(3.00, 13.00, Pathfinder.d2r(0.00)),
                        new Waypoint(11.67, 18.31, Pathfinder.d2r(0.00))
                }
        ));
        trajectories.put("SwitchFrontRight",generatePair(
                 4.0, 3.0, 60.0, 3.5, new Waypoint[]{
                        new Waypoint(3.00, 13.00, Pathfinder.d2r(0.00)),
                        new Waypoint(11.67, 10.00, Pathfinder.d2r(20.00))
                }
        ));
        trajectories.put("SwitchMidLeft",generatePair(
                 4.0, 3.0, 60.0, 3.5, new Waypoint[]{
                        new Waypoint(3.00, 23.21, Pathfinder.d2r(0.00)),
                        new Waypoint(12.68, 23.21, Pathfinder.d2r(0.00)),
                        new Waypoint(16.00, 20.20, Pathfinder.d2r(90.00))
                }
        ));
        trajectories.put("SwitchMidRight",generatePair(
                 4.0, 3.0, 60.0, 3.5, new Waypoint[]{
                        new Waypoint(3.00, 3.79, Pathfinder.d2r(0.00)),
                        new Waypoint(12.68, 3.79, Pathfinder.d2r(0.00)),
                        new Waypoint(16.00, 6.80, Pathfinder.d2r(90.00))
                }
        ));

        return trajectories;
    }

    public static class TrajectoryPair {
        private Trajectory leftTrajectory, rightTrajectory;
        public TrajectoryPair(Trajectory left, Trajectory right) {

        }

        public Trajectory getLeft() {
            return leftTrajectory;
        }

        public Trajectory getRight() {
            return rightTrajectory;
        }
    }

    private static TrajectoryPair generatePair(double velocity, double acceleration, double jerk, double wheelbase, Waypoint[] points) {
        Trajectory trajectory = Pathfinder.generate(
                points,
                new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, TIME_STEP, velocity, acceleration, jerk)
        );

        TankModifier modifier = new TankModifier(trajectory);
        modifier.modify(wheelbase);
        return new TrajectoryPair(modifier.getLeftTrajectory(), modifier.getRightTrajectory());
    }
}
