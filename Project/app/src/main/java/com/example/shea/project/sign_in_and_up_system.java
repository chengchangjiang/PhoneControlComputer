package com.example.shea.project;
/////////////////////////////////////////////////////////////
//                     ray's work                          //
//                     2016年5月                           //
/////////////////////////////////////////////////////////////
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

/**
 * Created by shea on 2016/5/23.
 */
public class sign_in_and_up_system extends AsyncTask <Void,Void,String>{
    private final static int LOGIN=1;
    private final static int SIGNUP=0;
    private final static int CHANGEPSD=2;
    private ProgressDialog progressDialog;
    private String password="";
    private String id="";
    private Context mycontext;
    private int myjudge;
    public  sign_in_and_up_system(String getID,String getPSD,Context context,int judge){
        this.id=getID;
        this.password=getPSD;
        mycontext=context;
        this.myjudge=judge;
    }
    @Override
    protected void onPreExecute() {
        progressDialog=ProgressDialog.show(mycontext,"正在登录中~","");

    }

    @Override
    protected String doInBackground(Void... params) {

        Map<String, String> params1 = new HashMap<String, String>();
        params1.put("username", id);
        params1.put("passwd", password);
        StringBuilder data = new StringBuilder();
        if (params1 != null && !params1.isEmpty()) {
            for (Map.Entry<String, String> entry : params1.entrySet()) {
                data.append(entry.getKey()).append("=");
                data.append(entry.getValue()).append("&");
            }
            data.deleteCharAt(data.length() - 1);
        }
        byte[] entiy = data.toString().getBytes(); // 生成实体数据
        System.out.println("-->>" + entiy.toString());
        if (myjudge==SIGNUP)
        {
            try {
                URL url=new URL("http://121.42.40.96:8080/login/insert");
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpURLConnection.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
                httpURLConnection.setRequestProperty("Charset", "UTF-8");
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setInstanceFollowRedirects(true);
                httpURLConnection.connect();
                OutputStream os = httpURLConnection.getOutputStream();
                Log.d("---------->传送结束", "<------------");
                os.write(entiy);
                os.flush();
                os.close();
                httpURLConnection.getInputStream();Log.d("---------->传送结束2", "<------------");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String result="";
                String readline;
                while((readline = bufferedReader.readLine())!=null) {
                    result +=readline;
                    Log.d("------->output",readline);
                    return readline;
                }
            } catch (MalformedURLException e) {
                Log.e("-------->错误调试","MalformedURLException，联网失败");
                return "3";
            }
            catch (ProtocolException e) {
                e.printStackTrace();
                return "3";
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }else if (myjudge==LOGIN)
        {
            try {
                URL url=new URL("http://121.42.40.96:8080/login/Logincl");
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpURLConnection.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
                httpURLConnection.setRequestProperty("Charset", "UTF-8");
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setInstanceFollowRedirects(true);
                httpURLConnection.connect();
                OutputStream os = httpURLConnection.getOutputStream();
                Log.d("---------->传送结束", "<------------");
                os.write(entiy);
                os.flush();
                os.close();
                httpURLConnection.getInputStream();Log.d("---------->传送结束2", "<------------");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String result="";
                String readline;
                while((readline = bufferedReader.readLine())!=null) {
                    result +=readline;
                    Log.d("------->output",readline);
                    return readline;
                }
            } catch (MalformedURLException e) {
                Log.e("-------->错误调试","MalformedURLException，联网失败");
                return "3";
            }
            catch (ProtocolException e) {
                e.printStackTrace();
                return "3";
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if (myjudge==CHANGEPSD)
        {
            try {
                URL url=new URL("http://121.42.40.96:8080/login/update");
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpURLConnection.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
                httpURLConnection.setRequestProperty("Charset", "UTF-8");
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setInstanceFollowRedirects(true);
                httpURLConnection.connect();
                OutputStream os = httpURLConnection.getOutputStream();
                Log.d("---------->传送结束", "<------------");
                os.write(entiy);
                os.flush();
                os.close();
                httpURLConnection.getInputStream();Log.d("---------->传送结束2", "<------------");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String result="";
                String readline;
                while((readline = bufferedReader.readLine())!=null) {
                    result +=readline;
                    Log.d("------->output",readline);
                    return readline;
                }
            } catch (MalformedURLException e) {
                Log.e("-------->错误调试","MalformedURLException，联网失败");
                return "3";
            }
            catch (ProtocolException e) {
                e.printStackTrace();
                return "3";
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return "";

    }

    @Override
    protected void onPostExecute(String s) {
        progressDialog.cancel();
    }
}
