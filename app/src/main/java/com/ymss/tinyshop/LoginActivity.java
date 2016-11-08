package com.ymss.tinyshop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ymss.keyboard.KeyboardTool;

public class LoginActivity extends AppCompatActivity {

    EditText userName;
    EditText passWord;
    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //ColorDrawable dr = new ColorDrawable(Color.argb(255,0,255,0));
        //this.getWindow().setBackgroundDrawable(dr);
       // KeyboardTool tool = new KeyboardTool(this.getApplicationContext(),this);
       // tool.setKeyboardTypeById(R.id.user_name,0);
        userName = (EditText)findViewById(R.id.user_name);
        passWord = (EditText)findViewById(R.id.pass_word);
        mSharedPreferences = getSharedPreferences("TestSharedPreferences", 0);
        String name = mSharedPreferences.getString("userName","");
        String password = mSharedPreferences.getString("passWord","");
        userName.setText(name);
        passWord.setText(password);

        userName.setSelection(name.length());
        passWord.setSelection(password.length());

        Button btnLogin = (Button) findViewById(R.id.login_start);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userName.getText().length() > 1 && passWord.getText().length() > 1) {
                    mSharedPreferences = getSharedPreferences("TestSharedPreferences", 0);
                    SharedPreferences.Editor mEditor = mSharedPreferences.edit();
                    mEditor.putString("userName", userName.getText().toString());
                    mEditor.putString("passWord", passWord.getText().toString());
                    mEditor.commit();
                    Intent intent =new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();
                } else{
                    Toast.makeText(v.getContext(),"请输入用户名或者密码",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
