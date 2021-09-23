var exec = require('cordova/exec');

var PLUGIN_NAME = 'MireoPlugin';

exports.navigateTo = function (street, houseNumber, postal, city, area, country, countryIso, lon, lat, noUI, success, error) {
  exec(success, error, PLUGIN_NAME, "navigateTo", [street, houseNumber, postal, city, area, country, countryIso, lon, lat, noUI]);
};
