package com.example.shea.project;

import android.content.Context;
import android.location.Location;
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
 * Created by shea on 2016/5/28.
 */
public class sendlocation2server extends AsyncTask<Void,Void,Integer> {
    private double latitude;
    private double longitude;
    private Context mycontext;
    @Override
    protected Integer doInBackground(Void... params) {
        Map<String, Double> params1 = new HashMap<String, Double>();
        params1.put("latitude", latitude);
        params1.put("longitude", longitude);

        StringBuilder data = new StringBuilder();
        if (params1 != null && !params1.isEmpty()) {
            for (Map.Entry<String, Double> entry : params1.entrySet()) {
                data.append(entry.getKey()).append("=");
                data.append(entry.getValue()).append("&");
            }
            data.deleteCharAt(data.length() - 1);
        }
        byte[] entiy = data.toString().getBytes(); // 生成实体数据
        System.out.println("-->>" + entiy.toString());
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

                return Integer.valueOf(readline);
            }
        } catch (MalformedURLException e) {
            Log.e("-------->错误调试","MalformedURLException，联网失败");
            return 3;
        }
        catch (ProtocolException e) {
            e.printStackTrace();
            return 3;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 3;

    }
    public  sendlocation2server(double latitude, double longitude, Context context){
        this.latitude=latitude;
        this.longitude=longitude;
        mycontext=context;
    }
}
