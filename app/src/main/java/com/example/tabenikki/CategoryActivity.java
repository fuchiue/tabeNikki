package com.example.tabenikki;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        //SharedPreferencesの生成
        SharedPreferences data = getSharedPreferences("Data", MODE_PRIVATE);
        //入力用editor生成
        SharedPreferences.Editor editor = data.edit();

        //arraylist作成
        ArrayList<String> strlist = new ArrayList<>();
        strlist.add("カフェ");
        strlist.add("ラーメン");
        //リファレンスに保存する名前を作成
        String list = "カテゴリ";
        //arrayListをString変換してリファレンスに登録
        editor.putString(list,String.join(",",strlist));
        //データの確定COMMIT
        editor.apply();

        //リファレンスからデータを取得
        String onString = data.getString(list,"");
        System.out.println(onString);
        //変換してコンマ区切りになったStringデータをアレイリストに変換
        ArrayList<String> splitStr = new ArrayList<>(Arrays.asList(onString.split("\\s*,\\s*")));
        System.out.println(splitStr);





    }
}