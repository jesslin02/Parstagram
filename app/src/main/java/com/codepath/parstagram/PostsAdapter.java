package com.codepath.parstagram;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.parstagram.fragments.PostDetailFragment;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    Context context;
    List<Post> posts;

    public PostsAdapter(Context c, List<Post> p) {
        this.context = c;
        this.posts = p;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post, position);
    }

    @Override
    public int getItemCount() {
        return this.posts.size();
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Post> lst) {
        posts.addAll(lst);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUsername;
        private TextView tvUsernameUnder;
        private ImageView ivImage;
        private TextView tvDescription;
        private TextView tvDate;
        private ImageView ivLike;
        private TextView tvNumLikes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvUsernameUnder = itemView.findViewById(R.id.tvUsernameUnder);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDate = itemView.findViewById(R.id.tvDate);
            ivLike = itemView.findViewById(R.id.ivLike);
            tvNumLikes = itemView.findViewById(R.id.tvNumLikes);
        }

        public void bind(Post post, int position) {
            // Bind the post data to the view elements
            tvDescription.setText(post.getDescription());
            tvUsername.setText(post.getUser().getUsername());
            tvUsernameUnder.setText(post.getUser().getUsername());
            tvDate.setText(post.getTimeAgo());
            tvNumLikes.setText(String.valueOf(post.getNumLikes()) + " likes");
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivImage);
                ivImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, PostDetailActivity.class);
                        i.putExtra("post", Parcels.wrap(post));
                        context.startActivity(i);
                    }
                });
            }
            updateHeart(post);
            ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    post.addLike(ParseUser.getCurrentUser().getObjectId());
                    updateHeart(post);
                    tvNumLikes.setText(String.valueOf(post.getNumLikes()) + " likes");
                }
            });
        }

        private void updateHeart(Post post) {
            String uri = "@drawable/ufi_heart_icon";  // where myresource (without the extension) is the file

            int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());

            Drawable res = context.getResources().getDrawable(imageResource);
            ivLike.setImageBitmap(null);
            ivLike.setImageDrawable(res);


            ArrayList<String> likes = post.getLikes();
            String currentId = ParseUser.getCurrentUser().getObjectId();
            if (likes != null && likes.contains(currentId)) {
                String uri_active = "@drawable/ufi_heart_active";  // where myresource (without the extension) is the file

                int imageResource_active = context.getResources().getIdentifier(uri_active, null, context.getPackageName());

                Drawable res_active = context.getResources().getDrawable(imageResource_active);
                ivLike.setImageBitmap(null);
                ivLike.setImageDrawable(res_active);
            }
        }

    }


}
