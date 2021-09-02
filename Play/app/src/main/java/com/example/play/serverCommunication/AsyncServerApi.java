package com.example.play.serverCommunication;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncServerApi {

    public static void readFromServer(String url, ProgressBar progressBar, final ReadDataHandler rdh) {
        progressBar.setVisibility(View.VISIBLE);
        AsyncTask<String, Integer, String> task = new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... strings) {
                String response = "";
                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String red;
                    int i = 0;
                    while((red=br.readLine()) != null) {
                        i += 1;
                        response += red;
                        Thread.sleep(1000);
                        publishProgress(i);
                    }
                    br.close();
                    con.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            protected void onPostExecute(String response) {
                rdh.setJson(response);
                progressBar.setVisibility(View.GONE);
                rdh.sendEmptyMessage(0);
            }
        };
        task.execute(url);
    }


    public static void jsonObjectRequest(int request, String url, Context context, JSONObject data, TextView errorLabel, ProgressBar progressBar, final VolleyCallBack callback) {
        RequestQueue queue = Volley.newRequestQueue(context);
        if(progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(request, url, data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
                try {
                    if(response.getInt("statusCode") >= 200 && response.getInt("statusCode") <= 226) {
                        callback.onSuccess(response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = null;

                NetworkResponse response = error.networkResponse;
                if(response != null && response.data != null) {
                    message = new String(response.data).replace("\"", " ");
                }

                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
//                } else if (error instanceof ServerError) {
//                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection or server may be offline.";
                }

                if(progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }

                callback.onConnectionError(message);


            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                JSONObject responseObject = new JSONObject();
                if(response != null) {
                    try {
                        if(response.data.length > 0) {
                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            responseObject.put("json", jsonObject);
                        }
                        responseObject.put("statusCode", Integer.valueOf(response.statusCode));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return Response.success(responseObject, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        queue.add(jsonObjectRequest);
    }


}
