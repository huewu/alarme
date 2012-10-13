#include <SPI.h>
#include <HttpClient.h>
#include <Ethernet.h>
#include <EthernetClient.h>
#include <aJSON.h>
#include "arraylist.h"


byte mac[] = {  0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0x11 };
const int kNetworkTimeout = 30*1000;
const int kNetworkDelay = 1000;
ArrayList * arrayList = new ArrayList();
char host[] = "192.168.77.8";
int port = 9090;

boolean setAlarmOff(String aid, String cid){
  String path = "/alarm/";
  path = path + aid +"?cid=" + cid + "&status=OFF";
  
  char *pathChar = (char *)malloc(path.length() + 1);
  path.toCharArray(pathChar, path.length() + 1);
  

  String resultBody = getHttpBody(host, 9090, pathChar);
  //Serial.println(resultBody);
  char *jsonStr = (char *)malloc(resultBody.length() + 1);
  
  resultBody.toCharArray(jsonStr, resultBody.length() + 1);
  
  aJsonObject* root = aJson.parse(jsonStr);
  if (root != NULL){
    aJsonObject* msg = aJson.getObjectItem(root, "msg");
    if (msg != NULL){
      Serial.println(msg->valuestring);
    }
  }
  
  free(jsonStr);
  free(pathChar);
  
  return true;  
}

String getAlarmList(String cid){
  String path = "/alarmlist/";
  path = path + "?cid=" + cid;

  char *pathChar = (char *)malloc(path.length() + 1);
  path.toCharArray(pathChar, path.length() + 1);
  
  String resultBody = getHttpBody(host, port, pathChar);
  
  free(pathChar);
  return resultBody;
}

String getHttpBody(const char* host, int port, const char* path){
  String body = "";
  int err = 0;
  EthernetClient c;
  HttpClient http(c);
  
  err = http.get(host, port, path);
  if (err == 0)
  {
    err = http.responseStatusCode();
    if (err >= 0)
    {
      err = http.skipResponseHeaders();
      if (err >= 0)
      {
        int bodyLen = http.contentLength();
        Serial.println(bodyLen);
        // Now we've got to the body, so we can print it out
        unsigned long timeoutStart = millis();
        char c;
        // Whilst we haven't timed out & haven't reached the end of the body
        while ( (http.connected() || http.available()) &&
               ((millis() - timeoutStart) < kNetworkTimeout) )
        {
            if (http.available())
            {
                c = http.read();
                // Print out this character
                //Serial.print(c);
                body = body + c;
                bodyLen--;
                // We read something, reset the timeout counter
                timeoutStart = millis();
            }
            else
            {
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

void parseAlarmList(String arg){
  char *jsonStr = (char *)malloc(arg.length() + 1);
  arg.toCharArray(jsonStr, arg.length() + 1);
  
  aJsonObject* root = aJson.parse(jsonStr);
  if (root != NULL){
    aJsonObject* retArray = aJson.getObjectItem(root, "ret");
    if (retArray != NULL){
      unsigned char result = aJson.getArraySize(retArray);
      Serial.println(result);  
      
      for (int i=0;i<result;i++){
        
        aJsonObject* ret = aJson.getArrayItem(retArray, (char)i);
        result++;
        if (ret != NULL){
          aJsonObject* aid = aJson.getObjectItem(ret, "aid");
          aJsonObject* time = aJson.getObjectItem(ret, "time");
          aJsonObject* type = aJson.getObjectItem(ret, "type");
          Item * item = new Item();
          item->setAid(aid->valuestring);
          item->setTime(time->valueint);
          item->setType(type->valuestring);
           arrayList->addItem(item);
            
        } else {
          break;
        }
      }
    } 
  
  }
  free(jsonStr);
 
}



void setup() {
  Serial.begin(9600);
   while (!Serial) {
    ; // wait for serial port to connect. Needed for Leonardo only
  }

  // start the Ethernet connection:
  if (Ethernet.begin(mac) == 0) {
    Serial.println("Failed to configure Ethernet using DHCP");
    // no point in carrying on, so do nothing forevermore:
    for(;;)
      ;
  }
  // give the Ethernet shield a second to initialize:
  delay(1000);
  Serial.println(Ethernet.localIP());
  Serial.println("connecting...");

}

//192.168.77.8 9090
// ghfal.herokuapp.com 80
void loop()
{

  setAlarmOff("KL_GROUP_1349530819326", "clock1");
  delay(1000);
  String alarms = getAlarmList("clock1");
  parseAlarmList(alarms);
  
  Serial.println("...");
  for (int i=0; i<arrayList->getSize(); i++){
    Item * item = arrayList->getItem(i);
    Serial.println(item->getTime());
    Serial.println(item->getAid());
    Serial.println(item->getType());
  }
  
  for(;;){
      ;
  }
}

