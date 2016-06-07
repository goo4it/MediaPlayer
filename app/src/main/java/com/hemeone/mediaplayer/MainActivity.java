package com.hemeone.mediaplayer;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MediaPlayView mpv;
    private ImageView btn_prev, btn_play, btn_next;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mpv = (MediaPlayView) findViewById(R.id.mpv);
        btn_prev = (ImageView) findViewById(R.id.btn_prev);
        btn_prev.setOnClickListener(this);
        btn_play = (ImageView) findViewById(R.id.btn_play);
        btn_play.setOnClickListener(this);
        btn_next = (ImageView) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg == null) return;
                switch (msg.what) {
                    case 0x111:
                        btn_play.setImageResource(R.drawable.play_btn_play_selector);
                        break;
                    case 0x112:
                        btn_play.setImageResource(R.drawable.play_btn_pause_selector);
                        break;
                    default:
                        break;
                }
            }
        };

        mpv.setUiHandler(handler);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play:
                if (mpv.isPlay()) {
                    mpv.pause();
                } else {
                    mpv.play();
                }
                break;
            case R.id.btn_next:
                mpv.next();
                break;
            case R.id.btn_prev:
                mpv.prev();
                break;
            default:
                break;
        }
    }
}
