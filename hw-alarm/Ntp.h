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

#ifndef _NTP_H_
#define _NTP_H_

#include <Arduino.h>
#include <Ethernet.h>

class Ntp
{
    private:
        //EthernetUDP         udp;

        //static const int    NTP_PACKET_SIZE = 48;   // NTP time stamp is in the first 48 bytes of the message
        //byte                packetBuffer[NTP_PACKET_SIZE];

    public:
        Ntp() {}

        void init(void);
        static unsigned long sync(void);
        static void query_time(const IPAddress& server);
        static void parse_time(unsigned long epoch);
};

#endif //_NTP_H_

