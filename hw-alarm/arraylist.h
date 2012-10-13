
#define GROUP 0
#define PRIVATE 1

class Item{
  public:
    Item(void);
    ~Item(void);
    
    void setAid(String aid);
    String getAid();
    void setTime(int time);
    int getTime();
    void setType(char* type);
    int getType();
    void setAlive(boolean alive);
    boolean isAlive();
    
  private:
    String _aid;
    int _time;
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


