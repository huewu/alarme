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
#include "LcdDisplay.h"
#include "Pusher.h"
#include "arraylist.h"
#include "aJSON.h"
#include "Clock.h"

extern SerialDebug  debug;
extern LcdDisplay   lcd;
extern ArrayList * arrayList;
extern Clock clk;
extern char* cid;

void Pusher::init(void)
{
    const char privatekey[] = "81a20baa40945ce4b0a6";

    while (!pc.connect(privatekey)) {
        debug.println("pusher connection failed.");
        delay(500);
        clk.display();
    }
    debug.println("pusher connection successful.");
    lcd.select_line(1);
    lcd.print("Pusher connected");
    clk.display();
    bind_event();
}

void Pusher::bind_event(void)
{
    pc.subscribe(cid);
    pc.bind("alarmSET", set_alarm);
    pc.bind("alarmOFF", dismiss_alarm);
}

void Pusher::monitor(void)
{
    //static int i = 0;
    if (pc.connected()) {
        //debug.print("... monitoring ");
        //debug.println(i++);
        pc.monitor();
    }
}

void Pusher::set_alarm(String data)
{
    Serial.println(data);
    // parsing data by using json
    char *jsonStr = (char *)malloc(data.length() + 1);
    data.toCharArray(jsonStr, data.length() + 1);

    aJsonObject* root = aJson.parse(jsonStr);
    if (root != NULL){
        aJsonObject* dd = aJson.getObjectItem(root, "data");

        aJsonObject* root2 = aJson.parse(dd->valuestring);
        aJsonObject* aid = aJson.getObjectItem(root2, "aid");
        aJsonObject* time = aJson.getObjectItem(root2, "time");
        aJsonObject* type = aJson.getObjectItem(root2, "type");

        if (aid != NULL && time != NULL && type != NULL){

            Item* item = new Item();
            item->setAid(aid->valuestring);
            item->setTime(time->valuestring);
            item->setType(type->valuestring);
            item->setAlive(true);
            Serial.println(aid->valuestring);
            Serial.println(time->valuestring);
            arrayList->addItem(item);
        }

    }
   // debug.println(data);
    lcd.select_line(1);
    lcd.print("Alarm set ......");

    free(jsonStr);
}

void Pusher::dismiss_alarm(String data)
{
    // turn off alarm
    // ....
    debug.println(data);
    clk.stop_alarm();

    char *jsonStr = (char *)malloc(data.length() + 1);
    data.toCharArray(jsonStr, data.length() + 1);

    aJsonObject* root = aJson.parse(jsonStr);
  
    if (root != NULL){
        aJsonObject* dd = aJson.getObjectItem(root, "data");
        aJsonObject* root2 = aJson.parse(dd->valuestring);
        aJsonObject* aid = aJson.getObjectItem(root2, "aid");

        for (int i=0; i<arrayList->getSize(); i++){
            Item* item = arrayList->getItem(i);
            if (item->getAid() == aid->valuestring)
                item->setAlive(false); 
        }
    }

    free(jsonStr);
}

