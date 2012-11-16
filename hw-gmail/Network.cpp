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
#include "Network.h"

extern SerialDebug  debug;

void Network::init(void)
{
    while (!associate()) {
        debug.println("Network association is failed.");
        delay(1000);
    }
    debug.println("Network association is successful.");
}

bool Network::associate(void)
{
#if WIFLY
    const char ssid[] = "CNN401-2";
    const char passphrase[] = "0025644172";
    WiFly.begin();
    //WiFly.configure(WIFLY_BAUD, 38400);
    return WiFly.join(ssid, passphrase);
#else // Ethernet
    byte mac[] = { 0x08, 0x01, 0x27, 0x73, 0x2E, 0x31 };
    return Ethernet.begin(mac);
#endif
}

