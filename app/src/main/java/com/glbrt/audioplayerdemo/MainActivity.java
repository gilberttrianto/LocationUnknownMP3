package com.glbrt.audioplayerdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    // Inisialiasi variabel
    TextView plyrPosition, plyrDuration;
    SeekBar seekBar;
    ImageView btRew, btPlay, btPause, btFf;

    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //deklarasi variabel
        plyrPosition = findViewById(R.id.player_position);
        plyrDuration = findViewById(R.id.player_duration);
        seekBar = findViewById(R.id.sk_bar);
        btRew = findViewById(R.id.bt_rew);
        btPlay = findViewById(R.id.bt_play);
        btPause = findViewById(R.id.bt_pause);
        btFf = findViewById(R.id.bt_ff);

        // inisialisai media player
        mediaPlayer = MediaPlayer.create(this,R.raw.unlocationunknow );

        //inisialiasi runnable
        runnable = new Runnable() {
            @Override
            public void run() {
                // seting seek bar
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                //handler post delay from 0.5 second
                handler.postDelayed(this,500);
            }
        };

        //get durasi lagu untuk di seek bar
        int duration = mediaPlayer.getDuration();
        // konvert milisecond ke menit dan detik untuk seek bar
        String sDurasi = convertFormat(duration);
        //set durasi di text view
        plyrDuration.setText(sDurasi);

        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide play button
                btPlay.setVisibility(View.GONE);
                //show pause button
                btPause.setVisibility(View.VISIBLE);
                //start media player
                mediaPlayer.start();
                //set max on seek bar
                seekBar.setMax(mediaPlayer.getDuration());
                //start handler
                handler.postDelayed(runnable, 0);
            }
        });

        btPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide pause button
                btPause.setVisibility(View.GONE);
                //show play button
                btPlay.setVisibility(View.VISIBLE);
                //pause media player
                mediaPlayer.pause();
                //stop handler
                handler.removeCallbacks(runnable);
            }
        });

        btFf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get current posisi dari media player
                int currentPosisi = mediaPlayer.getCurrentPosition();
                // get durasi dari medi aplayer
                int durasi = mediaPlayer.getDuration();
                //cek kodisi
                if (mediaPlayer.isPlaying() && durasi != currentPosisi){
                    //ketia media is play dan durasi tidak sma dengan posisi saat ini
                    //fast forward fot 5 second
                    currentPosisi = currentPosisi+ 5000;
                    //set lokasi saatini dari textView
                    plyrPosition.setText(convertFormat(currentPosisi));
                    //set progres pada seekbar
                    mediaPlayer.seekTo(currentPosisi);
                }
            }
        });

        btRew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get posisi saat ini dari media player
                int currentPosition = mediaPlayer.getCurrentPosition();
                //cek kodisi
                if (mediaPlayer.isPlaying() && currentPosition > 5000) {
                    // ketia media is play dan posisi saat ini lebih besar dari 5 detik
                    //rewind 5 detik
                    currentPosition = currentPosition- 5000;
                    //get current position on textView
                    plyrPosition.setText(convertFormat(currentPosition));
                    //set progres pada seekbar
                    mediaPlayer.seekTo(currentPosition);
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // cek kodisi
                if (fromUser) {
                    //ketika drag seekbar set progress on seekbar
                    mediaPlayer.seekTo(progress);
                }
                //set current posisi on text view
                plyrPosition.setText(convertFormat(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //hide pause button
                btPause.setVisibility(View.GONE);
                //show play button
                btPlay.setVisibility(View.VISIBLE);
                //set media player to initial posisi
                mediaPlayer.seekTo(0);
            }
        });
    }

    private String convertFormat (int duration) {
        return String.format("%02d:%02d"
        , TimeUnit.MILLISECONDS.toMinutes(duration)
        , TimeUnit.MILLISECONDS.toSeconds(duration) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }
}