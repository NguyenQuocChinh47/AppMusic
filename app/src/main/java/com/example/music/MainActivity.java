package com.example.music;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.music.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView txtTitle, txtTime1, txtTime2;
    ImageView ivPrevious, ivContinue, ivStop, ivNext, ivDisc;
    SeekBar sbMusic;
    ArrayList<com.example.applicationmusic.Song> arraySong;
    int position = 0;
    MediaPlayer mediaPlayer;
    Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        animation = AnimationUtils.loadAnimation(this, R.anim.disc_rotate);

        anhXa();
        addSong();
        createMediaplayer();
        playSong();
        stopSong();
        nextSong();
        previousSong();
        changedSeekBar();
        updateTimeSong();
    }

    private void updateTimeSong() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                txtTime1.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                // update Process sbMusic
                sbMusic.setProgress(mediaPlayer.getCurrentPosition());
                //check overtime
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        position++;
                        if(position >= arraySong.size()){
                            position=0;
                        }
                        if(mediaPlayer.isPlaying()){
                            mediaPlayer.stop();
                        }
                        createMediaplayer();
                        mediaPlayer.start();
                        updateTimeSong();
                        setTimeTotal();
                    }
                });
                handler.postDelayed(this, 500);
            }
        },100);
    }

    private void changedSeekBar() {
        sbMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
    }

    private void setTimeTotal() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        txtTime2.setText(simpleDateFormat.format(mediaPlayer.getDuration())+"");
        //gan max sbMusic = mediaPlayer.getDuration()
        sbMusic.setMax(mediaPlayer.getDuration());
    }

    private void previousSong() {
        ivPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position--;
                if(position<0){
                    position = arraySong.size()-1;
                }
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    createMediaplayer();
                    mediaPlayer.start();
                }else{
                    createMediaplayer();
                }
                updateTimeSong();
                setTimeTotal();
            }
        });
    }

    private void nextSong() {
        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position++;
                if(position >= arraySong.size()){
                    position=0;
                }
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    createMediaplayer();
                    mediaPlayer.start();
                }else{
                    createMediaplayer();
                }
                updateTimeSong();
                setTimeTotal();
            }
        });
    }

    private void anhXa() {
        txtTitle = findViewById(R.id.txt_Title);
        txtTime1 = findViewById(R.id.txt_Time1);
        txtTime2 = findViewById(R.id.txt_Time2);
        ivPrevious = findViewById(R.id.iv_previous);
        ivContinue = findViewById(R.id.iv_continue);
        ivStop = findViewById(R.id.iv_stop);
        ivNext = findViewById(R.id.iv_next);
        ivDisc = findViewById(R.id.iv_Disc);
        sbMusic = findViewById(R.id.sb_Music);
    }

    private void createMediaplayer() {
        mediaPlayer = MediaPlayer.create(MainActivity.this, arraySong.get(position).getFile());
        txtTitle.setText(arraySong.get(position).getTitle());
    }

    private void stopSong() {
        ivStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                ivContinue.setImageResource(R.drawable.continuee);
                createMediaplayer();
            }
        });
    }

    private void playSong() {
        ivContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    // if playing => pause => set image "continue"
                    mediaPlayer.pause();
                    ivContinue.setImageResource(R.drawable.continuee);
                }else{
                    // if pausing => start = > set image "pause"
                    mediaPlayer.start();
                    ivContinue.setImageResource(R.drawable.pause);
                }
                setTimeTotal();
                updateTimeSong();
                ivDisc.startAnimation(animation);
            }
        });
    }

    private void addSong() {
        arraySong = new ArrayList<>();
        arraySong.add(new com.example.applicationmusic.Song("Cho anh say", R.raw.cho_anh_say));
    }
}