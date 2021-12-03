package com.example.wordify_00009987;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@SuppressLint("StaticFieldLeak")
public class GitHubRequest extends AsyncTask<String, Void, String[]> {
    private final TextView bioTv;
    private final TextView infoTv;
    private final TextView usernameTv;
    private final ImageView avatarImg;
    private final LinearLayout githubProfileContainer;

    public GitHubRequest(TextView usernameTv, TextView bioTv, TextView infoTv, ImageView avatarImg, LinearLayout githubProfileContainer) {
        this.usernameTv = usernameTv;
        this.bioTv = bioTv;
        this.infoTv = infoTv;
        this.avatarImg = avatarImg;
        this.githubProfileContainer = githubProfileContainer;
    }

    @Override
    protected String[] doInBackground(String... usernames) {
        String bio = "";
        String info = "";
        String img = "";

        try {
            // create an api call url and open connection
            URL url = new URL(String.format("https://api.github.com/users/%s", usernames[0]));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            // reading data from stream which is received over the network
            InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

            // creating a mutable string
            StringBuilder builder = new StringBuilder();
            String bufferedInput;

            while ((bufferedInput = bufferedReader.readLine()) != null) {
                builder.append(bufferedInput);
            }

            String response = builder.toString();

            // getting the json obj and values
            JSONObject obj = new JSONObject(response);
            bio = obj.getString("bio");
            String following = String.valueOf(obj.getInt("following"));
            String followers = String.valueOf(obj.getInt("followers"));
            String repositories = String.valueOf(obj.getInt("public_repos"));
            info = String.format("%s followers | %s following | %s repositories", followers, following, repositories);


            // getting the avatar img and converting bitmap to string
            img = obj.getString("avatar_url");
            URL imgUrl = new URL(img);
            Bitmap bmp = BitmapFactory.decodeStream(imgUrl.openConnection().getInputStream());
            img = BitMapToString(bmp);

        } catch (IOException | JSONException e) {
            githubProfileContainer.setVisibility(View.GONE);
            e.printStackTrace();
        }

        String[] responseArr = new String[]{bio, info, img, usernames[0]};
        return responseArr;
    }

    @Override
    protected void onPostExecute(String[] responseArr) {
        if (githubProfileContainer.getVisibility() != View.GONE) {
            githubProfileContainer.setVisibility(View.VISIBLE);
            this.bioTv.setText(responseArr[0]);
            this.infoTv.setText(responseArr[1]);
            this.avatarImg.setImageBitmap(StringToBitMap(responseArr[2]));
            this.usernameTv.setText(responseArr[3]);
        }
    }

    // source for the 2 methods below:
    // https://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa
    private String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    private Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
