package com.codepath.parstagram.fragments;

import android.media.Image;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.parstagram.MainActivity;
import com.codepath.parstagram.Post;
import com.codepath.parstagram.R;
import com.parse.ParseFile;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostDetailFragment extends Fragment {
    public static final String ARG_POST = "post";
    public static final String ARG_POSITION = "position";
    Post post;
    int position;
    TextView tvUsername;
    TextView tvDescription;
    TextView tvDate;
    ImageView ivImage;

    public PostDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param post Post object
     * @param position position of Post in Feed
     * @return A new instance of fragment PostDetailFragment.
     */
    public static PostDetailFragment newInstance(Post post, int position) {
        PostDetailFragment fragment = new PostDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_POST, post);
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.post = getArguments().getParcelable(ARG_POST);
            this.position = getArguments().getInt(ARG_POSITION);
        }

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                MainActivity mainActivity = (MainActivity) getActivity();
                FeedFragment fFragment = FeedFragment.newInstance(position);
                mainActivity.showProgressBar();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContainer, fFragment).commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvUsername = view.findViewById(R.id.tvUsername);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvDate = view.findViewById(R.id.tvDate);
        ivImage = view.findViewById(R.id.ivImage);
        
        showPostDetails();
    }

    private void showPostDetails() {
        tvUsername.setText(post.getUser().getUsername());
        tvDescription.setText(post.getDescription());
        tvDate.setText(post.getTimeAgo());

        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(ivImage);
        }
    }
}