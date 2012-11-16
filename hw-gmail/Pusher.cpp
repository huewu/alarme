/*
 *  Alarme Project for Google HackFair 2012 in Seoul
 *  Arduino hardware alarm sketch
 *      
 *  Kwanlae Kim <voidopennet@gmail.com>
 *  Chanseok Yang <huewu.yang@gmail.com>
 *  Jinserk Baik <jinserk.baik@gmail.com>
 *  Wonseok Yang <before30@gmail.com>
 *
 *  Copyright (c) 2012, all rights reserved.
 */

#include "SerialDebug.h"
#include "Pusher.h"
#include "aJSON.h"

#include "Adafruit_Thermal.h"



extern SerialDebug  debug;
extern Adafruit_Thermal printer;

void Pusher::init(void)
{
    const char privatekey[] = "95189161bfb8e7276632";

    while (!pc.connect(privatekey)) {
        debug.println("pusher connection failed.");
        delay(500);
    }
    debug.println("pusher connection successful.");
    bind_event();
}

void Pusher::bind_event(void)
{
    pc.subscribe("ghf.alarme");
    pc.bind("print", printme);
}

void Pusher::monitor(void)
{
    if (pc.connected()) {
        pc.monitor();
    }
}

void Pusher::printme(String data)
{
    Serial.println(data);
    // parsing data by using json
    char *jsonStr = (char *)malloc(data.length() + 1);
    data.toCharArray(jsonStr, data.length() + 1);

    aJsonObject* root = aJson.parse(jsonStr);
    if (root != NULL){
        aJsonObject* dd = aJson.getObjectItem(root, "data");

        aJsonObject* root2 = aJson.parse(dd->valuestring);
        aJsonObject* name = aJson.getObjectItem(root2, "name");
        aJsonObject* msg = aJson.getObjectItem(root2, "message");

        if (name != NULL && msg != NULL ){
            Serial.println(name->valuestring);
            Serial.println(msg->valuestring);

            printer.wake();
            printer.setDefault();
            printer.boldOn();
            printer.println(); 
            printer.setSize('M');
            printer.justify('C');
            printer.println(name->valuestring);
            printer.boldOn();
            printer.setSize('L');
            printer.justify('C');
            printer.print(msg->valuestring);
            printer.println(); 
            printer.println(); 
            printer.boldOff();
            printer.feed(2);
            printer.sleep();
        
        }

    }

    free(jsonStr);
}

