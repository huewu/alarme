var Pusher = require('node-pusher');

var pusher = new Pusher({
  appId: '',
  key: '',
  secret: ''
});

var channel = 'clock1';
var event = 'alarmOFF';
var data = {
	msg: 'OFF'
};
//var socket_id = '1302.1081607';

pusher.trigger(channel, event, data);			