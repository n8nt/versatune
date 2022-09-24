package com.datvexpress.ws.versatune.background;

import com.datvexpress.ws.versatune.screenutils.DisplayMessage;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class StartSlideShowTask implements Runnable{
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    DisplayMessage displayMessage;

    final List<String> allowedExtensions = Arrays.asList("jpeg", "mp4", "png", "jpg", "gif");
    String option;

    @Override
    public void run() {
        logger.info("Background  for StartSlideShow task running.");
        startSlideShow();
    }

    private void startSlideShow(){
        List<String> commandList = new ArrayList<>();

        StringBuffer sb = new StringBuffer("#!/bin/bash \n");
        commandList.add(sb.toString());
        commandList.add("cd /usr/local/apps/btsocket/images/ \n");
        commandList.add("sudo killall vlc >/dev/null 2>/dev/null \n");
        commandList.add("sleep 0.1 \n");
        sb = new StringBuffer("su -c 'cvlc ffmpeg  --codec h264_v4l2m2m --no-video-title-show ");

        File[] files = new File("/usr/local/apps/btsocket/images").listFiles();
        for(File file : files){
            if(file.isFile()){
                // check to make sure file is one of the allowed extensions
                String extension = FilenameUtils.getExtension(file.getName());
                if (allowedExtensions.contains(extension.toLowerCase())){
                    sb.append("file:///usr/local/apps/btsocket/images/" + file.getName() + " ");
                }
            }
        }
        if (null == files ||  files.length < 1){
            sb.append("file:///home/pi/MyGrandPianoInItsNewHome.jpeg");
        }
        // to get current hdmi screen resolution
        //  cat /sys/class/graphics/fb0/virtual_size
        // ours is 1920 x 1080
        //sb.append(" -L' pi &\n");
        sb.append(" -L' pi \n");

        commandList.add(sb.toString());
        try{
            executeCommandList(commandList);
        }catch(Exception e){
            logger.error("ERROR: could not execute the script to  start the slide show.", e.getMessage());
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
        File tempScript = File.createTempFile("scriptX", null);
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
