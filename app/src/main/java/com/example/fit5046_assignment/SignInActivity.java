package com.example.fit5046_assignment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fit5046_assignment.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SignInActivity extends AppCompatActivity {
    NetworkConnection networkConnection=null;
    SharedPreferences sp;
    private AppBarConfiguration mAppBarConfiguration;
    public static int personId;
    public static String userFirstName;
    String username1;

    private EditText et_password;
    private ImageView iv_showPassword;
    private Boolean showPassword = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        networkConnection=new NetworkConnection();

        et_password = (EditText) findViewById(R.id.et_password);
        iv_showPassword = (ImageView) findViewById(R.id.iv_showPassword);
        iv_showPassword.setImageDrawable(getResources().getDrawable(R.drawable.eye_c));

        iv_showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.iv_showPassword:
                        if (showPassword) {// 显示密码
                            iv_showPassword.setImageDrawable(getResources().getDrawable(R.drawable.password_eye));
                            et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            et_password.setSelection(et_password.getText().toString().length());
                            showPassword = !showPassword;
                        } else {// 隐藏密码
                            iv_showPassword.setImageDrawable(getResources().getDrawable(R.drawable.eye_c));
                            et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            et_password.setSelection(et_password.getText().toString().length());
                            showPassword = !showPassword;
                        }
                        break;

                    default:
                        break;
                }

            }
        });


        sp = getSharedPreferences("data",Context.MODE_PRIVATE);
        Button signinButton = findViewById(R.id.bt_signin);
        Button signupButton = findViewById(R.id.bt_signup);
        final EditText username = findViewById(R.id.et_username);
        final EditText password = findViewById(R.id.et_password);
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username1 = username.getText().toString();
                String password1 = password.getText().toString();
                if (TextUtils.isEmpty(username1) || TextUtils.isEmpty(password1)){
                    Toast.makeText(SignInActivity.this,"Username and password can't be null",Toast.LENGTH_SHORT).show();
                } else {
                    SigninTask signinTask = new SigninTask ();
                    signinTask.execute(username1,password1);
                }


            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intent);

            }
        });

    }

    private class SigninTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return networkConnection.getCredential(params[0],params[1]);
        }
        @Override
        protected void onPostExecute(String record) {


            //-------------------------
            super.onPostExecute(record);
            //==================
            int credentialId = 0;
            record = record.replace("[","");
            record = record.replace("]","");
            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject(record);
                credentialId = jsonObj.getInt("credentialId");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //---------------------------------
            if(record.length()>2){
                System.out.println(credentialId);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt(username1,credentialId);
                editor.putInt("personId",credentialId);
                editor.commit();
                Intent intent = new Intent(SignInActivity.this,MainActivity.class);
                startActivity(intent);
                GetFirstName gfn = new GetFirstName();
                gfn.execute(credentialId);
            } else {
                Toast.makeText(SignInActivity.this,"Your username or password is wrong, try again",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class GetFirstName extends AsyncTask<Integer, Void, String> {
        @Override
        protected String doInBackground(Integer... params) {
            return networkConnection.getFirstName(params[0]);
        }

        @Override
        protected void onPostExecute(String record) {
            super.onPostExecute(record);
            String firstName = "";
            record = record.replace("[", "");
            record = record.replace("]", "");
            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject(record);
                firstName = jsonObj.getString("fname");
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("first name", firstName);
                editor.putString("address",jsonObj.getString("address"));
                System.out.println("address sign -------" + jsonObj.getString("address"));
                editor.commit();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            userFirstName = firstName;
            System.out.println(firstName);
        }
    }




}