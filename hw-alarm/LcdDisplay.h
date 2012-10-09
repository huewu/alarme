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

#include <LiquidCrystal.h>

class LcdDisplay
{
    private:
        LiquidCrystal   lcd_;

    public:
        LcdDisplay() : lcd_(12, 11, 5, 4, 3, 2) {}

        void setup()
        {
            // set up the LCD's number of columns and rows: 
            lcd_.begin(16, 2);
            // Print a message to the LCD.
            lcd_.print("hello, world!");
        }

        void loop() {
            // set the cursor to column 0, line 1
            // (note: line 1 is the second row, since counting begins with 0):
            lcd_.setCursor(0, 1);
            // print the number of seconds since reset:
            lcd_.print(millis()/1000);
        }
};

#endif //_LCD_DISPLAY_H_

