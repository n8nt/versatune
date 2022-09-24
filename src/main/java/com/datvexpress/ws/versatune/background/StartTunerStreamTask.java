package com.datvexpress.ws.versatune.background;

import com.datvexpress.ws.versatune.screenutils.DisplayMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class StartTunerStreamTask implements Runnable{
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    DisplayMessage displayMessage;

    @Override
    public void run() {

        logger.info("Running Start Tuner Stream.");

        /*


cd /home/pi

AUDIO_DEVICE="hw:CARD=b1,DEV=0"
# Create dummy marquee overlay file
sudo rm /home/pi/bob/vlc_overlay.txt >/dev/null 2>/dev/null
echo " " > /home/pi/bob/vlc_overlay.txt

sudo killall -9 vlc >/dev/null 2>/dev/null
sleep 1

# Play a very short dummy file to prime VLC
cvlc ffmpeg  --video-title-timeout=10 \
  --sub-filter marq --marq-size 20 --marq-x 25 --marq-file "/home/pi/bob/vlc_overlay.txt" \
  --gain 3 --alsa-audio-device $AUDIO_DEVICE \
   /home/pi/dvbt/blank.ts vlc://quit &
sleep 0.1
echo running cvlc


# Start VLC
cvlc  --video-title-timeout=100 \
  --codec h264_v4l2m2m --quiet \
  --sub-filter marq --marq-size 20 --marq-x 25 --marq-position=8 --marq-file "/home/pi/bob/vlc_overlay.txt" \
  --gain 3 --alsa-audio-device $AUDIO_DEVICE \
  udp://@127.0.0.1:1314  >/dev/null 2>/dev/null &

# cvlc ffmpeg --video-title-timeout=100 --codec h264_v4l2m2m --sub-filter marq --marq-size 20 --marq-x 25 --marq-position=8 --marq-file "/home/pi/bob/vlc_overlay.txt"--gain 3 --alsa-audio-device $AUDIO_DEVICE udp://@127.0.0.1:1314 &


         */

        logger.info("running inside StartTunerStream.");

        List<String> commandList = new ArrayList<>();
        commandList.add("#!/bin/bash \n");
        commandList.add("cd /home/pi/dvbt");
        commandList.add("su -c '/home/pi/dvbt/dvb-t_start_tuner_udp.sh' pi &");
        try{
            executeCommandList(commandList);
        }catch(Exception e){
            logger.error("ERROR: could not execute the script to start the Tuner Stream.", e.getMessage());
        }
    }

    private void executeCommandList(List<String> commandList) throws IOException {
        File tempScript = createTempScriptFromCommandList(commandList);
        logger.info("creating and starting the process to execute a commandlist.");

        logger.info("Executing script: " + tempScript.toString());
        try {
            ProcessBuilder pb = new ProcessBuilder("bash", tempScript.toString());
            pb.inheritIO();
            Process process = pb.start();
            logger.info("Process started... " + process.info());
            int exitCode = process.waitFor();
            if (exitCode != 0){
                logger.info("*** Script did NOT run properly.");
                logger.info("   ERRORCODE = exitCode");
            }
        }catch (InterruptedException ie){
            logger.error("Caught interrupted exception. Not sure what to do. " + ie.getMessage());
        } finally {
            tempScript.delete();
        }

    }

    private File createTempScriptFromCommandList(List<String> commands) throws IOException {
        logger.info("creating the temporary script file from command list.");
        File tempScript = File.createTempFile("scriptX1", null);
        Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
                tempScript));
        PrintWriter printWriter = new PrintWriter(streamWriter);
        for (String s : commands){
            printWriter.println(s);
        }
        printWriter.close();
        return tempScript;
    }
}
