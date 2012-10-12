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
#include "Ntp.h"

extern SerialDebug  debug;
extern LcdDisplay   lcd;

EthernetUDP         udp;
const IPAddress     NTP_SERVER(211,233,84,186);     // kr.pool.ntp.org NTP server
const unsigned int  PORT = 8888; 

const int           NTP_PACKET_SIZE = 48;   // NTP time stamp is in the first 48 bytes of the message
byte                packetBuffer[NTP_PACKET_SIZE];

void Ntp::init(void)
{
    while (!udp.begin(PORT)) {
        debug.println("UDP connection is failed.");
        delay(1000);
    }
    //lcd.select_line(1);
    //lcd.print("NTP synced......");
}

unsigned long Ntp::sync(void)
{
    query_time(NTP_SERVER);
    delay(1000);  
    
    if (udp.parsePacket()) {  
        udp.read(packetBuffer, NTP_PACKET_SIZE);

        // the timestamp starts at byte 40 of the received packet and is four bytes,
        // or two words, long. First, extract the two words:
        unsigned long highWord = word(packetBuffer[40], packetBuffer[41]);
        unsigned long lowWord = word(packetBuffer[42], packetBuffer[43]);  
        // combine the four bytes (two words) into a long integer
        // this is NTP time (seconds since Jan 1 1900):
        unsigned long secsSince1900 = highWord << 16 | lowWord;  
        debug.print("Seconds since Jan 1 1900 = " );
        debug.println(secsSince1900);               

        // now convert NTP time into everyday time:
        // Unix time starts on Jan 1 1970. In seconds, that's 2208988800:
        const unsigned long seventyYears = 2208988800UL;     
        // subtract seventy years:
        unsigned long epoch = secsSince1900 - seventyYears;  
        // print Unix time:
        debug.print("Unix time = ");
        debug.println(epoch);                               

        // convert UTC into KST
        epoch += 3600*9;

        parse_time(epoch);

        return epoch;
    }
    else
        return 0;
}

void Ntp::query_time(const IPAddress& server)
{
    // set all bytes in the buffer to 0
    memset(packetBuffer, 0, NTP_PACKET_SIZE); 
    // Initialize values needed to form NTP request
    // (see URL above for details on the packets)
    packetBuffer[0] = 0b11100011;   // LI, Version, Mode
    packetBuffer[1] = 0;            // Stratum, or type of clock
    packetBuffer[2] = 6;            // Polling Interval
    packetBuffer[3] = 0xEC;         // Peer Clock Precision
    // 8 bytes of zero for Root Delay & Root Dispersion
    packetBuffer[12] = 49; 
    packetBuffer[13] = 0x4E;
    packetBuffer[14] = 49;
    packetBuffer[15] = 52;

    // all NTP fields have been given values, now
    // you can send a packet requesting a timestamp:           
    udp.beginPacket(server, 123); //NTP requests are to port 123
    udp.write(packetBuffer, NTP_PACKET_SIZE);
    udp.endPacket(); 
}

void Ntp::parse_time(unsigned long epoch)
{
    // print the hour, minute and second:
    debug.print("The KST time is ");       // UTC is the time at Greenwich Meridian (GMT)
    debug.print((epoch  % 86400L) / 3600); // print the hour (86400 equals secs per day)
    debug.print(':');  
    if ( ((epoch % 3600) / 60) < 10 ) {
        // In the first 10 minutes of each hour, we'll want a leading '0'
        debug.print('0');
    }
    debug.print((epoch  % 3600) / 60); // print the minute (3600 equals secs per minute)
    debug.print(':'); 
    if ( (epoch % 60) < 10 ) {
        // In the first 10 seconds of each minute, we'll want a leading '0'
        debug.print('0');
    }
    debug.println(epoch %60); // print the second
}

