var flag = {
	LOG: 'true',
	GCM: 'true',
	PUSHER: 'true'
}

// node.js core
var path = require('path');
var fs = require('fs');

// google cloud messaging to phone
var gcm = new (require('gcm').GCM)(''); // api Key

// pusher.com to clock
var pusher = new (require('node-pusher'))({
  appId: '',
  key: '',
  secret: ''
});

// mongodb 
var mongoose = require('mongoose');

var UserSchema = new mongoose.Schema({
    uid: String,
    uname: String,
    rid: String,
    cid: String
});

var AlarmSchema = new mongoose.Schema({
    aid: String,
    type: String,
    time: Date,
    members : [{uid: String, status: String}]       
});

var User = mongoose.model('User', UserSchema);
var Alarm = mongoose.model('Alarm', AlarmSchema);
var db = mongoose.connect('mongodb://admin:admin@ds033617-a.mongolab.com:33617/ghf');
//var db = mongoose.connect('mongodb://localhost/ghf');

// web server framework
var express = require('express');

var clientPath = function() {
    var _root = path.resolve(__dirname, '..');	
	return {		
		js: _root + '/js',
		css: _root + '/css',
		start: _root + '/index.html'	,
		root: _root
	}
}

var app = express();
app.configure(function() {	
	app.use('/js', express['static'](clientPath().js));	
	app.use('/css', express['static'](clientPath().css));	
	app.use('/', express['static'](clientPath().root + '/clock'));
	app.use(express.bodyParser());
});
log('Server started.');

// create User
app.post('/user', function(req, res) {
	log('create user');
	//var uid = req.body.uid;
	var uid = req.body.uname.split('@')[0];	
	
    User.findOne({uid: uid}, function(err, user) {
    	if (err) {
		
    	} else if (null == user) { // new user
			new User({
				uname: req.body.uname,
				uid: req.body.uid,				
				rid: req.body.rid,
				cid: req.body.cid			
			}).save(function(err, newUser) {
				log(newUser);
				if (err) {
				
				} else {
    				res.json(200, newUser); 
				}				
			});   		
    	} else { // already exist			
			res.json(400, user);	
    	}
    }); 
});

// set alarm
app.post('/user/:uid/alarm', function(req, res) {
	log('set alarm');
	var rb = req.body;
	var master = req.params.uid;
	var aid = master + '_' + rb.type + '_' + rb.time;
	var members = [];
	
	if (1 == rb.members.length) {  // private
		members.push({
			uid: req.params.uid,
			status: "ON"
			//status: rb.members[0].status		
		});
	} else { // group		
		for (i = 0; i < rb.members.length; i++) {			
			members.push({
				uid: rb.members[i].uid,
				status: (rb.members[i].uid == master)? "ON" : "OFF" // default is OFF except master
				// status: rb.members[i].status
			});
		}
	}

	new Alarm({
		aid: aid,
		type: rb.type,
		time: rb.time,
		members: members
	}).save(function(err, newAlarm) {
		if (err) {
		
		} else {
			log(newAlarm);
			
			for (i = 0; i < newAlarm.members.length; i++) {				
				User.findOne({uid: newAlarm.members[i].uid}, function(err, user) {
					var msgToPhone = {
						registration_id: user.rid, 
						'data.type': 'alarmRequest',
						'data.aid': newAlarm.aid,
						'data.time': newAlarm.time,
						'data.members': newAlarm.members
					}
					
					if (user.uid != master) {
						sendGCM(msgToPhone);
					}
					
					var msgToClock = {
						aid: newAlarm.aid,
						type: newAlarm.type,
						time: newAlarm.time
					}
					sendPusher(user.cid, 'alarmSET', msgToClock);	
				});			
			}			
    		res.json(200, newAlarm); 
		}
	});	
});

// group alarm accept
app.post('/accept/:aid/:uid', function(req, res) {
	confirmAlarm(req, res, 'ON');	
});

// group alarm reject
app.post('/reject/:aid/:uid', function(req, res) {
	confirmAlarm(req, res, 'OFF');	
});

function confirmAlarm(req, res, state) {
	var filter = {
		aid: req.params.aid,
		'members.uid': req.params.uid
	}
	
	Alarm.update(filter, {$set: {'members.$.status': state}}, function(err, data) {
		if (err) {
			res.json(400, {msg: 'error'});
		} else if (data != 0) {
			res.json(200, {msg: 'success'});		
		} else {
			res.json(400, {msg: 'not found'});
		}		
	});
}

// get alarm list
app.get('/alarms', function(req, res) {
	Alarm.find({}, function(err, alarmList) {		
		res.json(200, alarmList);
	});
});

// get alarm list from clock
app.get('/alarmlist', function(req, res) {	// /alarmlist?cid=1
	User.findOne({cid: req.query.cid}, function(err, user) {
		if (err) {
			res.json(400, {msg: 'error'});
		} else if (null != user) {
			var uid = user.uid;
			
			Alarm.find({'members.uid': uid}, function(err, alarm) {											
				var msgToClock = [];				
				for (i = 0; i < alarm.length; i++) {				
					msgToClock.push({
						aid: alarm[i].aid,
						type: alarm[i].type,
						time: alarm[i].time.getTime()						
					});
				}
				
				log({'ret': msgToClock});				
				res.json(200, {'ret': msgToClock});
			});			
		}
	});	
});

