package com.example.kr.huarongdao;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class OptionActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE
            = "com.example.android.huarongdao.extra.MESSAGE";
    private static String level_1;
    private static String level_2;
    private static String level_3;
    private TextView mStep_1;
    private TextView mStep_2;
    private TextView mStep_3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        mStep_1 = findViewById(R.id.status_one);
        mStep_2 = findViewById(R.id.status_two);
        mStep_3 = findViewById(R.id.status_three);
    }
    public void chooseLevel(View view) {
        String message = "";
        switch (view.getId()){
            case R.id.option_one:
                message = "第一关";
                break;
            case R.id.option_two:
                message = "第二关";
                break;
            case R.id.optione_three:
                message = "第三关";
                break;
        }
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivityForResult(intent, 10);
    }
    @Override
    protected void onActivityResult(int responseCode, int resultCode, Intent data){
        String info = data.getStringExtra(EXTRA_MESSAGE);
        switch (resultCode) {
            case 1:
                if(level_1 == null || info.compareTo(level_1) < 0) {
                    level_1 = info;
                    mStep_1.setText(getString(R.string.is_win) + level_1);
                }
                break;
            case 2:
                if(level_2 == null || info.compareTo(level_2) < 0) {
                    level_2 = info;
                    mStep_2.setText(getString(R.string.is_win) + level_2);
                }
                break;
            case 3:
                if(level_3 == null || info.compareTo(level_3) < 0) {
                    level_3 = info;
                    mStep_3.setText(getString(R.string.is_win) + level_3);
                }
                break;
        }
    }
}
