var exec = cordova.require('cordova/exec'); // eslint-disable-line no-undef

module.exports = {
	initRFID: function (success, error) {
		exec(success, error, "WTKPlugin", "initRFID", []);
	},
	startInventoryTag: function (success, error) {
		exec(success, error, "WTKPlugin", "startInventoryTag", []);
	},
	stopInventory: function (account, success, error) {
		exec(success, error, "WTKPlugin", "stopInventory", []);
	},
	freeRFID: function (success, error) {
		exec(success, error, "WTKPlugin", "freeRFID", []);
	},
};
