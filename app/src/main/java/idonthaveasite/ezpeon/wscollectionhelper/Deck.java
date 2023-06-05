package idonthaveasite.ezpeon.wscollectionhelper;

import java.io.Serializable;
import java.util.ArrayList;

public class Deck implements Serializable {
    private String name;
    private ArrayList <Card> cardlist;

    public Deck (String name){
        this.name = name;
        this.cardlist = new ArrayList<Card>();
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public void addCard (Card c){
        boolean found = false;
        for (Card ccc : cardlist){
            if (ccc.same(c) && found == false){
                found = true;
                //ccc.addOne();
            }
        }
        if (!found){
            this.cardlist.add(c);
        }
    }
    public ArrayList <Card> getList (){
        return this.cardlist;
    }
}
