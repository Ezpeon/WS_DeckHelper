package idonthaveasite.ezpeon.wscollectionhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button nlbtn = (Button) findViewById(R.id.newList);
        nlbtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                movetoside(v);
                return true;
            }
        });
    }

    public void newListTapped (View v) {
        Toast.makeText(this, "bubu", Toast.LENGTH_LONG).show();
    }
    public void movetoside (View v){
        Intent intent = new Intent(this, Side.class);
        startActivity(intent);
    }
    public void textclicked (View v) {

        File temp = new File (getFilesDir(), getResources().getResourceEntryName(v.getId()) + ".deck");
        if (temp.exists()){
            Intent intent = new Intent(this, ViewList.class);
            intent.putExtra("deckID", getResources().getResourceEntryName(v.getId()));
            startActivity(intent);
        } else {
            Button btn = findViewById(R.id.newList);
            btn.setText("file not there, trying to create?");
            try {
                temp.createNewFile();
            } catch (IOException e) {
                Toast.makeText(this, "cannot create the file", Toast.LENGTH_LONG).show();
            }
        }

        /*
        File temp = new File (getFilesDir(), getResources().getResourceEntryName(v.getId()) + ".deck");

        Button btn = findViewById(R.id.newList);
        boolean t = temp.exists();
        btn.setText("the verdict is: " + t);*/
    }
}