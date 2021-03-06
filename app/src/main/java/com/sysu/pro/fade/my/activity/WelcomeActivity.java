package com.sysu.pro.fade.my.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sysu.pro.fade.MainActivity;
import com.sysu.pro.fade.R;
import com.sysu.pro.fade.utils.Const;
/*
app启动时的欢迎界面
 */
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPreferences = getSharedPreferences(Const.USER_SHARE,MODE_PRIVATE);
        String login_type = sharedPreferences.getString(Const.LOGIN_TYPE,"");

        if(login_type == ""){
            startActivity(new Intent(WelcomeActivity.this,GuideActivity.class));
            finish();
        }else{
            startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
            finish();
        }
//        startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
//        finish();
    }
}
