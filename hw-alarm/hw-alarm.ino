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
#include "pitches.h"

SerialDebug     debug;
LcdDisplay      lcd;
Network         net;

Clock           clk;
Ntp             ntp;
Nfc             nfc;

Pusher          pusher;
Heroku          heroku;

ArrayList* arrayList = new ArrayList();
char* cid = "clock1";
bool alarm = false;

// notes in the melody:
int melody[] = {
      NOTE_C4, NOTE_G3,NOTE_G3, NOTE_A3, NOTE_G3,0, NOTE_B3, NOTE_C4};

// note durations: 4 = quarter note, 8 = eighth note, etc.:
int noteDurations[] = {
      4, 8, 8, 4,4,4,4,4 };

void doAlarm(){
  for (int thisNote = 0; thisNote < 8; thisNote++) {

          // to calculate the note duration, take one second 
          // divided by the note type.
          //e.g. quarter note = 1000 / 4, eighth note = 1000/8, etc.
          int noteDuration = 1000/noteDurations[thisNote];
          tone(49, melody[thisNote],noteDuration);

          // to distinguish the notes, set a minimum time between them.
          // the note's duration + 30% seems to work well:
          int pauseBetweenNotes = noteDuration * 1.30;
          delay(pauseBetweenNotes);
          // stop the tone playing:
          noTone(49);
  }
}

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
    String alarms = heroku.getAlarmList(cid);
    heroku.parseAlarmList(alarms);
}

void loop(void)
{
    clk.update();
    nfc.test();
    pusher.monitor();
 //  heroku.get_response();
 //   heroku.setAlarmOff("KL_GROUP_1349530819326", cid);
    if (alarm){
        doAlarm();
    }
    Serial.println("...");
    for (int i=0; i<arrayList->getSize(); i++){
       Item * item = arrayList->getItem(i);
       debug.println(item->getAid());
       debug.println(item->getTime());
       debug.println(item->isAlive());
    }
    
    lcd.check_button();
    lcd.select_line(1);
    lcd.print("                "); // clear second line of LCD
}

