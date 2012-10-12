/*
 *  Alarme Project for hc HackFair 2012 in Seoul
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
#include "Heroku.h"

extern SerialDebug  debug;

void Heroku::init(void)
{
    while (!hc.connect("google.com", 80)) {
        debug.println("Heroku connection is failed.");
        delay(1000);
    }
    hc.println("GET / HTTP/1.0");
    hc.println();
    delay(1000);
}

void Heroku::get_response(void)
{
    if (!hc.available()) return;

    char c = hc.read();
    while (c != -1) {
        debug.print(c);
        c = hc.read();
    }
}

