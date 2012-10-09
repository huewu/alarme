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

#ifndef _NETWORK_H_
#define _NETWORK_H_

#include <WebSocketClient.h>

#if WIFLY
    #include <WiFly.h>
#else // Ethernet
    #include <Ethernet.h>
#endif

class Network
{
    public:
        Network() {}

        void init(void);
        bool associate(void);
};

#endif //_NETWORK_H_

