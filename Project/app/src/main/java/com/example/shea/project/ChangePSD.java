package com.example.shea.project;
/////////////////////////////////////////////////////////////
//                     ray's work                          //
//                     2016年5月                           //
/////////////////////////////////////////////////////////////

import android.app.ProgressDialog;
import android.content.Intent;
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

import java.util.concurrent.ExecutionException;

public class ChangePSD extends AppCompatActivity implements View.OnClickListener {
    private Button ConfirmBTN;
    private EditText InputUsername;
    private EditText InputPassword;
    private EditText InputPassword2;
    private TextView back2;
    public static final int CHANGE_PSD_FAILED=0;
    public static final int CHANGE_PSD_SUCCESSFULY=1;
    public static final int INTERNET_ERROR=3;
    public static final int INCORRECT_PASSWORD=0;
    public static final int LOG_IN_SUCCESSFULY=1;
    public static final int USERIDNOTFOUND=2;
    private String Password = "";
    private String Password2 = "";
    private String Userid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        back2= (TextView) findViewById(R.id.back2);
        InputUsername= (EditText) findViewById(R.id.changePSDusername);
        InputPassword= (EditText) findViewById(R.id.changePSDpassword);
        InputPassword2= (EditText) findViewById(R.id.changePSDnewpassword);
        ConfirmBTN= (Button) findViewById(R.id.confirm);
        ConfirmBTN.setOnClickListener(this);
        back2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String finalresult=null;
        switch (v.getId())
        {
            case R.id.back2:
            {
                Intent intent=new Intent(ChangePSD.this,LoginAcitvity.class);
                finish();
                startActivity(intent);
            }break;
            case R.id.confirm:
            {
                Userid=InputUsername.getText().toString();
                Password=InputPassword.getText().toString();
                Password2=InputPassword2.getText().toString();
                if (Userid.isEmpty()||Password.isEmpty()||Password2.isEmpty())
                {
                    if (Userid.isEmpty())
                        Toast.makeText(ChangePSD.this,"请输入账户",Toast.LENGTH_SHORT).show();
                    if (Password.isEmpty())
                        Toast.makeText(ChangePSD.this,"请输入密码",Toast.LENGTH_SHORT).show();
                    if (Password2.isEmpty())
                        Toast.makeText(ChangePSD.this,"请输入新密码",Toast.LENGTH_SHORT).show();
                    Log.d("user message","userid:"+Userid+"\npassword"+Password);
                    break;
                }
                if (Password2.length()<6)
                    if (Password.length()<6)
                    {
                        Toast.makeText(ChangePSD.this,"密码位数太短！请输入一个大于六位的密码",Toast.LENGTH_SHORT).show();
                    }
                sign_in_and_up_system signin=new sign_in_and_up_system(Userid,Password,ChangePSD.this,1);
                try {
                    String result=signin.execute().get();
                    if (result.isEmpty())
                    {Toast.makeText(ChangePSD.this,"无网络连接",Toast.LENGTH_SHORT).show();
                        break;}

                    Log.d("--------->", "账户密码均存在" + result);
                    switch (Integer.valueOf(result))
                    {
                        case INCORRECT_PASSWORD:
                            Toast.makeText(ChangePSD.this,"密码错误，请输入正确的密码",Toast.LENGTH_SHORT).show();
                            break;
                        case INTERNET_ERROR:
                            Toast.makeText(ChangePSD.this,"网络连接错误！请检查网络连接",Toast.LENGTH_SHORT).show();
                            break;
                        case USERIDNOTFOUND:
                            Toast.makeText(ChangePSD.this,"该帐号不存在",Toast.LENGTH_SHORT).show();
                        case LOG_IN_SUCCESSFULY:
                        {
                            sign_in_and_up_system changepsd=new sign_in_and_up_system(Userid,Password2,ChangePSD.this,2);
                            finalresult=changepsd.execute().get();
                            Log.d("finalresult---->",finalresult);
                            Toast.makeText(ChangePSD.this,"登录成功",Toast.LENGTH_SHORT).show();

                        }
                        break;
                        default:Toast.makeText(ChangePSD.this,"未知错误",Toast.LENGTH_SHORT).show();
                    }
                    switch (Integer.valueOf(finalresult))
                    {
                        case LOG_IN_SUCCESSFULY:
                        {
                            Intent intent = new Intent(ChangePSD.this, MainActivity.class);

                            intent.putExtra("username",Userid);
                            finish();
                            startActivity(intent);
                        }break;
                        case INCORRECT_PASSWORD:
                            Toast.makeText(ChangePSD.this,"修改失败",Toast.LENGTH_SHORT).show();
                            break;
                        case INTERNET_ERROR:
                            Toast.makeText(ChangePSD.this,"网络连接错误！请检查网络连接",Toast.LENGTH_SHORT).show();
                            break;
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
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

    @Override
    public void onBackPressed() {
        finish();
    }

    class sendlocation extends AsyncTask<Void,Void,Integer>
    {
        @Override
        protected Integer doInBackground(Void... params) {
            return null;
        }
    }
}
