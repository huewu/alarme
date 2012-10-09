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
#include "Hiroku.h"

extern SerialDebug  debug;

void Hiroku::init(void)
{
    while (!hc.connect("google.com", 80)) {
        debug.println("Hiroku connection is failed.");
        delay(1000);
    }
    hc.println("GET / HTTP/1.0");
    hc.println();
    delay(1000);
}

void Hiroku::get_response(void)
{
    if (!hc.available()) return;

    char c = hc.read();
    while (c != -1) {
        debug.print(c);
        c = hc.read();
    }
}

