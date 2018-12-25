package com.example.kiran.sourcemanagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Kiran on 7/16/2017.
 */
public class Income {
    private long id;
    private double amt;
    private String name;
    private String date;
    private String currency;
    private String card;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getAmt() {
        return amt;
    }

    public void setAmt(double amt) {
        if(amt<0)
            throw new IllegalArgumentException("Income amount should not be negative");
        String amount=String.valueOf(amt);
        if(amount.substring(amount.indexOf('.')).length()>5) {
            amount = amount.substring(0, amount.indexOf('.') + 5);
        }
        try {
            amt = Double.parseDouble(amount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.amt = amt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name==null||name.trim().equals(""))
            throw new IllegalArgumentException("Income name should not be empty or null");
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        String s=checkDate(date);

        this.date = s;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        if(currency==null||currency.trim().equals(""))
        {
            currency="Rupees";
        }

        this.currency = currency;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        if(card==null||card.trim().equals(""))
        {
            card="By Cash";
        }

        this.card = card;
    }
    public String checkDate(String date)
    {
        StringBuilder sb=new StringBuilder();
        if(date==null||date.trim().equals(""))
        {
            String str=new Date().toString();
            String[] as=str.split(" ");
            sb.append(as[5]);
            sb.append("-");
            sb.append(performMoth(as[1]));
            sb.append("-");
            sb.append(performDay(as[2]));
            sb.append(" ");
            sb.append(as[3]);
            return sb.toString();
        }
        else if(date.split(" ").length!=2)
        {
            String[] as=date.split(" ");
            sb.append(as[2]);

            sb.append("-");
            sb.append(performMoth(as[0]));

            sb.append("-");
            sb.append(performDay(as[1]));

            sb.append(" ");
            sb.append(as[3]);

            return sb.toString();
        }
        else
            return date;
    }
    public String performMoth(String str)
    {
        if(str.trim().equals("Jan"))
            return "01";
        else if(str.trim().equals("Feb"))
            return "02";
        else if(str.trim().equals("Mar"))
            return "03";
        else if(str.trim().equals("Apr"))
            return "04";
        else if(str.trim().equals("May"))
            return "05";
        else if(str.trim().equals("Jun"))
            return "06";
        else if(str.trim().equals("Jul"))
            return "07";
        else if(str.trim().equals("Aug"))
            return "08";
        else if(str.trim().equals("Sep"))
            return "09";
        else if(str.trim().equals("Oct"))
            return "10";
        else if(str.trim().equals("Nov"))
            return "11";
        else if(str.trim().equals("Dec"))
            return "12";
        else
            return "00";
    }
    public String performDay(String s)
    {
        String[] s1=s.split(",");
        String s2=s1[0];
        if(s2.trim().equals("1"))
            return "01";
        if(s2.trim().equals("2"))
            return "02";
        if(s2.trim().equals("3"))
            return "03";
        if(s2.trim().equals("4"))
            return "04";
        if(s2.trim().equals("5"))
            return "05";
        if(s2.trim().equals("6"))
            return "06";
        if(s2.trim().equals("7"))
            return "07";
        if(s2.trim().equals("8"))
            return "08";
        if (s2.trim().equals("9"))
            return "09";
        else
            return s2;
    }

    public Income(long id, double amt, String name, String date, String currency, String card) {
        this.id = id;
        if(amt<0)
            throw new IllegalArgumentException("Income Amount should not be negative");
        String amount=String.valueOf(amt);
        if(amount.substring(amount.indexOf('.')).length()>5) {
            amount = amount.substring(0, amount.indexOf('.') + 5);
        }
        try {
            amt = Double.parseDouble(amount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.amt = amt;
        if(name==null||name.trim().equals(""))
            throw new IllegalArgumentException("Income name should not be null or empty");
        this.name = name;
        String str=checkDate(date);
        this.date = str;
        if(currency==null||currency.trim().equals(""))
            currency="Rupees";
        this.currency = currency;
        if(card==null||card.trim().equals(""))
            card="By Cash";
        this.card = card;
    }

    public Income(double amt, String name, String date, String currency, String card) {
        if(amt<0)
            throw new IllegalArgumentException("Income Amount should not be negative");
        String amount=String.valueOf(amt);
        if(amount.substring(amount.indexOf('.')).length()>5) {
            amount = amount.substring(0, amount.indexOf('.') + 5);
        }
        try {
            amt = Double.parseDouble(amount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.amt = amt;
        if(name==null||name.trim().equals(""))
            throw new IllegalArgumentException("Income name should not be null or empty");
        this.name = name;
        String str=checkDate(date);
        this.date = str;
        if(currency==null||currency.trim().equals(""))
            currency="Rupees";
        this.currency = currency;
        if(card==null||card.trim().equals(""))
            card="By Cash";
        this.card = card;
    }
    public Income()
    {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Income income = (Income) o;

        if (id != income.id) return false;
        if (Double.compare(income.amt, amt) != 0) return false;
        if (name != null ? !name.equals(income.name) : income.name != null) return false;
        if (date != null ? !date.equals(income.date) : income.date != null) return false;
        if (currency != null ? !currency.equals(income.currency) : income.currency != null)
            return false;
        return !(card != null ? !card.equals(income.card) : income.card != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        temp = Double.doubleToLongBits(amt);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (card != null ? card.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Income{" +
                "id=" + id +
                ", amt=" + amt +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", currency='" + currency + '\'' +
                ", card='" + card + '\'' +
                '}';
    }
}
