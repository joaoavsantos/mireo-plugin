package com.teamresilience.mireo.plugin;

import android.util.Log;
import android.widget.Toast;
import android.content.ComponentName;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicReference;

import hr.mireo.arthur.api.API;
import hr.mireo.arthur.api.APIAsyncRequest;
import hr.mireo.arthur.api.EasyAPI;
import hr.mireo.arthur.api.GeoAddress;
import hr.mireo.arthur.api.DisplaySurface;


public class MireoPlugin extends CordovaPlugin {

    private static final String TAG = "MireoPlugin";

    private EasyAPI mAPI;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {

        Log.d(TAG, "Initializing Mireo Cordova plugin");

        // create API instance
        new EasyAPI("gm", cordova.getContext(), new ComponentName("com.daf.smartphone", "hr.mireo.arthur.common.services.APIMessengerService"));
        mAPI.setScreenFlags(DisplaySurface.screen_is_weblink);


        // GeoAddress address = new GeoAddress();
        // address.area = "Overijssel|OV";
        // address.houseNumber = String.valueOf(53);
        // address.postal = "7711";
        // address.country = "Nederland";
        // address.iso = 528;
        // address.street = "Bosmansweg";
        // address.city = "Nieuwleusen, Dalfsen";
        // address.setLonLat(6.276339590549469, 52.58167967539245);
        // address.type = String.valueOf(1080);

        // EasyAPI.AddressResult listener2 = (status, address1) -> {
        //     String message = "onAddress - status = " + status + ", address = " + address1;

        //     Toast toast = Toast.makeText(cordova.getActivity(), message,
        //             Toast.LENGTH_LONG);
        //     // Display toast
        //     toast.show();
        // };
        // APIAsyncRequest apiAsyncRequest = mAPI.navigateTo(address, false, listener2);
    }

    public String getMireoVersion()
    {
        AtomicReference<String> result = new AtomicReference<>("Unknown");

        mAPI.getVersionEx((status, version) -> {
            if (status == API.RESULT_OK) {
                Log.d(TAG, "Mireo Application version: " + version.appVersion);
                Log.d(TAG, "Mireo Build number: " + version.buildNumber);
                Log.d(TAG, "Mireo Map version: " + version.mapVersion);
                Log.d(TAG, "Mireo Device serial number: " + version.serial);

                result.set(version.formatAppVersion(false));
            }
            else {
                Log.e(TAG, "getVersionEx ERROR: " + EasyAPI.statusMessage(status));
                result.set(EasyAPI.statusMessage(status));
            }
        }).waitForResult(20_000);

        return result.get();
    }

    public boolean navigateTo(String street, Integer houseNumber, Integer postal, String city, String area, String country, Integer countryIso, Double lon, Double lat, boolean noUI)
    {
        AtomicReference<Integer> apiResult = new AtomicReference<>(API.RESULT_FAIL);

        GeoAddress address = new GeoAddress();
        address.area = area; //"Overijssel|OV";
        address.houseNumber = String.valueOf(houseNumber); //String.valueOf(53);
        address.postal = String.valueOf(postal); //"7711";
        address.country = country; // "Nederland";
        address.iso = countryIso; //528;
        address.street = street; //"Bosmansweg";
        address.city = city; //"Nieuwleusen, Dalfsen";
//        address.setLonLat(6.276339590549469, 52.58167967539245);
        address.setLonLat(lon, lat);
//        address.type = String.valueOf(1080);
        
        
        EasyAPI.AddressResult listener = (status, foundAddress) -> {
            apiResult.set(status);
        };
        Log.v("Mireo-Plugin", listener.toString());
        mAPI.navigateTo(address, noUI, listener).waitForResult(20_000);

        return apiResult.get() == API.RESULT_OK;
    }

    @Override
    public boolean execute(String action, JSONArray args,
                           final CallbackContext callbackContext) throws JSONException {
        if("getVersion".equals(action))
        {
            String mireoVersion = getMireoVersion();
            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, mireoVersion);
            callbackContext.sendPluginResult(pluginResult);
            return true;
        }
        else if("navigateTo".equals(action))
        {
            JSONObject options = args.getJSONObject(0);

            String street = options.optString("street");
            Integer houseNumber = options.optInt("houseNumber", -1);
            Integer postal = options.optInt("postal", -1);
            String city = options.optString("city");
            String area = options.optString("area");
            String country = options.optString("country");
            Integer countryIso = options.optInt("countryIso", -1);
            Double lon = options.optDouble("lon", Double.NaN);
            Double lat = options.optDouble("lat", Double.NaN);
            boolean noUI = options.getBoolean("noUI");

            boolean navigationResult = navigateTo(street, houseNumber, postal, city, area, country, countryIso, lon, lat, noUI);

            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, navigationResult);
            callbackContext.sendPluginResult(pluginResult);
            return true;
        }
        else
        {
            callbackContext.error("\"" + action + "\" is not a recognized action.");
            return false;
        }
    }
}
