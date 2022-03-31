//package com.tournoux.ws.btsocket.scheduled;
//
//import com.tournoux.ws.btsocket.pi4j.Pi4jMinimalBT;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Component
//@EnableScheduling
//public class GpioTask {
//
//    Logger logger = LoggerFactory.getLogger(getClass());
//
//    @Scheduled(fixedDelay=5000)
//    public void gpioWatcher(){
//        logger.info("Waiting 60 seconds before starting gpio watcher...");
//        try {
//            Thread.sleep(60000);
//        } catch (InterruptedException e) {
//            logger.error("Got Interrupted Exception. " + e.getMessage());
//            return;
//        }
//        logger.info("Starting GPIO watcher.");
//        Pi4jMinimalBT manager = new Pi4jMinimalBT();
//        try {
//            manager.manageGpios();
//            logger.info("We are running.");
//        }catch(Exception e){
//            logger.error("Well, shucks, Guss, there is an error. " + e.getMessage());
//        }
//    }
//}
