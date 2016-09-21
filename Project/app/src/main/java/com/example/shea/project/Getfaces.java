package com.example.shea.project;
/////////////////////////////////////////////////////////////
//                     ray's work                          //
//                     2016年5月                           //
/////////////////////////////////////////////////////////////

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Getfaces extends AppCompatActivity implements View.OnClickListener {
    private int count=0;
    private final  static int PHOTONUMBERS=4;
    private Button getfaces;
    private ImageView photo;
    private String fileName;
    private String mFilePath;
    private Bitmap img = null;
    private String username;
    private FileInputStream fis = null;
    public ProgressDialog progressDialog=null;
    private ImageView PHOTO1;
    private ImageView PHOTO2;
    private ImageView PHOTO3;
    private ImageView PHOTO4;
    private ImageView PHOTO5;
    private List<ImageView> imageViewList=new ArrayList<>();
    public static final int INTERNET_ERROR=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getfaces);
        PHOTO1= (ImageView) findViewById(R.id.photo1);
        PHOTO2= (ImageView) findViewById(R.id.photo2);
        PHOTO3= (ImageView) findViewById(R.id.photo3);
        PHOTO4= (ImageView) findViewById(R.id.photo4);
        PHOTO5= (ImageView) findViewById(R.id.photo5);
        imageViewList.add(0,PHOTO1);
        imageViewList.add(1,PHOTO2);
        imageViewList.add(2,PHOTO3);
        imageViewList.add(3,PHOTO4);
        imageViewList.add(4,PHOTO5);
        getfaces= (Button) findViewById(R.id.GetFaces);
        Resources res=getResources();
        Bitmap addpiacture=BitmapFactory.decodeResource(res,R.drawable.add_photo1);
        PHOTO1.setImageBitmap(addpiacture);
        PHOTO2.setImageBitmap(addpiacture);
        PHOTO3.setImageBitmap(addpiacture);
        PHOTO4.setImageBitmap(addpiacture);
        PHOTO5.setImageBitmap(addpiacture);
        PHOTO2.setVisibility(View.INVISIBLE);
        PHOTO3.setVisibility(View.INVISIBLE);
        PHOTO4.setVisibility(View.INVISIBLE);
        PHOTO5.setVisibility(View.INVISIBLE);
        getfaces.setOnClickListener(this);
        PHOTO1.setOnClickListener(this);
        PHOTO2.setOnClickListener(this);
        PHOTO3.setOnClickListener(this);
        PHOTO4.setOnClickListener(this);
        PHOTO5.setOnClickListener(this);
        Intent getdataintent=getIntent();
        username=getdataintent.getStringExtra("username");
        Log.d("getdataintent",username+"");

    }

    @Override
    public void onClick(View v) {
        Resources res=getResources();
        Bitmap addpiacture=BitmapFactory.decodeResource(res,R.drawable.add);
        switch (v.getId())
        {
            case R.id.GetFaces:
            {

                Intent intent=new Intent(Getfaces.this,MainActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }break;
            case R.id.photo1:
            {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 加载路径
                mFilePath = Environment.getExternalStorageDirectory().getPath();
                mFilePath = mFilePath + "/" + "photo1.png";
                Uri uri = Uri.fromFile(new File(mFilePath));
                // 指定存储路径，这样就可以保存原图了
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, 0);
                PHOTO2.setVisibility(View.VISIBLE);
            }break;
            case R.id.photo2:{

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 加载路径
                mFilePath = Environment.getExternalStorageDirectory().getPath();
                mFilePath = mFilePath + "/" + "photo2.png";
                Uri uri = Uri.fromFile(new File(mFilePath));
                // 指定存储路径，这样就可以保存原图了
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                startActivityForResult(intent, 1);
                PHOTO3.setVisibility(View.VISIBLE);

            }break;
            case R.id.photo3:{

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 加载路径
                mFilePath = Environment.getExternalStorageDirectory().getPath();
                mFilePath = mFilePath + "/" + "photo3.png";
                Uri uri = Uri.fromFile(new File(mFilePath));
                // 指定存储路径，这样就可以保存原图了
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, 2);
                PHOTO4.setVisibility(View.VISIBLE);
            }break;
            case R.id.photo4:{
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 加载路径
                mFilePath = Environment.getExternalStorageDirectory().getPath();
                mFilePath = mFilePath + "/" + "photo4.png";
                Uri uri = Uri.fromFile(new File(mFilePath));
                // 指定存储路径，这样就可以保存原图了
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, 3);
                PHOTO5.setVisibility(View.VISIBLE);
            }break;
            case R.id.photo5:{
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 加载路径
                mFilePath = Environment.getExternalStorageDirectory().getPath();
                mFilePath = mFilePath + "/" + "photo4.png";
                Uri uri = Uri.fromFile(new File(mFilePath));
                // 指定存储路径，这样就可以保存原图了
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent,4);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {

            try {
                Log.d("----------->",""+requestCode);
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
            detectface detectface1=new detectface(progressDialog,img,username);
            try {
                int res=detectface1.execute().get();
                if (res==1)
                {imageViewList.get(requestCode).setImageBitmap(bitmap);
                    Log.d("debuginfo",username);
                    Toast.makeText(Getfaces.this,"检测到人脸！",Toast.LENGTH_SHORT).show();
                }else if (res==0)
                {
                    Toast.makeText(Getfaces.this,"未检测到人脸，请重新添加",Toast.LENGTH_SHORT).show();
                    imageViewList.get(requestCode+1).setVisibility(View.INVISIBLE);
                }
                else if (res==3)
                {
                    Toast.makeText(Getfaces.this,"网络错误",Toast.LENGTH_SHORT).show();
                }
                /*else if (res==4)
                    Toast.makeText(Getfaces.this,"未知错误",Toast.LENGTH_SHORT).show();*/

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            System.out.println("==========显示图片完毕！============");

            Log.i("setimage", "setimage succefully");

            // 关闭流
            try {
                fis.close();
            } catch (IOException e) {
                Log.e("IOException", "this is a IOException");
            }
        }
    }

    class detectface extends AsyncTask <Void,Void,Integer>
    {
        private Bitmap image;
        String faceid="";
        String personname="";
        ProgressDialog waitingdialog;
        public detectface(ProgressDialog pd,Bitmap bitmap,String username){
            waitingdialog=pd;
            image=bitmap;
            personname=username;
        }

        @Override
        protected void onPreExecute() {
            waitingdialog=ProgressDialog.show(Getfaces.this,"识别中...","");

        }

        @Override
        protected Integer doInBackground(Void... params) {
            Log.d("doInBackground","开始调用");
            HttpRequests httpRequests=new HttpRequests("fc5bf2fc197d349f56098f99567dd9fd","PvLTct4TFIvA03fWrzLJyPQiDICVyp12",true,true);
            //PostParameters postParameters=new PostParameters().setUrl("http://7xrkx0.com1.z0.glb.clouddn.com/t8.jpg");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            float scale = Math.min(1, Math.min(600f / image.getWidth(), 600f / image.getHeight()));
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            Bitmap imgSmall = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, false);
            imgSmall.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] array = stream.toByteArray();
            PostParameters postParameters=new PostParameters().setImg(array);
            try {
                JSONObject result=httpRequests.detectionDetect(postParameters);
                faceid=result.getJSONArray("face").getJSONObject(0).getString("face_id");
                httpRequests.personAddFace(new PostParameters().setPersonName(username).setFaceId(faceid));
                httpRequests.trainVerify(new PostParameters().setPersonName(username));
                Log.d("faceid--------->",result.toString());
                if (!faceid.isEmpty())
                    return 1;
                else return 0;
            } catch (FaceppParseException e) {
                return INTERNET_ERROR;
            } catch (JSONException e) {
                Log.e("error---->","json解析错误");
                return 0;
            }

        }

        @Override
        protected void onPostExecute(Integer integer) {
            //new AlertDialog.Builder(Getfaces.this);
            waitingdialog.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
