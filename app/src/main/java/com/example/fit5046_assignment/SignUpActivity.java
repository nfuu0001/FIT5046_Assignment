package com.example.fit5046_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.Toast;
import com.example.fit5046_assignment.Credential;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    NetworkConnection networkConnection=null;
    private EditText et_fistName;
    private EditText et_surName;
    private EditText et_email;
    private EditText et_passwrod;
    private EditText et_passwrod2;
    private RadioGroup rb_gender;
    private Button b_datepick;
    private TextView tv_date;
    private EditText et_address;
    private Spinner spinner_state;
    private EditText et_postcode;
    private Button bt_register;
    private String gender = "Male";
    private String signUpDate;
    private String state;
    private String dob;
    private String curDate;
    private Person person;
    private Credential credential;
    private int newId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        et_fistName = findViewById(R.id.et_first_name);
        et_surName = findViewById(R.id.et_sur_name);
        rb_gender = findViewById(R.id.radio_button_gender);
        b_datepick = findViewById(R.id.b_datepick);
        tv_date = findViewById(R.id.tv_date);
        et_address = findViewById(R.id.et_address);
        spinner_state = findViewById(R.id.spinner_state);
        et_postcode = findViewById(R.id.et_postcode);
        et_email = findViewById(R.id.et_email);
        et_passwrod = findViewById(R.id.et_password);
        et_passwrod2 = findViewById(R.id.et_password2);

        networkConnection = new NetworkConnection();
        b_datepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer date = new StringBuffer();
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                date.append(year);
                date.append("-");

                if (month < 10) {
                    date.append("0");
                    date.append(month);
                } else {
                    date.append(month);
                }

                date.append("-");

                if (day < 10) {
                    date.append(0);
                    date.append(day);
                } else {
                    date.append(day);
                }
                curDate = date.toString();
                date.append("T00:00:00+08:00");
                signUpDate = date.toString();

                DatePickerDialog datePicker = new DatePickerDialog(SignUpActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year1, int month1,
                                          int day1) {
                        StringBuffer date = new StringBuffer();
                        month1 = month1 + 1;
                        date.append(year1);
                        date.append("-");
                        if (month1 < 10) {
                            date.append("0");
                            date.append(month1);
                        } else {
                            date.append(month1);
                        }
                        date.append("-");
                        if (day1 < 10) {
                            date.append(0);
                            date.append(day1);
                        } else {
                            date.append(day1);
                        }

                        tv_date.setText(date.toString());
                        //System.out.println(tv_date.getText().toString());
                        date.append("T00:00:00+08:00");

                        dob = date.toString();
                    }
                }, year, month, day);
                DatePicker dp = datePicker.getDatePicker();
                dp.setMaxDate(System.currentTimeMillis());
                datePicker.show();
            }
        });

        //-----------------------

        // get spinner value

        state = (String) spinner_state.getSelectedItem();
        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state = spinner_state.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                state = "VIC";
            }
        });

        System.out.println(state);
        // get radio button value
        rb_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectRadioButton();
            }
            private void selectRadioButton() {
                RadioButton rb = (RadioButton) findViewById(rb_gender.getCheckedRadioButtonId());
                gender = rb.getText().toString();
            }
        });
        System.out.println(gender);


        Button registerButton = findViewById(R.id.bt_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(et_fistName.getText().toString())){
                    et_fistName.setError("First name can't be null");
                    return;
                }
                if(TextUtils.isEmpty(et_surName.getText().toString())){
                    et_surName.setError("Last name can't be null");
                    return;
                }
                if(et_passwrod.getText().toString().equals(et_passwrod2.getText().toString()) == false){
                    et_passwrod2.setError("Confirm password is not same as password");
                    return;
                }
                if (curDate.compareTo(tv_date.getText().toString()) < 0){
                    tv_date.setError("DOB is illegal");
                }
                if(TextUtils.isEmpty(et_address.getText().toString())){
                    et_address.setError("Address can't be null");
                    return;
                }
                if (isEmail(et_email.getText().toString()) == false){
                    et_email.setError("Email format is wrong");
                    return;
                }
                if (et_postcode.getText().toString().length() != 4){
                    et_postcode.setError("postcode must be 4 digits");
                    return;
                }

                credential = new Credential();
                person = new Person();

                credential.setUsername(et_email.getText().toString());
                credential.setSignUpDate(signUpDate);
                String convertedPassword = networkConnection.encryption(et_passwrod.getText().toString());
                credential.setPasswordHash(convertedPassword);

                person.setFname(et_fistName.getText().toString());
                person.setSurname(et_surName.getText().toString());
                person.setDob(dob);
                person.setAddress(et_address.getText().toString());
                person.setPostcode(et_postcode.getText().toString());
                person.setState(state);
                person.setGender(gender);

                UsernameDuplicateTask usernameDuplicateTask = new UsernameDuplicateTask();
                usernameDuplicateTask.execute();


            }
        });
    }

    private class UsernameDuplicateTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {

            return networkConnection.findAllUsernames() ;
        }
        @Override
        protected void onPostExecute(String result) {
            //System.out.println("======================");
            JSONArray jsonArray = null;
            boolean flag = true;
            try{
                jsonArray = new JSONArray(result);
                for (int i=0; i<jsonArray.length();i++){
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    //System.out.println("----"+jsonObject.get("username"));
                    if (jsonObject.get("username").equals(et_email.getText().toString())){
                        Toast.makeText(SignUpActivity.this,"The email has been registered",Toast.LENGTH_SHORT).show();
                        flag = false;
                    }
                }

            } catch(JSONException e){
                e.printStackTrace();
            }
            if (flag == true){
                FindMaxPersonIdTask findMaxPersonIdTask = new FindMaxPersonIdTask();
                findMaxPersonIdTask.execute();
            }

        }
    }

    private class FindMaxPersonIdTask extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... voids) {

            return networkConnection.findMaxPersonId() ;
        }
        @Override
        protected void onPostExecute(Integer result) {
            newId = result + 1;
            credential.setCredentialId(newId);
            JSONArray jsonArray = null;

            AddCredentialTask addCredentialTask = new AddCredentialTask();
            addCredentialTask.execute(credential);

        }
    }

    private class AddCredentialTask extends AsyncTask<Credential, Void, String> {
        @Override
        protected String doInBackground(Credential... params) {

            return networkConnection.addCredential(params[0]) ;
        }
        @Override
        protected void onPostExecute(String result) {
            person.setPersonId(newId);
            person.setCredentialId(credential);
            AddPersonTask addPersonTask = new AddPersonTask();
            addPersonTask.execute(person);
        }
    }

    private class AddPersonTask extends AsyncTask<Person, Void, String> {
        @Override
        protected String doInBackground(Person... params) {

            return networkConnection.addPerson(params[0]) ;
        }
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(SignUpActivity.this,"Register successfully",Toast.LENGTH_SHORT).show();
        }
    }


    public static boolean isEmail(String string) {
        if (string == null)
            return false;
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p;
        Matcher m;
        p = Pattern.compile(regEx1);
        m = p.matcher(string);
        if (m.matches())
            return true;
        else
            return false;
    }


}
