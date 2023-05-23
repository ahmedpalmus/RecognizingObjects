package com.example.recognizingobjects;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.IOException;
import java.util.HashMap;

public class VideoPage extends AppCompatActivity {
String term;
WebView videoView;
    private final String URL = Server.ip + "getvideo.php";

//ImageButton play;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_page);
        term=getIntent().getStringExtra("url");

        videoView=findViewById(R.id.vid_view);

/*        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoView.start();
            }
        });
        Uri videoUri = Uri.parse(Server.ip+url);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" +                R.raw.chair));*/
getInfos();

    }

    private void getInfos() {
        class Async extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(VideoPage.this, "please waite...", "Connecting....");
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("term", term);
                String result = con.sendPostRequest(URL, data);
                return result.trim();
            }

            @Override
            protected void onPostExecute(String result) {
                loadingDialog.dismiss();

                if (result.isEmpty() || result.equals("Error"))
                    Toast.makeText(getApplicationContext(), "Check connection", Toast.LENGTH_LONG).show();
                else if (result.equals("failure")) {
                    Toast.makeText(getApplicationContext(), "No Infos", Toast.LENGTH_LONG).show();
                } else {
                    videoView.loadData("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + result + "\" frameborder=\"0\" allowfullscreen></iframe>", "text/html", "utf-8");
                    videoView.getSettings().setJavaScriptEnabled(true);
                    videoView.setWebChromeClient(new WebChromeClient() {
                    });

                }
            }
        }
        Async la = new Async();
        la.execute();
    }
}