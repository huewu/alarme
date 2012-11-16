############################################################################
#
# if you want to use this, you should install the following Perl libraries
#	YAML
#	Device::SerialPort
#
# on Mac OS X, do "sudo perl -MCPAN -e shell" and when you get the prompt,
# do "install YAML" and "install Device::SerialPort" and exit.
#
# please refer http://mjo.tc/atelier/2009/02/arduino-cli.html
#
############################################################################

ARDUINO_DIR = /Applications/Arduino.app/Contents/Resources/Java
ARDMK_DIR = ../Arduino-Makefile
USER_LIB_PATH = ..

BOARD_TAG    = mega2560
ARDUINO_PORT = /dev/cu.usb*

ARDUINO_LIBS = Ethernet
ARDUINO_LIBS += Ethernet/utility
ARDUINO_LIBS += Wire
ARDUINO_LIBS += Wire/utility
ARDUINO_LIBS += SPI
ARDUINO_LIBS += SoftwareSerial
#ARDUINO_LIBS += LiquidCrystal
#ARDUINO_LIBS += WiFly-Shield
#ARDUINO_LIBS += Adafruit-RGB-LCD-Shield-Library
#ARDUINO_LIBS += Adafruit_NFCShield_I2C
ARDUINO_LIBS += Adafruit-Thermal-Printer-Library
ARDUINO_LIBS += ArduinoPusherClient
#ARDUINO_LIBS += ArduinoTime
ARDUINO_LIBS += HttpClient
ARDUINO_LIBS += aJson
ARDUINO_LIBS += aJson/utility
#ARDUINO_LIBS += MsTimer2

include ../Arduino-Makefile/arduino-mk/Arduino.mk 
