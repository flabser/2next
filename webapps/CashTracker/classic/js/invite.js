nbApp.sendInvite = function(email) {
	nbApp.xhrSendInvite(email).then(function(res) {
		console.log(res);
	});
};
