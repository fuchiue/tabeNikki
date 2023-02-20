package com.example.tabenikki;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class RestaurantActivity extends AppCompatActivity {
    ListView RestListView;

    SharedPreferences data;
    SharedPreferences.Editor editor;
    String ListImg;
    String ListName;
    ArrayList<String> RimgList; //カテゴリデータを保存するArrayList
    ArrayList<String> RnameList; //カテゴリデータを保存するArrayList

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        // 他のアクティビティからデータを受け取るためにIntentを取得
        Intent GetCategoryIntent = getIntent();
        // 送信されたデータを取得
        String GetCategory = GetCategoryIntent.getStringExtra("CategoryKey");
        // タイトル設定
        setTitle("店舗新規登録");
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
        ////////////////ここまで初期設定///////////////////////////////////////////////////////////////////////////

        //Listの宣言
        RestListView = findViewById(R.id.RestListView);
        //ボタンの宣言
        Button RestNewButton = findViewById(R.id.RestNewButton);

        RestNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateRestaurantActivity.class);
                intent.putExtra("CategoryKey",GetCategory); // 送信するデータを設定
                startActivity(intent);
            }
        });

    }
}