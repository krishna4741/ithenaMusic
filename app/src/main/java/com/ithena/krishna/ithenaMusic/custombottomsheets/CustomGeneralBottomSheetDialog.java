package com.ithena.krishna.ithenaMusic.custombottomsheets;

import android.app.Activity;
import android.os.Bundle;
//import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ithena.krishna.ithenaMusic.activities.HomeActivity;
import com.ithena.krishna.ithenaMusic.models.UnifiedTrack;
import com.ithena.krishna.ithenaMusic.R;
import com.ithena.krishna.ithenaMusic.imageloader.ImageLoader;



public class CustomGeneralBottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    LinearLayout playText, playNextText, addToQueueText, addToPlaylistText, addToFavouriteText;

    ImageView generalSongImage;
    TextView generalSongTitle, generalSongArtist;

    HomeActivity activity;

    int position = 0;
    String fragment;
    UnifiedTrack generalTrack;

    ImageLoader imgLoader;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (HomeActivity) activity;
        imgLoader = new ImageLoader(activity);
    }

    public void setPosition(int pos) {
        position = pos;
    }

    public void setTrack(UnifiedTrack generalTrack) {
        this.generalTrack = generalTrack;
    }

    public void setFragment(String fragment) {
        this.fragment = fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.general_song_bottom_sheet, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        generalSongImage = (ImageView) view.findViewById(R.id.general_song_bottom_sheet_image);
        generalSongTitle = (TextView) view.findViewById(R.id.general_song_bottom_sheet_title);
        generalSongArtist = (TextView) view.findViewById(R.id.general_song_bottom_sheet_artist);

        if (generalTrack.getType()) {
            imgLoader.DisplayImage(generalTrack.getLocalTrack().getPath(), generalSongImage);
            generalSongTitle.setText(generalTrack.getLocalTrack().getTitle());
            generalSongArtist.setText(generalTrack.getLocalTrack().getArtist());
        } else {
            imgLoader.DisplayImage(generalTrack.getStreamTrack().getArtworkURL(), generalSongImage);
            generalSongTitle.setText(generalTrack.getStreamTrack().getTitle());
            generalSongArtist.setText("");
        }

        playText = (LinearLayout) view.findViewById(R.id.general_song_bottom_sheet_play_wrapper);
        playText.setOnClickListener(this);
        playNextText = (LinearLayout) view.findViewById(R.id.general_song_bottom_sheet_play_next_wrapper);
        playNextText.setOnClickListener(this);
        addToQueueText = (LinearLayout) view.findViewById(R.id.general_song_bottom_sheet_add_to_queue_wrapper);
        addToQueueText.setOnClickListener(this);
        addToPlaylistText = (LinearLayout) view.findViewById(R.id.general_song_bottom_sheet_add_to_playlist_wrapper);
        addToPlaylistText.setOnClickListener(this);
        addToFavouriteText = (LinearLayout) view.findViewById(R.id.general_song_bottom_sheet_add_to_fav_wrapper);
        addToFavouriteText.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.general_song_bottom_sheet_play_wrapper:
                activity.bottomSheetListener(position, "Play", fragment, generalTrack.getType());
                break;
            case R.id.general_song_bottom_sheet_play_next_wrapper:
                activity.bottomSheetListener(position, "Play Next", fragment, generalTrack.getType());
                break;
            case R.id.general_song_bottom_sheet_add_to_queue_wrapper:
                activity.bottomSheetListener(position, "Add to Queue", fragment, generalTrack.getType());
                break;
            case R.id.general_song_bottom_sheet_add_to_playlist_wrapper:
                activity.bottomSheetListener(position, "Add to Playlist", fragment, generalTrack.getType());
                break;
            case R.id.general_song_bottom_sheet_add_to_fav_wrapper:
                activity.bottomSheetListener(position, "Add to Favourites", fragment, generalTrack.getType());
                break;
        }
        dismiss();
    }
}
