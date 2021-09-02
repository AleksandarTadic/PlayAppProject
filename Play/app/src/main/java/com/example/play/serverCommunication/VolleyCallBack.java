package com.example.play.serverCommunication;

import org.json.JSONObject;

public interface VolleyCallBack {
    void onSuccess(JSONObject response);
    void onConnectionError(String message);
}
