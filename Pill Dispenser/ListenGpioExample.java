/**
 *
 * @authors: Erick Akins & Daniel Ramirez
 *
 **/

package myremoteproj2;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class ListenGpioExample {
    
    boolean pressed = false;

    public void Button() throws InterruptedException {
        // Create GPIO controller
        final GpioController gpio = GpioFactory.getInstance();

        // Provision GPIO pin #02 as an input pin with its internal pull down resistor enabled
        final GpioPinDigitalInput myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_07, PinPullResistance.PULL_DOWN);

        // Set shutdown state for this input pin
        myButton.setShutdownOptions(true);

        // Create and register GPIO pin listener
        myButton.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // Display pin state on console
                
                if (event.getState().isLow())
                {
                    pressed = true;
                }
                
                if (event.getState().isHigh())
                {
                    pressed = false;
                }
            }
        });       
        // keep program running until user aborts (CTRL-C)
    }
}