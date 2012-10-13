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
#include "Network.h"
#include "Ntp.h"
#include "Clock.h"
#include "NFC.h"
#include "Pusher.h"
#include "Heroku.h"

SerialDebug     debug;
LcdDisplay      lcd;
Network         net;

Ntp             ntp;
Clock           clk;
Nfc             nfc;

Pusher          pusher;
Heroku          heroku;


/*
ISR(TIMER1_COMPA_vect)
{
    clk.update();
}

void init_timer1()
{
    cli();          // disable global interrupts
    TCCR1A = 0;     // set entire TCCR1A register to 0
    TCCR1B = 0;     // same for TCCR1B
    TCNT1 = 0;

    // set compare match register to desired timer count:
    OCR1A = 15624;  // 16MHz/1024/1Hz-1
    // turn on CTC mode:
    TCCR1B |= (1 << WGM12);
    // Set CS10 and CS12 bits for 1024 prescaler:
    TCCR1B |= (1 << CS10);
    TCCR1B |= (1 << CS12);
    // enable timer compare interrupt:
    TIMSK1 |= (1 << OCIE1A);
    // enable global interrupts:
    sei();
}
*/

void setup(void)
{
    debug.init();
    debug.on();
    debug.println("Alarme v0.1 - Social Alarm Project");
    debug.println("Google HackFair 2012 in Seoul");

    lcd.init();
    net.init();

    ntp.init();
    clk.init(ntp);
    nfc.init();

    pusher.init();
    heroku.init();
}

void loop(void)
{
    clk.update();
    nfc.test();
    pusher.monitor();
    heroku.get_response();

    lcd.test();
    lcd.select_line(1);
    lcd.print("                "); // clear second line of LCD
}

