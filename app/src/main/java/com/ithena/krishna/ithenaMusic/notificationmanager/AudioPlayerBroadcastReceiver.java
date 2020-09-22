package com.ithena.krishna.ithenaMusic.notificationmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
public class AudioPlayerBroadcastReceiver extends BroadcastReceiver {

    public interface onCallbackListener {
        public void onCallbackCalled(int i);
        public void togglePLayPauseCallback();
        public boolean getPauseClicked();
        public void setPauseClicked(boolean bool);
        public MediaPlayer getMediaPlayer();
    }

    onCallbackListener callback;

    @Override
    public void onReceive(Context context, Intent intent) {

        callback = (onCallbackListener) context;

        String action = intent.getAction();
        if (action.equalsIgnoreCase("com.ithena.krishna.ithenaMusic.ACTION_PLAY_PAUSE")) {
            try {
                if (!callback.getPauseClicked()) {
                    callback.setPauseClicked(true);
                }
                callback.togglePLayPauseCallback();
                callback.onCallbackCalled(6);
            } catch (Exception e) {

            }

        } else if (action.equalsIgnoreCase("com.ithena.krishna.ithenaMusic.ACTION_NEXT")) {

            try {
                callback.onCallbackCalled(2);
            } catch (Exception e) {

            }
        } else if (action.equalsIgnoreCase("com.ithena.krishna.ithenaMusic.ACTION_PREV")) {
            try {
                callback.onCallbackCalled(3);
            } catch (Exception e) {

            }
        }
    }
}
