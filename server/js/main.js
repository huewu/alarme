
$(document).ready(function() {
	$('#type > option[value="PRIVATE"]').attr('selected', true);
	$('#alarmTime').val(new Date().getTime());
});

$('#createUser').on('click', function() {
	var self = this;
	
	var userData = {
		uname: $('#uname').val(),
		rid: $('#rid').val(),
		cid: $('#cid').val()
	}
		
	$.post('/user', userData, function(data) {

	})	
	.error(function(jqXHR, textStatus, errorThrown) {

	})
	.complete(function(jqXHR, textStatus) {	
		var serverMsg = JSON.parse(jqXHR.responseText).msg;	
		showResult(self, serverMsg);	
	});	
});

$('#removeUser').on('click', function()  {
	var self = this;
	
	$.ajax({
		type: 'DELETE',
		url: '/user/' + $('#uid').val()
	}).done(function(data) {
		var serverMsg = data.msg;
		showResult(self, serverMsg);
	}).fail(function(jqXHR, textStatus) {
		var serverMsg = JSON.parse(jqXHR.responseText).msg;
		showResult(self, serverMsg);
	});
});

$('#showUsers').on('click', function() {
	var self = this;
	
	$.get('/users', function(data) {
		console.log(data);
		var msg = '';		
		for (i = 0; i < data.length; i++) {
			msg += JSON.stringify(data[i]);
		}		
		showResult(self, msg);
	});
});		

$('#setAlarm').on('click', function() {
	var self = this;
	
	var member = [];
	var master = $('#uid_al').val();
	member.push({ // common, private or group master
		uid: master,
		status: 'ON'
	});
	
	var alarmType = $('#type option:selected').val(); 
	if ('PRIVATE' == alarmType) {
		
	} else {
		var buddies = $('#member').val().replace(/\s/g,'').split(',');
		var len = buddies.length;
		for (i = 0; i < len; i++) {
			member.push({
				uid: buddies[i],
				status: 'ON'
			});
		}
	}
	
	var alarm = {
		type: alarmType,
		time: $('#alarmTime').val(),
		member: member
	}
	
	$.post('/user/' + master + '/alarm', alarm, function(data) {
	
	})
	.error(function(jqXHR, textStatus, errorThrown) {

	})
	.complete(function(jqXHR, textStatus) {	
		var serverMsg = JSON.parse(jqXHR.responseText).msg;
		showResult(self, serverMsg);
	});
});

 $('#type').change(function(){
	 if ( 'GROUP' == $("#type option:selected").val()) {
		$('#member').show();
	 } else {
		$('#member').hide();
	 }
});		

$('#showAlarms').on('click', function() {
	var self = this;
	
	$.get('/alarms', function(data) {		
		var msg = '';		
		for (i = 0; i < data.length; i++) {
			msg += JSON.stringify(data[i]);
		}
		showResult(self, msg);		
	});
});

$('#removeAlarm').on('click', function()  {
	var self = this;
	
	$.ajax({
		type: 'DELETE',
		url: '/alarm/' + $('#aid').val()
	}).done(function(data) {
		var serverMsg = data.msg;
		showResult(self, serverMsg);
	}).fail(function(jqXHR, textStatus) {
		console.log(jqXHR.responseText);
		var serverMsg = JSON.parse(jqXHR.responseText).msg;
		showResult(self, serverMsg);
	});
});

$('#alarmOFFPhone').on('click', function() {
	var self = this;
	
	$.ajax({
		type: 'PUT',
		url: '/alarm/' + $('#aid_off_phone').val(),
		data: {		
			uid: $('#uid_off_phone').val(),
			status: 'OFF'
		}
	}).done(function(data) {
		var serverMsg = data.msg;
		showResult(self, serverMsg);
	});
});

// util
function showResult(here, msg) {
	$(here).parent().children('.result').empty().append(msg);
}
