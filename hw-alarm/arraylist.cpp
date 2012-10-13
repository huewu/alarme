#include "Arduino.h"
#include "arraylist.h"

ArrayList::ArrayList(void){
  size = 0;
}

ArrayList::~ArrayList(void){
}

void 
ArrayList::addItem(Item* item){
  Item** newList = (Item**)malloc((size+1)*sizeof(Item));
  for (int i=0; i<size; i++){
    newList[i] = itemList[i];
  }
  newList[size] = item;
  itemList = newList;
  
  size = size + 1;
}

Item *
ArrayList::getItem(int idx){
  return itemList[idx];
}

int 
ArrayList::getSize(){
  return size;
}

Item::Item(void){
  _alive = true;
}

Item::~Item(void){
}

void 
Item::setAid(String aid){
  _aid = aid;
}

String 
Item::getAid(){
  return _aid;
}

void 
Item::setTime(int time){
  _time = time;
}

int 
Item::getTime(){
  return _time;
}

void 
Item::setType(char* type){
  if (strcmp(type, "GROUP")){
    _type = GROUP;
  } else {
    _type = PRIVATE;
  }
}

int 
Item::getType(){
  return _type;
}

void 
Item::setAlive(boolean alive){
  _alive = alive;
}

boolean 
Item::isAlive(){
  return _alive;
}

