package com.example.shea.project;
/////////////////////////////////////////////////////////////
//                     ray's work                          //
//                     2016年5月                           //
/////////////////////////////////////////////////////////////

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

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

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    private Button SignUpBTN;
    private EditText InputUsername;
    private EditText InputPassword;
    private EditText InputPassword2;
    private TextView back;
    public static final int SIGN_UP_FAILED=0;
    public static final int SIGN_UP_SUCCESSFULY=1;
    public static final int INTERNET_ERROR=3;
    private String Password = "";
    private String Password2 = "";
    private String Userid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        SignUpBTN= (Button) findViewById(R.id.sign_up_btn);
        InputUsername= (EditText) findViewById(R.id.sign_up_id);
        InputPassword= (EditText) findViewById(R.id.sign_up_psd);
        InputPassword2= (EditText) findViewById(R.id.sign_up_psd2);
        back= (TextView) findViewById(R.id.back1);
        SignUpBTN.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back1:
            {
                Intent intent=new Intent(SignupActivity.this,LoginAcitvity.class);
                finish();
                startActivity(intent);
            }
            break;
            case R.id.sign_up_btn:
            {
                Userid=InputUsername.getText().toString();
                Password=InputPassword.getText().toString();
                Password2=InputPassword2.getText().toString();
                if (Userid.isEmpty()||Password.isEmpty()||Password2.isEmpty())
                {
                    if (Userid.isEmpty())
                        Toast.makeText(SignupActivity.this,"请输入账户",Toast.LENGTH_SHORT).show();
                    if (Password.isEmpty())
                        Toast.makeText(SignupActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
                    if (Password2.isEmpty())
                        Toast.makeText(SignupActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
                    Log.d("user message","userid:"+Userid+"\npassword"+Password);
                    break;
                }
                if (!Password.equals(Password2))
                { Toast.makeText(SignupActivity.this,"两次密码不一致，请重新输入",Toast.LENGTH_SHORT).show();
                break;}
                if (Password.length()<6)
                {
                    Toast.makeText(SignupActivity.this,"密码位数太短！请输入一个大于六位的密码",Toast.LENGTH_SHORT).show();
                    break;
                }
                sign_in_and_up_system test=new sign_in_and_up_system(Userid,Password,SignupActivity.this,0);

                try {
                    String result=test.execute().get();
                    if (result.isEmpty())
                    {Toast.makeText(SignupActivity.this,"无网络连接",Toast.LENGTH_SHORT).show();
                        break;}
                    Log.d("result",result);
                    switch (Integer.valueOf(result))
                    {
                        case SIGN_UP_FAILED:
                            Toast.makeText(SignupActivity.this,"账号已存在",Toast.LENGTH_SHORT).show();
                            break;
                        case INTERNET_ERROR:
                            Toast.makeText(SignupActivity.this,"网络连接错误！请检查网络连接",Toast.LENGTH_SHORT).show();
                            break;
                        case SIGN_UP_SUCCESSFULY:
                        {
                            Toast.makeText(SignupActivity.this,"注册成功！已登录",Toast.LENGTH_SHORT).show();
                            Addpreson addpreson=new Addpreson(Userid);
                            addpreson.execute();
                            Intent intent = new Intent(SignupActivity.this, Getfaces.class);
                            intent.putExtra("username",Userid);
                            finish();
                            startActivity(intent);
                        }
                        break;
                        default:Toast.makeText(SignupActivity.this,"未知错误",Toast.LENGTH_SHORT).show();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
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

    class Addpreson extends AsyncTask<Void,Void,Integer>
    {
        private String personname="";
        private String groupname="faces";
        public Addpreson(String name){
            this.personname=name;
            Log.d("debuginfo",personname);

        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            HttpRequests httpRequests=new HttpRequests("fc5bf2fc197d349f56098f99567dd9fd","PvLTct4TFIvA03fWrzLJyPQiDICVyp12",true,true);
            PostParameters postParameters=new PostParameters().setPersonName(personname).setGroupName("faces");
            try {
                Log.d("debug-personname",personname);
                //JSONObject result=httpRequests.personCreate(new PostParameters().setGroupId(groupname).setPersonName(personname));
                JSONObject result=httpRequests.personCreate(postParameters);
                String person_id=result.getString("person_id");
                Log.d("是否成功加入了该个人",person_id);
            } catch (FaceppParseException e) {
                Log.e("error","FaceppParseException");
            } catch (JSONException e) {
                Log.e("error","JSONException");
            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
