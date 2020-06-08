/**
 *
 * @authors: Erick Akins & Daniel Ramirez
 *
 **/
package myremoteproj;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;  
import java.util.Date;  
import javax.sound.sampled.UnsupportedAudioFileException;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Calendar;

public class Clock
{
    public static void main(String args[]) throws InterruptedException 
    {
        System.out.println("Code is running!");
        
        int setmode = 0;
        
        // Initialize GPIO controller
        final GpioController gpio = GpioFactory.getInstance();
        
        // Configure buttons
        Button button = new Button(gpio, RaspiPin.GPIO_07);        
        Button buttontime = new Button(gpio, RaspiPin.GPIO_21);
        
        // Configure LCD
        I2CDevice _device = null;
        I2CLCD _lcd = null;
        try 
        {       
            I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
            _device = bus.getDevice(0x27);
            _lcd = new I2CLCD(_device);
            _lcd.init();
            _lcd.backlight(true);
        } 
        catch (Exception ex) 
        {
            System.out.println(ex.toString());
        }  
        
        // COnfigure Date field information 
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");  
        Date date = new Date();  
        
        String tracker = formatter.format(date);
        String daystring = "Mon";
        int hour = 12;
        int min = 7;
        int sec = 50;
        
        int refdayint = 1;
        String refdaystring = "Mon";
        
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);        
        
        switch (dayOfWeek)
        {
            case 1:
            {
                daystring = "Sun";
                break;
            }
            
            case 2:
            {
                daystring = "Mon";
                break;
            }
            
            case 3:
            {
                daystring = "Tue";
                break;
            }
            
            case 4:
            {
                daystring = "Wed";
                break;
            }

            case 5:
            {
                daystring = "Thu";
                break;
            }
            
            case 6:
            {
                daystring = "Fri";
                break;
            }

            case 7:
            {
                daystring = "Sat";
                break;
            }         
        }
        
        switch (refdayint)
        {
            case 1:
            {
                refdaystring = "Sun";
                break;
            }
            
            case 2:
            {
                refdaystring = "Mon";
                break;
            }
            
            case 3:
            {
                refdaystring = "Tue";
                break;
            }
            
            case 4:
            {
                refdaystring = "Wed";
                break;
            }

            case 5:
            {
                refdaystring = "Thu";
                break;
            }
            
            case 6:
            {
                refdaystring = "Fri";
                break;
            }

            case 7:
            {
                refdaystring = "Sat";
                break;
            }         
        }        
        
        // Configure Time field information
        String timer = String.format("%d:%d:%d",hour,min,sec);
        
        if (hour < 10)
            timer = String.format("0%d:%d:%d",hour,min,sec);
            
        if (min < 10)
            timer = String.format("%d:0%d:%d",hour,min,sec);
            
        if (sec < 10)
            timer = String.format("%d:%d:0%d",hour,min,sec);     
        
        System.out.println(tracker);
        System.out.println(timer);  
        
        int temptime = 0;
        
