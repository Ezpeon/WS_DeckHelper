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
    private String filedeck; // also known as deckID
    //we display deck.getName()
    private TextView tv = null;
    private String m_Text = "";
    private Button nameButton = null;

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
        nameButton= (Button) findViewById(R.id.changeName);
        //read deck file, if it does not exist create new-------------------

        try {
            deck = loadDeck(filedeck);
        } catch (RuntimeException e) {
            deck = new Deck(deckID);
            saveDeck(deck, filedeck);
        }

        updateView();
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

        try (FileInputStream fis = new FileInputStream(jsonFile)) {
            int content;
            while ((content = fis.read()) != -1) {
                originalJson.append((char) content);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        d = gson.fromJson(originalJson.toString(), Deck.class);
        if (d.getName()==null || originalJson.toString().equals("")) {
            Toast.makeText(this, "empty file", Toast.LENGTH_LONG).show();
            throw new RuntimeException( "problem loading deck");
        }
        return d;
    }

    public void addCardEvent(View v) {
        callSetAlert();
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
                String sanitizedSet = inputset.replaceAll ("[^A-Za-z0-9]+", "").toLowerCase();
                //toastString ("received input: " + inputset);
                if (sanitizedSet.equalsIgnoreCase("")){
                    toastString ("null or malformed input");
                } else {
                    String setsCsv = readRawFile("sets");

                    String [] entries = setsCsv.split(";");
                    int n = entries.length;
                    boolean found = false;
                    int i = 0;
                    boolean stayIn = true;
                    while (stayIn){
                        String removedLineBreaks = entries[i].replaceAll("[\r\n]+", "");
                        if (removedLineBreaks.equalsIgnoreCase(sanitizedSet)) {
                            toastString ("set " + sanitizedSet + " found");
                            found = true;
                            stayIn = false;
                        } else {
                            i++;
                        }
                        if (i >= n){
                            stayIn = false;
                        }
                    }
                    if (found){
                        callCardAlert(sanitizedSet+"");
                    } else {
                        toastString ("set " + inputset + "not found");
                    }
                }
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
                String inputCardName = input.getText().toString();
                String sanitizedCardName = inputCardName.replaceAll ("[^A-Za-z0-9]+", "").toLowerCase();
                attemptToAdd(set, sanitizedCardName); // checks are made in the next function - maybe should be implemented differently
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
        //File file = new File("res/raw/" + set.toLowerCase() + ".cards");
        String csvString = readRawFile(set);

        String [] entries = csvString.split(";");
        toastString ("we found " + entries.length + " cards in the set " + set);
        for (int i = 0; i< entries.length; i++){
            String [] singleCard = entries[i].split(",");
            String name1 = singleCard[0];
            String name2 = singleCard[2];
            name1 = name1.replaceAll ("[^A-Za-z0-9]+", "").toLowerCase();
            name2 = name2.replaceAll ("[^A-Za-z0-9]+", "").toLowerCase();
            if(name1.equalsIgnoreCase(card) || name2.equalsIgnoreCase(card)){
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("rename deck");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName = input.getText().toString();
                deck.setName (newName);
                saveDeck(deck, filedeck);
                updateView();
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
    public void toastString (String s){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
        System.out.println ("toasting: " + s);
    }

    public void openCardViewer (String url){

        Intent intent = new Intent(this, ViewCard.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    public String readRawFile (String filename){ // reads "filename" from raw folder into a string
        String fileContent = null;
        int resID = this.getResources().getIdentifier(filename, "raw", this.getPackageName());
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
            fileContent = byteArrayOutputStream.toString();
        } catch (IOException e) {
            toastString ("IOexception while reading" + e.toString());
            e.printStackTrace();
        }
        return fileContent; // remember to check before using it
    }

    void updateView() {
        nameButton.setText(deck.getName());
    }
}

