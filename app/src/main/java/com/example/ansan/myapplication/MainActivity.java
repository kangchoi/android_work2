package com.example.ansan.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et=(EditText)findViewById(R.id.editText);

    }
    public void  onClick(View v){
        if (isStoragePermissionGranted()==false){

            Toast.makeText(getApplicationContext(),"SD Card 사용 불가",Toast.LENGTH_SHORT).show();

            return;
        }

        String path= Environment.getExternalStorageDirectory().getAbsolutePath();//SDCard 위치 가져오기
        String folder=path+"/mysoobinDir";
        String filename=folder+"/mysoo.txt";

        File myfolder=new File(folder);

        switch (v.getId()){
            case R.id.button://폴더생성
                myfolder.mkdir();
                Toast.makeText(getApplicationContext(),"폴더 생성",Toast.LENGTH_SHORT).show();
                break;

            case R.id.button2://폴더삭제
                myfolder.delete();
                Toast.makeText(getApplicationContext(),"폴더 삭제",Toast.LENGTH_SHORT).show();
                break;

            case R.id.button3://파일생성
                try {
                    FileOutputStream fos=new FileOutputStream(filename);
                    String str="Hello";
                    fos.write(str.getBytes());
                    fos.close();

                    Toast.makeText(getApplicationContext(),"파일 생성",Toast.LENGTH_SHORT).show();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(),"파일 생성 실패",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                break;

            case R.id.button4://파일 읽기
                try {
                    FileInputStream fis=new FileInputStream(filename);
                    byte arr[]=new byte[fis.available()];
                    fis.read(arr);
                    fis.close();

                    Toast.makeText(getApplicationContext(),new String(arr),Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(),"파일 읽기 실패",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                break;

            case R.id.button5://파일 목록가져오기
                File filelist[]=new File(path).listFiles();//모든 파일들을 다가져옴
                String str="";
                for(int i=0; i<filelist.length; i++){
                    if(filelist[i].isDirectory())
                        str+="<폴더>"+filelist[i].toString()+"\n";
                    else
                        str+="<파일>"+filelist[i].toString()+"\n";
                }
                et.setText(str);
                break;
        }
    }

    String TAG="TEST";

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }
}