// alarm off from phone 
app.put('/alarm/:aid', function(req, res) {
	var filter = {
		aid: req.params.aid,
		'members.uid': req.body.uid
	}
	updateAlarm(filter, req, res);
});

// alarm off from clock
app.get('/alarm/:aid', function(req, res) { // /alarm/:aid?cid=1	
	User.findOne({cid: req.query.cid}, function(err, user) {
		var filter = {
			aid: req.params.aid,
			'members.uid': user.uid
		}
		updateAlarm(filter, req, res);		
	});
});

function updateAlarm(filter, req, res) {	
	Alarm.update(filter, {$set: {'members.$.status': 'OFF'}}, function(err, data) {
		if (err) {
			res.json(400, {msg: 'error'});
		} else if (data != 0) {
			Alarm.findOne({aid: req.params.aid}, function(err, alarm) {
				if (err) {	
				
				} else {		
					for (i = 0; i < alarm.members.length; i++) {
						if (alarm.members[i].uid != filter['members.uid']) {							
							User.findOne({uid: alarm.members[i].uid}, function(err, user) {													
								var msgToPhone = {
									registration_id: user.rid, 
									'data.type': 'alarmUpdate',
									'data.aid': alarm.aid,
									'data.time': alarm.time,
									'data.members': alarm.members,
									'data.master': filter['members.uid'],
									'data.offTime': (new Date()).getTime()
								}								
								sendGCM(msgToPhone);
								log('Sending GCM to ' + alarm.members[i].uid);								
							});							
						}						
					}					
					
					log(alarm.members);
					
					var allOff = true;					
					for (i = 0; i < alarm.members.length; i++) {
						if (alarm.members[i].status == 'ON') {
							allOff = false;
							break;
						}					
					}
					if (allOff) { // alarm off using push
						log('all alarms are off'); 
						offPrivateAlarm(alarm);							
					}
					res.json(200, {msg: 'success'});
				}			
			});			
		} else {
			res.json(400, {msg: 'fail'});
		}		
	});
}

function offPrivateAlarm(alarm) {
	for (i = 0; i < alarm.members.length; i++) {		
		User.findOne({uid: alarm.members[i].uid} , function(err, user) {
			if (err) {
			
			} else {
				// phone off using GCM
				var msgToPhone = {
					registration_id: user.rid, 
					'data.aid': alarm.aid,
					'data.msg': 'alarmOFF',
					'data.offTime': (new Date()).getTime()
				};				
				sendGCM(msgToPhone);
				log('Sending to GCM ' + msgToPhone);
				
				// clock off using pusher
				var msgToClock = {
					aid: alarm.aid
				}
				sendPusher(user.cid, 'alarmOFF', msgToClock);
				log('Sending to Pusher ' + msgToClock);
			}
		});
	}
}

app.listen(process.env.PORT || process.env.C9_PORT || 9090);
///////////////////////////
// admin page
app.get('/', function(req, res) {
	res.sendfile(clientPath().start);
});

// mock clock
app.get('/clock', function(req, res) {
	res.sendfile(clientPath().root + '/clock/clock.html');
});

app.post('/flag', function(req, res) {
	flag.LOG = req.body.LOG;
	flag.GCM = req.body.GCM;
	flag.PUSHER = req.body.PUSHER;
	
	console.log(flag);
	res.json(200, flag);
});

function log(info) {		
	if (flag.LOG == 'true') {
		console.log(info);
	}
}

function sendGCM(msg) {
	if (flag.GCM == 'true') {
		gcm.send(msg, function(err, messageId) {
			if (err) {
				console.log("Something has gone wrong!");
			} else {
				console.log("Sent with message ID: ", messageId);
			}
		});	
	}	
}

function sendPusher(channel, event, msg) {
	if (flag.PUSHER == 'true') {		
		pusher.trigger(channel, event, msg);	
	}
}

// show Users
app.get('/users', function(req, res) {
	User.find({}, function(err, data) {
		log(data);
		
		res.json(200, data);
	});
});

// remove user
app.delete('/user/:uid', function(req, res) {
	User.remove({uid: req.params.uid}, function(err, data) {
		console.log(data);
		if (err | data == 0) {
			res.json(400, {msg: 'non existing user'});
		} else {
			res.json(200, {msg: 'success'});
		}		
	});	
});

// remove alarm
app.delete('/alarm/:aid', function(req, res) {
	Alarm.remove({aid: req.params.aid}, function(err, data) {
		console.log(data);
		if (err | data == 0) {
			res.json(400, {msg: 'non existing user'});
		} else {
			res.json(200, {msg: 'success'});
		}		
	});	
});

// util
JSON.clone = function(o) {
	return JSON.parse(JSON.stringify(o));
}
