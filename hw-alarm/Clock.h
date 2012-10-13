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

#ifndef _CLOCK_H_
#define _CLOCK_H_

#include <Time.h>
#include "Ntp.h"

class Clock
{
    public:
        Clock() {}

        void init(Ntp& ntp);
        void update(void);

        void display_clock(void);
        void print_digits(int digits);

        void ring_alarm(void);
        void stop_alarm(void);
};

#endif //_CLOCK_H_

