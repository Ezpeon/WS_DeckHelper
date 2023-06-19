package idonthaveasite.ezpeon.wscollectionhelper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewList extends AppCompatActivity {
    private Deck deck;
    private String filedeck;
    private TextView tv = null;
    private String m_Text = "";

    private HashMap<Integer, String> viewIDtoCardAddress = null;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);
        String deckID;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                deckID = "deck1";
            } else {
                deckID = extras.getString("deckID");
            }
        } else {
            deckID = (String) savedInstanceState.getSerializable("deckID");
        }
        viewIDtoCardAddress = new HashMap<Integer, String>();
        filedeck = deckID + ".deck";
        Button namebutton = (Button) findViewById(R.id.changeName);
        //read deck file, if it does not exist create new-------------------

        try {
            deck = loadDeck(filedeck);
        } catch (RuntimeException e) {
            deck = new Deck(deckID);
            saveDeck(deck, filedeck);
        }


        namebutton.setText(deck.getName());
        final TableLayout tl = (TableLayout) findViewById(R.id.scrollTable);
        ArrayList<Card> thislist = deck.getList();
        for (Card ccc : thislist) {
            // Creation row
            final TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

            final TextView text1 = new TextView(this);
            text1.setText(ccc.getName() + "\n" +ccc.getFeatures());
            int tempID = View.generateViewId();
            viewIDtoCardAddress.put(tempID, ccc.getPNGurl());
            text1.setId (tempID);

            text1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            text1.setClickable(true);
            text1.setPadding(10,10,10,10);
            text1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String theCardYouWant = viewIDtoCardAddress.get(v.getId());
                    openCardViewer(theCardYouWant);

                }
            });




/*
            final TextView text2 = new TextView(this);
            text2.setText(ccc.getFeatures());
            text2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
*/
            tableRow.addView(text1);
            //tableRow.addView(text2);

            tl.addView(tableRow);
        }


    }


    public void saveDeck(Deck d, String filename) {//change this thing----------------------------------------------------------------------------------------------
        Gson gson = new Gson();
        String json = gson.toJson(d);
        File jsonFile = new File(getFilesDir(), filename);
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(jsonFile);

            os.write(json.getBytes());

        } catch (IOException e) {
            tv.setText(e.toString());
            e.printStackTrace();
        } finally {
            if (os != null){
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    tv.setText(e.toString());
                    e.printStackTrace();
                }
            }
        }

    }


    public Deck loadDeck(String filename) {//change this thing----------------------------------------------------------------------------------------------
        Deck d = null;
        File jsonFile = new File(getFilesDir(), filename);
        FileInputStream is = null;
        StringBuilder originalJson = new StringBuilder();

        //Toast.makeText(this, "load: variables initialized", Toast.LENGTH_LONG).show();
        //FileInputStream fis = null;
        try (FileInputStream fis = new FileInputStream(jsonFile)) {
            //Toast.makeText(this, "load: lets try it this way", Toast.LENGTH_LONG).show();
            int content;
            // reads a byte at a time, if it reached end of the file, returns -1
            while ((content = fis.read()) != -1) {
                originalJson.append((char) content);
                //System.out.println((char)content);
            }
            //Toast.makeText(this, "did we read it? " + originalJson.toString(), Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        d = gson.fromJson(originalJson.toString(), Deck.class);
        if (d.getName()==null || originalJson.toString().equals("")) {
            Toast.makeText(this, "empty file", Toast.LENGTH_LONG).show();
            throw new RuntimeException( "bubu");
        }
        //Toast.makeText(this, "last test, display the name", Toast.LENGTH_LONG).show();
        Toast.makeText(this, d.getName(), Toast.LENGTH_LONG).show();
        return d;
    }

    public void addCardEvent(View v) {


        callSetAlert();

        /*
        Card yuuki = new Card("Mothers Rosario Yuuki", "ASAO10", "S100_E026", 1, 0, 'A', 1);
        Toast.makeText(this, "adding card " + yuuki.getName(), Toast.LENGTH_LONG).show();
        deck.addCard(yuuki);*/
    }
    public void callSetAlert(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("gimme the set");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String inputset = input.getText().toString();
                callCardAlert(inputset+"");
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void callCardAlert (String set){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("gimme the card");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String inputcardname = input.getText().toString();
                attemptToAdd(set, inputcardname);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void attemptToAdd (String set, String card){
        //add different routine if set == ""  (empty string)
        File file = new File("res/raw/" + set.toLowerCase() + ".cards");
        String csvString = null;
        int resID = this.getResources().getIdentifier(set, "raw", this.getPackageName());
        try (InputStream ins = this.getResources().openRawResource(resID)) {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            int i;
            try {
                i = ins.read();
                while (i != -1)
                {
                    byteArrayOutputStream.write(i);
                    i = ins.read();
                }
                ins.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            csvString = byteArrayOutputStream.toString();
        } catch (IOException e) {

            toastString ("we got a stinky " + e.toString());
            e.printStackTrace();
        }
        String [] entries = csvString.split(";");
        toastString ("we have " + entries.length + " entries in our database");
        String name0 = card.replaceAll ("[^A-Za-z0-9]+", "").toLowerCase();
        for (int i = 0; i< entries.length; i++){
            String [] singleCard = entries[i].split(",");
            String name1 = singleCard[0];
            String name2 = singleCard[2];
            name1 = name1.replaceAll ("[^A-Za-z0-9]+", "").toLowerCase();
            name2 = name2.replaceAll ("[^A-Za-z0-9]+", "").toLowerCase();
            if(name1.equalsIgnoreCase(name0) || name2.equalsIgnoreCase(name0)){
                i = entries.length+10;
                Card addingNow = new Card(singleCard[0], singleCard[1], singleCard[2], Integer.parseInt(singleCard[3]), Integer.parseInt(singleCard[4]),singleCard[5].charAt(0) , 1);
                Toast.makeText(this, "adding card " + addingNow.getName(), Toast.LENGTH_LONG).show();
                deck.addCard(addingNow);
                saveDeck(deck, filedeck);
                Toast.makeText(this, "deck should be saved now", Toast.LENGTH_LONG).show();


            }
        }
    }

    public void nameEvent(View v) {

        Toast.makeText(this, "deck event triggered", Toast.LENGTH_LONG).show();
    }
    public void toastString (String s){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();

    }

    public void openCardViewer (String url){

        Intent intent = new Intent(this, ViewCard.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

}
