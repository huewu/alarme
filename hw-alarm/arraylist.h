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

#ifndef _ARRAY_LIST_H_
#define _ARRAY_LIST_H_

#define GROUP 0
#define PRIVATE 1

class Item{
  public:
    Item(void);
    ~Item(void);
    
    void setAid(String aid);
    String getAid();
    void setTime(char* time);
    unsigned long getTime();
    void setType(char* type);
    int getType();
    void setAlive(boolean alive);
    boolean isAlive();
    
  private:
    String _aid;
    unsigned long _time;
    int _type;
    boolean _alive;
};

class ArrayList {
public:	
  ArrayList(void); //ArrayStringList(void);
  ~ArrayList(void);
  void addItem(Item * item);
  Item * getItem(int idx);
  int getSize();
private:
  Item** itemList;
  int size;	
};

#endif //_ARRAY_LIST_H_

