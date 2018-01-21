# 2018-robot-code
Code for robot for the FIRST Power Up season. Built with Gradle because we're cool now.

## Set-Up
**NOTE: Instructions are IDE independent for the most part**

### Prerequisites
- A good internet connection

### Instructions
1. Clone this repository
2. Import this project into your IDE of choice. For Eclipse
and IntelliJ users, it might be a good idea to generate the
IDE's project files. You can generate IDEA or Eclipse 
project files by using the Gradle task `idea` or `eclipse`,
respectively.
3. (Optional) Add the Gradle wrapper to your IDE's Gradle view.
This will also synchronize the dependencies and tasks 
to your workspace. This might take some time, so grab a cup of coffee or something
while you're at it. This is only optional as you could also run
Gradle from the commandline, but you don't have the added benefit
of using the Java compiler as it won't recognize the Gradle
dependencies.

## Gradle Tasks
Gradle tasks can all be run at the root of the project simply
by using the name of the task as an argument to the command
- `> gradlew` (Windows)
- `$ ./gradlew` (Linux/macOS)

## Subprojects
- `:robot` - The robot subproject, containing all the code meant to be
deployed to the roboRIO. Found in the `/robot/` subfolder.
- `:shuffleboard` - Custom Shuffleboard widgets subproject. 
Found in the `/shuffleboard/` subfolder.
- `:vision` - Vision processing subproject, containing code to process.
vision on a coprocessor, specifically a Raspberry Pi. Found in the 
`/vision/` subfolder.