package com.android.mads.madses;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.mads.app.AppConfig;
import com.android.mads.app.AppController;
import com.android.mads.helper.SQLiteHandler;
import com.android.mads.helper.SessionManager;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {

    private EditText ed_email, ed_password;
    private Button btn_login, btn_newUser;
    private ProgressDialog pDialog;

    private SessionManager session;
    private SQLiteHandler db;

    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ed_email = (EditText) findViewById(R.id.edtxt_login_email);
        ed_password = (EditText) findViewById(R.id.edtxt_login_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_newUser = (Button) findViewById(R.id.btn_login_newUser);
        pDialog = new ProgressDialog(this);

        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

        if (session.isLoggedIn()) {
            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(i);
            finish();
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ed_email.getText().toString();
                String password = ed_password.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please Enter Details", Toast.LENGTH_LONG);
                } else {
                    checkLogin(email, password);
                }
            }
        });

        btn_newUser.setPaintFlags(btn_newUser.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btn_newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    public void checkLogin(final String email, final String password) {
        String tag_req = "login_request";
        pDialog.setMessage("Logging In...");
        showDialog();

        final StringRequest strR = new StringRequest(
                Request.Method.POST, AppConfig.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();

                        try {
                            JSONObject jsb = new JSONObject(response);
                            boolean error = jsb.getBoolean("error");
                            if (!error) {
                                String uid = jsb.getString("uid");

                                JSONObject user = jsb.getJSONObject("user");
                                String fname = user.getString("fname");
                                String lname = user.getString("lname");
                                String email = user.getString("email");
                                String dob = user.getString("dob");
                                String address = user.getString("address");
                                String bloodgroup = user.getString("bloodgroup");
                                String contact1 = user.getString("contact1");
                                String contact2 = user.getString("contact2");
                                String vechiclename = user.getString("vechiclename");
                                String fueltype = user.getString("fueltype");
                                String mileage = user.getString("mileage");
                                String created_at = user.getString("created_at");
                                db.addUser(uid, fname, lname, email, dob,
                                        address, bloodgroup, contact1, contact2,
                                        vechiclename, fueltype, mileage, created_at);

                                session.setLogin(true);

                                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                String error_msg = jsb.getString("error_msg");
                                Toast.makeText(LoginActivity.this, error_msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TAG ", error.getMessage());
                    }
                }
        ) {
            public Map<String, String> getParams() {
                Map<String, String> p = new HashMap<String, String>();
                p.put("tag", "login");
                p.put("email", email);
                p.put("password", password);

                return p;
            }
        };

        AppController.getmInstance().addToRequestQueue(strR, tag_req);

    }

    public void showDialog() {
        if (!pDialog.isShowing()) {
            pDialog.show();
        }
    }

    public void hideDialog() {
        if (pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }
}



