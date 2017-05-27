package com.bignerdranch.android.osm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class SplashScreenActivity extends Activity {

    // Время в милесекундах, в течение которого будет отображаться Splash Screen
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    private String[] tip = new String[3];
    private TextView mTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        tip[0] = "Подсказка: при создании записи для удобства вы можете воспользоваться встроенным секундомером";
        tip[1] = "Подсказка: для удобства поиска нужной записи воспользуйтесь сортировкой или поиском по дате";
        tip[2] = "Подсказка: теперь вы можете уведомлять своего тренера о результате пробы, воспользовавшись функцией - отправить результат";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        mTextView = (TextView) findViewById(R.id.splash_tip);
        int max = 2;
        mTextView.setText(tip[(int) (Math.random() * ++max)]);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // По истечении времени, запускаем главный активити, а Splash Screen закрываем
                Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                SplashScreenActivity.this.startActivity(mainIntent);
                SplashScreenActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

}
