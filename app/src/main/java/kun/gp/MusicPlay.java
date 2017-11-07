package kun.gp;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * Created by kun on 2017-06-13.
 */

public class MusicPlay {
    public static MediaPlayer mediaPlayer;

    public MusicPlay(Context mContext) {
        if (mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = MediaPlayer.create(mContext,R.raw.testmusic);
        Log.e( "MusicPlay: ", mediaPlayer.hashCode()+"  " );
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
            }
        });
    }

    public MediaPlayer getMediaPlayer(){

        return mediaPlayer;
    }

}
