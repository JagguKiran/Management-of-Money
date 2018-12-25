package com.example.kiran.sourcemanagement;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Kiran on 7/16/2017.
 */
public class Card
{
    private String cName;
    private long id;
    private String cardNumber;

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        if(cName==null||cName.trim().equals(""))
            cName="By Cash";
        this.cName = cName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        if(cardNumber==null||cardNumber.trim().equals(""))
            cardNumber="aaa0000000";
        Pattern p=Pattern.compile("[A-Za-z]{3}[0-9]{7}");
        Matcher m=p.matcher(cardNumber);
        if(m.find()&&m.group().trim().equals(cardNumber))
            this.cardNumber = cardNumber;
        else
            throw new IllegalArgumentException("Invalid Card Number");
    }

    public Card(long id,String cName, String cardNumber) {
        this.id = id;
        if(cName==null||cName.trim().equals(""))
            cName="aaa0000000";
        this.cName = cName;
        if(cardNumber==null||cardNumber.trim().equals(""))
            cardNumber="aaa0000000";
        Pattern p=Pattern.compile("[A-Za-z]{3}[0-9]{7}");
        Matcher m=p.matcher(cardNumber);
        if(m.find()&&m.group().trim().equals(cardNumber))
            this.cardNumber = cardNumber;
        else
            throw new IllegalArgumentException("Invalid Card Number");

    }

    public Card(String cName, String cardNumber) {
        if(cName==null||cName.trim().equals(""))
            cName="aaa0000000";
        this.cName = cName;
        if(cardNumber==null||cardNumber.trim().equals(""))
            cardNumber="aaa0000000";
        Pattern p=Pattern.compile("[A-Za-z]{3}[0-9]{7}");
        Matcher m=p.matcher(cardNumber);
        if(m.find()&&m.group().trim().equals(cardNumber))
            this.cardNumber = cardNumber;
        else
            throw new IllegalArgumentException("Invalid Card Number");
    }
    public Card()
    {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        if (id != card.id) return false;
        if (cName != null ? !cName.equals(card.cName) : card.cName != null) return false;
        return !(cardNumber != null ? !cardNumber.equals(card.cardNumber) : card.cardNumber != null);

    }

    @Override
    public int hashCode() {
        int result = cName != null ? cName.hashCode() : 0;
        result = 31 * result + (int) (id ^ (id >>> 32));
        result = 31 * result + (cardNumber != null ? cardNumber.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Card{" +
                "cName='" + cName + '\'' +
                ", id=" + id +
                ", cardNumber='" + cardNumber + '\'' +
                '}';
    }
}
