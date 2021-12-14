
mvn install:install-file =Dfile=/usr/local/lib/pi4j-2.0-SNAPSHOT/pi4j-core.jar -DgroupId=com.pi4j -DartifactId=pi4j-core -Dversion=2.0-SNAPSHOT -Dpackaging=jar -Dsources=/usr/local/lib/pi4j-2.0-SNAPSHOT/pi4j-core-sources.jar
mvn install:install-file =Dfile=/usr/local/lib/pi4j-2.0-SNAPSHOT/pi4j-library-pigpio.jar -DgroupId=com.pi4j -DartifactId=pi4j-library-pigpio -Dversion=2.0-SNAPSHOT -Dpackaging=jar
mvn install:install-file =Dfile=/usr/local/lib/pi4j-2.0-SNAPSHOT/pi4j-plugin-pigpio.jar -DgroupId=com.pi4j -DartifactId=pi4j-plugin-pigpio -Dversion=2.0-SNAPSHOT -Dpackaging=jar
mvn install:install-file =Dfile=/usr/local/lib/pi4j-2.0-SNAPSHOT/pi4j-plugin-raspberrypi.jar -DgroupId=com.pi4j -DartifactId=pi4j-plugin-raspberrypi -Dversion=2.0-SNAPSHOT -Dpackaging=jar
mvn install:install-file =Dfile=/usr/local/lib/pi4j-2.0-SNAPSHOT/slf4j-api.jar -DgroupId=org.slf4j -DartifactId=slf4j-api -Dversion=1.7.30 -Dpackaging=jar
mvn install:install-file =Dfile=/usr/local/lib/pi4j-2.0-SNAPSHOT/slf4j-simple.jar -DgroupId=org.slf4j -DartifactId=slf4j-simple -Dversion=1.7.30  -Dpackaging=jar