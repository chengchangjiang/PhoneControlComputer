package com.example.shea.project;
/////////////////////////////////////////////////////////////
//                     ray's work                          //
//                     2016年5月                           //
/////////////////////////////////////////////////////////////

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private Button btn_take_photo;
    private ImageView photo;
    private String fileName;
    private String mFilePath;
    private Bitmap img = null;
    private FileInputStream fis = null;
    String issameperson = "";
    String percentage = "";
    public String faceid = "";
    private String username = null;
    public ProgressDialog progressDialog = null;
    mytask ts;
    private double latitude = 0;
    private double longitude = 0;
    private String provider;
    private LocationManager locationManager;
    private String locationProvider;
    private double distance=0;
    public static final int SIGN_IN_FAILED=0;
    public static final int SIGN_IN_SUCCESSFULY=1;
    public static final int OUTOFRANGE=2;
    public static final int INTERNET_ERROR=3;
    public static final int DETECTFAILED=4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFilePath = Environment.getExternalStorageDirectory().getPath();
        // 文件名
        mFilePath = mFilePath + "/" + "photo.png";
        System.out.println("-->" + mFilePath);
        btn_take_photo = (Button) this.findViewById(R.id.btn_camera);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        Log.d("------->username", username);
        photo = (ImageView) findViewById(R.id.photo);


        //获取地理位置管理器
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
            return;
        }
        //获取Location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationProvider);
        if(location!=null){
            //不为空,显示地理位置经纬度
            Log.d("showlocation", "开始调用showlocation");
            showLocation(location);
           // Toast.makeText(MainActivity.this,latitude+"\n"+longitude,Toast.LENGTH_SHORT).show();

        }
        //监视地理位置变化
        locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
        btn_take_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO 自动生成的方法存根

                distance=getDistance(latitude,longitude,41.926692,123.404475);
                Log.d("instance",Double.toString(distance));
