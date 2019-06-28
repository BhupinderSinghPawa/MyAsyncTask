package com.example.myasynctask;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    URL ImageUrl = null;
    InputStream is = null;
    Bitmap bmImg = null;
    ImageView imageView= null;
    TextView textView = null;
    ProgressDialog p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button=findViewById(R.id.asyncTaskButton);
        imageView=findViewById(R.id.image);
        textView=findViewById(R.id.textView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskExample asyncTask=new AsyncTaskExample();
                //asyncTask.execute("https://www.tutorialspoint.com/images/tp-logo-diamond.png");
                asyncTask.execute("https://www.cartoonhd.org.in/wp-content/uploads/2019/04/cartoonhd-official-website-1-300x300.png");
            }
        });
    }

    // When you extend the AsyncTask class you’ll need to add angle brackets and
    // provide three types corresponding to the parameters you’d like to use for the
    // ‘doInBackground’, ‘onProgressUpdate’, and ‘onPostExecute’ functions.

    private class AsyncTaskExample extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            textView.setText("Download Started");

            /* deprecated
            // Put up a progress dialog
            p = new ProgressDialog(MainActivity.this);
            p.setMessage("Please wait...It is downloading");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
            */
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                // get the passed ImageUrl
                ImageUrl = new URL(strings[0]);

                // open http URL connection, connect, getInputStream
                HttpURLConnection conn = (HttpURLConnection) ImageUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();
                is = conn.getInputStream();
                // put in Manifest
                // <uses-permission android:name = "android.permission.INTERNET"/>

                publishProgress(0);

                // decode input stream to get Bitmap
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                bmImg = BitmapFactory.decodeStream(is, null, options);

                publishProgress(100);

                // introducing a delay
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            // return Bitmap
            return bmImg;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            textView.setText("Progress is " + values[0]);

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            if(imageView!=null) {
                // p.hide();
                textView.setText("Image downloaded");
                imageView.setImageBitmap(bitmap);
            }else {
                // p.show();
            }
        }
    }

}
