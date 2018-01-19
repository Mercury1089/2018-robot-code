# 2018-robot-code
Code for robot for the FIRST Power Up season. Built with Gradle because we're cool now.

## Subprojects
The entire project is broken into two different subprojects:
- robot: Robot code to be run on the RIO.
- vision: Vision coprocessor code that's to be run on a Raspberry Pi.

## Useful Gradle Tasks
These can either be run in the command line using `gradlew` or
in the tasks list for whatever IDE you're in.

- `deploy` - Deploys robot code to RIO, and vision code to rPi.
- `shuffleboard` - As it says on the tin, opens the Shuffleboard Dash.
- `build` - Builds the project, but does not deploy. This is good
for seeing if everything has been properly configured.