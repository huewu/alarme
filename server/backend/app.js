
var express = require('express');
//var request = require('request');
var path = require('path');
var fs = require('fs');
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
    member : [{uid: String, status: String}]       
});

var User = mongoose.model('User', UserSchema);
var Alarm = mongoose.model('Alarm', AlarmSchema);
//var db = mongoose.connect('mongodb://localhost/ghf');
var db = mongoose.connect('mongodb://admin:admin@ds033617-a.mongolab.com:33617/ghf');

var clientPath = function() {
    var _root = path.resolve(__dirname, '..');	
	return {		
		js: _root + '/js',
		css: _root + '/css',
		start: _root + '/index.html'		
	}
}

var app = express();
app.configure(function() {	
	app.use('/js', express['static'](clientPath().js));	
	app.use('/css', express['static'](clientPath().css));	
	app.use(express.bodyParser());
});
console.log('Server started...');

// create User
app.post('/user', function(req, res) {
	console.log(req.body);
	
	var requestBody = req.body;		
	var uid = requestBody.uname.split('@')[0];
	
    User.findOne({uid: uid}, function(err, doc) {
    	if (err) {
    		
    	} else if (null == doc) {			
    		var newUser = {
				uid: uid,
				uname: requestBody.uname,
				rid: requestBody.rid,
				cid: requestBody.cid}
				
			new User(newUser).save(function(err, data) {
				if (err) {
				
				} else {
					var ret = JSON.clone(data);
					ret.msg = "success";
    				res.json(200, ret); 
				}				
			});   		
    	} else { // already exist
			var ret = JSON.clone(doc);
			ret.msg = "already exist";
			res.json(400, ret);	
    	}
    }); 
});

