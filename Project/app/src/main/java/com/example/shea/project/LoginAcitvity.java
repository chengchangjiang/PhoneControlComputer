package com.example.shea.project;
/////////////////////////////////////////////////////////////
//                     ray's work                          //
//                     2016年5月                           //
/////////////////////////////////////////////////////////////

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class LoginAcitvity extends AppCompatActivity implements View.OnClickListener {
    private String Password = "";
    private String Userid = "";
    private EditText InputPSD;
    private EditText InputID;
    private Button SigninBTN;
    private TextView ChangePSD;
    private TextView SignUP;
    private ProgressDialog progressDialog;
    public static final int INCORRECT_PASSWORD=0;
    public static final int LOG_IN_SUCCESSFULY=1;
    public static final int INTERNET_ERROR=3;
    public static final int USERIDNOTFOUND=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        InputID = (EditText) findViewById(R.id.sign_in_id);
        InputPSD= (EditText) findViewById(R.id.sign_in_psd);
        SigninBTN= (Button) findViewById(R.id.sign_in_btn);
        assert SigninBTN != null;
        SigninBTN.setOnClickListener(this);
        //SigninBTN.setBackgroundColor(Color.parseColor("#ff9357"));
        SignUP= (TextView) findViewById(R.id.SignUp);
        ChangePSD= (TextView) findViewById(R.id.changePSD);
        SignUP.setOnClickListener(this);
        ChangePSD.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_btn: {

                Password = InputPSD.getText().toString();
                Userid = InputID.getText().toString();
                Log.i("用户信息\n","userid:"+Userid+"\npassword"+Password);
                if (Password.isEmpty()||Userid.isEmpty())
                {
                    if (Userid.isEmpty())
                        Toast.makeText(LoginAcitvity.this,"请输入账号",Toast.LENGTH_SHORT).show();
                    else if (Password.isEmpty())
                        Toast.makeText(LoginAcitvity.this,"请输入密码",Toast.LENGTH_SHORT).show();
                    break;
                }
                sign_in_and_up_system signin=new sign_in_and_up_system(Userid,Password,LoginAcitvity.this,1);
                String result= null;
                try {
                    result = signin.execute().get();
                    if (result.isEmpty())
                    {Toast.makeText(LoginAcitvity.this,"无网络连接",Toast.LENGTH_SHORT).show();
                        break;}

                    Log.d("--------->", "账户密码均存在" + result);
                    switch (Integer.valueOf(result))
                    {
                        case INCORRECT_PASSWORD:
                            Toast.makeText(LoginAcitvity.this,"密码错误，请输入正确的密码",Toast.LENGTH_SHORT).show();
                            break;
                        case INTERNET_ERROR:
                            Toast.makeText(LoginAcitvity.this,"网络连接错误！请检查网络连接",Toast.LENGTH_SHORT).show();
                            break;
                        case USERIDNOTFOUND:
                            Toast.makeText(LoginAcitvity.this,"该帐号不存在",Toast.LENGTH_SHORT).show();
                            break;
                        case LOG_IN_SUCCESSFULY:
                        {
                            Toast.makeText(LoginAcitvity.this,"登录成功",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginAcitvity.this, MainActivity.class);
                            intent.putExtra("username",Userid);
                            finish();
                            startActivity(intent);
                        }
                        break;
                        default:Toast.makeText(LoginAcitvity.this,"未知错误",Toast.LENGTH_SHORT).show();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            break;
            case R.id.changePSD: {
                Intent intent=new Intent(LoginAcitvity.this,ChangePSD.class);
                startActivity(intent);

            }
            break;
            case R.id.SignUp:{
                Intent intent=new Intent(LoginAcitvity.this,SignupActivity.class);
                startActivity(intent);
            }break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(null != this.getCurrentFocus()){
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super .onTouchEvent(event);
    }
}