        // Configure clock functionalities
        try 
        {
            SimpleAudioPlayer audioPlayer = new SimpleAudioPlayer();
            boolean once = false;
            
            while (true)
            {
                // Display Date information
                formatter = new SimpleDateFormat("HH:mm:ss");   
                date = new Date();            

                try
                {
                    String output = String.format("%s %s",daystring,formatter.format(date));
                    _lcd.display_string_pos(output, 2, 4);
                } 
                catch (Exception ex) 
                {
                    System.out.println(ex.toString());
                }     

                tracker = formatter.format(date);
                dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                
                switch (dayOfWeek) {
                    case 1: {
                        daystring = "Sun";
                        break;
                    }

                    case 2: {
                        daystring = "Mon";
                        break;
                    }

                    case 3: {
                        daystring = "Tue";
                        break;
                    }

                    case 4: {
                        daystring = "Wed";
                        break;
                    }

                    case 5: {
                        daystring = "Thu";
                        break;
                    }

                    case 6: {
                        daystring = "Fri";
                        break;
                    }

                    case 7: {
                        daystring = "Sat";
                        break;
                    }
                }   
                
                switch (refdayint) {
                    case 1: {
                        refdaystring = "Sun";
                        break;
                    }

                    case 2: {
                        refdaystring = "Mon";
                        break;
                    }

                    case 3: {
                        refdaystring = "Tue";
                        break;
                    }

                    case 4: {
                        refdaystring = "Wed";
                        break;
                    }

                    case 5: {
                        refdaystring = "Thu";
                        break;
                    }

                    case 6: {
                        refdaystring = "Fri";
                        break;
                    }

                    case 7: {
                        refdaystring = "Sat";
                        break;
                    }
                }                

                // Display time information
                try 
                {
                    String output = String.format("Timer: %s", timer);    
                    _lcd.display_string_pos(output, 3, 1);
                } 
                catch (Exception ex) 
                {
                    System.out.println(ex.toString());
                }
                
                try 
                {
                    String output = String.format("Ref: %s", refdaystring);    
                    _lcd.display_string_pos(output, 4, 6);
                } 
                catch (Exception ex) 
                {
                    System.out.println(ex.toString());
                }                

                // Assign pushbutton tasks
                if (button.pressed == true)
                {
                    if (temptime >= 5)
                    {
                        _lcd.display_string_pos("          ", 1, 5);

                        setmode = setmode + 1;

                        if (setmode > 4)
                            setmode = 0;

                        temptime = 0;
                    }
                    temptime = temptime + 1;
                    
                    if (once == true)
                    {
                        audioPlayer.stop();
                        once = false;
                    }
                }
                else
                {
                    temptime = 0;
                }

                // Allow user to toggle between different modes of setting the clock
                switch (setmode)
                {
                    // Toggle to Clock Mode
                    case 0:
                    {
                        _lcd.display_string_pos("Clock Mode", 1, 5);
                        break;
                    }

                    // Toggle to set hour
                    case 1:
                    {
                        _lcd.display_string_pos("Set Mode H", 1, 5);
                        if (buttontime.pressed == true)
                        {
                            hour = hour + 1;

                            if (hour > 24)
                                hour = 0;                    
                            
                            if (hour < 10)
                                timer = String.format("0%d:%d:%d",hour,min,sec);
                            else
                                timer = String.format("%d:%d:%d",hour,min,sec);                        
                        }                    
                        break;
                    }

                    // Toggle to set minute
                    case 2:
                    {
                        _lcd.display_string_pos("Set Mode M", 1, 5);
                        if (buttontime.pressed == true)
                        {
                            min = min + 1;

                            if (min > 59)
                                min = 0;                    

                            if (min < 10)
                                timer = String.format("%d:0%d:%d",hour,min,sec);
                            else
                                timer = String.format("%d:%d:%d",hour,min,sec);
                        }                      
                        break;
                    }

                    // Toggle to set second
                    case 3:
                    {
                        _lcd.display_string_pos("Set Mode S", 1, 5);
                        if (buttontime.pressed == true)
                        {
                            sec = sec + 1;

                            if (sec > 59)
                                sec = 0;                    

                            if (sec < 10)
                                timer = String.format("%d:%d:0%d",hour,min,sec);
                            else
                                timer = String.format("%d:%d:%d",hour,min,sec);
                        }                      
                        break;
                    }
                    
                    // Toggle to set day
                    case 4:
                    {
                        _lcd.display_string_pos("Set Mode D", 1, 5);
                        if (buttontime.pressed == true) 
                        {
                            refdayint = refdayint + 1;

                            if (refdayint > 7) {
                                refdayint = 0;
                            }
                        }                        
                        break;
                    }
                }
                
                // Configure clock action when timer is reached
                if (timer.equals(tracker))
                {
                    // Play alarm and communicate with pill dispenser   
                    if (once == false)
                    {
                        System.out.println("Timer Went Off!");
                        audioPlayer.alarm();    
                        once = true;
                        
                        // Send message to broker
                        // Declare variables
                        String topic        = "clock";
                        
                        String content      = Integer.toString(dayOfWeek);
                        String content2     = "8";
                        
                        String content3     = "I've been hacked!";
                        
                        int qos             = 2;
                        String broker       = "tcp://10.0.0.4:1883";
                        String clientId     = "JavaSample";

                        // Construct MQTT client connection objects and connect
                        MemoryPersistence persistence = new MemoryPersistence();                        
                        MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
                        MqttConnectOptions connOpts = new MqttConnectOptions();
                        connOpts.setCleanSession(true);
                        sampleClient.connect(connOpts);

                        // Construct and send message
                        MqttMessage message = new MqttMessage(content.getBytes());
                        message.setQos(qos);
                        sampleClient.publish(topic, message);
                        
                        // If refill is necessary, send corresponding message to pill dispenser
                        if (daystring.equals(refdaystring))
                        {
                            MqttMessage message2 = new MqttMessage(content2.getBytes());
                            message.setQos(qos);
                            sampleClient.publish(topic, message2);
                            
                            MqttMessage message3 = new MqttMessage(content3.getBytes());
                            message.setQos(qos);
                            sampleClient.publish(topic, message3);                            
                        }
                        
                        // Disconnect from client
                        sampleClient.disconnect();                                                
                    }
                }            
            }
        }
        catch (Exception ex)
        {
            System.out.println("Error with playing sound."); 
            ex.printStackTrace();                     
        }
            
    }
}
