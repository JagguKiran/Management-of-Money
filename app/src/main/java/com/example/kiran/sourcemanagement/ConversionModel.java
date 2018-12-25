package com.example.kiran.sourcemanagement;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Kiran on 7/25/2017.
 */
public class ConversionModel {
    public ConversionModel()
    {

    }
    public String numberToWord(int num) {
        int num1=num;
        String[] w1 = {"", "One ", "Two ", "Three ", "Four ", "Five ", "Six ", "Seven ", "Eight ", "Nine "};
        String[] w10 = {"", "Ten ", "Twenty ", "Thirty ", "Fourty ", "Fifty ", "Sixty ", "Seventy ", "Eighty ", "Ninty "};
        String[] w11 = {"", "Eleven ", "Twelve ", "Thirteen ", "Fourteen ", "Fifteen ", "Sixteen ", "Seventeen ", "Eighteen ", "Ninteen "};
        String[] w100 = {"", "Hundred ", "Thousand ", "Lack ", "Crore "};
        StringBuilder sb = new StringBuilder();
        int len = 1;
        while ((int) (Math.pow(10, len)) <= num) len++;
        if(num<=0)
        {
            return "Zero ";
        }
        else
        {
            if(len>3&&(len%2==0))
                len++;
            do
            {
                if(len>3)
                {
                    int temp1=num/(int)(Math.pow(10,len-2));
                    if(temp1/10==1&&temp1%10!=0)
                        sb.append(w11[temp1%10]);
                    else
                    {
                        sb.append(w10[temp1/10]);
                        sb.append(w1[temp1%10]);
                    }
                    num=num%(int)(Math.pow(10,len-2));
                    sb.append(w100[len/2]);
                    if(num>0)
                        len=len-2;
                    else
                        len=0;
                }
                else
                {
                    int temp2=num/100;
                    int temp3=num%100;
                    if(temp2>0)
                    {
                        sb.append(w1[temp2]);
                        sb.append(w100[1]);
                    }
                    if(temp3/10==1&&temp3%10!=0)
                        sb.append(w11[temp3%10]);
                    else
                    {
                        sb.append(w10[temp3/10]);
                        sb.append(w1[temp3%10]);
                    }
                    len=0;
                }
            }while(len!=0);
            return sb.toString();
        }
    }
    public String numberToDollarWord(int num)
    {
        int len=1;
        StringBuilder sb=new StringBuilder();
        String[] w1000={"","Thousand ","Million ","Billion "};
        String[] w1 = {"", "One ", "Two ", "Three ", "Four ", "Five ", "Six ", "Seven ", "Eight ", "Nine "};
        String[] w10 = {"", "Ten ", "Twenty ", "Thirty ", "Fourty ", "Fifty ", "Sixty ", "Seventy ", "Eighty ", "Ninty "};
        String[] w11 = {"", "Eleven ", "Twelve ", "Thirteen ", "Fourteen ", "Fifteen ", "Sixteen ", "Seventeen ", "Eighteen ", "Ninteen "};
        while((int)(Math.pow(10,len))<num)len++;
        if(num<=0)
            return "Zero ";
        else
        {
            while(len>0)
            {
                while(len%3!=0)
                {
                    len++;
                }
                int temp1=num/(int)(Math.pow(10,(len-3)));
                if(temp1/100>0)
                {
                    sb.append(w1[temp1/100]);
                    sb.append("Hundred ");
                }
                int temp2=temp1%100;
                if(temp2/10==1&&temp2%10!=0)
                {
                    sb.append(w11[temp2%10]);
                }
                else
                {
                    sb.append(w10[temp2/10]);
                    sb.append(w1[temp2%10]);
                }
                num=num%(int)(Math.pow(10,(len-3)));
                sb.append(w1000[len/4]);
                len=len-3;
            }
            return sb.toString();
        }
    }
    public List<String> getPeriod()
    {
        List<String> al=new ArrayList<>();
        al.add("All Day's Report");
        al.add("Today's Report ");
        al.add("Selected FromTo Date");
        return al;
    }
    public List<String> getData()
    {
        List<String> data=new ArrayList<>();
        data.add("INCOME");
        data.add("EXPENSE");
        data.add("REPORT");
        data.add("BALANCE");
        data.add("CARDS");
        data.add("SETTINGS");
        return data;
    }
    public List<String> listAllCurrency()
    {
        List<String> al=new ArrayList<>();
        al.add("CHOOSE CURRENCY");
        al.add("SET CURRENCY");
        al.add("ADD NEW CURRENCY");
        al.add("REMOVE CURRENCY");
        al.add("LIST ALL CURRENCIES");
        return al;
    }
    public List<String> getAllTheme()
    {
        List<String> al=new ArrayList<>();
        al.add("CHOOSE THEME");
        al.add("WHITE");
        al.add("BLUE");
        al.add("YELLOW");
        al.add("GREEN");
        al.add("PINK");
        return al;
    }
    public List<String> getAllApp()
    {
        List<String> al=new ArrayList<>();
        al.add("INCOME:Record your incomes and classify them by category.In the income display you can see your income details.");
        al.add("EXPENSE:Add all the expenses you want and classify them by category.");
        al.add("REPORT:Shows the movements you have had within one day,within one week,within one month or given date.You can see all your reports of income and expenses.");
        al.add("BALANCE:Shows the balance of each card that you used in the app as a notification or directly by cash in a balance category");
        al.add("CARD:It allows you creating your own card could be credit,debts etc.Remember that all movements and categories are only visible within the card where you have created them");
        al.add("SETTINGS:There are some tools to create new categories,reset,backup and restore your database.Choose the currency format and formate data.");
        return al;
    }
    public String performDate(String s)
    {
        StringBuilder sb = new StringBuilder();
        if(s==null)
        {
            String str=new Date().toString();
            String[] as=str.split(" ");
            sb.append(as[5]);
            sb.append("-");
            sb.append(performMonth(as[1]));
            sb.append("-");
            sb.append(performDay(as[2]));
            sb.append(" ");
            sb.append(as[3]);
            return sb.toString();
        }
        else {

            String[] as = s.split(" ");
            String month = performMonth(as[0]);
            String day = as[1].substring(0, as[1].length() - 1);
            sb.append(as[2]);
            sb.append("-");
            sb.append(month);
            sb.append("-");
            sb.append(day);
            sb.append(" ");
            sb.append(as[3]);
            return sb.toString();
        }
    }
    public String performMonth(String str)
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
    public String performDay(String s) {
        String[] s1 = s.split(",");
        String s2 = s1[0];
        if (s2.trim().equals("1"))
            return "01";
        if (s2.trim().equals("2"))
            return "02";
        if (s2.trim().equals("3"))
            return "03";
        if (s2.trim().equals("4"))
            return "04";
        if (s2.trim().equals("5"))
            return "05";
        if (s2.trim().equals("6"))
            return "06";
        if (s2.trim().equals("7"))
            return "07";
        if (s2.trim().equals("8"))
            return "08";
        if (s2.trim().equals("9"))
            return "09";
        else
            return s2;
    }
    public double getTruncate(double d)
    {
        String amount=String.valueOf(d);
        if(amount.substring(amount.indexOf('.')).length()>5) {
            amount = amount.substring(0, amount.indexOf('.') + 5);
        }
        try {
            d = Double.parseDouble(amount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }
    public String convertDoubleToWord(double num)
    {
        String st3="",st4="";
        StringBuilder sb=new StringBuilder();
        String str=String.valueOf(num);
        String st1=str.substring(0,str.indexOf('.'));
        String st2=str.substring((str.indexOf('.')+1),str.length());
        try
        {
            int num2=Integer.parseInt(st1);
            st3=numberToWord(num2);
            int num3=Integer.parseInt(st2);
            st4=afterDecimal(num3);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        sb.append(st3);
        sb.append("point ");
        sb.append(st4);
        return sb.toString();
    }
    public String afterDecimal(int num)
    {
        String[] w1 = {"", "One ", "Two ", "Three ", "Four ", "Five ", "Six ", "Seven ", "Eight ", "Nine "};
        StringBuilder sb=new StringBuilder();
        int temp=num;
        int len=1;
        while(num>(int)(Math.pow(10,len)))len++;
        while(temp!=0)
        {
            int temp1=temp/(int)(Math.pow(10,(len-1)));
            sb.append(w1[temp1]);
            temp=temp%(int)(Math.pow(10,(len-1)));
            len=len-1;
        }
        sb.append("Zero Only.");
        return sb.toString();
    }
    public String convertDoubleToDollar(double num)
    {
        String result="";
        String st3="",st4="";
        StringBuilder sb=new StringBuilder();
        String str=String.valueOf(num);
        String st1=str.substring(0,str.indexOf('.'));
        String st2=str.substring((str.indexOf('.')+1),str.length());
        try
        {
            int num2=Integer.parseInt(st1);
            st3=numberToDollarWord(num2);
            int num3=Integer.parseInt(st2);
            st4=afterDecimal(num3);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        sb.append(st3);
        sb.append("point(.) ");
        sb.append(st4);
        return sb.toString();
    }
}
