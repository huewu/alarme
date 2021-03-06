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
#include "Heroku.h"

extern SerialDebug  debug;
extern LcdDisplay   lcd;
extern ArrayList*   arrayList;
extern bool alarm;
extern Heroku heroku;
extern char* cid;

static const uint16_t timeZoneOffset = 3600L*9;

void Clock::init(Ntp& ntp)
{
    setSyncProvider(ntp.sync);
    while (timeStatus() == timeNotSet); // wait until time sync from NTP
    lcd.select_line(1);
    lcd.print("Time synced.....");
    display();
}

void Clock::update(void)
{
    display();

    bool flag = false;
    for (int i = 0; i < arrayList->getSize(); ++i) {
        Item* p = arrayList->getItem(i);
        if (p->isAlive() && (now() >= (p->getTime()+timeZoneOffset)))
            flag = true;    
    }

    if (flag) ring_alarm();

    debug.print("current time:");
    debug.println(now());
}

void Clock::display(void)
{
    lcd.select_line(0);
    lcd.print(' ');
    print_digits(month());
    lcd.print('/');
    print_digits(day());
    lcd.print(' ');
    print_digits(hour());
    lcd.print(':');
    print_digits(minute());
    lcd.print(':');
    print_digits(second());
    lcd.print(' ');
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
    lcd.print("Ring Alarm .....");
    alarm = true;
}

void Clock::report_stop_alarm(void)
{
    for (int i = 0; i < arrayList->getSize(); ++i) {
        Item* p = arrayList->getItem(i);
        if (p->isAlive() && (now() >= (p->getTime()+timeZoneOffset))){
            //p->setAlive(false);   
            heroku.setAlarmOff(p->getAid(), cid);
        }
    }
    //alarm = false;
}

void Clock::do_stop_alarm(void)
{
    bool allStatus = true;
    for (int i = 0; i < arrayList->getSize(); ++i) {
        Item* p = arrayList->getItem(i);
        if ((now() >= (p->getTime()+timeZoneOffset)) && p->isAlive()){
            allStatus = false;
        }
    }

    if (allStatus) {
        if (alarm) {
            lcd.select_line(1);
            lcd.print("Stop Alarm .....");
        }

        alarm = false;
    }
}

