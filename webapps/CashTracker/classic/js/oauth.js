nbApp.oauth = function(providerID) {

	var config = {
		"vk" : {
			providerID : "vk",
			authorization : "https://oauth.vk.com/authorize",
			client_id : "4832372",
			response_type : "token",
			state : "",
			scope : "email",
			redirect_uri : "http://localhost:7777/CashTracker/Provider?type=page&id=alloperations"
		}
	};

	return new JSO(config[providerID]);
};
