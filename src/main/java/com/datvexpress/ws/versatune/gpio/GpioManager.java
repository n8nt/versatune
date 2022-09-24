//package com.tournoux.ws.btsocket.gpio;
//
//import com.pi4j.io.gpio.*;
//import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
//import com.pi4j.io.gpio.event.GpioPinListenerDigital;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import javax.swing.*;
//import java.text.SimpleDateFormat;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.concurrent.atomic.AtomicInteger;
//
///*
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
///*
// * More information about
// * this project can be found here:  https://pi4j.com/
// *
// * Also see https://pi4j.com/1.4/example/shutdown.html for information on how to use this gpio library
// */
//
//@Component
//public class GpioManager {
//  Logger logger = LoggerFactory.getLogger(getClass());
//
//
///*
//
//gpio:
//    repeatFirst: 200
//    repeatDelay: 100
//    rxGood: 4
//    buttons:
//        POWER:  16
//        UP:     14
//        DOWN:   27
//        LEFT:   22
//        RIGHT:  23
//        SELECT: 24
//        BACK:   25
//        MENU:   5
//        MUTE:   6
//
//    switches:
//        highgoing:
//            OSDON: 26
//        lowgoing:
//            OSDOFF: 26
//
// */
//
//
//
//  public void gpioHandler() throws InterruptedException {
//
//    GpioFactory.setDefaultProvider(new RaspiGpioProvider(RaspiPinNumberingScheme.BROADCOM_PIN_NUMBERING));
//    final GpioController gpio = GpioFactory.getInstance();
//    // INPUTS
//    AtomicInteger counter = new AtomicInteger(0);
//
//    final GpioPinDigitalInput muteButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_22, PinPullResistance.PULL_DOWN);
//    muteButton.setShutdownOptions(true);
//    final GpioPinDigitalInput menuButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_21, PinPullResistance.PULL_DOWN);
//    menuButton.setShutdownOptions(true);
//    final GpioPinDigitalInput upButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_15, PinPullResistance.PULL_DOWN);
//    upButton.setShutdownOptions(true);
//    final GpioPinDigitalInput button02 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_UP);
//      button02.setMode(PinMode.DIGITAL_INPUT);
//    button02.setShutdownOptions(true);
//    final GpioPinDigitalInput button03 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03, PinPullResistance.PULL_UP);
//    button03.setShutdownOptions(true);
//    button03.setMode(PinMode.DIGITAL_INPUT);
//    final GpioPinDigitalInput button04 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, PinPullResistance.PULL_UP);
//    button04.setShutdownOptions(true);
//    button04.setMode(PinMode.DIGITAL_INPUT);
//    final GpioPinDigitalInput button05  = gpio.provisionDigitalInputPin(RaspiPin.GPIO_05, PinPullResistance.PULL_UP);
//    button05.setMode(PinMode.DIGITAL_INPUT);
//    button05.setShutdownOptions(true);
//    final GpioPinDigitalInput button06 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_06, PinPullResistance.PULL_DOWN);
//    button06.setShutdownOptions(true);
//
//    final GpioPinDigitalInput button07 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_07, PinPullResistance.PULL_DOWN);
//    final GpioPinDigitalInput button12 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_12, PinPullResistance.PULL_DOWN);
//    final GpioPinDigitalInput button13 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_13, PinPullResistance.PULL_DOWN);
//    final GpioPinDigitalInput button14 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_14, PinPullResistance.PULL_DOWN);
//    final GpioPinDigitalInput button25 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_25, PinPullResistance.PULL_DOWN);
//
//    final GpioPinDigitalInput button16 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_16, PinPullResistance.PULL_DOWN);
//    button16.setShutdownOptions(true);
//    button16.setMode(PinMode.DIGITAL_INPUT);
//    final GpioPinDigitalInput button11 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_11, PinPullResistance.PULL_DOWN);
//    final GpioPinDigitalInput button27 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_27, PinPullResistance.PULL_DOWN);
//    button27.setShutdownOptions(true);
//
//    int i2 = button02.getState().getValue();
//    int i3 = button03.getState().getValue();
//    int i4 = button04.getState().getValue();
//    int i5 = button05.getState().getValue();
//    int i6 = button06.getState().getValue();
//    int i16 = button16.getState().getValue();
//    int i22 = muteButton.getState().getValue();
//    int i21 = menuButton.getState().getValue();
//    int i14 = button14.getState().getValue();
//    int i15 = upButton.getState().getValue();
//    int i25 = button25.getState().getValue();
//    int i27 = button27.getState().getValue();
//    logger.info("button 2 is: " + i2);
//    logger.info("button 3 is: " + i3);
//    logger.info("button 4 is: " + i4);
//    logger.info("button 5 is: " + i5);
//    logger.info("button 6 is: " + i6);
//    logger.info("button 14 is: " + i14);
//    logger.info("button 15 is: " + i15);
//    logger.info("button 16 is: " + i16);
//
//    logger.info("button 21 is: " + i21);
//    logger.info("button 22 is: " + i22);
//    logger.info("button 25 is: " + i25);
//    logger.info("button 27 is: " + i27);
//
//
//    // OUTPUTS
//    //final GpioPinDigitalOutput rxGood = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, PinState.HIGH);
//
//    // Configure the input listeners
//
//    // create and register gpio pin listener
//
//
//    upButton.addListener(new GpioPinListenerDigital() {
//      Boolean ledState = false;
//
//      @Override
//      public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
//        // display pin state on console
//        logger.info(" --> " + getTimeStamp() + " ** UP BUTTON (wpi15) EVENT ** GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
//      }
//    });
//
//    button16.addListener(new GpioPinListenerDigital() {
//
//
//      @Override
//      public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
//        // display pin state on console
//        logger.info(" --> " + getTimeStamp() + " ** DOWN (wpi16) BUTTON EVENT ** GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
//      }
//    });
//    button02.addListener(new GpioPinListenerDigital() {
//      Boolean ledState = false;
//
//      @Override
//      public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
//        // display pin state on console
//        logger.info(" --> " + getTimeStamp() + " ** wpi02 BUTTON EVENT ** GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
//      }
//    });
//
//
//    button03.addListener(new GpioPinListenerDigital() {
//      Boolean ledState = false;
//
//      @Override
//      public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
//        // display pin state on console
//        logger.info(" --> " + getTimeStamp() + " ** wpi03 BUTTON EVENT ** GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
//      }
//    });
//
//
//    button04.addListener(new GpioPinListenerDigital() {
//      Boolean ledState = false;
//
//      @Override
//      public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
//        // display pin state on console
//        logger.info(" --> " + getTimeStamp() + " ** wpi04 BUTTON EVENT ** GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
//      }
//    });
//
//
//
//    button05.addListener(new GpioPinListenerDigital() {
//      Boolean ledState = false;
//
//      @Override
//      public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
//        // display pin state on console
//        logger.info(" --> " + getTimeStamp() + " ** wpi05 BUTTON EVENT ** GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
//      }
//    });
//
//
//    menuButton.addListener(new GpioPinListenerDigital() {
//      Boolean ledState = false;
//
//      @Override
//      public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
//        // display pin state on console
//        logger.info(" --> " + getTimeStamp() + " ** MENU BUTTON EVENT ** GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
//        int i2 = button02.getState().getValue();
//        int i3 = button03.getState().getValue();
//        int i4 = button04.getState().getValue();
//        int i5 = button04.getState().getValue();
//        int i16 = button16.getState().getValue();
//        int i22 = muteButton.getState().getValue();
//        int i21 = menuButton.getState().getValue();
//        int i15 = upButton.getState().getValue();
//        int i27 = button27.getState().getValue();
//        logger.info("button 2 is: " + i2);
//        logger.info("button 3 is: " + i3);
//        logger.info("button 4 is: " + i4);
//        logger.info("button 5 is: " + i5);
//        logger.info("button 16 is: " + i16);
//        logger.info("button 22 is: " + i22);
//        logger.info("button 21 is: " + i21);
//        logger.info("button 15 is: " + i15);
//        logger.info("button 27 is: " + i27);
//        if (counter.addAndGet(1) >5 ){
//           //do shutdown
//          gpio.shutdown();
//          return;
//        }
//      }
//    });
//
//    button06.addListener(new GpioPinListenerDigital() {
//
//      @Override
//      public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
//        // display pin state on console
//        logger.info(" --> " + getTimeStamp() + " ** WPI06 MUTE BUTTON EVENT ** GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
//      }
//    });
//
//    button14.addListener(new GpioPinListenerDigital() {
//
//      @Override
//      public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
//        // display pin state on console
//        logger.info(" --> " + getTimeStamp() + " ** WPI14 UP BUTTON EVENT ** GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
//      }
//    });
//
//
//
//    button07.addListener(new GpioPinListenerDigital() {
//      Boolean ledState = false;
//
//      @Override
//      public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
//        // display pin state on console
//        logger.info(" --> " + getTimeStamp() + " ** WPI07 BUTTON EVENT ** GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
//      }
//    });
//
//    button11.addListener(new GpioPinListenerDigital() {
//
//
//      @Override
//      public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
//        // display pin state on console
//        logger.info(" --> " + getTimeStamp() + " ** wpi11 BUTTON EVENT ** GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
//      }
//    });
//    button12.addListener(new GpioPinListenerDigital() {
//
//
//      @Override
//      public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
//        // display pin state on console
//        logger.info(" --> " + getTimeStamp() + " ** wpi12 BUTTON EVENT ** GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
//      }
//    });
//    button13.addListener(new GpioPinListenerDigital() {
//
//
//      @Override
//      public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
//        // display pin state on console
//        logger.info(" --> " + getTimeStamp() + " ** wpi13 BUTTON EVENT ** GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
//      }
//    });
//
//    button27.addListener(new GpioPinListenerDigital() {
//
//
//      @Override
//      public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
//        // display pin state on console
//        logger.info(" --> " + getTimeStamp() + " ** wpi27 BUTTON EVENT ** GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
//      }
//    });
//
//    muteButton.addListener(new GpioPinListenerDigital() {
//
//
//      @Override
//      public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
//        // display pin state on console
//        logger.info(" --> " + getTimeStamp() + " ** MUTE BUTTON EVENT ** GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
//      }
//    });
//
//
//    // keep program running until user aborts (CTRL-C)
//    for (;;) {
//      Thread.sleep(500);
//    }
//
//  }
//
//  private String getTimeStamp(){
//    String timeStamp = LocalDateTime.now()
//            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
//    return timeStamp;
//  }
//
//}
