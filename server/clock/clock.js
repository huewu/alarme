
var alarm = null;
var BEEP = false;

$(document).ready(function() {
	var pusher = new Pusher('81a20baa40945ce4b0a6');
	var channel = pusher.subscribe($('#cid').val()); // channel is cid
	channel.bind('alarmOFF', function(data) {
	  console.log(data.msg);
	});
	
	if (!alarm) {
		$('#alarmStatus').empty().append('alarm disabled');
	}	
	setInterval(display, 1000);		
});

$('#alarmSet').on('click', function() {
	var alarmHour = $('#alarmHour').val();
	var alarmMinute = $('#alarmMinute').val();		
	var alarmDisplay = 'alarm enabled@ ' + alarmHour + ' : ' + alarmMinute;
	$('#alarmStatus').empty().append(alarmDisplay);
	
	alarm = {}
	alarm.hour = alarmHour;
	alarm.minute = alarmMinute;
	alarm.ref = new Date().getSeconds();
	$('#beep').empty();
	BEEP = false;
});
	
$('#alarmUnset').on('click', function() {
	$('#alarmStatus').empty().append('alarm disabled');
	$('#alarmHour').val(null);
	$('#alarmMinute').val(null);
	alarm = null;
	$('#beep').empty();
});

$('#alarmMute').on('click', function() {
	$('#beep').empty();
	BEEP = false;
	if (alarm) {
		alarm.ref = -1;
	}		
	// go to pusher.com
	console.log($('#cid').val());
});  

function display() {	
	var now = formatTimeToJson(new Date());	
	$('#time').empty().append(now.display);		

	if (alarm && !BEEP && 
		now.hh == alarm.hour &&
		now.min == alarm.minute &&
		(now.ss == 0 || now.ss == alarm.ref + 1))  {		
		$('#beep').empty().append('((( BEEP )))');
		BEEP = true;
		alarm.ref = -1;
	}	
}

function formatTimeToJson(now) {
	var yyyy = now.getFullYear();
	var mm = now.getMonth() + 1;
	var dd = now.getDate();
	var hh = now.getHours();
	var min = now.getMinutes();
	var ss = now.getSeconds();
	
	var ret = {
		yyyy: yyyy,
		mm: mm,
		dd: dd,
		hh: hh,
		min: min,
		ss: ss,
		display: yyyy + '-' + mm + '-' + dd + 'T' + hh + ':' + min + ':' + ss
	}	
	return ret;
}