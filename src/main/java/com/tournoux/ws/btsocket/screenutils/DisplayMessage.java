package com.tournoux.ws.btsocket.screenutils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class DisplayMessage {
    Logger logger = LoggerFactory.getLogger(getClass());
    public void dsiplayMessageText(String message){

        List<String> commandList = new ArrayList<>();
        StringBuffer sb = new StringBuffer("sudo convert -size 1280x720 xc:black -stroke white ");
        sb.append(" -gravity NorthWest -pointsize 40 -annotate 0 ");
        sb.append("\" Versatune Interim DVB-T Receiver\n\n");
        sb.append(message);
        sb.append("\" /home/pi/tmp/message.jpg");

        commandList.add("#!/bin/bash -x");
        commandList.add("cd /home/pi/tmp");
        commandList.add("sudo rm /home/pi/tmp/message.jpg >/dev/null 2>/dev/null");
        commandList.add(sb.toString());
        commandList.add("sleep 0.1");
        // display on the screen
        commandList.add(" sudo fbi -T 1 -noverbose -a /home/pi/tmp/message.jpg >/dev/null 2>/dev/null");
        // sleep a while then kill fbi
        commandList.add("sleep 0.1; sudo killall -9 fbi >/dev/null 2>/dev/null");   // remove run in background at end
        try{
            executeCommandList(commandList);
        }catch(Exception e){
            logger.error("ERROR: could not execute the script to display the message text.", e.getMessage());
        }
    }
    public void executeCommands() throws IOException {

        File tempScript = createTempScript();
        if (logger.isTraceEnabled()) logger.trace("creating and starting the process.");
        try {
            ProcessBuilder pb = new ProcessBuilder("bash", tempScript.toString());
            pb.inheritIO();
            Process process = pb.start();
            if (logger.isTraceEnabled()) logger.trace("Process started... " + process.info());
            process.waitFor();
        }catch (InterruptedException ie){
            logger.error("Caught interrupted exception. Not sure what to do. " + ie.getMessage());
        } finally {
            tempScript.delete();
        }
    }

    /*
            When vlc is not active, then we send messages to the screen.
    */
    public void executeCommandList(List<String> commandList) throws IOException{
        File tempScript = createTempScriptFromCommandList(commandList);
        if (logger.isTraceEnabled()) logger.trace("creating and starting the process to execute a commandlist.");

        if (logger.isTraceEnabled()) logger.trace("Executing script: " + tempScript.toString());
        try {
            ProcessBuilder pb = new ProcessBuilder("bash", tempScript.toString());
            pb.inheritIO();
            Process process = pb.start();
            if (logger.isTraceEnabled()) logger.trace("Process started... " + process.info());
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

    public File createTempScript() throws IOException {
        if (logger.isTraceEnabled()) logger.trace("creating the temporary script file.");
        File tempScript = File.createTempFile("script", null);

        Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
                tempScript));
        PrintWriter printWriter = new PrintWriter(streamWriter);

        printWriter.println("#!/bin/bash -x");
        printWriter.println("cd /home/pi/dvbt");

        printWriter.println("su -c '/home/pi/dvbt/dvb-t_start.sh' pi &");

        printWriter.close();

        return tempScript;
    }

    public File createTempScriptFromCommandList(List<String> commands) throws IOException {
        if (logger.isTraceEnabled()) logger.trace("creating the temporary script file from command list.");
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
