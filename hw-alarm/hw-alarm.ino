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

// include the library code:
#include <LiquidCrystal.h>
#include <Ethernet.h>
#include <PusherClient.h>

// function local declarations
void setup_lcd();
void setup_pusherclient();
void do_lcd();
void do_pusherclient();
void set_alarm(String data);
void dismiss_alarm(String data);

// initialize the library with the numbers of the interface pins
LiquidCrystal lcd(12, 11, 5, 4, 3, 2);

// initialize PusherClient using WebSocketClient library
byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };
PusherClient client;

void setup() {
    setup_lcd();
    setup_pusherclient();
}

void setup_lcd() {
    // set up the LCD's number of columns and rows: 
    lcd.begin(16, 2);
    // Print a message to the LCD.
    lcd.print("hello, world!");
}

void setup_pusherclient() {
    //Serial.begin(9600);
    if (Ethernet.begin(mac) == 0) {
        //Serial.println("Init Ethernet failed");
        for(;;)
            ;
    }

    if(client.connect("your-api-key-here")) {
        client.bind("set_alarm", set_alarm);
        client.bind("dismiss_alarm", dismiss_alarm);
        client.subscribe("alarme");
    }
    else {
        while(1) {}
    }
}

void loop() {
    do_lcd();
    do_pusherclient();
}

void do_lcd() {
    // set the cursor to column 0, line 1
    // (note: line 1 is the second row, since counting begins with 0):
    lcd.setCursor(0, 1);
    // print the number of seconds since reset:
    lcd.print(millis()/1000);
}

void do_pusherclient() {
    if (client.connected()) {
        client.monitor();
    }
}

void set_alarm(String data) {
}

void dismiss_alarm(String data) {
}

