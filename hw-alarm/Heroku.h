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

#ifndef _HEROKU_H_
#define _HEROKU_H_

#include "NetClient.h"

class Heroku
{
    private:
        NetClient   hc;

    public:
        Heroku() {}

        void init(void);
        void get_response(void);
};

#endif //_HEROKU_H_

