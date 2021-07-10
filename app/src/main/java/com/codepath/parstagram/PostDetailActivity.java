package com.codepath.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.codepath.parstagram.databinding.ActivityMainBinding;
import com.codepath.parstagram.databinding.ActivityPostDetailBinding;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;

public class PostDetailActivity extends AppCompatActivity {
    ActivityPostDetailBinding binding;
    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_post_detail);

        post = Parcels.unwrap(getIntent().getParcelableExtra("post"));

        binding = ActivityPostDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        
        showPostDetails();
    }

    private void showPostDetails() {
        binding.tvUsername.setText(post.getUser().getUsername());
        binding.tvUsernameUnder.setText(post.getUser().getUsername());
        binding.tvDescription.setText(post.getDescription());
        binding.tvDate.setText(post.getTimeAgo());
        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(binding.ivImage);
        }
        updateHeart();
        binding.ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post.addLike(ParseUser.getCurrentUser().getObjectId());
                updateHeart();
                binding.tvNumLikes.setText(String.valueOf(post.getNumLikes()) + " likes");
            }
        });
        binding.tvNumLikes.setText(String.valueOf(post.getNumLikes()) + " likes");
    }

    private void updateHeart() {
        ArrayList<String> likes = post.getLikes();
        if (likes != null && likes.contains(ParseUser.getCurrentUser().getObjectId())) {
            String uri = "@drawable/ufi_heart_active";  // where myresource (without the extension) is the file

            int imageResource = getResources().getIdentifier(uri, null, getPackageName());

            Drawable res = getResources().getDrawable(imageResource);
            binding.ivLike.setImageBitmap(null);
            binding.ivLike.setImageDrawable(res);
        }
    }
}