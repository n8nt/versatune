package com.tournoux.ws.btsocket.background;

import com.tournoux.ws.btsocket.pi4j.Pi4jMinimalBT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GpioWorker implements Runnable{
    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void run() {
        logger.info("Running GpioWorker");

        Pi4jMinimalBT manager = new Pi4jMinimalBT();
        try {
            manager.manageGpios();
            logger.info("We are running.");
        }catch(Exception e){
            logger.error("Well, shucks, Guss, there is an error. " + e.getMessage());
        }
    }
}
