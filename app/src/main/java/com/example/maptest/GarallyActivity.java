package com.example.maptest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Locale;

public class GarallyActivity extends AppCompatActivity {

    private static final int RESULT_PICK_IMAGEFILE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garally);


        //写真を一覧で表示
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        //Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);     //画像の複数選択を可能にする
        intent.setType("image/*");

        startActivityForResult(intent, RESULT_PICK_IMAGEFILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        super.onActivityResult(requestCode, resultCode, resultData);

        if (requestCode == RESULT_PICK_IMAGEFILE && resultCode == Activity.RESULT_OK) {
            if(resultData.getData() != null){

                //ファイルを指す
                ParcelFileDescriptor pfDescriptor = null;

                try{
                    //コンテンツURIを取得
                    Uri contentUri = resultData.getData();

                    // Uriを表示
                    TextView tv_uri = findViewById(R.id.tv_uri);
                    tv_uri.setText(String.format(Locale.US, "Uri:　%s",contentUri.toString()));

                    Log.d("contentUri", "URI=" + tv_uri.getText());

                    //ContentResolver:コンテンツモデルへのアクセスを提供
                    ContentResolver contentResolver = getContentResolver();

                    //URI下のデータにアクセスする
                    pfDescriptor = contentResolver.openFileDescriptor(contentUri, "r");
                    if(pfDescriptor != null){

                        //実際のFileDescriptorを取得
                        FileDescriptor fileDescriptor = pfDescriptor.getFileDescriptor();
                        Bitmap bmp = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                        pfDescriptor.close();

                        ImageView imageView = findViewById(R.id.imageView);
                        imageView.setImageBitmap(bmp);
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                } finally {
                    try{
                        if(pfDescriptor != null){
                            pfDescriptor.close();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        }
    }


}