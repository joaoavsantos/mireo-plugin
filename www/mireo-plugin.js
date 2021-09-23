   
var exec = require('cordova/exec');

var PLUGIN_NAME = 'MireoPlugin';

exports.navigateTo = function (street, houseNumber, postal, city, area, country, countryIso, lon, lat, noUI, success, error) {
  exec(success, error, PLUGIN_NAME, "navigateTo", [{
  "street": street,
  "houseNumber": housenumber,
  "postal": postal,
  "city": city,
  "area": area,
  "country": country,
  "countryIso": countryIso,
  "lon": lon,
  "lat": lat, 
  "noUI": noUI
}]);
};
