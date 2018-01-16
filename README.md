# 2018-robot-code
Code for robot for the FIRST Power Up season. Built with Gradle because we're cool now.

## Useful Gradle Tasks
These can either be run in the command line using `gradlew` or
in the tasks list for whatever IDE you're in.

- `deploy` - Deploys everything to required targets.
This could be just the roboRIO, coprocessors, or even multiple roboRIOs
- `shuffleboard` - As it says on the tin, opens the Shuffleboard Dash.
- `build` - Builds the project, but does not deploy. This is good
for seeing if everything has been properly configured.