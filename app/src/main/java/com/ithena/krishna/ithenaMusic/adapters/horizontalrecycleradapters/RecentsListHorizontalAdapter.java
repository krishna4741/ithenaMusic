package com.ithena.krishna.ithenaMusic.adapters.horizontalrecycleradapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ithena.krishna.ithenaMusic.models.LocalTrack;
import com.ithena.krishna.ithenaMusic.models.Track;
import com.ithena.krishna.ithenaMusic.models.UnifiedTrack;
import com.ithena.krishna.ithenaMusic.R;
import com.ithena.krishna.ithenaMusic.imageloader.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.List;


public class RecentsListHorizontalAdapter extends RecyclerView.Adapter<RecentsListHorizontalAdapter.MyViewHolder> {

    private List<UnifiedTrack> recentslyPlayed;
    Context ctx;
    ImageLoader imgLoader;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView art;
        TextView title, artist;
        RelativeLayout bottomHolder;

        public MyViewHolder(View view) {
            super(view);
            art = (ImageView) view.findViewById(R.id.backImage);
            title = (TextView) view.findViewById(R.id.card_title);
            artist = (TextView) view.findViewById(R.id.card_artist);
            bottomHolder = (RelativeLayout) view.findViewById(R.id.bottomHolder);
        }
    }

    public RecentsListHorizontalAdapter(List<UnifiedTrack> recentslyPlayed, Context ctx) {
        this.recentslyPlayed = recentslyPlayed;
        this.ctx = ctx;
        imgLoader = new ImageLoader(ctx);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card_layout2, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        UnifiedTrack ut = recentslyPlayed.get(position);
        if (ut.getType()) {
            LocalTrack lt = ut.getLocalTrack();
            imgLoader.DisplayImage(lt.getPath(), holder.art);
            holder.title.setText(lt.getTitle());
            holder.artist.setText(lt.getArtist());
        } else {
            Track t = ut.getStreamTrack();
            Picasso.with(ctx)
                    .load(t.getArtworkURL())
                    .resize(100, 100)
                    .error(R.drawable.ic_default)
                    .placeholder(R.drawable.ic_default)
                    .into(holder.art);
            holder.title.setText(t.getTitle());
            holder.artist.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return recentslyPlayed.size();
    }


}
