package com.example.hirokiminami.webservices;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class Participant {

    private String name;
    private String email;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}

public class MainActivity extends Activity {
    private List<Participant> participant = new ArrayList<Participant>();
    private List<HashMap<String, String>> mParticipantsMapList = new ArrayList<>();
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final TextView mTextView = (TextView) findViewById(R.id.respTV);
        final Button getReq = (Button) findViewById(R.id.requestBtn);
        final Button postReq = (Button) findViewById(R.id.postRequestBtn);
        final EditText nameET = (EditText) findViewById(R.id.nameET);
        final EditText emailET = (EditText) findViewById(R.id.emailET);
        final ListView mListView = (ListView) findViewById(R.id.list);

        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String URL = "http://esm-rest-service.herokuapp.com/participants/";

        // GET
        getReq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Request a string response from the provided URL.
                final StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                mTextView.setText("Response is: "+ response.substring(0,500));
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mTextView.setText("That didn't work!");
                    }
                });

                queue.add(stringRequest);
            }
        });

        // POST
        postReq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("name", nameET.getText().toString());
                params.put("email", emailET.getText().toString());

                ListAdapter adapter = new SimpleAdapter(MainActivity.this, mParticipantsMapList, R.layout.participant,
                        new String[] { KEY_NAME, KEY_EMAIL },
                        new int[] { R.id.participantNameTV, R.id.participantEmailTV});
                mListView.setAdapter(adapter);

                final JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL,
                        new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    VolleyLog.v("Response:%n %s", response.toString(4));
                                    mTextView.setText("Response is: "+ response.toString());
                                    for (Participant participant : mParticipantsMapList) {

                                        HashMap<String, String> map = new HashMap<>();

                                        map.put(KEY_VER, android.getVer());
                                        map.put(KEY_NAME, android.getName());
                                        map.put(KEY_API, android.getApi());

                                        mAndroidMapList.add(map);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                        mTextView.setText("POST didn't work!");
                    }
                });

                queue.add(req);
            }
        });
    }
}
