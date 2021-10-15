package com.example.a7_18074161_thaihuyhoang;

import static com.example.a7_18074161_thaihuyhoang.MyApplication.CHANNEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MyService extends Service {

    public static final int ACTION_PAUSE = 1;
    public static final int ACTION_RESUME = 2;
    public static final int ACTION_CLEAR = 3;
    public static final int ACTION_START = 4;

    public static final int ACTION_ADD = 11;
    public static final int ACTION_TRU = 22;
    public static final int ACTION_NHAN = 33;
    public static final int ACTION_CHIA = 44;


    private MediaPlayer mMediaPlayer;
    private boolean isPlaying;
    private Song mSong;
    private String res ="";
    private Double kq= 0.0;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("Tincoder", "My Service onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;//nếu không sử dụng BoundService
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.getSerializableExtra("object_song") != null){
            Song song = (Song) intent.getSerializableExtra("object_song");

            if(song != null){
                mSong = song;
                startMusic(song);
                Log.e("TAG", "onStartCommand: " );
                sendNotification(song);
            }
        }

//        int actionMusic = intent.getIntExtra("action_music_service",0);
//        Log.e("Hoang","abbbbb" +actionMusic);
//         handleActionMusic(actionMusic);

         if(intent.getIntExtra("action",0) != 0){
             Song s1 = new Song(intent.getStringExtra("NumA"),intent.getStringExtra("NumB")
             ,intent.getIntExtra("image",R.drawable.cong),R.drawable.cong);
             int action = intent.getIntExtra("action",0);
             Add(intent.getStringExtra("NumA"),intent.getStringExtra("NumB"));
//             s1.setTitle(intent.getStringExtra("NumA"));
//             s1.setSingle(intent.getStringExtra("NumB"));
//             s1.setImage(R.drawable.cong);
//             s1.setResourece(0);
             switch (action){
                 case 11:
                     Add(intent.getStringExtra("NumA"),intent.getStringExtra("NumB"));
                     sendNotification(s1);
                     break;
                 case 22:
                     Tru(intent.getStringExtra("NumA"),intent.getStringExtra("NumB"));
                     sendNotification(s1);
                     break;
                 case 33:
                     Nhan(intent.getStringExtra("NumA"),intent.getStringExtra("NumB"));
                     sendNotification(s1);
                     break;
                 case 44:
                     Chia(intent.getStringExtra("NumA"),intent.getStringExtra("NumB"));
                     sendNotification(s1);
                     break;
                 case 55:
                     stopSelf();
                     break;
             }
         }

        return START_NOT_STICKY;
    }

    private void Add(String numA, String numB) {
        Double a = Double.parseDouble(numA);
        Double b = Double.parseDouble(numB);
        Double result = a + b;
        kq= result;
        sendActiontoActivity(11,result);
    }
    private void Tru(String numA, String numB) {
        Double a = Double.parseDouble(numA);
        Double b = Double.parseDouble(numB);
        Double result = a - b;
        kq= result;
        sendActiontoActivity(22,result);
    }
    private void Nhan(String numA, String numB) {
        Double a = Double.parseDouble(numA);
        Double b = Double.parseDouble(numB);
        Double result = a * b;
        kq= result;
        sendActiontoActivity(33,result);
    }

    private void Chia(String numA, String numB) {
        int a = Integer.parseInt(numA);
        int b = Integer.parseInt(numB);
        double result = a / b;
        kq= result;
        sendActiontoActivity(44,result);
    }

    private void startMusic(Song song) {
        if(mMediaPlayer == null){
            mMediaPlayer = MediaPlayer.create(getApplicationContext(),song.getResourece());
        }
        mMediaPlayer.start();
        isPlaying = true;
//        sendActiontoActivity(ACTION_START);
    }

    private void handleActionMusic(int action){
        switch (action){
            case ACTION_PAUSE:
                pauseMusic();
                break;
            case ACTION_RESUME:
                resumMusic();
                break;
            case ACTION_CLEAR:
                stopSelf();// => stop service=> onDestroy
//                sendActiontoActivity(ACTION_CLEAR);
                break;
        }
    }

    private void pauseMusic(){
        if(mMediaPlayer != null && isPlaying==true){
            mMediaPlayer.pause();
            isPlaying=false;
            sendNotification(mSong);//updateView thì phải send lại Notifycation
//            sendActiontoActivity(ACTION_PAUSE);
        }


    }
    private void resumMusic(){
        if(mMediaPlayer != null && isPlaying==false){
            mMediaPlayer.start(); //tiếp tục chạy
            isPlaying = true;
            sendNotification(mSong);
//            sendActiontoActivity(ACTION_RESUME);
        }
    }


    private void sendNotification(Song song) {
        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),song.getImage());


        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.custom);
        remoteViews.setTextViewText(R.id.tvA, song.getTitle()+"");
        remoteViews.setTextViewText(R.id.tvB, song.getSingle()+"");
        remoteViews.setTextViewText(R.id.tvKetQua, kq+"");
        remoteViews.setImageViewBitmap(R.id.img_song1,bitmap);//tvKetQua

        //icon nhỏ
//        remoteViews.setImageViewResource(R.id.img_play_or_pause,R.drawable.ic_play);
//        remoteViews.setImageViewResource(R.id.img_clear,R.drawable.ic_clear);
//
//        //Bắt sự kiện cho component trong custom notification
//        if(isPlaying){
//            //Nếu như đang chạy mà click chọn sư kiện nút img_play thì nó sẽ pause lại
//            remoteViews.setOnClickPendingIntent(R.id.img_play_or_pause,getPendingIntent(this, ACTION_PAUSE));
//            remoteViews.setImageViewResource(R.id.img_play_or_pause,R.drawable.ic_pause);
//        }else{
//            remoteViews.setOnClickPendingIntent(R.id.img_play_or_pause,getPendingIntent(this, ACTION_RESUME));
//            remoteViews.setImageViewResource(R.id.img_play_or_pause,R.drawable.ic_play);
//        }

        remoteViews.setOnClickPendingIntent(R.id.img_clear,getPendingIntent(this, ACTION_CLEAR));

        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)//sau khi đã có peding thì mới st, không thì th
                .setCustomContentView(remoteViews)
                .setSound(null)
                .build();
        //Tham số đầu tiên là id của noti
        //tham số hai là đối tượng notification
        startForeground(1,notification);

    }

    private PendingIntent getPendingIntent( Context context, int action){
        Intent intent = new Intent(this, com.example.a7_18074161_thaihuyhoang.MyReceiver.class);//truỳen action sang my Rêciver
        intent.putExtra("action_music",action);
        //Log.e("Tincoder",action+"" );
        return PendingIntent.getBroadcast(context.getApplicationContext(),action, intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Tincoder","MyService onDestroy");
        if (mMediaPlayer != null){
            //Khi Service bị kill thì đối tượng cũng phải close
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void sendActiontoActivity(int action, double result){
        Intent intent = new Intent("send_data_to_activity");
        Bundle bundle  =new Bundle();
//        bundle.putSerializable("object_song",mSong);
//        bundle.putBoolean("status_player",isPlaying);
        bundle.putInt("action",action);
        bundle.putDouble("result",result);

        intent.putExtras(bundle);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
