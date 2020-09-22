package com.ithena.krishna.ithenaMusic.fragments.LocalMusicFragments;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ithena.krishna.ithenaMusic.models.Artist;
import com.ithena.krishna.ithenaMusic.R;

import java.util.List;


public class ArtistRecyclerAdapter extends RecyclerView.Adapter<ArtistRecyclerAdapter.MyViewHolder> {

    List<Artist> artistList;

    public ArtistRecyclerAdapter(List<Artist> artistList) {
        this.artistList = artistList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_4, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ArtistRecyclerAdapter.MyViewHolder holder, int position) {
        Artist ab = artistList.get(position);
        holder.title.setText(ab.getName());
        if (ab.getArtistSongs().size() > 1)
            holder.numSongs.setText(ab.getArtistSongs().size() + " Songs");
        else
            holder.numSongs.setText(ab.getArtistSongs().size() + " Song");
    }

    @Override
    public int getItemCount() {
        return artistList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, numSongs;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.artist_name);
            numSongs = (TextView) view.findViewById(R.id.num_songs);
        }
    }

}
