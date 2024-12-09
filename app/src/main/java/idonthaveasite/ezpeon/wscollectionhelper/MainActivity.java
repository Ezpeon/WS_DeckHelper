package idonthaveasite.ezpeon.wscollectionhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button nlbtn = (Button) findViewById(R.id.newList);
        updateView();
        nlbtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                movetoside(v);
                return true;
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateView() { //this should be changed to actually get the deck names with Deck.getName()
        TextView tv = null;
        tv = (TextView) findViewById(R.id.deck1);
        tv.setText("<Deck N.1>");
        tv = (TextView) findViewById(R.id.deck2);
        tv.setText("<Deck N.2>");
        tv = (TextView) findViewById(R.id.deck3);
        tv.setText("<Deck N.3>");
        tv = (TextView) findViewById(R.id.deck4);
        tv.setText("<Deck N.4>");
        tv = (TextView) findViewById(R.id.deck5);
        tv.setText("<Deck N.5>");
        tv = (TextView) findViewById(R.id.deck6);
        tv.setText("<Deck N.6>");
        tv = (TextView) findViewById(R.id.deck7);
        tv.setText("<Deck N.7>");
        tv = (TextView) findViewById(R.id.deck8);
        tv.setText("<Deck N.8>");
        tv = (TextView) findViewById(R.id.deck9);
        tv.setText("<Deck N.9>");
        tv = (TextView) findViewById(R.id.deck10);
        tv.setText("<Deck N.10>");
        tv = (TextView) findViewById(R.id.deck11);
        tv.setText("<Deck N.11>");
        tv = (TextView) findViewById(R.id.deck12);
        tv.setText("<Deck N.12>");
        tv = (TextView) findViewById(R.id.deck13);
        tv.setText("<Deck N.13>");
        tv = (TextView) findViewById(R.id.deck14);
        tv.setText("<Deck N.14>");
        tv = (TextView) findViewById(R.id.deck15);
        tv.setText("<Deck N.15>");
    }

    public void newListTapped (View v) {
        Toast.makeText(this, "new list tapped debug", Toast.LENGTH_LONG).show();
    }
    public void movetoside (View v){
        Intent intent = new Intent(this, Side.class);
        startActivity(intent);
    }
    public void textclicked (View v) {
        String deckFileName =  getResources().getResourceEntryName(v.getId()) + ".deck";
        accessDeck (v, true, deckFileName);
    }
    public void accessDeck (View v, boolean canCreate, String deckFileName){
        File temp = new File (getFilesDir(), deckFileName);
        if (temp.exists()){
            Intent intent = new Intent(this, ViewList.class);
            intent.putExtra("deckID", getResources().getResourceEntryName(v.getId()));
            startActivity(intent);
        } else if (canCreate){
            Toast.makeText(this, "creating new deck", Toast.LENGTH_LONG).show();
            try {
                temp.createNewFile();
                accessDeck (v, false, deckFileName);
            } catch (IOException e) {
                Toast.makeText(this, "cannot create the file", Toast.LENGTH_LONG).show();
            }
        }
    }
}