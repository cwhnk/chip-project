package com.example.kr.huarongdao;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE
            = "com.example.android.huarongdao.extra.MESSAGE";
    private TextView mLevelName;//关卡名
    private TextView mCount;//步数用组件
    private ChessboardView mBoard;//棋盘组件类
    private int finalStep;//最终步数
    private int level;//难度
    private int[] level_1 = {0, 3, 0, 1, 1, 1, 2, 0, 3, 0, 2, 3, 2, 2, 3, 2, 2, 4, 3, 4};//每两个代表一个坐标，左上角坐标
    private int[] level_2 = {1, 1, 0, 0, 0, 2, 2, 3, 3, 1, 1, 0, 3, 0, 1, 3, 1, 4, 3, 3};
    private int[] level_3 = {1, 0, 0, 0, 3, 0, 0, 2, 3, 2, 1, 2, 0, 4, 3, 4, 1, 3, 2, 3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mLevelName = findViewById(R.id.level_name);
        mCount = findViewById(R.id.stepCount);
    }
    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);//获取关卡名
        mLevelName.setText(message);
        mBoard = findViewById(R.id.chessboardView);
        switch (message) {
            case "第一关":
                mBoard.setLevelInfo(level_1);
                level = 1;
                break;
            case "第二关":
                mBoard.setLevelInfo(level_2);
                level = 2;
                break;
            case "第三关":
                mBoard.setLevelInfo(level_3);
                level = 3;
                break;
        }
        setCount(0);
        mBoard.init();
        mBoard.findGameWin(new ChessboardView.winCallBack(){
            @Override
            public void isWin(int count) {
                gameWin(count);
                finalStep = count;
            }
            @Override
            public void showCount(int count) {
                setCount(count);
            }
        });
    }
    private void showWinDialog(int c) {
        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(this);
        final View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.activity_win,null);
        customizeDialog.setMessage(getString(R.string.win_game) + c);
        customizeDialog.setView(dialogView);
        customizeDialog.setPositiveButton("好的",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        certainWin();
                    }
                });
        customizeDialog.show();
    }

    public void gameWin(int c) {
        showWinDialog(c);
    }
    public void setCount(int c) {
        mCount.setText(getString(R.string.game_step) + c);
    }
    public void certainWin() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_MESSAGE, String.valueOf(finalStep));
        setResult(level, intent);
        finish();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
}
