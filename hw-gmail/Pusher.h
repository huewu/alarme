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

#ifndef _PUSHER_H_
#define _PUSHER_H_

#include <PusherClient.h>

class Pusher
{
    private:
        PusherClient    pc;

    public:
        Pusher() {}
        
        void init(void);
        void bind_event(void);
        void monitor(void);

        static void printme(String data);
};

#endif //_PUSHER_H_

