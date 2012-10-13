
var Pusher = require('node-pusher');

var pusher = new Pusher({
  appId: '27999',
  key: '81a20baa40945ce4b0a6',
  secret: '9cad8056337cdeb8d68b'
});

var channel = 'clock1';
var event = 'alarmOFF';
var data = {
	msg: 'OFF'
};
//var socket_id = '1302.1081607';

pusher.trigger(channel, event, data);			