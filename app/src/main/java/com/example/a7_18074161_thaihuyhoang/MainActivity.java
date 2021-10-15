package com.example.a7_18074161_thaihuyhoang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText edtDataIntent,txtA,txtB;
    private ImageButton btnStartService,btnTru, btnNhan, btnChia;
    private Button btnSopService;

    private RelativeLayout layoutBottom;
    private ImageView imgSong, imgPlayOrPause, imgClear,img1,img2;
    private TextView tvTitleSong, tvSingleSong,txtKetQua;

    private Song mSong;
    private boolean isPlaying;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //sau khi new thì phải override cái hàm
            //Thứ ba nhận dữ liệu trong hàm này onRêciver
            Bundle bundle = intent.getExtras();
            if(bundle == null){
                return;
            }
//            mSong = (Song) bundle.get("object_song");
//            isPlaying = bundle.getBoolean("status_player");
            int actionMusic = bundle.getInt("action");
            double ketqua = bundle.getDouble("result");

            handleLayout(actionMusic,ketqua);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Đầu tiên phải lắng nghe BraodCast Reciever ở OnCreate
        //thứ hai đã tạo register thì phải destroy nó
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,new IntentFilter("send_data_to_activity"));

        btnStartService = findViewById(R.id.btn_start_service);
        btnSopService = findViewById(R.id.btn_stop_service);

        layoutBottom = findViewById(R.id.layout_bottom);
        imgSong = findViewById(R.id.img_song1);
        imgPlayOrPause = findViewById(R.id.img_play_or_pause);
        imgClear = findViewById(R.id.img_clear);
        tvTitleSong = findViewById(R.id.tv_title_song);
        tvSingleSong = findViewById(R.id.tv_single_song);
        txtA = findViewById(R.id.txtA);
        txtB = findViewById(R.id.txtB);
        txtKetQua = findViewById(R.id.txtKetQua);
        btnChia = findViewById(R.id.btnChia);
        btnNhan = findViewById(R.id.btnNhan);
        btnTru = findViewById(R.id.btnTru);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);

        Animation xoay = AnimationUtils.loadAnimation(this,R.anim.anim_taylor);
        Animation amnhac = AnimationUtils.loadAnimation(this,R.anim.anim_rotate);

        img1.startAnimation(xoay);
        img2.startAnimation(amnhac);

        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickStartService(11,R.drawable.cong);
            }
        });
        btnTru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickStartService(22,R.drawable.tru);
            }
        });
        btnNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickStartService(33,R.drawable.nhana);
            }
        });
        btnChia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickStartService(44,R.drawable.chia);
            }
        });

        btnSopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickStopService();
            }
        });
    }

    private void clickStartService(int action,int image) {
        Song song = new Song("Big city boy", "Tincoder", R.drawable.img_music,R.raw.file_isaac);

        // COntext    Service muốn chạy
        Intent intent = new Intent(this,MyService.class);
//        intent.putExtra("object_song",song);
        intent.putExtra("action",action);
        intent.putExtra("NumA",txtA.getText().toString());
        intent.putExtra("NumB",txtB.getText().toString());
        intent.putExtra("image",image);
        startService(intent);//chạy onCreate lần đầu rồi chạy onStartCommand
    }

    private void clickStopService() {
        //copy service muốn dừng rồi stop là xong
        Intent intent = new Intent(this,MyService.class);
        stopService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Thứ hai là phải ủnegister
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    private void handleLayoutMusic(int action) {
        switch (action) {
            case MyService.ACTION_START:
                layoutBottom.setVisibility(View.VISIBLE);
                showInforSong();
                setStatusButtonPlayOrPause();
                break;
            case MyService.ACTION_PAUSE:
                setStatusButtonPlayOrPause();
                break;
            case MyService.ACTION_RESUME:
                setStatusButtonPlayOrPause();
                break;
            case MyService.ACTION_CLEAR:
                layoutBottom.setVisibility(View.GONE);
                break;
        }
    }
    private void handleLayout(int actionMusic, double ketqua) {
        switch (actionMusic) {
            case 11:
                layoutBottom.setVisibility(View.VISIBLE);
                showInforSong(ketqua);
                setStatusButtonPlayOrPause();
                break;
            case 22:
                setStatusButtonPlayOrPause();
                layoutBottom.setVisibility(View.VISIBLE);
                showInforSong(ketqua);
                break;
            case 33:
                setStatusButtonPlayOrPause();
                layoutBottom.setVisibility(View.VISIBLE);
                showInforSong(ketqua);
                break;
            case 44:
                layoutBottom.setVisibility(View.VISIBLE);
                showInforSong(ketqua);
                setStatusButtonPlayOrPause();
                break;
        }
    }
    private void showInforSong(double ketqua){
        txtKetQua.setText(ketqua+"");
    }
    private void showInforSong(){
//        txtKetQua.setText(ketqua+"");
//        if(mSong == null){
//            return;
//        }
//
//        imgSong.setImageResource(mSong.getImage());
//        tvTitleSong.setText(mSong.getTitle());
//        tvSingleSong.setText(mSong.getSingle());
//
//        //Xử lý ngược lại
//        imgPlayOrPause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(isPlaying){
//                    sendActiontoServie(MyService.ACTION_PAUSE);
//                }else{
//                    sendActiontoServie(MyService.ACTION_RESUME);
//                }
//            }
//        });
//
//        imgClear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sendActiontoServie(MyService.ACTION_CLEAR);
//            }
//        });
    }

    private void setStatusButtonPlayOrPause(){
        if(isPlaying){
            imgPlayOrPause.setImageResource(R.drawable.ic_pause);
        }else{
            imgPlayOrPause.setImageResource(R.drawable.ic_play);
        }
    }

    //Xử lý ngược lại
    private void sendActiontoServie(int action){
        Intent intent = new Intent(this, MyService.class);
        //lợi dụng hàm bài trước
        intent.putExtra("action_music_service", action);
        startService(intent);
    }
}