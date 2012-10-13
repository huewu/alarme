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
#include "arraylist.h"
#include "Clock.h"

extern SerialDebug  debug;
extern LcdDisplay   lcd;
extern ArrayList*   arrayList;

void Clock::init(Ntp& ntp)
{
    setSyncProvider(ntp.sync);
    while (timeStatus() == timeNotSet); // wait until time sync from NTP
    lcd.select_line(1);
    lcd.print("Time synced.....");
}

void Clock::update(void)
{
    display_clock();

    bool flag = false;
    for (int i = 0; i < arrayList->getSize(); ++i) {
        Item* p = arrayList->getItem(i);
        if (p->isAlive() && (now() < p->getTime()))
            flag = true;    
    }

    if (flag) ring_alarm();
}

void Clock::display_clock(void)
{
    lcd.select_line(0);
    lcd.print(" ");
    lcd.print(month());
    lcd.print("/");
    lcd.print(day());
    lcd.print(" ");
    print_digits(hour());
    lcd.print(":");
    print_digits(minute());
    lcd.print(":");
    print_digits(second());
    lcd.print(" ");
}

void Clock::print_digits(int digits)
{
    if (digits < 10)
        lcd.print('0');
    lcd.print(digits);
}

void Clock::ring_alarm(void)
{
    lcd.select_line(1);
    lcd.print("Ring Alarm !!!!!");
}

void Clock::stop_alarm(void)
{
    lcd.select_line(1);
    lcd.print("Stop Alarm .....");

    for (int i = 0; i < arrayList->getSize(); ++i) {
        Item* p = arrayList->getItem(i);
        if (p->isAlive() && (now() < p->getTime()))
            p->setAlive(false);    
    }
}

