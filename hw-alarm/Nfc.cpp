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
#include "Nfc.h"

#define IRQ   (18)
#define RESET (3)  // Not connected by default on the NFC Shield

extern SerialDebug  debug;
extern LcdDisplay   lcd;

Nfc::Nfc()
 : dev(IRQ, RESET)
{
}

void Nfc::init(void)
{
    dev.begin();

    uint32_t versiondata = dev.getFirmwareVersion();
    if (!versiondata) {
        debug.print("Didn't find PN53x board");
        lcd.select_line(1);
        lcd.print("NFC init failed ");
        while (1); // halt
    }

    // Got ok data, print it out!
    debug.print("Found chip PN5");
    debug.println((versiondata>>24) & 0xFF, HEX); 
    debug.print("Firmware ver. ");
    debug.print((versiondata>>16) & 0xFF, DEC); 
    debug.print('.');
    debug.println((versiondata>>8) & 0xFF, DEC);

    // Set the max number of retry attempts to read from a card
    // This prevents us from waiting forever for a card, which is
    // the default behaviour of the PN532.
    dev.setPassiveActivationRetries(0x00);
    // configure board to read RFID tags
    dev.SAMConfig();

    debug.println("Waiting for an ISO14443A card");
}

void Nfc::test(void)
{
    boolean success;
    uint8_t uid[] = { 0, 0, 0, 0, 0, 0, 0 };  // Buffer to store the returned UID
    uint8_t uidLength;                        // Length of the UID (4 or 7 bytes depending on ISO14443A card type)

    // Wait for an ISO14443A type cards (Mifare, etc.).  When one is found
    // 'uid' will be populated with the UID, and uidLength will indicate
    // if the uid is 4 bytes (Mifare Classic) or 7 bytes (Mifare Ultralight)
    success = dev.readPassiveTargetID(PN532_MIFARE_ISO14443A, &uid[0], &uidLength);

    if (success) {
        debug.println("Found a card!");
        /*
        debug.print("UID Length: ");
        debug.print(uidLength, DEC);
        debug.println(" bytes");
        debug.print("UID Value: ");
        for (uint8_t i=0; i < uidLength; i++) {
            debug.print(" 0x");
            debug.print(uid[i], HEX); 
        }
        debug.println("");
        */

        lcd.select_line(1);
        lcd.print("NFC card found..");

        // Wait 1 second before continuing
        delay(200);
    }
    else
    {
        // PN532 probably timed out waiting for a card
        debug.println("Timed out waiting for a card");
    }
}
