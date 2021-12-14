#
# this runs on windows
# Do it first if you have not done it before. Currently these are NOT in Maven, so we will add to our own repository so we can
# build the application.
# NOTE ***** I had to execute these one line at a time. To be done as a script, I think you have to set each line up as a .bat or .cmd then call each one out of a parent script.
#
mvn install:install-file -Dfile=C:\usr\local\lib\pi4j-2.0-SNAPSHOT\pi4j-core.jar -DgroupId=com.pi4j -DartifactId=pi4j-core -Dversion=2.0-SNAPSHOT -Dpackaging=jar -Dsources=/usr/local/lib/pi4j-2.0-SNAPSHOT/pi4j-core-sources.jar
mvn install:install-file -Dfile=C:\usr\local\lib\pi4j-2.0-SNAPSHOT\pi4j-library-pigpio.jar -DgroupId=com.pi4j -DartifactId=pi4j-library-pigpio -Dversion=2.0-SNAPSHOT -Dpackaging=jar
mvn install:install-file -Dfile=C:\usr\local\lib\pi4j-2.0-SNAPSHOT\pi4j-plugin-pigpio.jar -DgroupId=com.pi4j -DartifactId=pi4j-plugin-pigpio -Dversion=2.0-SNAPSHOT -Dpackaging=jar
mvn install:install-file -Dfile=C:\usr\local\lib\pi4j-2.0-SNAPSHOT\pi4j-plugin-raspberrypi.jar -DgroupId=com.pi4j -DartifactId=pi4j-plugin-raspberrypi -Dversion=2.0-SNAPSHOT -Dpackaging=jar
mvn install:install-file -Dfile=C:\usr\local\lib\pi4j-2.0-SNAPSHOT\slf4j-api.jar -DgroupId=org.slf4j -DartifactId=slf4j-api -Dversion=1.7.30 -Dpackaging=jar
mvn install:install-file -Dfile=C:\usr\local\lib\pi4j-2.0-SNAPSHOT\slf4j-simple.jar -DgroupId=org.slf4j -DartifactId=slf4j-simple -Dversion=1.7.30  -Dpackaging=jar