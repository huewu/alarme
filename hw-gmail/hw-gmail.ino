/*
 *  Alarme Project for Google HackFair 2012 in Seoul
 *  Arduino hardware gmail notifier and printer sketch
 *      
 *  Kwanlae Kim <voidopennet@gmail.com>
 *  Chanseok Yang <huewu.yang@gmail.com>
 *  Jinserk Baik <jinserk.baik@gmail.com>
 *  Wonseok Yang <before30@gmail.com>
 *
 *  Copyright (c) 2012, all rights reserved.
 */

#include <Ethernet.h>
#include <PusherClient.h>

// function local declarations
void setup_led();
void setup_pusherclient();
void do_led();
void do_pusherclient();
void get_notification(String data);

// initialize PusherClient using WebSocketClient library
byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };
PusherClient client;

void setup() {
    setup_led();
    setup_pusherclient();
}

// Pin 13 has an LED connected on most Arduino boards.
// give it a name:
int led = 13;

// the setup routine runs once when you press reset:
void setup_led() {                
    // initialize the digital pin as an output.
    pinMode(led, OUTPUT);     
}

void setup_pusherclient() {
    //Serial.begin(9600);
    if (Ethernet.begin(mac) == 0) {
        //Serial.println("Init Ethernet failed");
        for(;;)
            ;
    }

    if(client.connect("your-api-key-here")) {
        client.bind("get_notification", get_notification);
        client.subscribe("alarme");
    }
    else {
        while(1) {}
    }
}

// the loop routine runs over and over again forever:
void loop() {
    do_led();
    do_pusherclient();
}

void do_led() {
    digitalWrite(led, HIGH);   // turn the LED on (HIGH is the voltage level)
    delay(1000);               // wait for a second
    digitalWrite(led, LOW);    // turn the LED off by making the voltage LOW
    delay(1000);               // wait for a second
}

void do_pusherclient() {
    if (client.connected()) {
        client.monitor();
    }
}

void get_notification(String data) {
}

