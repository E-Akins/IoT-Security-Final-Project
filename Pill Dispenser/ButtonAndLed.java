/**
 *
 * @authors: Erick Akins & Daniel Ramirez
 *
 **/
package myremoteproj2;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

import com.pi4j.io.gpio.PinState;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class ButtonAndLed 
{
    public static void main(String args[]) 
    {
        try
        {
            final GpioController gpio = GpioFactory.getInstance();

            // Connect to broker
            Broker broker = new Broker("[Broker #1]");
            broker.connect();        
            
            // Configure LED
	       final GpioPinDigitalOutput wed = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00);
            final GpioPinDigitalOutput fri = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02);
            final GpioPinDigitalOutput sat = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03);
            final GpioPinDigitalOutput sun = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_08);
            final GpioPinDigitalOutput tue = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_09);
            final GpioPinDigitalOutput thu = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_13);
            final GpioPinDigitalOutput mon = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_14);
            final GpioPinDigitalOutput ref = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25);
            
            // Set every LED off
            mon.high();
            tue.high();
            wed.high();
            thu.high();
            fri.high();
            sat.high();
            sun.high();
            ref.high();
            
            // Configure Pushbutton 
            ListenGpioExample test = new ListenGpioExample();
            test.Button();
            
            // Read broker variable and turn on corresponding LED
            while (true)
            { 
                if ("1".equals(broker.light))
                {
                    sun.low();
                    System.out.println("Broker Activated!");
                    broker.light = "0";
                }
                
                if ("2".equals(broker.light))
                {
                    mon.low();
                    System.out.println("Broker Activated!");
                    broker.light = "0";
                }

                if ("3".equals(broker.light))
                {
                    tue.low();
                    System.out.println("Broker Activated!");
                    broker.light = "0";
                }

                if ("4".equals(broker.light))
                {
                    wed.low();
                    System.out.println("Broker Activated!");
                    broker.light = "0";
                }

                if ("5".equals(broker.light))
                {
                    thu.low();
                    System.out.println("Broker Activated!");
                    broker.light = "0";
                }

                if ("6".equals(broker.light))
                {
                    fri.low();
                    System.out.println("Broker Activated!");
                    broker.light = "0";
                }

                if ("7".equals(broker.light))
                {
                    sat.low();
                    System.out.println("Broker Activated!");   
                    broker.light = "0";
                }
                
                if ("8".equals(broker.light))
                {
                    ref.low();
                    broker.light = "0";
                }
                
                // When pushbutton is pressed, turn off all LEDs
                if (test.pressed == true)
                {
                    mon.high();
                    tue.high();
                    wed.high();
                    thu.high();
                    fri.high();
                    sat.high();
                    sun.high();
                    ref.high();
                    test.pressed = false;
                }
            }
        }
        catch (Exception e) 
        {
	    e.printStackTrace();
	}
    }
}
