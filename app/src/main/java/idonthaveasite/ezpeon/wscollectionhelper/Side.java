package idonthaveasite.ezpeon.wscollectionhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Side extends AppCompatActivity {


    private Button buttontest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side);
        buttontest = (Button) findViewById(R.id.buttonothertest1);

        ImageView img = (ImageView) findViewById(R.id.imageTest);

        buttontest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonupdate (buttontest);
                buttonfunction (img);
            }
        });

    }


    public void buttonfunction (ImageView img) {

        img.setBackgroundResource(R.drawable.s102_te16sp);

    }
    public void buttonupdate (Button btn) {

        btn.setText("button was tapped");

    }
}