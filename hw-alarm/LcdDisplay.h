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

#ifndef _LCD_DISPLAY_H_
#define _LCD_DISPLAY_H_

#include <Arduino.h>
#include <Adafruit_RGBLCDShield.h>

class LcdDisplay
{
    private:
        Adafruit_RGBLCDShield   lcd;

    public:
        LcdDisplay() {}

        void init(void);
        void check_button(void);
        void test(void);

        size_t print(const String& s)         { lcd.print(s); }
        size_t print(const char* s)           { lcd.print(s); }
        size_t print(const unsigned char s)   { lcd.print(s); }
        size_t print(const char s)            { lcd.print(s); }
        size_t print(const unsigned int s)    { lcd.print(s); }
        size_t print(const int s)             { lcd.print(s); }
        size_t print(const unsigned long s)   { lcd.print(s); }
        size_t print(const long s)            { lcd.print(s); }
        size_t print(const double s)          { lcd.print(s); }

        void select_line(const byte l)        { lcd.setCursor(0, l); }
};

#endif //_LCD_DISPLAY_H_

