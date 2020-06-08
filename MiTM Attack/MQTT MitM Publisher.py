#
#
# @authors: Erick Akins & Daniel Ramirez
#
#

import paho.mqtt.client as paho

broker="10.0.0.4"
port=1883

def on_publish(client,userdata,result):         #create function for callback
    print("data published \n")
    pass

client1= paho.Client("control1")                #create client object
client1.on_publish = on_publish                 #assign function to callback
client1.connect(broker,port)                    #establish connection

while 1:
    ret= client1.publish("clock","1")    #publish
    ret= client1.publish("clock","2")    #publish
    ret= client1.publish("clock","3")    #publish
    ret= client1.publish("clock","4")    #publish
    ret= client1.publish("clock","5")    #publish
    ret= client1.publish("clock","6")    #publish
    ret= client1.publish("clock","7")    #publish
    ret= client1.publish("clock","8")    #publish
