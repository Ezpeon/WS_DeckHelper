package idonthaveasite.ezpeon.wscollectionhelper;

import java.io.Serializable;

public class Card implements Serializable {
    private String name;
    private String edition;
    private String code;
    private int level;
    private int cost;
    private char type;
    private int amount;

    public Card (String name, String edition, String code, int level, int cost, char type, int amount){
        this.name = name;
        this.edition = edition;
        this.code = code;
        this.level = level;
        this.cost = cost;
        this.type = type;
        this.amount = amount;
    }
    public String getName (){
        return this.name;
    }
    public String getID (){
        return this.code;
    }

    public String getFeatures (){
        String r = "E: " + this.edition + " C: " + this.code + " " + this.level + this. cost + this.type + " " + this.amount;
        return r;
    }
    public boolean same (Card comparing){
        boolean eq = false;
        if (this.code.equals(comparing.getID())){
            eq = true;
        }
        return eq;
    }
    public String getPNGurl (){
        String base = "https://en.ws-tcg.com/wp/wp-content/images/cardimages/";

        return (base + this.edition + "/" + this.code + ".png");
    }
    public void addOne(){
        this.amount ++;
    }
}
