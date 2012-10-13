
var GCM = require('gcm').GCM;
var gcm = new (require('gcm').GCM)('AIzaSyDE-wlLfIDo8eyB8h2tQph30fqj1mf0CUQ'); // api Key

var msgToPhone = {
	
	registration_id: 'APA91bEANrNqb_NgLtzIoulehzxkcH4JlMvrdY8cseqCaRvKmNe7Jgl6QZzs_fVXmg4bSzpETQuTu6SMWEvzoCN0uev0RCCiNs5qikCvVAuEdpCzMV0X4vMsR_gli2fgz_-ou4LrjMGV', 
	'data.msg': 'alarmOFF'    
};
console.log(msgToPhone);		

gcm.send(msgToPhone, function(err, messageId) {
	if (err) {
		console.log("Something has gone wrong!");
	} else {
		console.log("Sent with message ID: ", messageId);
	}
});
