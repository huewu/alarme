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
#include <PusherClient.h>
#if WIFLY
    #include <Credentials.h>
#endif

// function local declarations
void setup_serial();
void setup_lcd();
#if WIFLY
void setup_wifly();
#else
void setup_ethernet();
#endif
void setup_pusherclient();
void setup_googleclient();
void setup_ntpclient();
void do_lcd();
void do_pusherclient();
void do_googleclient();
void do_ntpclient();
void set_alarm(String data);
void dismiss_alarm(String data);
unsigned long sendNTPpacket(IPAddress& address);

// initialize the library with the numbers of the interface pins
LiquidCrystal lcd(12, 11, 5, 4, 3, 2);

// initialize PusherClient using WebSocketClient library
PusherClient pusher;
#if WIFLY
//WiFlyClient google;
#else
EthernetClient google;
#endif
EthernetUDP ntp;
IPAddress timeServer(211,233,84,186); // kr.pool.ntp.org NTP server
const int NTP_PACKET_SIZE= 48; // NTP time stamp is in the first 48 bytes of the message
byte packetBuffer[ NTP_PACKET_SIZE];

void setup() {
    setup_serial();
    //setup_lcd();
#if WIFLY
    setup_wifly();
#else
    setup_ethernet();
#endif
    setup_pusherclient();
    setup_googleclient();
    setup_ntpclient();
}

void setup_serial() {
    Serial.begin(9600);
    Serial.println("Alarme Clock v0.1");
}

/*
void setup_lcd() {
    // set up the LCD's number of columns and rows: 
    lcd.begin(16, 2);
    // Print a message to the LCD.
    lcd.print("hello, world!");
}
*/

#if WIFLY
void setup_wifly() {
    // Serial.begin(9600);
    WiFly.begin();

    if (!WiFly.join(ssid, passphrase)) {
        Serial.println("WiFly association failed.");
        while (1) {
            // Hang on failure
        }
    }
    Serial.println("WiFly association successful.");
    Serial.print("WiFly IP :");
    Serial.println(WiFly.ip());

    //WiFly.configure(WIFLY_BAUD, 38400);
}
#else
void setup_ethernet() {
    byte mac[] = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

    if (!Ethernet.begin(mac)) {
        Serial.println("Ethernet connection failed.");
        while (1) {}
    }
}
#endif

void setup_pusherclient() {
    if (pusher.connect("0bd9a76b9e35d7efe818")) {
        Serial.println("pusher connection successful.");
        pusher.bind("set_alarm", set_alarm);
        pusher.subscribe("cid");
    }
    else {
        Serial.println("pusher connection failed.");
        while(1) {}
    }
}

void setup_googleclient() {
    if (google.connect("google.com", 80)) {
        Serial.println("google pusher connected");
        google.println("GET / HTTP/1.0");
        google.println();
    }
    else {
        Serial.println("google connection failed.");
        while(1) {}
    }
}

void setup_ntpclient() {
    ntp.begin(8888);
}

void loop() {
    //do_lcd();
    do_pusherclient();
    do_googleclient();
    do_ntpclient();
}

void do_lcd() {
    // set the cursor to column 0, line 1
    // (note: line 1 is the second row, since counting begins with 0):
    lcd.setCursor(0, 1);
    // print the number of seconds since reset:
    lcd.print(millis()/1000);
}

void do_pusherclient() {
    //static int i = 0;
    if (pusher.connected()) {
        //Serial.print("... monitoring ");
        //Serial.println(i++);
        pusher.monitor();
    }
}

void do_googleclient() {
    if (google.available()) {
        char c;
        do {
            c = google.read();
            Serial.print(c);
        } while (c != -1);
    }
}

void do_ntpclient() {
    sendNTPpacket(timeServer); // send an NTP packet to a time server

    // wait to see if a reply is available
    delay(1000);  
    if ( ntp.parsePacket() ) {  
        // We've received a packet, read the data from it
        ntp.read(packetBuffer,NTP_PACKET_SIZE);  // read the packet into the buffer

        //the timestamp starts at byte 40 of the received packet and is four bytes,
        // or two words, long. First, esxtract the two words:

        unsigned long highWord = word(packetBuffer[40], packetBuffer[41]);
        unsigned long lowWord = word(packetBuffer[42], packetBuffer[43]);  
        // combine the four bytes (two words) into a long integer
        // this is NTP time (seconds since Jan 1 1900):
        unsigned long secsSince1900 = highWord << 16 | lowWord;  
        Serial.print("Seconds since Jan 1 1900 = " );
        Serial.println(secsSince1900);               

        // now convert NTP time into everyday time:
        Serial.print("Unix time = ");
        // Unix time starts on Jan 1 1970. In seconds, that's 2208988800:
        const unsigned long seventyYears = 2208988800UL;     
        // subtract seventy years:
        unsigned long epoch = secsSince1900 - seventyYears;  
        // print Unix time:
        Serial.println(epoch);                               

        epoch += 3600*9;

        // print the hour, minute and second:
        Serial.print("The KST time is ");       // UTC is the time at Greenwich Meridian (GMT)
        Serial.print((epoch  % 86400L) / 3600); // print the hour (86400 equals secs per day)
        Serial.print(':');  
        if ( ((epoch % 3600) / 60) < 10 ) {
            // In the first 10 minutes of each hour, we'll want a leading '0'
            Serial.print('0');
        }
        Serial.print((epoch  % 3600) / 60); // print the minute (3600 equals secs per minute)
        Serial.print(':'); 
        if ( (epoch % 60) < 10 ) {
            // In the first 10 seconds of each minute, we'll want a leading '0'
            Serial.print('0');
        }
        Serial.println(epoch %60); // print the second
    }
}

void set_alarm(String data) {
    Serial.println(data);
}

void dismiss_alarm(String data) {
}


// send an NTP request to the time server at the given address 
unsigned long sendNTPpacket(IPAddress& address)
{
    // set all bytes in the buffer to 0
    memset(packetBuffer, 0, NTP_PACKET_SIZE); 
    // Initialize values needed to form NTP request
    // (see URL above for details on the packets)
    packetBuffer[0] = 0b11100011;   // LI, Version, Mode
    packetBuffer[1] = 0;     // Stratum, or type of clock
    packetBuffer[2] = 6;     // Polling Interval
    packetBuffer[3] = 0xEC;  // Peer Clock Precision
    // 8 bytes of zero for Root Delay & Root Dispersion
    packetBuffer[12]  = 49; 
    packetBuffer[13]  = 0x4E;
    packetBuffer[14]  = 49;
    packetBuffer[15]  = 52;

    // all NTP fields have been given values, now
    // you can send a packet requesting a timestamp:           
    ntp.beginPacket(address, 123); //NTP requests are to port 123
    ntp.write(packetBuffer,NTP_PACKET_SIZE);
    ntp.endPacket(); 
}
