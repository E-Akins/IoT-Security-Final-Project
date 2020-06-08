/**
 *
 * @authors: Erick Akins & Daniel Ramirez
 *
 **/
package myremoteproj2;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ThreadLocalRandom;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Broker implements MqttCallback, IMqttActionListener 
{
    // Declare critical broker strings
    public static final String COMMAND_KEY = "COMMAND";
    public static final String COMMAND_SEPARATOR = ":";
    public static final String GET_ALTITUDE_COMMAND_KEY = "GET_ALTITUDE";
    public static final String TOPIC = "clock";
    public static final String ENCODING = "UTF-8";
    
    public String light = "0";
    
    // Quality of Service = Exactly once
    // I want to receive all messages exactly once
    public static final int QUALITY_OF_SERVICE = 2;
    protected String name;
    protected String clientId;
    protected MqttAsyncClient client;
    protected MemoryPersistence memoryPersistence;
    protected IMqttToken connectToken;
    protected IMqttToken subscribeToken;

    // Constructor
    public Broker(String name) { this.name = name; }
    
    // Returns the name variable of a Broker object
    public String getName() { return name; }

    // Establishes MQTT connection
    public void connect() 
    {
        try {

            // Establsih parameters for tcp connection with client
            MqttConnectOptions options = new MqttConnectOptions();
            memoryPersistence = new MemoryPersistence();
            String serverURI = "tcp://10.0.0.4:1883";
            clientId = MqttAsyncClient.generateClientId();
            client = new MqttAsyncClient(serverURI, clientId, memoryPersistence);
            
            // Instantiate callback instance
            client.setCallback(this);
            connectToken = client.connect(
                    options, null, this);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }    
    
    // Allows client to publish a text message
    public MessageActionListener publishTextMessage(String messageText) 
    {
        byte[] bytesMessage;
        try {
            bytesMessage = messageText.getBytes(ENCODING);
            MqttMessage message;
            message = new MqttMessage(bytesMessage);
            String userContext = "ListeningMessage";
            MessageActionListener actionListener = new MessageActionListener(TOPIC, messageText, userContext);
            client.publish(TOPIC, message, userContext, actionListener);
            return actionListener;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (MqttException e) {
            e.printStackTrace();
            return null;
        }
    }      
    
    // Method signaling that a message has arrived from the MQTT broker.
    // This method must return cleanly before the broker will send back an acknowledgement to the server
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception 
    {
        if (!topic.equals(TOPIC)) { return; }

        String messageText = new String(message.getPayload(), ENCODING);
        System.out.println(String.format("%s received %s: %s", name, topic, messageText));
        light = messageText;
        String[] keyValue = messageText.split(COMMAND_SEPARATOR);
        if (keyValue.length != 3) {
            return;
        }
        if (keyValue[0].equals(COMMAND_KEY) && keyValue[1].equals(GET_ALTITUDE_COMMAND_KEY) && keyValue[2].equals(name)) {
            
            // Process the get altitude command
            int altitudeInFeet = ThreadLocalRandom.current().nextInt(1, 6001);
            System.out.println(String.format("%s altitude: %d feet", name, altitudeInFeet));
        }
    }

    // The MQTT client lost the connection
    @Override
    public void connectionLost(Throwable cause) 
    {
	   cause.printStackTrace();
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) 
    {
        // Nothing here for now
    }

    // On a successful connection, print connection status, configure subscriber token, and publish listener message
    @Override
    public void onSuccess(IMqttToken asyncActionToken) 
    {
        if (asyncActionToken.equals(connectToken)) 
        {
            System.out.println(String.format("%s successfully connected", name));
            try 
            {
                subscribeToken = client.subscribe(TOPIC, QUALITY_OF_SERVICE, null, this);
            } 
            catch (MqttException e) 
            {
                e.printStackTrace();
            }
        } 
        else if (asyncActionToken.equals(subscribeToken)) 
        {
            System.out.println(String.format("%s subscribed to the %s topic", name, TOPIC));
            publishTextMessage(String.format("%s is listening.", name));
        }    
    }

    // Handles failures
    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception) 
    {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}
