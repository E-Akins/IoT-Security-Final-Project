/**
 *
 * @authors: Erick Akins & Daniel Ramirez
 *
 **/

package myremoteproj2;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

public class MessageActionListener implements IMqttActionListener 
{
    protected final String messageText;
    protected final String topic;
    protected final String userContext;

    // Constructor
    public MessageActionListener( String topic, String messageText, String userContext) 
    {
        this.topic = topic;
        this.messageText = messageText;
        this.userContext = userContext;
    }

    // Check token validity and print statement if successful
    @Override
    public void onSuccess(IMqttToken asyncActionToken) 
    {
        if ((asyncActionToken != null) && asyncActionToken.getUserContext().equals(userContext)) 
        {
            System.out.println( String.format("Message '%s' published to topic '%s'", messageText, topic));
        }
    }

    // Prints stack trace on failure
    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception) 
    {
        exception.printStackTrace();
    }
}
