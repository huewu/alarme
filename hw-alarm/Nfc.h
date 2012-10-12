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

#ifndef _NFC_H_
#define _NFC_H_

#include <Adafruit_NFCShield_I2C.h>

class Nfc
{
    private:
        Adafruit_NFCShield_I2C  dev;

    public:
        Nfc();

        void init(void);
        void test(void);
};

#endif //_NFC_H_

