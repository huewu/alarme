var GCM = require('gcm').GCM;
var gcm = new (require('gcm').GCM)(''); // api Key

var msgToPhone = {
	
	registration_id: '', 
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
