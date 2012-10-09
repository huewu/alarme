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

#ifndef _SERIAL_DEBUG_H_
#define _SERIAL_DEBUG_H_

#include <Arduino.h>

class SerialDebug
{
    private:
        bool enable;

    public:
        SerialDebug()
        {
            enable = false;
        }

        void init(void)
        {
            const int baud_rate = 9600;
            Serial.begin(baud_rate);
        }

        void on(void)  { enable = true;  }
        void off(void) { enable = false; }

        size_t print(const String& s)         { if (enable) Serial.print(s); }
        size_t print(const char* s)           { if (enable) Serial.print(s); }
        size_t print(const unsigned char s)   { if (enable) Serial.print(s); }
        size_t print(const char s)            { if (enable) Serial.print(s); }
        size_t print(const unsigned int s)    { if (enable) Serial.print(s); }
        size_t print(const int s)             { if (enable) Serial.print(s); }
        size_t print(const unsigned long s)   { if (enable) Serial.print(s); }
        size_t print(const long s)            { if (enable) Serial.print(s); }
        size_t print(const double s)          { if (enable) Serial.print(s); }

        size_t println(const String& s)       { if (enable) Serial.println(s); }
        size_t println(const char* s)         { if (enable) Serial.println(s); }
        size_t println(const unsigned char s) { if (enable) Serial.println(s); }
        size_t println(const char s)          { if (enable) Serial.println(s); }
        size_t println(const unsigned int s)  { if (enable) Serial.println(s); }
        size_t println(const int s)           { if (enable) Serial.println(s); }
        size_t println(const unsigned long s) { if (enable) Serial.println(s); }
        size_t println(const long s)          { if (enable) Serial.println(s); }
        size_t println(const double s)        { if (enable) Serial.println(s); }
        size_t println(void)                  { if (enable) Serial.println();  }
}; 

#endif //_SERIAL_DEBUG_H_

