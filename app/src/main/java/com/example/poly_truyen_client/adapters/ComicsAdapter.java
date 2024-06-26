package com.example.poly_truyen_client.adapters;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poly_truyen_client.R;
import com.example.poly_truyen_client.ViewDetailsComicActivity;
import com.example.poly_truyen_client.api.ComicServices;
import com.example.poly_truyen_client.api.ConnectAPI;
import com.example.poly_truyen_client.api.HistoryServices;
import com.example.poly_truyen_client.models.Comic;
import com.example.poly_truyen_client.models.User;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComicsAdapter extends RecyclerView.Adapter<ComicsAdapter.ComicViewHolder> {

    private ArrayList<Comic> list = new ArrayList<>();
    private Context context;
    private HistoryServices historyServices;
    private ComicServices comicServices;
    private SharedPreferences sharedPreferences;
    private boolean isSmaller = false;

    public ComicsAdapter(ArrayList<Comic> list, Context context, boolean isSmaller) {
        this.list = list;
        this.context = context;
        this.isSmaller = isSmaller;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(ArrayList<Comic> listUpdate) {
        list.clear();
        list.addAll(listUpdate);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ComicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout_type;
        if (isSmaller) {
            layout_type = R.layout.comic_item;
        } else {
            layout_type = R.layout.comic_item_backup; // comic_item_backup
        }

        return new ComicViewHolder(LayoutInflater.from(parent.getContext()).inflate(layout_type, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ComicViewHolder holder, int position) {
        historyServices = new ConnectAPI().connect.create(HistoryServices.class);
        comicServices = new ConnectAPI().connect.create(ComicServices.class);

//        if (isSmaller) {
//            // Setting width, height programmatically
//            int V = context.getResources().getDimensionPixelSize(R.dimen.comic_item_smaller_v); // Get padding in pixels
//            int H = context.getResources().getDimensionPixelSize(R.dimen.comic_item_smaller_h); // Get padding in pixels
//
//            int marginInPx = context.getResources().getDimensionPixelSize(R.dimen.comic_item_smaller_margin); // Get margin in pixels
//
//            // Set CardView layout parameters
//            CardView.LayoutParams layoutParams = new CardView.LayoutParams(CardView.LayoutParams.WRAP_CONTENT, H);
//            layoutParams.setMargins(marginInPx, marginInPx, marginInPx, marginInPx);
//            holder.item_card_view.setLayoutParams(layoutParams);
//
//            // Set FrameLayout layout parameters
//            FrameLayout.LayoutParams layoutParamsFrame = new FrameLayout.LayoutParams(V, FrameLayout.LayoutParams.MATCH_PARENT);
//            holder.item_frame.setLayoutParams(layoutParamsFrame);
//
//            // Set ImageView layout parameters
//            ViewGroup.LayoutParams layoutParamsImage = holder.ivPoster.getLayoutParams();
//            layoutParamsImage.width = V;
//            layoutParamsImage.height = V;
//            holder.ivPoster.setLayoutParams(layoutParamsImage);
//
//            holder.tvName.setTextSize(12);
//
//            holder.tvCountComment.setVisibility(View.GONE);
//        }


        sharedPreferences = context.getSharedPreferences("poly_comic", Context.MODE_PRIVATE);

        User loggedUser = new Gson().fromJson(sharedPreferences.getString("user", ""), User.class);

        Comic comic = list.get(holder.getAdapterPosition());

        Log.d(TAG, "onBindViewHolder: " + comic.getName());

        Picasso.get().load(new ConnectAPI().API_URL + "images/" + comic.getPoster()).into(holder.ivPoster);
        holder.tvName.setText(comic.getName());

        holder.ivPoster.setOnClickListener(v -> {
            context.startActivity(new Intent(context, ViewDetailsComicActivity.class).putExtra("comic", new Gson().toJson(comic)));
        });

        holder.tvCountViews.setText(comic.getViews());
        holder.tvCountComment.setText(comic.getComments());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ComicViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivPoster;
        private TextView tvName, tvCountComment, tvCountViews;
        private CardView item_card_view;
        private FrameLayout item_frame;

        public ComicViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCountViews = itemView.findViewById(R.id.tvViewCount);
            item_card_view = itemView.findViewById(R.id.item_card_view);
            item_frame = itemView.findViewById(R.id.item_frame);
            ivPoster = (ImageView) itemView.findViewById(R.id.ivPoster);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvCountComment = (TextView) itemView.findViewById(R.id.tvCommentCount);

        }
    }
}
