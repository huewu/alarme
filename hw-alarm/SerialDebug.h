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

#define SERIAL_BAUD     9600

class SerialDebug
{
    public:
        void setup()
        {
            Serial.begin(SERIAL_BAUD);
        }

        size_t print(const String& s)         { Serial.print(s); }
        size_t print(const char* s)           { Serial.print(s); }
        size_t print(const unsigned char s)   { Serial.print(s); }
        size_t print(const char s)            { Serial.print(s); }
        size_t print(const unsigned int s)    { Serial.print(s); }
        size_t print(const int s)             { Serial.print(s); }
        size_t print(const unsigned long s)   { Serial.print(s); }
        size_t print(const long s)            { Serial.print(s); }
        size_t print(const double s)          { Serial.print(s); }

        size_t println(const String& s)       { Serial.println(s); }
        size_t println(const char* s)         { Serial.println(s); }
        size_t println(const unsigned char s) { Serial.println(s); }
        size_t println(const char s)          { Serial.println(s); }
        size_t println(const unsigned int s)  { Serial.println(s); }
        size_t println(const int s)           { Serial.println(s); }
        size_t println(const unsigned long s) { Serial.println(s); }
        size_t println(const long s)          { Serial.println(s); }
        size_t println(const double s)        { Serial.println(s); }
        size_t println(void)                  { Serial.println();  }
}; 

#endif //_SERIAL_DEBUG_H_