// show Users
app.get('/users', function(req, res) {
	User.find({}, function(err, data) {
		console.log(data);
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

// set alarm
app.post('/user/:uid/alarm', function(req, res) {
	var requestBody = req.body;
	var aid = req.params.uid + '_' + requestBody.type + '_' + requestBody.time;
	var member = [];
	
	if (1 == requestBody.member.length) {  // private
		member.push({
			uid: req.params.uid,
			status: requestBody.member[0].status			
		});
	} else { // group
		var len = requestBody.member.length;
		for (i = 0; i < len; i++) {
			member.push({
				uid: requestBody.member[i].uid,
				status: requestBody.member[i].status			
			});
		}	
	}
	
	var newAlarm = {
		aid: aid,
		type: requestBody.type,
		time: requestBody.time,
		member: member
	}
	console.log(newAlarm);
	new Alarm(newAlarm).save(function(err, data) {
		if (err) {
		
		} else {
			console.log(data);
			var ret = JSON.clone(data);
			ret.msg = "success";
    		res.json(200, ret); 
		}
	});	
});

// show alarms
app.get('/alarms', function(req, res) {
	Alarm.find({}, function(err, data) {
		//console.log(data);
		res.json(200, data);
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


app.put('/alarm/:aid', function(req, res) {	
	var filter = {
		aid: req.params.aid,
		'member.uid': req.body.uid
	}
	console.log(filter);
	Alarm.update(filter, {$set: {'member.$.status': 'OFF'}}, function(err, data) {		
		if (err) {
			res.json(400, {msg: 'fail'});
		} else if (data != 0) {
			Alarm.findOne({aid: req.params.aid}, function(err, data) {
				if (err) {
				
				} else {
					
					var allOff = true;
					console.log(data.member);
					for (i = 0; i < data.member.length; i++) {
						if (data.member[i].status == 'ON') {
							allOff = false;
							break;
						}					
					}
					if (allOff) {
						console.log('all off'); // go to push
					}
					res.json(200, {msg: 'success'});
				}			
			});			
		} else {
			res.json(400, {msg: 'fail'});
		}		
	});
});
/*
Alarm.update({aID: '2nd', 'member.uID': 'kwanlae'}, {$set: {'member.$.status': 'ON'}}, function(err, data, raw) {
	if (err) {
		console.log(err);
	} else {
		console.log('updated...');
		console.log(raw);
		Alarm.findOne({aID: '2nd'}, function(err, data) {
			for (i = 0; i < data.member.length; i++) {
				console.log(data.member[i].status);
			}
		});
	}   
});
*/

///////////////////////////
app.get('/', function(req, res) {
	res.sendfile(clientPath().start);
});

app.get('/phoneNumber/:phoneNumber', function(req, res) {	
    console.log("API received:phone number is " + req.params.phoneNumber);
	res.sendfile(path.resolve(__dirname, '..') + '/aboutMe.html');
});

/*
app.get('/profile', function(req, res) {	
	var aboutMeUrl;
	var phoneNumber = req.query.phoneNumber;	
	console.log(phoneNumber);
	
	User.findOne({'phoneNumber' : phoneNumber}, function(err, doc) {	
		if (err) {
			
		} else {			
			try {
				aboutMeUrl = 'http://about.me/' + doc.profileID;
				console.log(aboutMeUrl);
				
				var url = 'https://api.about.me/api/v2/json/user/view/'
						+ doc.profileID
						+ '?client_id=327f64c34e821f59a398e0fd6946bd21f72850a3&extended=true';
				request.get(url, function(error, response, body) {		
					console.log(response.statusCode);
					res.send(body);
				});				
			} catch (exception) {
				console.log("unregistered user");
				var anonymousUser = {
					background: "",
					display_name: "Anonymous Caller",
					bio: ""
				}				
				res.send(anonymousUser);								
			}				
		}		
	});	
});
*/
app.get('/signIn', function(req, res) {	
    console.log('sign in process...');
    
    var userID = req.query.userID;
	var userPW = req.query.pw;	
	
	var plainText = userID + ";" + userPW;
	var primaryKey = crypto.createHash('sha1').update(plainText).digest('hex');
	
	if (primaryKey == "66c91c382a0555626a0e0ec634b04d2e30a0211f") { // in case of admin
		User.find({}, function(err, data){
        	res.json(data);
    	});
	} else {
		User.findOne({primaryKey: primaryKey}, function(err, doc) {
			res.send(doc);		
		});	
	}	
});

app.get('/signUp', function(req, res) {
	console.log('sign up process...');	

    var userID = req.query.userID;    
    User.findOne({'userID': userID}, function(err, doc) {
    	if (err) {
    		
    	} else if (doc == null) {
    		
    		var userData = {
    			'userID': userID
    		}
    		var user = new User(userData);
    		user.save(function(err, data) {    			
    			if (err) {
    				
    			} else {				
    				var temp = JSON.clone(data);					
    				temp.responseCode = 200;
    				temp.responseText = "Available ID";
    				console.log(temp);					
    				res.send(temp); 
    			}
    		});    		
    	} else { // already exist    		
    		var temp = JSON.clone(doc);
    		temp.responseCode = 400;
    		temp.responseText = "Already existing ID";
    		console.log(temp);
			res.send(temp);	
    	}
    });  
});

app.get('/signUpSubmit', function(req, res) {
	var userID = req.query.userID;
	var userPW = req.query.pw;
	var phoneNumber = req.query.phoneNumber;
	var profileID = req.query.aboutMeID;
	
	var plainText = userID + ";" + userPW;
	
	var userData = {		
		primaryKey : crypto.createHash('sha1').update(plainText).digest('hex'),
		// userID: userID, // it doesn't matter whether or not existing'
		profileID: profileID,
		phoneNumber: phoneNumber
	}	
	
	console.log(userData);	
	User.update({userID: userID}, userData, {upsert: true}, function() {
		console.log("updated");		
		res.send("success");
	});	
});

app.get('/delete', function(req, res) {
	var userID = req.query.userID;	
	
	User.remove({userID: userID}, function() {
		console.log("deleted " + userID);
		res.send("done");
	});	
});

app.listen(process.env.PORT || process.env.C9_PORT || 9090);

// util
JSON.clone = function(o) {
	return JSON.parse(JSON.stringify(o));
}
