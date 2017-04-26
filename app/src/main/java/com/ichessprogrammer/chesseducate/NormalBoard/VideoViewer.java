package com.ichessprogrammer.chesseducate.NormalBoard;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.ichessprogrammer.chesseducate.R;

/**
 * Created by 남지니 on 2016-08-30.
 */
public class VideoViewer extends Activity {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        String LINK = getIntent().getStringExtra("Path");
        setContentView(R.layout.layout_activity_video);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        VideoView videoView = (VideoView) findViewById(R.id.video);
        MediaController mc = new MediaController(this);
        mc.setAnchorView(videoView);
        mc.setMediaPlayer(videoView);
        Uri video = Uri.parse(LINK);
        videoView.setMediaController(mc);
        videoView.setVideoURI(video);
        videoView.start();
    }
}