/*                if (distance>200)
                {
                    Toast.makeText(MainActivity.this,"您当前距离图书馆"+Double.toString(distance)+"米，\n超出范围，签到失败",Toast.LENGTH_SHORT).show();
                }
                else if (distance<=200&&distance>0)
                    Toast.makeText(MainActivity.this,"您当前距离图书馆"+Double.toString(distance)+"米,\n在范围之内，签到成功",Toast.LENGTH_SHORT).show();
               // Toast.makeText(MainActivity.this,"您当前距离图书馆"+Double.toString(distance)+"米",Toast.LENGTH_SHORT).show();*/
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 加载路径
                Uri uri = Uri.fromFile(new File(mFilePath));
                // 指定存储路径，这样就可以保存原图了
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, 1);
            }
        });

    }
    LocationListener locationListener =  new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {
            //如果位置发生变化,重新显示
            showLocation(location);

        }
    };
    private void showLocation(Location location){
        String locationStr = "维度：" + location.getLatitude() +"\n"
                + "经度：" + location.getLongitude();

        latitude = location.getLatitude();
        longitude = location.getLongitude();
/*        sendlocation2server s = new sendlocation2server(latitude,longitude,this);
        s.execute();*/
       Log.d("location info","-->"+locationStr);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        TextView mytv = null;
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                try {
                    fis = new FileInputStream(mFilePath);
                } catch (FileNotFoundException e) {
                    // TODO 自动生成的 catch 块
                    e.printStackTrace();
                }
                // 把流解析成bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                img = bitmap;
                // 设置图片
                System.out.println("==========启动到显示图片============");
                //mytv= (TextView) findViewById(R.id.information);
                //mytask ts= new mytask(mytv,progressDialog);
                ts = new mytask(photo, progressDialog);
                try {
                    int res=ts.execute().get();
                    Log.d("识别结果",""+Integer.toString(res));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                System.out.println("==========显示图片完毕！============");
                photo.setImageBitmap(bitmap);
                Log.i("setimage", "setimage succefully");

                // 关闭流
                try {
                    fis.close();
                } catch (IOException e) {
                    Log.e("IOException", "this is a IOException");
                }

            } else if (requestCode == 11) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                fileName = cursor.getString(columnIndex);
                cursor.close();
                showPhoto(photo, fileName);
            }

        }
    }

    //获取距离方法
    private double getDistance(double lat1,double lng1,double lat2,double lng2 ) {
        double EARTH_RADIUS = 6378137;//地球半径
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);

        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    private static double rad(double d)
    {
        return d * Math.PI / 180.0000;
    }

    private void showPhoto(ImageView photo, String picturePath) {

        // 缩放图片, width, height 按相同比例缩放图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        // options 设为true时，构造出的bitmap没有图片，只有一些长宽等配置信息，但比较快，设为false时，才有图片
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);
        int scale = (int) (options.outWidth / (float) 300);
        if (scale <= 0)
            scale = 1;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(picturePath, options);

        photo.setImageBitmap(bitmap);
        photo.setMaxHeight(350);
    }


    class mytask extends AsyncTask<Integer, Integer, Integer> {
        String faceid = "";
        ImageView imageview;
        ProgressDialog waitingdialog;

        public mytask(ImageView iv, ProgressDialog pd) {
            this.imageview = iv;
            this.waitingdialog = pd;
        }

        @Override
        protected void onPreExecute() {
            //ProgressDialog pd = ProgressDialog.show(Main2Activity.this,"识别中...","");
            //waitingdialog.show(Main2Activity.this,"识别中...","");
            waitingdialog = ProgressDialog.show(MainActivity.this, "识别中...", "");
        }

        @Override
        protected Integer doInBackground(Integer... params) {

            if (distance>300)
                return OUTOFRANGE;

            {
                HttpRequests httpRequests = new HttpRequests("fc5bf2fc197d349f56098f99567dd9fd", "PvLTct4TFIvA03fWrzLJyPQiDICVyp12", true, true);
                //PostParameters postParameters=new PostParameters().setUrl("http://7xrkx0.com1.z0.glb.clouddn.com/t8.jpg");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                float scale = Math.min(1, Math.min(600f / img.getWidth(), 600f / img.getHeight()));
                Matrix matrix = new Matrix();
                matrix.postScale(scale, scale);
                Bitmap imgSmall = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, false);
                imgSmall.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] array = stream.toByteArray();
                try {
                    PostParameters postParameters = new PostParameters().setImg(array);
                    JSONObject result = httpRequests.detectionDetect(postParameters);
                    faceid = result.getJSONArray("face").getJSONObject(0).getString("face_id");
                    Log.d("faceid", "----------------->faceid" + faceid);
                    result = httpRequests.recognitionVerify(new PostParameters().setPersonName(username).setFaceId(faceid));
                    Log.d("result", "结果------------------>" + result);
                    issameperson = result.getString("is_same_person");
                    percentage = result.getString("confidence");
                    Log.d("issameperson", "是否是同一个人？：" + issameperson);
                    Log.d("percentage", "比率：" + percentage);
                    faceid = "是否是同一个人？:" + issameperson + '\n' + "置信度:" + percentage;
                    if (issameperson.equals("true"))
                        return SIGN_IN_SUCCESSFULY;
                } catch (FaceppParseException e) {
                    Log.e("exception", "------------->FaceppParseException");
                    faceid = "发生了网络错误";
                    return INTERNET_ERROR;
                } catch (JSONException e) {
                    Log.e("exception", "------------->JSONException");
                    return DETECTFAILED;
                }
            }
            return SIGN_IN_FAILED;
        }

        @Override
        protected void onPostExecute(Integer s) {
            waitingdialog.cancel();
            switch (s)
            {
                case 0:{Toast.makeText(MainActivity.this,"签到失败，请重试",Toast.LENGTH_SHORT).show();}break;
                case 1:{Toast.makeText(MainActivity.this,"签到成功",Toast.LENGTH_SHORT).show();}break;
                case 2:{Toast.makeText(MainActivity.this,"签到失败，您不在范围区域内",Toast.LENGTH_SHORT).show();}break;
                case 3:{Toast.makeText(MainActivity.this,"网络错误...",Toast.LENGTH_SHORT).show();}break;
                case 4:{Toast.makeText(MainActivity.this,"未检测到人脸...请重试",Toast.LENGTH_SHORT).show();}break;
            }
            /* if (faceid != null) {//textView.setText(faceid);
               new AlertDialog.Builder(MainActivity.this).setMessage(faceid).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        imageview.setVisibility(View.INVISIBLE);
                    }
                }).setTitle("检测结果").show();


            } else
                System.out.println("------------>输出信息为空");*/
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(MainActivity.this, LoginAcitvity.class);
        startActivity(intent);
    }

}