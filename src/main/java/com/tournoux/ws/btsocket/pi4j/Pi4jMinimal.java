package com.tournoux.ws.btsocket.pi4j;

/*-
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: EXAMPLE  :: Sample Code
 * FILENAME      :  MinimalExample.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2020 Pi4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.pi4j.Pi4J;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.io.gpio.digital.PullResistance;
import com.pi4j.util.Console;
import org.springframework.stereotype.Component;

/**
 * <p>This example fully describes the base usage of Pi4J by providing extensive comments in each step.</p>
 *
 * @author Frank Delporte (<a href="https://www.webtechie.be">https://www.webtechie.be</a>)
 * @version $Id: $Id
 */
@Component
public class Pi4jMinimal {

//    private static final int PIN_BUTTON = 24; // PIN 18 = BCM 24
//    private static final int PIN_LED = 22; // PIN 15 = BCM 22

    /**
     * added by bob
     */

    private static final int PIN_BUTTON_5 = 5; // PIN 29 = BCM 05
    private static final int PIN_BUTTON_6 = 6; // PIN 31 = BCM 06
    private static final int PIN_BUTTON_14 = 14; // PIN 8 = BCM 14
    private static final int PIN_BUTTON_23 = 23; // PIN 16 = BCM 23
    private static final int PIN_BUTTON_26 = 26; // PIN 37 = BCM 26
    private static final int PIN_BUTTON_22 = 22; // PIN 15 = BCM 22
    private static final int PIN_BUTTON_27 = 27; // PIN 13 = BCM 27
    private static final int PIN_BUTTON_24 = 24; // PIN 18 = BCM 24
    private static final int PIN_BUTTON_25 = 25; // PIN 22 = BCM 25
    private static final int PIN_BUTTON_16 = 16; // PIN 36 = BCM 16

    private static int pressCount = 0;

