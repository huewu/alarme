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
//#include "LcdDisplay.h"
#include "Network.h"
#include "Ntp.h"
#include "Pusher.h"
#include "Hiroku.h"

SerialDebug     debug;
//LcdDisplay      lcd;
Network         net;

Ntp             ntp;
Pusher          pusher;
Hiroku          hiroku;

void setup(void)
{
    debug.init();
    debug.on();
    debug.println("Alarme v0.1 - Social Alarm Project");
    debug.println("Google HackFair 2012 in Seoul");

    net.init();
    ntp.init();
    pusher.init();
    hiroku.init();
}

void loop(void)
{
    ntp.update();
    pusher.monitor();
    hiroku.get_response();
}

