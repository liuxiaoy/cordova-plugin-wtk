package com.liuxiaoy.cordova.wtk;

import android.util.Log;

import com.rscja.deviceapi.entity.UHFTAGInfo;
import com.rscja.deviceapi.exception.ConfigurationException;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WTKPlugin extends CordovaPlugin {
    private static final String TAG = "WTKPlugin";
    private boolean inventoryFlag = false;

    @Override
    protected void pluginInitialize() {
        super.pluginInitialize();
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        switch (action) {
            case "initRFID": {
                this.initRFID(callbackContext);
                break;
            }
            case "freeRFID": {
                this.freeRFID(callbackContext);
                break;
            }
            case "startInventoryTag": {
                this.startInventoryTag(callbackContext);
                break;
            }
            case "stopInventory": {
                this.stopInventory(callbackContext);
                break;
            }
            default: return false;
        }
        return true;
    }

    @Override
    public void onPause(boolean multitasking) {
        super.onPause(multitasking);
    }

    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initRFID(CallbackContext callbackContext){
        try {
            com.custom.RFIDWithUHFUARTUAE.getInstance().init();
        } catch (ConfigurationException e) {
            e.printStackTrace();
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR));
        }
    }

    private void freeRFID(CallbackContext callbackContext){
        try {
            com.custom.RFIDWithUHFUARTUAE.getInstance().free();
        } catch (ConfigurationException e) {
            e.printStackTrace();
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR));
        }
    }

    private void startInventoryTag(CallbackContext callbackContext){
        if (this.inventoryFlag) {
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
            return;
        }
        try {
            com.custom.RFIDWithUHFUARTUAE.getInstance().startInventoryTag();
            this.inventoryFlag = true;
            new TagThread(callbackContext).start();
        } catch (ConfigurationException e) {
            e.printStackTrace();
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR));
        }
    }

    private void stopInventory(CallbackContext callbackContext){
        if (!this.inventoryFlag) {
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
            return;
        }
        this.inventoryFlag = false;
        try {
            com.custom.RFIDWithUHFUARTUAE.getInstance().stopInventory();
        } catch (ConfigurationException e) {
            e.printStackTrace();
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR));
        }
    }

    class TagThread extends Thread {
        private CallbackContext callbackContext;
        public TagThread(CallbackContext callbackContext) {
            this.callbackContext = callbackContext;
        }

        public void run() {
            UHFTAGInfo uhftagInfo;
            while (WTKPlugin.this.inventoryFlag) {
                try {
                    uhftagInfo = com.custom.RFIDWithUHFUARTUAE.getInstance().readTagFromBuffer();
                } catch (ConfigurationException e) {
                    e.printStackTrace();
                    break;
                }
                if (uhftagInfo != null) {
                    Log.i(TAG,"EPC:"+uhftagInfo.getEPC());
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("epc", uhftagInfo.getEPC());
                        jsonObject.put("rssi", uhftagInfo.getRssi());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        continue;
                    }
                    PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, jsonObject);
                    pluginResult.setKeepCallback(true);
                    callbackContext.sendPluginResult(pluginResult);
                }
            }
        }
    }
}
