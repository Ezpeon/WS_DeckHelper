package idonthaveasite.ezpeon.wscollectionhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.InputStream;

public class ViewCard extends AppCompatActivity {
    private Button loadImage = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_card);
        String pngurl;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                pngurl = "https://en.ws-tcg.com/wp/wp-content/images/cardimages/AZL/S102_TE16SP.png";
            } else {
                pngurl = extras.getString("url");
            }
        } else {
            pngurl = (String) savedInstanceState.getSerializable("url");
        }



        loadImage = (Button) findViewById(R.id.loadImageBtn);

        ImageView img = (ImageView) findViewById(R.id.cardImage);

        loadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayImage (img, pngurl);
            }
        });

    }

    public void displayImage (ImageView v, String url){

        if (url.equals("https://en.ws-tcg.com/wp/wp-content/images/cardimages/AZL/S102_TE16SP.png")){//remove first part of the string
            //we have this one in drawables for testing
            v.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.s102_te16sp));
        } else {
            //we have to download it
            new DownloadImageTask((ImageView) v).execute(url);
        }
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                //Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
