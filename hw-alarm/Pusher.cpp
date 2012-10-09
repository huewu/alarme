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

extern SerialDebug  debug;

void Pusher::init(void)
{
    const char privatekey[] = "0bd9a76b9e35d7efe818";

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
    pc.bind("set_alarm", set_alarm);
    pc.bind("dismiss_alarm", dismiss_alarm);
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
    debug.println(data);
}

void Pusher::dismiss_alarm(String data)
{
}

