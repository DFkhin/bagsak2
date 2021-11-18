package com.example.loginui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.opengl.EGLObjectHandle;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginui.api.RequestPlaceholder;
import com.example.loginui.api.RetrofitBuilder;
import com.example.loginui.pojos.Login;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    public MaterialButton loginBtn;
    public EditText username, password;
    public TextView forgotpassword, register;
    public RetrofitBuilder retrofitBuilder;
    public RequestPlaceholder requestPlaceholder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = (MaterialButton) findViewById(R.id.loginBtn);
        username = (EditText) findViewById(R.id.username);
        password= (EditText) findViewById(R.id.password);
        forgotpassword = (TextView) findViewById(R.id.forgotpassword);
        register = (TextView) findViewById(R.id.register);


        retrofitBuilder = new RetrofitBuilder();
        requestPlaceholder = retrofitBuilder.getRetrofit().create(RequestPlaceholder.class);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(username.getText()!=null  && password.getText()!=null){
                    Call<Login> loginCall = requestPlaceholder.login(new Login(null, username.getText().toString(), null, null, password.getText().toString()));

                    loginCall.enqueue(new Callback<Login>() {
                        @Override
                        public void onResponse(Call<Login> call, Response<Login> response) {
                            if(!response.isSuccessful()){
                                if(response.code() == 404){
                                    Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                                    Log.e("LOGGING_ERR", response.message());
                                }else{
                                    Toast.makeText(LoginActivity.this, "There is an error upon logging in the API", Toast.LENGTH_SHORT).show();
                                    Log.e("LOGGING_ERR", response.message());
                                }

                            }else{
                                if(response.code() == 200){
                                    Login loginResponse = response.body();
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);

                                    intent.putExtra("USER_ID", loginResponse.getId());
                                    intent.putExtra("USERNAME", loginResponse.getUsername());
                                    intent.putExtra("TOKEN", loginResponse.getToken());

                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Login> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "There is an error upon logging in the API", Toast.LENGTH_SHORT).show();
                            Log.e("LOGGING_ERR", t.getMessage());
                        }
                    });

                }else{
                    Toast.makeText(LoginActivity.this, "Please supply all fields to login!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}