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

#include <Arduino.h>
#include "LcdDisplay.h"
#include "Clock.h"

// These #defines make it easy to set the backlight color
#define RED     0x1
#define GREEN   0x2
#define YELLOW  0x3
#define BLUE    0x4
#define VIOLET  0x5
#define TEAL    0x6
#define WHITE   0x7

extern Clock clk;

void LcdDisplay::init(void)
{
    // set up the LCD's number of columns and rows: 
    lcd.begin(16, 2);
    lcd.setBacklight(WHITE);

    select_line(0);
    print("  Alarme  v0.1  ");
}

void LcdDisplay::check_button(void)
{
    lcd.print(millis()/1000);
    uint8_t buttons = lcd.readButtons();

    if (buttons) {
        if (buttons) {
            clk.report_stop_alarm();    
        }
    }
}

void LcdDisplay::test(void)
{
    // set the cursor to column 0, line 1
    // (note: line 1 is the second row, since counting begins with 0):
    //lcd.setCursor(0, 1);
    // print the number of seconds since reset:
    lcd.print(millis()/1000);

    uint8_t buttons = lcd.readButtons();

    if (buttons) {
        select_line(1);
        if (buttons & BUTTON_UP) {
            lcd.print("UP ");
            //lcd.setBacklight(RED);
        }
        if (buttons & BUTTON_DOWN) {
            lcd.print("DOWN ");
            //lcd.setBacklight(YELLOW);
        }
        if (buttons & BUTTON_LEFT) {
            lcd.print("LEFT ");
            //lcd.setBacklight(GREEN);
        }
        if (buttons & BUTTON_RIGHT) {
            lcd.print("RIGHT ");
            //lcd.setBacklight(TEAL);
        }
        if (buttons & BUTTON_SELECT) {
            lcd.print("SELECT ");
            //lcd.setBacklight(VIOLET);
        }
        delay(200);
    }
}

