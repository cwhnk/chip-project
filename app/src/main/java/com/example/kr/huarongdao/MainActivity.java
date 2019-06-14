package com.example.kr.huarongdao;

import android.app.VoiceInteractor;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE
            = "com.example.android.huarongdao.extra.MESSAGE";

    Animation mAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ImageView mAnimationView = (ImageView) findViewById(R.id.imageView);
//        mAnimation = AnimationUtils.loadAnimation(this, R.anim.rotaterepeat);
//        mAnimationView.startAnimation(mAnimation);
    }
    public void startGame(View view) {
        Intent intent = new Intent(this, OptionActivity.class);
        startActivity(intent);
    }

    public void quitGame(View view) {
        System.exit(0);
    }
}
