package com.example.tabenikki;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    SharedPreferences data;
    SharedPreferences.Editor editor;
    String list;
    ArrayList<String> CategoryList; //カテゴリデータを保存するArrayList
    ListView CategoryListView;//ListViewの宣言
    ArrayAdapter<String> CategoryAdapter;//ListView表示用のアダプタ宣言
    int deleteIndex;//CategoryListのデータ削除のさいIndexを指定する変数



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        // タイトル設定
        setTitle("カテゴリ");
        //SharedPreferencesの生成
        data = getSharedPreferences("Data", MODE_PRIVATE);
        //入力用editor生成
        editor = data.edit();
        //リファレンスに保存する名前を作成
        list = "カテゴリ";
        //リファレンスからデータを取得
        String onString = data.getString(list, "");
        //変換してコンマ区切りになったStringデータをアレイリストに変換
        CategoryList = new ArrayList<>(Arrays.asList(onString.split("\\s*,\\s*")));
        //Listの０番目に空白がある場合削除する
        if(CategoryList.get(0).equals("")){
            CategoryList.remove(0);
        }

        //ボタンの宣言
        Button CategoryNewCreateButton = findViewById(R.id.CategoryNewCreateButton);
        //リストの宣言
        CategoryListView = findViewById(R.id.CategoryLisy);
        //表示リストの表示・更新
        setCategoryList();
        ////////////////////ここまで初期設定//////////////////////////////////////////////////////////////////////////////////////


        ////////////////////カテゴリ新規作成ダイアログ/////////////////////////////////////////////////////////////////////////////////
        AlertDialog.Builder newCategoryBuilder = new AlertDialog.Builder(this);
        newCategoryBuilder.setTitle("新規カテゴリを入力してください");

        // ユーザーに文字列を入力するためのEditTextを作成
        final EditText CategoryInput = new EditText(this);
        CategoryInput.setInputType(InputType.TYPE_CLASS_TEXT);
        newCategoryBuilder.setView(CategoryInput);

        // OKボタンが押されたときの処理を追加
        newCategoryBuilder.setPositiveButton("追加", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newCategory = CategoryInput.getText().toString();
                //CategoryArraylistに追加入力
                setCategoryData(newCategory);
                //表示リストの表示・更新
                setCategoryList();
                //入力値の初期化
                CategoryInput.setText(null);
            }
        });

        // キャンセルボタンが押されたときの処理を追加
        newCategoryBuilder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //入力値の初期化
                CategoryInput.setText(null);
                dialog.cancel();
            }
        });

        //カテゴリ新規作成ダイアログの変数生成
        AlertDialog newCategoryDialog = newCategoryBuilder.create();
        ////////////////カテゴリ新規作成ダイアログここまで//////////////////////////////////////////////////////////////////////////


        ////////////////カテゴリ削除ダイアログ////////////////////////////////////////////////////////////////////////////////////////
        AlertDialog.Builder CategoryDeleteBuilder = new AlertDialog.Builder(this);
        CategoryDeleteBuilder.setTitle("カテゴリを削除しますか");
        CategoryDeleteBuilder.setMessage("一度削除すると復元できません");
        CategoryDeleteBuilder.setPositiveButton("削除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // OKボタンがクリックされたときの処理
                //CategoryListからデータ削除
                deleteCategoryData(deleteIndex);
                //表示リストの表示・更新
                setCategoryList();
                dialog.dismiss(); // ダイアログを閉じる
            }
        });

        // キャンセルボタンが押されたときの処理を追加
        newCategoryBuilder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        //カテゴリ新規作成ダイアログの変数生成
        AlertDialog deleteCategoryDialog = CategoryDeleteBuilder.create();
        ////////////////カテゴリ削除ダイアログここまで/////////////////////////////////////////////////////////////////////////////////


        //新規作成ボタンが押されたときカテゴリ新規作成ダイアログを表示
        CategoryNewCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ダイアログを表示
                newCategoryDialog.show();
            }
        });

        //ListViewがクリックされたとき
        CategoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // クリックされた場所の処理を追加
                Intent RestIntent = new Intent(getApplicationContext(), RestaurantActivity.class);
                RestIntent.putExtra("CategoryKey", CategoryList.get(position)); // 送信するデータを設定
                startActivity(RestIntent);
            }
        });

        //ListViewが長押しされたとき
        CategoryListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // 長押しされた場所を取得する処理を記述する
                deleteIndex=position;
                deleteCategoryDialog.show();
                return true;
            }
        });
    }

    //CategoryListにデータの追加とリファレンスの更新
    public void setCategoryData(String newCategory){
        //既に登録されているカテゴリは追加しない
        if(CategoryList.indexOf(newCategory)!=-1){
            Toast.makeText(getApplicationContext(), "そのカテゴリは既に存在します", Toast.LENGTH_SHORT).show();
        }else {
            //CategoryListに新しいカテゴリを追加
            CategoryList.add(newCategory);
            //arrayListをString変換してリファレンスに登録
            editor.putString(list, String.join(",", CategoryList));
            //データの確定COMMIT
            editor.apply();
        }
    }

    //CategoryListの指定データの削除とリファレンスの更新
    public void deleteCategoryData(int deleteIndex){
        //CategoryListに新しいカテゴリを追加
        CategoryList.remove(deleteIndex);
        System.out.println(CategoryList);
        System.out.println(CategoryList.size());
        //arrayListをString変換してリファレンスに登録
        editor.putString(list,String.join(",",CategoryList));
        //データの確定COMMIT
        editor.apply();
    }

    //表示リストの表示・更新
    public void setCategoryList(){

        System.out.println("リスト更新前"+CategoryList);
            //ListViewに表示するアダプタにCateGoryListを代入
            CategoryAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, CategoryList);
            //アダプタの情報をListに表示
            CategoryListView.setAdapter(CategoryAdapter);
            System.out.println("List更新");
    }



}