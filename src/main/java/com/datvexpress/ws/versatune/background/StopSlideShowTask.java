package com.datvexpress.ws.versatune.background;

import com.datvexpress.ws.versatune.screenutils.DisplayMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StopSlideShowTask implements Runnable{

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    DisplayMessage displayMessage;

    @Override
    public void run() {
        logger.info("Background task to StopSlideShow is running.");
       // stopSlideShow();
    }
    private void stopSlideShow(){

        List<String> commandList = new ArrayList<>();
        StringBuffer sb = new StringBuffer("#!/bin/bash \n");
        sb.append("sudo killall vlc >/dev/null 2>/dev/null \n");
        sb.append("sleep 1s\n");

        commandList.add(sb.toString());
        try{
            displayMessage.executeCommandList(commandList);
        }catch(Exception e){
            logger.error("ERROR: could not execute the script to stop the slide show.", e.getMessage());
        }
    }
}