    /**
     * This application blinks a led and counts the number the button is pressed. The blink speed increases with each
     * button press, and after 5 presses the application finishes.
     *
     * @param args an array of {@link java.lang.String} objects.
     * @throws java.lang.Exception if any.
     */
    public void manageGpios() throws Exception {
        // Create Pi4J console wrapper/helper
        // (This is a utility class to abstract some of the boilerplate stdin/stdout code)
        final var console = new Console();

        // Print program title/header
        console.title("<-- The Pi4J Project -->", "Minimal Example project");

        // ************************************************************
        //
        // WELCOME TO Pi4J:
        //
        // Here we will use this getting started example to
        // demonstrate the basic fundamentals of the Pi4J library.
        //
        // This example is to introduce you to the boilerplate
        // logic and concepts required for all applications using
        // the Pi4J library.  This example will do use some basic I/O.
        // Check the pi4j-examples project to learn about all the I/O
        // functions of Pi4J.
        //
        // ************************************************************

        // ------------------------------------------------------------
        // Initialize the Pi4J Runtime Context
        // ------------------------------------------------------------
        // Before you can use Pi4J you must initialize a new runtime
        // context.
        //
        // The 'Pi4J' static class includes a few helper context
        // creators for the most common use cases.  The 'newAutoContext()'
        // method will automatically load all available Pi4J
        // extensions found in the application's classpath which
        // may include 'Platforms' and 'I/O Providers'
        var pi4j = Pi4J.newAutoContext();

        // ------------------------------------------------------------
        // Output Pi4J Context information
        // ------------------------------------------------------------
        // The created Pi4J Context initializes platforms, providers
        // and the I/O registry. To help you to better understand this
        // approach, we print out the info of these. This can be removed
        // from your own application.
        // OPTIONAL
        PrintInfo.printLoadedPlatforms(console, pi4j);
        PrintInfo.printDefaultPlatform(console, pi4j);
        PrintInfo.printProviders(console, pi4j);

        // Here we will create I/O interfaces for a (GPIO) digital output
        // and input pin. Since no specific 'provider' is defined, Pi4J will
        // use the default `DigitalOutputProvider` for the current default platform.
/*
        var ledConfig = DigitalOutput.newConfigBuilder(pi4j)
                .id("led")
                .name("LED Flasher")
                .address(PIN_LED)
                .shutdown(DigitalState.LOW)
                .initial(DigitalState.LOW)
                .provider("pigpio-digital-output");
        var led = pi4j.create(ledConfig);

        var buttonConfig = DigitalInput.newConfigBuilder(pi4j)
                .id("button")
                .name("Press button")
                .address(PIN_BUTTON)
                .pull(PullResistance.PULL_DOWN)
                .debounce(3000L)
                .provider("pigpio-digital-input");
        var button = pi4j.create(buttonConfig);
        button.addListener(e -> {
            if (e.state() == DigitalState.LOW) {
                pressCount++;
                console.println("Button was pressed for the " + pressCount + "th time");
            }
        });
*/
        var buttonConfig = DigitalInput.newConfigBuilder(pi4j)
                .id("button5")
                .name("Press button")
                .address(PIN_BUTTON_5)
                .pull(PullResistance.PULL_UP)
                .debounce(3000L)
                .provider("pigpio-digital-input");
        var button5 = pi4j.create(buttonConfig);
        button5.addListener(e -> {
            if (e.state() == DigitalState.LOW) {
                console.println("Button5 was pressed.");
            }
        });


        buttonConfig = DigitalInput.newConfigBuilder(pi4j)
                .id("button6")
                .name("Press button")
                .address(PIN_BUTTON_6)
                .pull(PullResistance.PULL_UP)
                .debounce(3000L)
                .provider("pigpio-digital-input");
        var button6 = pi4j.create(buttonConfig);
        button6.addListener(e -> {
            if (e.state() == DigitalState.LOW) {
                console.println("Button6 was pressed.");
            }
        });


        buttonConfig = DigitalInput.newConfigBuilder(pi4j)
                .id("button14")
                .name("Press button")
                .address(PIN_BUTTON_14)
                .pull(PullResistance.PULL_UP)
                .debounce(3000L)
                .provider("pigpio-digital-input");
        var button14 = pi4j.create(buttonConfig);
        button14.addListener(e -> {
            if (e.state() == DigitalState.LOW) {
                console.println("Button14 was pressed.");
            }
        });


        buttonConfig = DigitalInput.newConfigBuilder(pi4j)
                .id("button23")
                .name("Press button")
                .address(PIN_BUTTON_23)
                .pull(PullResistance.PULL_UP)
                .debounce(3000L)
                .provider("pigpio-digital-input");
        var button23 = pi4j.create(buttonConfig);
        button23.addListener(e -> {
            if (e.state() == DigitalState.LOW) {
                console.println("Button23 was pressed.");
            }
        });


        buttonConfig = DigitalInput.newConfigBuilder(pi4j)
                .id("button26")
                .name("Press button")
                .address(PIN_BUTTON_26)
                .pull(PullResistance.PULL_UP)
                .debounce(3000L)
                .provider("pigpio-digital-input");
        var button26 = pi4j.create(buttonConfig);
        button26.addListener(e -> {
            if (e.state() == DigitalState.LOW) {
                console.println("Button26 was pressed.");
                pressCount++;
                console.println("COUNT IS NOW " + pressCount);
            }
        });

        buttonConfig = DigitalInput.newConfigBuilder(pi4j)
                .id("button22")
                .name("Press button")
                .address(PIN_BUTTON_22)
                .pull(PullResistance.PULL_UP)
                .debounce(3000L)
                .provider("pigpio-digital-input");
        var button22 = pi4j.create(buttonConfig);
        button22.addListener(e -> {
            if (e.state() == DigitalState.LOW) {
                console.println("Button22 was pressed.");
            }
        });


        buttonConfig = DigitalInput.newConfigBuilder(pi4j)
                .id("button27")
                .name("Press button")
                .address(PIN_BUTTON_27)
                .pull(PullResistance.PULL_UP)
                .debounce(3000L)
                .provider("pigpio-digital-input");
        var button27 = pi4j.create(buttonConfig);
        button27.addListener(e -> {
            if (e.state() == DigitalState.LOW) {
                console.println("Button27 was pressed.");
            }
        });


        buttonConfig = DigitalInput.newConfigBuilder(pi4j)
                .id("button24")
                .name("Press button")
                .address(PIN_BUTTON_24)
                .pull(PullResistance.PULL_UP)
                .debounce(3000L)
                .provider("pigpio-digital-input");
        var button24 = pi4j.create(buttonConfig);
        button24.addListener(e -> {
            if (e.state() == DigitalState.LOW) {
                console.println("Button24 was pressed.");
            }
        });


        buttonConfig = DigitalInput.newConfigBuilder(pi4j)
                .id("button25")
                .name("Press button")
                .address(PIN_BUTTON_25)
                .pull(PullResistance.PULL_UP)
                .debounce(3000L)
                .provider("pigpio-digital-input");
        var button25 = pi4j.create(buttonConfig);
        button25.addListener(e -> {
            if (e.state() == DigitalState.LOW) {
                console.println("Button25 was pressed.");
            }
        });


        buttonConfig = DigitalInput.newConfigBuilder(pi4j)
                .id("button16")
                .name("Press button")
                .address(PIN_BUTTON_16)
                .pull(PullResistance.PULL_UP)
                .debounce(3000L)
                .provider("pigpio-digital-input");
        var button16 = pi4j.create(buttonConfig);
        button16.addListener(e -> {
            if (e.state() == DigitalState.LOW) {
                console.println("Button16 was pressed.");
            }
        });

        // OPTIONAL: print the registry
        PrintInfo.printRegistry(console, pi4j);

        while (pressCount < 5) {
            Thread.sleep(500 / (pressCount + 1));
        }

        // ------------------------------------------------------------
        // Terminate the Pi4J library
        // ------------------------------------------------------------
        // We we are all done and want to exit our application, we must
        // call the 'shutdown()' function on the Pi4J static helper class.
        // This will ensure that all I/O instances are properly shutdown,
        // released by the the system and shutdown in the appropriate
        // manner. Terminate will also ensure that any background
        // threads/processes are cleanly shutdown and any used memory
        // is returned to the system.

        // Shutdown Pi4J
        pi4j.shutdown();
    }
}

