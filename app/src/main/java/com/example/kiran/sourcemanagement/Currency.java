package com.example.kiran.sourcemanagement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kiran on 7/16/2017.
 */
public class Currency
{
    private long id;
    private String curName;
    private String curSymbol;
    private int val;

    public long getId() {
        return id;
    }

    public int getVal()
    {
        return val;
    }
    public void setVal(int v)
    {
        if(v<=0)
            v=1;
        val=v;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        if(curName==null||curName.trim().equals(""))
            curName="Rupees";
        this.curName = curName;
    }

    public String getCurSymbol() {
        return curSymbol;
    }

    public void setCurSymbol(String curSymbol) {
        if(curSymbol==null||curSymbol.trim().equals(""))
            curSymbol="Rs";
        this.curSymbol = curSymbol;
    }

    public Currency(long id, String curName, String curSymbol,int v) {
        this.id = id;
        if(curName==null||curName.trim().equals(""))
            curName="Rupees";
        this.curName = curName;
        if(curSymbol==null||curSymbol.trim().equals(""))
            curSymbol="Rs";
        this.curSymbol = curSymbol;
        if(v<=0)
            v=1;
        val=v;
    }

    public Currency(String curName, String curSymbol,int v) {
        if(curName==null||curName.trim().equals(""))
            curName="Rupees";
        if(curSymbol==null||curSymbol.trim().equals(""))
            curSymbol="Rs";
        if(v<=0)
            v=1;
        this.curName = curName;
        this.curSymbol = curSymbol;
        val=v;
    }
    public Currency()
    {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Currency currency = (Currency) o;

        if (id != currency.id) return false;
        if (curName != null ? !curName.equals(currency.curName) : currency.curName != null)
            return false;
        return !(curSymbol != null ? !curSymbol.equals(currency.curSymbol) : currency.curSymbol != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (curName != null ? curName.hashCode() : 0);
        result = 31 * result + (curSymbol != null ? curSymbol.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "id=" + id +
                ", curName='" + curName + '\'' +
                ", curSymbol='" + curSymbol + '\'' +
                ", curValue='" + val + '\'' +
                '}';
    }

}
