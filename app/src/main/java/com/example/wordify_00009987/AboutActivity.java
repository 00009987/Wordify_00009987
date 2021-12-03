package com.example.wordify_00009987;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // get necessary fields
        TextView usernameTv = findViewById(R.id.github_username);
        TextView bioTv = findViewById(R.id.github_bio);
        TextView infoTv = findViewById(R.id.github_info);
        ImageView avatarImg = findViewById(R.id.github_img);
        LinearLayout githubProfileContainer = findViewById(R.id.github_profile_container);

        // perform an api call
        GitHubRequest gitHubRequest = new GitHubRequest(usernameTv, bioTv, infoTv, avatarImg, githubProfileContainer);
        gitHubRequest.execute("00009987");
    }
}