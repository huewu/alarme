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

BOARD_TAG    = uno
ARDUINO_PORT = /dev/cu.usb*

ARDUINO_LIBS = Ethernet
ARDUINO_LIBS += Ethernet/utility
ARDUINO_LIBS += SPI
ARDUINO_LIBS += LiquidCrystal
ARDUINO_LIBS += WiFly-Shield/src/WiFly
ARDUINO_LIBS += Wire
ARDUINO_LIBS += Wire/utility
ARDUINO_LIBS += Adafruit_NFCShield_I2C

include ../Arduino-Makefile/arduino-mk/Arduino.mk 
