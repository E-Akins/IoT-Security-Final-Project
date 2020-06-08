/**
 *
 * @authors: Erick Akins & Daniel Ramirez
 *
 **/
package myremoteproj;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Button 
{
    private GpioPinDigitalInput buttonPin;
    boolean pressed = false;

    public Button(GpioController gpio, Pin pin) 
    {    
        // Provision the pin
        buttonPin = gpio.provisionDigitalInputPin(pin, PinPullResistance.PULL_UP);

        // Add a listener to the button
        buttonPin.addListener(new GpioPinListenerDigital() 
        {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {

                if (event.getState().isLow()) 
                {   
                    pressed = true;
                }
                else
                {
                    pressed = false;
                }
            }
        });
    }
}
