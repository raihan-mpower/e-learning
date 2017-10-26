package mpower.org.elearning_module.services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaSessionManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import java.io.File;
import java.io.IOException;

import mpower.org.elearning_module.application.ELearningApp;
import mpower.org.elearning_module.utils.AppConstants;

/**
 * Created by sabbir on 10/26/17.
 */

public class MediaPlayerService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnInfoListener {
    public static final String ACTION_PLAY = "com.sabbir.android.music.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.sabbir.android.music.ACTION_PAUSE";
    public static final String ACTION_PREVIOUS = "com.sabbir.android.music.ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "com.sabbir.android.music.ACTION_NEXT";
    public static final String ACTION_STOP = "com.sabbir.android.music.ACTION_STOP";

    private MediaSessionManager mediaSessionManager;
    private MediaSessionCompat mediaSession;
    private MediaControllerCompat.TransportControls transportControls;

    //AudioPlayer notification ID
    private static final int NOTIFICATION_ID = 101;

    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;
    //path to the audio file
    private String mediaFile;
    private int resumePosition;

    private boolean onGoingCall=false;
    private PhoneStateListener mPhoneStateListener;
    private TelephonyManager mTelephonyManager;

    private final IBinder musicBinder=new AudioBinder();
    private String currentAudioPath;

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {

    }

    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }


    public enum PlaybackStatus {
        PLAYING,
        PAUSED
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBinder;
    }

    public class AudioBinder extends Binder{
        public MediaPlayerService getService(){
            return MediaPlayerService.this;
        }
    }




    public void playAudio(String name){
        if (name!=null && !name.endsWith(".mp3")) currentAudioPath+=".mp3";
       currentAudioPath= ELearningApp.IMAGES_FOLDER_NAME+ File.separator+name;
       initMediaPlayer();

    }


    private void muteAudio(){

    }

    public void muteAudio(boolean flag){
        if (flag) muteAudio();
        else unMuteAudio();
    }

    private void unMuteAudio() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

            //An audio file is passed to the service through putExtra();
        String name=null;
        if (intent.getExtras()!=null){
            name=intent.getExtras().getString(AppConstants.AUDIO_FILE_NAME);
        }

        //  mediaFile = intent.getExtras().getString("media");
        playAudio(name);
        return super.onStartCommand(intent, flags, startId);
    }

    private void initMediaPlayer(){
        if (mMediaPlayer == null){
            mMediaPlayer=new MediaPlayer();
        }

        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnSeekCompleteListener(this);
        mMediaPlayer.setOnInfoListener(this);
        //Reset so that the MediaPlayer is not pointing to another data source
        mMediaPlayer.reset();

        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);



        try {


            // Uri trackUri= ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,activeSong.getId());
            mMediaPlayer.setDataSource(currentAudioPath);

        } catch (IOException e) {
            e.printStackTrace();
            stopSelf();
        }

        mMediaPlayer.prepareAsync();
    }

    public void playMedia() {
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
        }
    }

    public void stopMedia() {
        if (mMediaPlayer == null) return;
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
    }

    public void pauseMedia() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            resumePosition = mMediaPlayer.getCurrentPosition();
        }
    }

    public void resumeMedia() {
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.seekTo(resumePosition);
            mMediaPlayer.start();
        }
    }

}