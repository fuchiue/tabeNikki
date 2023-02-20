package com.example.tabenikki;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class CreateRestaurantActivity extends AppCompatActivity {
    SharedPreferences data;
    SharedPreferences.Editor editor;
    String ListImg;
    String ListName;
    ArrayList<String> RimgList; //カテゴリデータを保存するArrayList
    ArrayList<String> RnameList; //カテゴリデータを保存するArrayList

    EditText RestNewName;
    ImageView RestNewImg;

    Uri newImagURI;

    //画像をライブラリから取得するために必要
    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent resultData  = result.getData();
                    if (resultData  != null) {
                        openImage(resultData);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);

        // 他のアクティビティからデータを受け取るためにIntentを取得
        Intent GetCategoryIntent = getIntent();
        // 送信されたデータを取得
        String GetCategory = GetCategoryIntent.getStringExtra("CategoryKey");
        // タイトル設定
        setTitle("店舗登録");
        //SharedPreferencesの生成
        data = getSharedPreferences("Data", MODE_PRIVATE);
        //入力用editor生成
        editor = data.edit();
        ////リファレンスに保存する名前を作成
        ListImg = GetCategory+"Rimg";
        ListName = GetCategory+"Rname";

        ////リファレンスからデータを取得
        //Imgデータ取得
        String onString = data.getString(ListImg, "");
        //変換してコンマ区切りになったStringデータをアレイリストに変換
        RimgList = new ArrayList<>(Arrays.asList(onString.split("\\s*,\\s*")));

        //nameデータ取得
        onString = data.getString(ListName, "");
        //変換してコンマ区切りになったStringデータをアレイリストに変換
        RnameList = new ArrayList<>(Arrays.asList(onString.split("\\s*,\\s*")));
        //Listの０番目に空白がある場合削除する
        if(RimgList.get(0).equals("")){
            RimgList.remove(0);
        }
        if(RnameList.get(0).equals("")){
            RnameList.remove(0);
        }

        //部品の宣言
        RestNewImg = findViewById(R.id.RestNewImg);
        ImageButton SelectImgButton = findViewById(R.id.SelectImgButton);
        RestNewName = findViewById(R.id.RestNewName);
        Button RestNewEntryButton = findViewById(R.id.RestNewEntryButton);


        //イメージボタンが押されたとき写真を選ぶ
        SelectImgButton.setOnClickListener( v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");

            resultLauncher.launch(intent);
        });

        RestNewEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = RestNewName.getText().toString();
                if(!newName.equals("")){
                    setPreferences(newName,newImagURI.toString());
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "店名を入力してください", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ////////////////ここまで初期設定///////////////////////////////////////////////////////////////////////////



    }

    //リファレンスの登録
    public void setPreferences(String newImg,String newName){
        RimgList.add(newImg);
        RnameList.add(newName);
        System.out.println("レストランIMGリスト::"+RimgList);
        System.out.println("レストラン名前リスト::"+RnameList);
        //arrayListをString変換してリファレンスに登録
        editor.putString(ListImg, String.join(",", RimgList));
        editor.putString(ListName, String.join(",", RnameList));
        //データの確定COMMIT
        editor.apply();
    }

    //画像表示
    void openImage(Intent resultData){
        ParcelFileDescriptor pfDescriptor = null;
        try{
            newImagURI = resultData.getData();

            pfDescriptor = getContentResolver().openFileDescriptor(newImagURI, "r");
            if(pfDescriptor != null){
                FileDescriptor fileDescriptor = pfDescriptor.getFileDescriptor();
                Bitmap bmp = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                pfDescriptor.close();
                RestNewImg.setImageBitmap(bmp);
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