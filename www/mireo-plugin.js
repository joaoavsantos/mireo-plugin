   
var exec = require('cordova/exec');

var PLUGIN_NAME = 'MireoPlugin';

exports.navigateTo = function (street, houseNumber, postal, city, area, country, countryIso, lon, lat, noUI, success, error) {
  exec(success, error, PLUGIN_NAME, "navigateTo", [{
  "street": street,
  "houseNumber": houseNumber,
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

exports.setFavorite = function (street, houseNumber, postal, city, area, country, countryIso, lon, lat, add, success, error) {
  exec(success, error, PLUGIN_NAME, "setFavorite", [{
  "street": street,
  "houseNumber": houseNumber,
  "postal": postal,
  "city": city,
  "area": area,
  "country": country,
  "countryIso": countryIso,
  "lon": lon,
  "lat": lat, 
  "add": add
}]);
};
