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
#include "arraylist.h"
#include "aJSON.h"
//#include "clock.h"

extern SerialDebug  debug;
extern ArrayList * arrayList;
//extern Clock clk;

void Pusher::init(void)
{
    const char privatekey[] = "81a20baa40945ce4b0a6";

    while (!pc.connect(privatekey)) {
        debug.println("pusher connection failed.");
        delay(1000);
    }
    debug.println("pusher connection successful.");
    bind_event();
}

void Pusher::bind_event(void)
{
    pc.subscribe("cid");
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
    // parsing data by using json
    char *jsonStr = (char *)malloc(data.length() + 1);
    data.toCharArray(jsonStr, data.length() + 1);

    aJsonObject* root = aJson.parse(jsonStr);
    if (root != NULL){
        aJsonObject* aid = aJson.getObjectItem(root, "aid");
        aJsonObject* time = aJson.getObjectItem(root, "time");
        aJsonObject* type = aJson.getObjectItem(root, "type");

        Item* item = new Item();
        item->setAid(aid->valuestring);
        item->setTime(time->valueint);
        item->setType(type->valuestring);
        item->setAlive(true);

        arrayList->addItem(item);
    }
   // debug.println(data);

    free(jsonStr);
}

void Pusher::dismiss_alarm(String data)
{
    // turn off alarm
    // ....
    //clk.stop_alarm();

    char *jsonStr = (char *)malloc(data.length() + 1);
    data.toCharArray(jsonStr, data.length() + 1);

    aJsonObject* root = aJson.parse(jsonStr);
  
    if (root != NULL){
        aJsonObject* aid = aJson.getObjectItem(root, "aid");

        for (int i=0; i<arrayList->getSize(); i++){
            Item* item = arrayList->getItem(i);
            if (item->getAid() == aid->valuestring)
                item->setAlive(false); 
        }
    }

    free(jsonStr);
}

