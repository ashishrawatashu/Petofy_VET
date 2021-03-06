package com.cynoteck.petofyvet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.cynoteck.petofyvet.R;
import com.cynoteck.petofyvet.api.ApiClient;
import com.cynoteck.petofyvet.api.ApiResponse;
import com.cynoteck.petofyvet.api.ApiService;
import com.cynoteck.petofyvet.params.registerparams.Data;
import com.cynoteck.petofyvet.params.registerparams.Registerparams;
import com.cynoteck.petofyvet.response.RegisterResponse.RegisterResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Response;


public class RegisterActivity extends FragmentActivity implements ApiResponse, View.OnClickListener {

    private TextInputLayout firstname_TIL, lastName_TIL, email_TIL, phoneNumber_TIL, password_TIL, confirmPassword_TIL;
    private TextInputEditText firstname_TIET, lastName_TIET, email_TIET, phoneNumber_TIET, password_TIET, confirmPassword_TIET;
    private Button signUp_BT;
    private String firstName="", lastName="", email="", phoneNumber="",password="",confirmPassword="";
    private TextView signIN_TV;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityregister);
        init();

    }

    private void init() {
        firstname_TIL = findViewById(R.id.firstName_TIL);
        lastName_TIL = findViewById(R.id.lastName_TIL);
        email_TIL = findViewById(R.id.email_TIL);
        phoneNumber_TIL = findViewById(R.id.number_TIL);
        password_TIL = findViewById(R.id.password_TIL);
        confirmPassword_TIL = findViewById(R.id.cPassword_TIL);


        firstname_TIET = findViewById(R.id.firstName_TIET);
        lastName_TIET = findViewById(R.id.lastName_TIET);
        email_TIET = findViewById(R.id.email_TIET);
        phoneNumber_TIET = findViewById(R.id.number_TIET);
        password_TIET = findViewById(R.id.password_TIET);
        confirmPassword_TIET = findViewById(R.id.cPassword_TIET);

        signIN_TV = findViewById(R.id.signIn_TV);

        signUp_BT=findViewById(R.id.signUp_BT);

        signUp_BT.setOnClickListener(this);
        signIN_TV.setOnClickListener(this);





    }

    private void registerUser(Registerparams registerparams) {
        ApiService<RegisterResponse> service = new ApiService<>();
        service.get( this, ApiClient.getApiInterface().registerApi(registerparams), "Register");
        Log.d("DATALOG","check1=> "+registerparams);
    }

    @Override
    public void onResponse(Response response, String key) {
        switch (key)
        {
            case "Register":
                try {
                    Log.d("DATALOG",""+response.body().toString());
                    RegisterResponse registerResponse = (RegisterResponse) response.body();
                    int responseCode = Integer.parseInt(registerResponse.getResponse().getResponseCode());
                    if (responseCode==109){
                        Toast.makeText(this, registerResponse.getResponse().getResponseMessage(), Toast.LENGTH_SHORT).show();
                    }else if(responseCode==615) {
                        Toast.makeText(this, registerResponse.getResponse().getResponseMessage(), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this, "Please Try Again !", Toast.LENGTH_SHORT).show();
                    }

                }
                catch(Exception e) {
                    e.printStackTrace();
                    Log.e("eeeeeee",e.getLocalizedMessage());
                }
                break;
        }
    }

    @Override
    public void onError(Throwable t, String key) {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.signUp_BT:
                firstName = firstname_TIET.getText().toString().trim();
                lastName = lastName_TIET.getText().toString().trim();
                email = email_TIET.getText().toString().trim();
                password = password_TIET.getText().toString().trim();
                confirmPassword = confirmPassword_TIET.getText().toString().trim();
                phoneNumber = phoneNumber_TIET.getText().toString().trim();

                if (firstName.isEmpty()){
                    firstname_TIL.setError("Name is empty");
                    lastName_TIL.setError(null);
                    email_TIL.setError(null);
                    phoneNumber_TIL.setError(null);
                    password_TIL.setError(null);
                    confirmPassword_TIL.setError(null);
                }else if (lastName.isEmpty()){
                    lastName_TIL.setError("Last Name is empty");
                    firstname_TIL.setError(null);
                    email_TIL.setError(null);
                    phoneNumber_TIL.setError(null);
                    password_TIL.setError(null);
                    confirmPassword_TIL.setError(null);
                }else if (email.isEmpty()){
                    email_TIL.setError("Email is empty");
                    firstname_TIL.setError(null);
                    lastName_TIL.setError(null);
                    phoneNumber_TIL.setError(null);
                    password_TIL.setError(null);
                    confirmPassword_TIL.setError(null);
                }else if (phoneNumber.isEmpty()){
                    phoneNumber_TIL.setError("Phone Number is empty");
                    firstname_TIL.setError(null);
                    lastName_TIL.setError(null);
                    email_TIL.setError(null);
                    password_TIL.setError(null);
                    confirmPassword_TIL.setError(null);
                }else if (password.isEmpty()){
                    password_TIL.setError("Password is empty");
                    firstname_TIL.setError(null);
                    lastName_TIL.setError(null);
                    email_TIL.setError(null);
                    phoneNumber_TIL.setError(null);
                    confirmPassword_TIL.setError(null);
                }else if (confirmPassword.isEmpty()){
                    confirmPassword_TIL.setError("Password is empty");
                    firstname_TIL.setError(null);
                    lastName_TIL.setError(null);
                    email_TIL.setError(null);
                    phoneNumber_TIL.setError(null);
                    password_TIL.setError(null);
                }else if (!password_TIET.getText().toString().equals(confirmPassword_TIET.getText().toString())){
                    confirmPassword_TIL.setError("Password is not matched ");
                    firstname_TIL.setError(null);
                    lastName_TIL.setError(null);
                    email_TIL.setError(null);
                    phoneNumber_TIL.setError(null);
                    password_TIL.setError(null);
                }else {
                    firstname_TIL.setError(null);
                    lastName_TIL.setError(null);
                    email_TIL.setError(null);
                    phoneNumber_TIL.setError(null);
                    password_TIL.setError(null);
                    confirmPassword_TIL.setError(null);
                    Registerparams registerparams = new Registerparams();
                    Data data = new Data();
                    data.setEmail(email);
                    data.setPassword(password);
                    data.setConfirmPassword(confirmPassword);
                    data.setFirstName(firstName);
                    data.setLastName(lastName);
                    data.setPhoneNumber(phoneNumber);
                    registerparams.setData(data);
                    registerUser(registerparams);

                }

                break;

            case R.id.signIn_TV:

                Intent signIn_Intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(signIn_Intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);

                break;



        }

    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finishAffinity();
            System.exit(0);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
