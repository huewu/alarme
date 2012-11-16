/*
 *  Alarme Project for hc HackFair 2012 in Seoul
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
#include "Heroku.h"
#include "Clock.h"

extern SerialDebug  debug;
extern ArrayList*   arrayList;
extern Clock        clk;

const int kNetworkTimeout = 30*1000;
const int kNetworkDelay = 1000;
char host[] = "192.168.77.8";
int port = 9090;
//char host[] = "ghfalarme.herokuapp.com";
//int port = 80;

void Heroku::init(void)
{ 
    clk.display();
    /*
    while (!hc.connect("google.com", 80)) {
        debug.println("Heroku connection is failed.");
        delay(1000);
    }
    hc.println("GET / HTTP/1.0");
    hc.println();
    delay(1000);
    */
}

void Heroku::get_response(void)
{
    /*
    if (!hc.available()) return;

    char c = hc.read();
    while (c != -1) {
        debug.print(c);
        c = hc.read();
    }
    */
}

boolean 
Heroku::setAlarmOff(String aid, String cid){
    String path = "/alarm/";
    path = path + aid +"?cid=" + cid + "&status=OFF";

    debug.println(path);

    char *pathChar = (char *)malloc(path.length() + 1);
    path.toCharArray(pathChar, path.length() + 1);


    String resultBody = getHttpBody(host, port, pathChar);
    Serial.println(resultBody);
    char *jsonStr = (char *)malloc(resultBody.length() + 1);

    resultBody.toCharArray(jsonStr, resultBody.length() + 1);

    debug.println(resultBody);

    aJsonObject* root = aJson.parse(jsonStr);
    if (root != NULL) {
        aJsonObject* msg = aJson.getObjectItem(root, "msg");
        if (msg != NULL) {
            Serial.println(msg->valuestring);
        }
    }

    free(jsonStr);
    free(pathChar);

    return true;
}

String
Heroku::getAlarmList(String cid){
    String path = "/alarmlist/";
    path = path + "?cid=" + cid;

    char *pathChar = (char *)malloc(path.length() + 1);
    path.toCharArray(pathChar, path.length() + 1);

    String resultBody = getHttpBody(host, port, pathChar);

    free(pathChar);
    return resultBody;
}

String 
Heroku::getHttpBody(const char* host, int port, const char* path){
    String body = "";
    int err = 0;
    EthernetClient c;
    HttpClient http(c);

    err = http.get(host, port, path);
    if (err == 0) {
        err = http.responseStatusCode();
        if (err >= 0) {
            err = http.skipResponseHeaders();
            if (err >= 0) {
                int bodyLen = http.contentLength();
                Serial.println(bodyLen);
                // Now we've got to the body, so we can print it out
                unsigned long timeoutStart = millis();
                char c;
                // Whilst we haven't timed out & haven't reached the end of the body
                while ( (http.connected() || http.available()) &&
                        ((millis() - timeoutStart) < kNetworkTimeout) ) {
                    if (http.available()) {
                        c = http.read();
                        // Print out this character
                        Serial.print(c);
                        body = body + c;
                        bodyLen--;
                        // We read something, reset the timeout counter
                        timeoutStart = millis();
                    }
                    else {
                        // We haven't got any data, so let's pause to allow some to
                        // arrive
                        delay(kNetworkDelay);
                    }
                }
            }
        }
    }

    http.stop();

    return body;
}

Item*
Heroku::parseAlarm(aJsonObject* object)
{
    aJsonObject* aid = aJson.getObjectItem(object, "aid");
    aJsonObject* time = aJson.getObjectItem(object, "time");
    aJsonObject* type = aJson.getObjectItem(object, "type");
    Item * item = new Item();
    item->setAid(aid->valuestring);
    item->setTime(time->valuestring);
    item->setType(type->valuestring);
    return item;

}

void
Heroku::parseAlarmList(String arg)
{
    char *jsonStr = (char *)malloc(arg.length() + 1);
    arg.toCharArray(jsonStr, arg.length() + 1);

    aJsonObject* root = aJson.parse(jsonStr);
    if (root != NULL) {
        aJsonObject* retArray = aJson.getObjectItem(root, "ret");
        if (retArray != NULL) {
            unsigned char result = aJson.getArraySize(retArray);
            Serial.println(result);

            for (int i=0;i<result;i++) {
                aJsonObject* ret = aJson.getArrayItem(retArray, (char)i);
                result++;
                if (ret != NULL) {
                    /*
                    aJsonObject* aid = aJson.getObjectItem(ret, "aid");
                    aJsonObject* time = aJson.getObjectItem(ret, "time");
                    aJsonObject* type = aJson.getObjectItem(ret, "type");
                    Item * item = new Item();
                    item->setAid(aid->valuestring);
                    item->setTime(time->valueint);
                    item->setType(type->valuestring);
                    arrayList->addItem(item);
                    */
                    Item* item = parseAlarm(ret);
                    arrayList->addItem(item);
                }
                else {
                    break;
                }
            }
        }

    }
    free(jsonStr);
}

