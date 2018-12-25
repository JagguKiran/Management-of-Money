package com.example.kiran.sourcemanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.transform.dom.DOMLocator;

/**
 * Created by Kiran on 8/11/2017.
 */
public class ActDatabase extends SQLiteOpenHelper
{
    Context context;
    public ActDatabase(Context context,String name,SQLiteDatabase.CursorFactory cursorFactory,int version)
    {
        super(context,name,null,version);
        this.context=context;
    }
    public static final String DBNAME = "sourcemanagement";
    public static final int DBVERSION = 1;

    public static final String TBLINCOME = "Income";
    public static final String TBLEEXPENSE = "Expense";
    public static final String TBLCARD = "Card";
    public static final String TBLCURRENCY = "Currency";

    public static final String COLINCOMEID = "iid";
    public static final String COLINNAME = "income_name";
    public static final String COLINA = "income_amount";
    public static final String COLINDATE = "income_date";
    public static final String COLINCARD = "income_card";
    public static final String COLINCURRENCY = "income_currency";

    public static final String COLEXPENSEID = "eid";
    public static final String COLEXNAME = "expense_name";
    public static final String COLEXA = "expense_amount";
    public static final String COLEXDATE = "expense_date";
    public static final String COLINCOMEEXPENSEID = "ieid";

    public static final String COLCARDID = "cid";
    public static final String COLCARDNAME = "card_name";
    public static final String COLCARDNUM = "card_num";

    public static final String COLCURRENCYID = "kid";
    public static final String COLCURRENCYNAME = "currency_name";
    public static final String COLCURRENCYSYMBOL = "currency_symbol";
    public static final String COLCURRENCYVALUE="currency_value";

    public String createCard() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ");
        sb.append(TBLCARD);
        sb.append("(");
        sb.append(COLCARDID);
        sb.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb.append(COLCARDNAME);
        sb.append(" TEXT NOT NULL,");
        sb.append(COLCARDNUM);
        sb.append(" TEXT NOT NULL)");
        return sb.toString();
    }

    public String createCurrency() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ");
        sb.append(TBLCURRENCY);
        sb.append("(");
        sb.append(COLCURRENCYID);
        sb.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb.append(COLCURRENCYNAME);
        sb.append(" TEXT NOT NULL,");
        sb.append(COLCURRENCYSYMBOL);
        sb.append(" TEXT NOT NULL,");
        sb.append(COLCURRENCYVALUE);
        sb.append(" REAL NOT NULL)");
        return sb.toString();
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    public String createExpense() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ");
        sb.append(TBLEEXPENSE);
        sb.append("(");
        sb.append(COLEXPENSEID);
        sb.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb.append(COLEXNAME);
        sb.append(" TEXT NOT NULL,");
        sb.append(COLEXA);
        sb.append(" REAL NOT NULL,");
        sb.append(COLEXDATE);
        sb.append(" DATETIME NOT NULL,");
        sb.append(COLINCOMEEXPENSEID);
        sb.append(" INTEGER,FOREIGN KEY(");
        sb.append(COLINCOMEEXPENSEID);
        sb.append(") REFERENCES ");
        sb.append(TBLINCOME);
        sb.append("(");
        sb.append(COLINCOMEID);
        sb.append(") ON DELETE CASCADE)");
        return sb.toString();
    }

    public String createIncome() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ");
        sb.append(TBLINCOME);
        sb.append("(");
        sb.append(COLINCOMEID);
        sb.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb.append(COLINNAME);
        sb.append(" TEXT NOT NULL,");
        sb.append(COLINA);
        sb.append(" REAL NOT NULL,");
        sb.append(COLINDATE);
        sb.append(" DATETIME NOT NULL,");
        sb.append(COLINCARD);
        sb.append(" TEXT NOT NULL,");
        sb.append(COLINCURRENCY);
        sb.append(" TEXT NOT NULL)");
        return sb.toString();
    }
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(createCard());
            db.execSQL(createCurrency());
            db.execSQL(createIncome());
            db.execSQL(createExpense());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TBLCARD + " IF EXISTS");
        db.execSQL("DROP TABLE " + TBLCURRENCY + " IF EXISTS");
        db.execSQL("DROP TABLE " + TBLEEXPENSE + " IF EXISTS");
        db.execSQL("DROP TABLE " + TBLINCOME + " IF EXISTS");
        onCreate(db);
    }
    public void columnName(String name) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(name, null, null, null, null, null, null);
        String[] as = c.getColumnNames();
        for (int i = 0; i < as.length; i++) {
            Log.d("KIRANKUMARUPPIN", as[i]);
        }
    }
    public boolean addCard(Card c)
    {
        c.setcName(c.getcName().toUpperCase());
        SQLiteDatabase db=getWritableDatabase();
        ContentValues val=new ContentValues();
        val.put(COLCARDNAME, c.getcName());
        val.put(COLCARDNUM, c.getCardNumber());
        long id=db.insert(TBLCARD, null, val);
        if(id!=-1)
            return true;
        return false;
    }
    public List<Card> getAllCard()
    {
        SQLiteDatabase db=getReadableDatabase();
        List<Card> al=new ArrayList<>();
        Cursor c=db.query(TBLCARD, null, null, null, null, null, null);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            do {
                Card card=new Card();
                card.setId(c.getLong(c.getColumnIndex(COLCARDID)));
                card.setcName(c.getString(c.getColumnIndex(COLCARDNAME)));
                card.setCardNumber(c.getString(c.getColumnIndex(COLCARDNUM)));
                al.add(card);
            }while(c.moveToNext());
        }
        return al;
    }
    public Card getCard(String name)
    {
        name=name.toUpperCase();
        SQLiteDatabase db=getReadableDatabase();
        Card card=new Card();
        Cursor c=db.query(TBLCARD, null, COLCARDNAME + "=?", new String[]{name}, null, null, null);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            card.setId(c.getLong(c.getColumnIndex(COLCARDID)));
            card.setcName(c.getString(c.getColumnIndex(COLCARDNAME)));
            card.setCardNumber(c.getString(c.getColumnIndex(COLCARDNUM)));
        }
        return card;
    }
    public boolean deleteCard(long id)
    {
        SQLiteDatabase db=getWritableDatabase();
        long idd=db.delete(TBLCARD, COLCARDID + "=?", new String[]{String.valueOf(id)});
        if(idd!=-1)
            return true;
        return false;
    }
    public boolean updateCard(Card card)
    {
        card.setcName(card.getcName().toUpperCase());
        SQLiteDatabase db=getWritableDatabase();
        ContentValues val=new ContentValues();
        val.put(COLCARDNAME,card.getcName());
        val.put(COLCARDNUM,card.getCardNumber());
        long id=db.update(TBLCARD, val, COLCARDID + "=?", new String[]{String.valueOf(card.getId())});
        if(id!=-1)
            return true;
        return false;
    }
    public List<String> getAllCardNames()
    {
        SQLiteDatabase db=getWritableDatabase();
        List<String> al=new ArrayList<>();
        Cursor c=db.query(TBLCARD, null, null, null, null, null, null, null);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            do {
                String str=c.getString(c.getColumnIndex(COLCARDNAME));
                al.add(str.toUpperCase());
            }while(c.moveToNext());
        }
        return al;
    }
    public boolean addCurrency(Currency c)
    {
        c.setCurName(c.getCurName().toUpperCase());
        SQLiteDatabase db=getWritableDatabase();
        ContentValues val=new ContentValues();
        val.put(COLCURRENCYNAME,c.getCurName());
        val.put(COLCURRENCYSYMBOL,c.getCurSymbol());
        val.put(COLCURRENCYVALUE,c.getVal());
        long id=db.insert(TBLCURRENCY, null, val);
        if(id!=-1)
            return true;
        return false;
    }
    public Currency getCurrency(String name)
    {
        name=name.toUpperCase();
        SQLiteDatabase db=getReadableDatabase();
        Currency cur=null;
        Cursor c=db.query(TBLCURRENCY, null, COLCURRENCYNAME + "=?", new String[]{name}, null, null, null);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            cur=new Currency();
            cur.setId(c.getLong(c.getColumnIndex(COLCURRENCYID)));
            cur.setCurName(c.getString(c.getColumnIndex(COLCURRENCYNAME)).toUpperCase());
            cur.setCurSymbol(c.getString(c.getColumnIndex(COLCURRENCYSYMBOL)));
            cur.setVal(c.getInt(c.getColumnIndex(COLCURRENCYVALUE)));
        }
        return cur;
    }
    public List<Currency> getAllCurrency()
    {
        List<Currency> al=new ArrayList<>();
        SQLiteDatabase db=getReadableDatabase();
        Cursor c=db.query(TBLCURRENCY, null, null, null, null, null, null, null);
        if(c.getCount()>0) {
            c.moveToFirst();
            do {
                Currency cur = new Currency();
                cur.setId(c.getLong(c.getColumnIndex(COLCURRENCYID)));
                cur.setCurName(c.getString(c.getColumnIndex(COLCURRENCYNAME)).toUpperCase());
                cur.setCurSymbol(c.getString(c.getColumnIndex(COLCURRENCYSYMBOL)));
                cur.setVal(c.getInt(c.getColumnIndex(COLCURRENCYVALUE)));
                al.add(cur);
            }while(c.moveToNext());
        }
        return al;
    }
    public List<String> getAllCurrencyName()
    {
        List<String> al=new ArrayList<>();
        SQLiteDatabase db=getReadableDatabase();
        Cursor c=db.query(TBLCURRENCY, null, null, null, null, null, null, null);
        if(c.getCount()>0) {
            c.moveToFirst();
            do {
                String str=c.getString(c.getColumnIndex(COLCURRENCYNAME));
                al.add(str.toUpperCase());
            }while(c.moveToNext());
        }
        return al;
    }
    public boolean deleteCurrency(String name)
    {
        name=name.toUpperCase();
        SQLiteDatabase db=getWritableDatabase();
        long id=db.delete(TBLCURRENCY, COLCURRENCYNAME + "=?", new String[]{String.valueOf(name)});
        if(id!=-1)
            return true;
        return false;
    }
    public boolean addIncome(Income income)
    {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues val=new ContentValues();
        val.put(COLINNAME,income.getName().toLowerCase());
        val.put(COLINA,income.getAmt());
        val.put(COLINDATE,income.getDate());
        val.put(COLINCARD,income.getCard().toUpperCase());
        val.put(COLINCURRENCY,income.getCurrency().toUpperCase());
        long id=db.insert(TBLINCOME, null, val);
        if(id!=-1)
            return true;
        return false;
    }
    public List<Income> getAllIncome()
    {
        SQLiteDatabase db=getReadableDatabase();
        List<Income> al=new ArrayList<>();
        Cursor c=db.query(TBLINCOME, null, null, null, null, null, null);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            do {
                Income income=new Income();
                income.setId(c.getLong(c.getColumnIndex(COLINCOMEID)));
                income.setName(c.getString(c.getColumnIndex(COLINNAME)).toUpperCase());
                income.setAmt(c.getDouble(c.getColumnIndex(COLINA)));
                income.setDate(c.getString(c.getColumnIndex(COLINDATE)));
                income.setCard(c.getString(c.getColumnIndex(COLINCARD)).toUpperCase());
                income.setCurrency(c.getString(c.getColumnIndex(COLINCURRENCY)).toUpperCase());
                al.add(income);
            }while (c.moveToNext());
        }
        return al;
    }
    public List<Income> getAllIncomeByDate()
    {
        SQLiteDatabase db=getReadableDatabase();
        List<Income> al=new ArrayList<>();
        Cursor c=db.query(TBLINCOME, null, null, null, null, null, COLINDATE);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            do {
                Income income=new Income();
                income.setId(c.getLong(c.getColumnIndex(COLINCOMEID)));
                income.setName(c.getString(c.getColumnIndex(COLINNAME)).toUpperCase());
                income.setAmt(c.getDouble(c.getColumnIndex(COLINA)));
                income.setDate(c.getString(c.getColumnIndex(COLINDATE)));
                income.setCard(c.getString(c.getColumnIndex(COLINCARD)));
                income.setCurrency(c.getString(c.getColumnIndex(COLINCURRENCY)));
                al.add(income);
            }while (c.moveToNext());
        }
        return al;
    }
    public List<Income> getAllTodaysIncome()
    {
        SQLiteDatabase db=getReadableDatabase();
        Date d=new Date();
        String name="";
        List<Income> al=new ArrayList<>();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        try
        {
            name=sdf.format(d);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        String name1=name+" "+"00:00:00";
        String name2=name+" "+"23:59:59";
        Cursor c=db.query(TBLINCOME, null, COLINDATE + " BETWEEN " + "'" + name1 + "'" + " AND " + "'" + name2 + "'", null, null, null, COLINDATE);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            do {
                Income income = new Income();
                income.setId(c.getLong(c.getColumnIndex(COLINCOMEID)));
                income.setName(c.getString(c.getColumnIndex(COLINNAME)).toUpperCase());
                income.setAmt(c.getDouble(c.getColumnIndex(COLINA)));
                income.setDate(c.getString(c.getColumnIndex(COLINDATE)));
                income.setCard(c.getString(c.getColumnIndex(COLINCARD)));
                income.setCurrency(c.getString(c.getColumnIndex(COLINCURRENCY)));
                al.add(income);
            }while(c.moveToNext());
        }
        return al;
    }
    public boolean updateIncome(Income income)
    {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues val=new ContentValues();
        val.put(COLINNAME,income.getName().toLowerCase());
        val.put(COLINA,income.getAmt());
        val.put(COLINDATE,income.getDate().toUpperCase());
        val.put(COLINCARD,income.getCard().toUpperCase());
        val.put(COLINCURRENCY, income.getCurrency());
        long id=db.update(TBLINCOME, val, COLINCOMEID + "=?", new String[]{String.valueOf(income.getId())});
        if(id!=-1)
            return true;
        return false;
    }
    public boolean deleteIncome(long id)
    {
        SQLiteDatabase db=getWritableDatabase();
        long idd=db.delete(TBLINCOME, COLINCOMEID + "=?", new String[]{String.valueOf(id)});
        if(idd!=-1)
            return true;
        return false;
    }
    public Income getIncome(long id)
    {
        Income income=new Income();
        SQLiteDatabase db=getReadableDatabase();
        Cursor c=db.query(TBLINCOME, null, COLINCOMEID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            income.setId(id);
            income.setName(c.getString(c.getColumnIndex(COLINNAME)).toUpperCase());
            income.setAmt(c.getDouble(c.getColumnIndex(COLINA)));
            income.setDate(c.getString(c.getColumnIndex(COLINDATE)));
            income.setCard(c.getString(c.getColumnIndex(COLINCARD)));
            income.setCurrency(c.getString(c.getColumnIndex(COLINCURRENCY)));
        }
        return income;
    }
    public List<String> getAllIncomeName()
    {
        SQLiteDatabase db=getReadableDatabase();
        List<String> al=new ArrayList<>();
        Cursor c=db.query(TBLINCOME, null, null, null, null, null, null);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            do {
                String str=c.getString(c.getColumnIndex(COLINNAME));
                al.add(str.toUpperCase());
            }while(c.moveToNext());
        }
        return al;
    }
    public List<Income> getAllIncomeByAmount()
    {
        SQLiteDatabase db=getReadableDatabase();
        List<Income> al=new ArrayList<>();
        Cursor c=db.query(TBLINCOME, null, null, null, null, null, COLINA + " DESC");
        if(c.getCount()>0)
        {
            c.moveToFirst();
            do {
                Income income=new Income();
                income.setId(c.getLong(c.getColumnIndex(COLINCOMEID)));
                income.setName(c.getString(c.getColumnIndex(COLINNAME)).toUpperCase());
                income.setAmt(c.getDouble(c.getColumnIndex(COLINA)));
                income.setDate(c.getString(c.getColumnIndex(COLINDATE)));
                income.setCard(c.getString(c.getColumnIndex(COLINCARD)));
                income.setCurrency(c.getString(c.getColumnIndex(COLINCURRENCY)));
                al.add(income);
            }while(c.moveToNext());
        }
        return al;
    }
    public List<Income> getAllIncomeGivenDate(String fromDate,String toDate)
    {
        SQLiteDatabase db=getReadableDatabase();
        fromDate=getCorrectDate(fromDate);
        toDate=getCorrectDate(toDate);
        List<Income> al=new ArrayList<>();
        Cursor c=db.query(TBLINCOME, null, COLINDATE + " BETWEEN " + "'" + fromDate + "'" + " AND " + "'" + toDate + "'", null, null, null, COLINDATE);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            do {
                c.moveToFirst();
                Income income=new Income();
                income.setId(c.getLong(c.getColumnIndex(COLINCOMEID)));
                income.setName(c.getString(c.getColumnIndex(COLINNAME)).toUpperCase());
                income.setAmt(c.getDouble(c.getColumnIndex(COLINA)));
                income.setDate(c.getString(c.getColumnIndex(COLINDATE)));
                income.setCard(c.getString(c.getColumnIndex(COLINCARD)));
                income.setCurrency(c.getString(c.getColumnIndex(COLINCURRENCY)));
                al.add(income);
            }while(c.moveToNext());
        }
        return al;
    }
    public Income getIncome(String name)
    {
        name=name.toLowerCase();
        SQLiteDatabase db=getReadableDatabase();
        Income income=null;
        Cursor c=db.query(TBLINCOME, null, COLINNAME + "=?", new String[]{name}, null, null, null);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            income=new Income();
            income.setId(c.getLong(c.getColumnIndex(COLINCOMEID)));
            income.setName(c.getString(c.getColumnIndex(COLINNAME)).toUpperCase());
            income.setAmt(c.getDouble(c.getColumnIndex(COLINA)));
            income.setDate(c.getString(c.getColumnIndex(COLINDATE)));
            income.setCard(c.getString(c.getColumnIndex(COLINCARD)));
            income.setCurrency(c.getString(c.getColumnIndex(COLINCURRENCY)).toUpperCase());
        }
        return income;
    }
    public List<Float> getAllIncomeAmount()
    {
        SQLiteDatabase db=getReadableDatabase();
        List<Float> al=new ArrayList<>();
        Cursor c=db.query(TBLINCOME,null,null,null,null,null,null);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            do {
                Currency currency=getCurrency(c.getString(c.getColumnIndex(COLINCURRENCY)));
                double b=1.0*currency.getVal()/SettingAct.set_currency_value;
                double a=c.getDouble(c.getColumnIndex(COLINA));
                float cc=(float)(a*b);
                al.add(cc);
            }while (c.moveToNext());
        }
        return al;
    }
    public List<Float> getAllExpenseAmount()
    {
        SQLiteDatabase db=getReadableDatabase();
        List<Float> al=new ArrayList<>();
        Cursor c=db.query(TBLEEXPENSE,null,null,null,null,null,null);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            do {
                long id=c.getLong(c.getColumnIndex(COLINCOMEEXPENSEID));
                Income income=getIncome(id);
                Currency cur=getCurrency(income.getCurrency());
                double b=1.0*cur.getVal()/SettingAct.set_currency_value;
                double a=c.getDouble(c.getColumnIndex(COLEXA));
                float cc=(float)(a*b);
                al.add(cc);
            }while (c.moveToNext());
        }
        return al;
    }
    public List<Expense> getAllExpenseByIncomeName(String name)
    {
       List<Expense> al=new ArrayList<>();
        return al;
    }
    public List<Expense> getAllExpenseByName(String name)
    {
        SQLiteDatabase db=getReadableDatabase();
        List<Expense> al=new ArrayList<>();
        Cursor c=db.query(TBLEEXPENSE,null,COLEXNAME+"=?",new String[]{name},null,null,null);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            do {
                Expense expense = new Expense();
                expense.setEid(c.getLong(c.getColumnIndex(COLEXPENSEID)));
                expense.seteName(c.getString(c.getColumnIndex(COLEXNAME)).toUpperCase());
                expense.setAmt(c.getDouble(c.getColumnIndex(COLEXA)));
                expense.seteDate(c.getString(c.getColumnIndex(COLEXDATE)));
                expense.setIid(c.getLong(c.getColumnIndex(COLINCOMEEXPENSEID)));
                al.add(expense);
            }while(c.moveToNext());
        }
        return al;
    }
    public List<String> getExpenseNamesByCard(String name)
    {
        SQLiteDatabase db=getReadableDatabase();
        List<String> al=new ArrayList<>();
        Cursor c=db.query(TBLEEXPENSE, null, null, null, null, null, null);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            do {
                long id=c.getLong(c.getColumnIndex(COLINCOMEEXPENSEID));
                Income income=getIncome(id);
                if(income.getCard().trim().equals(name))
                {
                    String str=c.getString(c.getColumnIndex(COLEXNAME));
                    al.add(str.toUpperCase());
                }
            }while(c.moveToNext());
        }
        return al;
    }
    public List<Float> getExpenseAmountsByCard(String name)
    {
        SQLiteDatabase db=getReadableDatabase();
        List<Float> al=new ArrayList<>();
        Cursor c=db.query(TBLEEXPENSE, null, null, null, null, null, null);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            do {
                long id=c.getLong(c.getColumnIndex(COLINCOMEEXPENSEID));
                Income income=getIncome(id);
                Currency currency=getCurrency(income.getCurrency());
                double d=1.0*currency.getVal()/SettingAct.set_currency_value;
                if(income.getCard().trim().equalsIgnoreCase(name))
                {
                    double amt=c.getDouble(c.getColumnIndex(COLEXA));
                    float amount=(float)(d*amt);
                    al.add(amount);
                }
            }while(c.moveToNext());
        }
        return al;
    }
    public boolean addExpense(Expense expense)
    {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues val=new ContentValues();
        val.put(COLEXNAME,expense.geteName().toLowerCase());
        val.put(COLEXA,expense.getAmt());
        val.put(COLEXDATE,expense.geteDate());
        val.put(COLINCOMEEXPENSEID,expense.getIid());
        long id=db.insert(TBLEEXPENSE, null, val);
        if(id!=-1)
            return true;
        return false;
    }
    public List<Expense> getAllTodayExpense()
    {
        SQLiteDatabase db=getReadableDatabase();
        List<Expense> al=new ArrayList<>();
        Date d=new Date();
        String name="";
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        try
        {
            name=sdf.format(d);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        String name1=name+" "+"00:00:00";
        String name2=name+" "+"23:59:59";
        Cursor c=db.query(TBLEEXPENSE, null, COLEXDATE + " BETWEEN " + "'" + name1 + "'" + " AND " + "'" + name2 + "'", null, null, null, COLEXDATE);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            do {
                Expense expense = new Expense();
                expense.setEid(c.getLong(c.getColumnIndex(COLEXPENSEID)));
                expense.seteName(c.getString(c.getColumnIndex(COLEXNAME)).toUpperCase());
                expense.setAmt(c.getDouble(c.getColumnIndex(COLEXA)));
                expense.seteDate(c.getString(c.getColumnIndex(COLEXDATE)));
                expense.setIid(c.getLong(c.getColumnIndex(COLINCOMEEXPENSEID)));
                al.add(expense);
            }while(c.moveToNext());
        }
        return al;
    }
    public List<Expense> getAllExpenseGivenDate(String fromDate,String toDate)
    {
        SQLiteDatabase db=getReadableDatabase();
        fromDate=getCorrectDate(fromDate);
        toDate=getCorrectDate(toDate);
        List<Expense> al=new ArrayList<>();
        Cursor c=db.query(TBLEEXPENSE, null, COLEXDATE + " BETWEEN " + "'" + fromDate + "'" + " AND " + "'" + toDate + "'", null, null, null, COLEXDATE);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            do {
                Expense expense = new Expense();
                expense.setEid(c.getLong(c.getColumnIndex(COLEXPENSEID)));
                expense.seteName(c.getString(c.getColumnIndex(COLEXNAME)).toUpperCase());
                expense.setAmt(c.getDouble(c.getColumnIndex(COLEXA)));
                expense.seteDate(c.getString(c.getColumnIndex(COLEXDATE)));
                expense.setIid(c.getLong(c.getColumnIndex(COLINCOMEEXPENSEID)));
                al.add(expense);
            }while(c.moveToNext());
        }
        return al;
    }
    public List<String> getAllExpenseName()
    {
        SQLiteDatabase db=getReadableDatabase();
        List<String> al=new ArrayList<>();
        Cursor c=db.query(TBLEEXPENSE, null, null, null, null, null, null);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            do {
                String str=c.getString(c.getColumnIndex(COLEXNAME));
                str=str.toUpperCase();
                if(!al.contains(str))
                    al.add(str);
            }while(c.moveToNext());
        }
        return al;
    }
    public boolean deleteAllExpense()
    {
        SQLiteDatabase db=getWritableDatabase();
        long id=db.delete(TBLEEXPENSE, null, null);
        if(id!=-1)
            return true;
        return false;
    }
    public List<Expense> getAllExpense()
    {
        SQLiteDatabase db=getReadableDatabase();
        List<Expense> al=new ArrayList<>();
        Cursor c=db.query(TBLEEXPENSE, null, null, null, null, null, null);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            do {
                Expense expense=new Expense();
                expense.setEid(c.getLong(c.getColumnIndex(COLEXPENSEID)));
                expense.seteName(c.getString(c.getColumnIndex(COLEXNAME)).toUpperCase());
                expense.setAmt(c.getDouble(c.getColumnIndex(COLEXA)));
                expense.seteDate(c.getString(c.getColumnIndex(COLEXDATE)));
                expense.setIid(c.getLong(c.getColumnIndex(COLINCOMEEXPENSEID)));
                al.add(expense);
            }while(c.moveToNext());
        }
        return al;
    }
    public List<Expense> getAllExpenseByDate()
    {
        SQLiteDatabase db=getReadableDatabase();
        List<Expense> al=new ArrayList<>();
        Cursor c=db.query(TBLEEXPENSE, null, null, null, null, null, COLEXDATE);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            do {
                Expense expense=new Expense();
                expense.setEid(c.getLong(c.getColumnIndex(COLEXPENSEID)));
                expense.seteName(c.getString(c.getColumnIndex(COLEXNAME)).toUpperCase());
                expense.setAmt(c.getDouble(c.getColumnIndex(COLEXA)));
                expense.seteDate(c.getString(c.getColumnIndex(COLEXDATE)));
                expense.setIid(c.getLong(c.getColumnIndex(COLINCOMEEXPENSEID)));
                al.add(expense);
            }while(c.moveToNext());
        }
        return al;
    }
    public boolean deleteExpense(String name)
    {
        name=name.toLowerCase();
        SQLiteDatabase db=getReadableDatabase();
        Cursor c=db.query(TBLEEXPENSE, null, COLEXNAME + "=?", new String[]{name}, null, null, null);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            do {
                Expense expense=new Expense();
                expense.setEid(c.getLong(c.getColumnIndex(COLEXPENSEID)));
                expense.seteName(c.getString(c.getColumnIndex(COLEXNAME)).toUpperCase());
                expense.setAmt(c.getDouble(c.getColumnIndex(COLEXA)));
                expense.setIid(c.getLong(c.getColumnIndex(COLINCOMEEXPENSEID)));
                expense.seteDate(c.getString(c.getColumnIndex(COLEXDATE)));
                boolean t=deleteExpense(expense);
                if(t==false)
                    return false;
            }while(c.moveToNext());
            return true;
        }
        else
            return false;
    }
    public boolean deleteExpense(Expense expense)
    {
        SQLiteDatabase db=getWritableDatabase();
        long id=db.delete(TBLEEXPENSE, COLEXPENSEID + "=?", new String[]{String.valueOf(expense.getEid())});
        if(id!=-1)
            return true;
        return false;
    }
    public boolean updateExpense(Expense expense)
    {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues val=new ContentValues();
        val.put(COLEXNAME, expense.geteName().toLowerCase());
        val.put(COLEXA, expense.getAmt());
        val.put(COLEXDATE, expense.geteDate());
        val.put(COLINCOMEEXPENSEID, expense.getIid());
        long id=db.update(TBLEEXPENSE, val, COLEXPENSEID + "=?", new String[]{String.valueOf(expense.getEid())});
        if(id!=-1)
            return true;
        return false;
    }
    public String getCorrectDate(String s)
    {
        StringBuilder sb=new StringBuilder();
        String[] as=s.split(" ");
        sb.append(as[2]);
        sb.append("-");
        sb.append(performMoth(as[0]));
        sb.append("-");
        String n=as[1].substring(0, as[1].length() - 1);
        if(n.length()==1)
        {
            n="0"+n;
        }
        sb.append(n);
        sb.append(" ");
        sb.append(as[3]);
        return sb.toString();
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
    public List<Income> getAllIncomeByName(String name)
    {
        name=name.toLowerCase();
        SQLiteDatabase db=getReadableDatabase();
        List<Income> al=new ArrayList<>();
        Cursor c=db.query(TBLINCOME,null,COLINNAME+"=?",new String[]{name},null,null,null);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            do {
                Income income=new Income();
                income.setId(c.getLong(c.getColumnIndex(COLINCOMEID)));
                income.setName(c.getString(c.getColumnIndex(COLINNAME)).toUpperCase());
                income.setAmt(c.getDouble(c.getColumnIndex(COLINA)));
                income.setDate(c.getString(c.getColumnIndex(COLINDATE)));
                income.setCard(c.getString(c.getColumnIndex(COLINCARD)));
                income.setCurrency(c.getString(c.getColumnIndex(COLINCURRENCY)));
                al.add(income);
            }while(c.moveToNext());
        }
        return al;
    }
    public double getBalance(String name)
    {
        double sum1=0;
        double sum2=0;
        SQLiteDatabase db=getReadableDatabase();
        Cursor c1=db.query(TBLINCOME,null,COLINCARD+"=?",new String[]{name},null,null,null);
        if(c1.getCount()>0)
        {
            c1.moveToFirst();
            do {
                Currency currency=getCurrency(c1.getString(c1.getColumnIndex(COLINCURRENCY)));
                double d=1.0*currency.getVal()/SettingAct.set_currency_value;
                sum1=sum1+(c1.getDouble(c1.getColumnIndex(COLINA))*d);
            }while(c1.moveToNext());
        }
        Cursor c2=db.query(TBLEEXPENSE,null,null,null,null,null,null);
        if(c2.getCount()>0)
        {
            c2.moveToFirst();
            do {
                Income income=getIncome(c2.getLong(c2.getColumnIndex(COLINCOMEEXPENSEID)));
                if(income.getCard().trim().equals(name)) {
                    Currency currency=getCurrency(income.getCurrency());
                    double d=1.0*currency.getVal()/SettingAct.set_currency_value;
                    sum2 = sum2 + (c2.getDouble(c2.getColumnIndex(COLEXA))*d);
                }
            }while(c2.moveToNext());
        }
        return sum1-sum2;
    }
    public List<Expense> getAllExpenseByIncomeId(long id)
    {
        SQLiteDatabase db=getReadableDatabase();
        List<Expense> al=new ArrayList<>();
        Cursor c=db.query(TBLEEXPENSE,null,COLINCOMEEXPENSEID+"=?",new String[]{String.valueOf(id)},null,null,null);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            do {
                Expense expense=new Expense();
                expense.setEid(c.getLong(c.getColumnIndex(COLEXPENSEID)));
                expense.seteName(c.getString(c.getColumnIndex(COLEXNAME)).toUpperCase());
                expense.setAmt(c.getDouble(c.getColumnIndex(COLEXA)));
                expense.seteDate(c.getString(c.getColumnIndex(COLEXDATE)));
                expense.setIid(c.getLong(c.getColumnIndex(COLINCOMEEXPENSEID)));
                al.add(expense);
            }while(c.moveToNext());
        }
        return al;
    }
    public double getTotalIncomeByCard(String name)
    {
        double amount=0;
        SQLiteDatabase db=getReadableDatabase();
        name=name.toUpperCase();
        Cursor c=db.query(TBLINCOME,null,COLINCARD+"=?",new String[]{name},null,null,null);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            do {
                Currency currency=getCurrency(c.getString(c.getColumnIndex(COLINCURRENCY)));
                double d=1.0*currency.getVal()/SettingAct.set_currency_value;
                amount=amount+(c.getDouble(c.getColumnIndex(COLINA))*d);
            }while(c.moveToNext());
        }
        return amount;
    }
    public double getTotalExpenseByCard(String name)
    {
        double amount=0;
        SQLiteDatabase db=getReadableDatabase();
        name=name.toUpperCase();
        Cursor c=db.query(TBLEEXPENSE,null,null,null,null,null,null);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            do {
                long id=c.getLong(c.getColumnIndex(COLINCOMEEXPENSEID));
                Income income=getIncome(id);
                Currency currency=getCurrency(income.getCurrency());
                double d=1.0*currency.getVal()/SettingAct.set_currency_value;
                if(income.getCard().trim().equalsIgnoreCase(name))
                {
                    amount=amount+(c.getDouble(c.getColumnIndex(COLEXA))*d);
                }
            }while(c.moveToNext());
        }
        return amount;
    }
    public double getBalance(String name1,String name2)
    {
        double sum1=0.0;
        double sum2=0.0;
        SQLiteDatabase db=getReadableDatabase();
        Cursor c=db.query(TBLINCOME,null,COLINCARD+"=?",new String[]{name1},null,null,null);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            do {
                if (c.getString(c.getColumnIndex(COLINCURRENCY)).trim().equalsIgnoreCase(name2)) {
                    sum1 = sum1 + c.getDouble(c.getColumnIndex(COLINA));
                }
            }while (c.moveToNext());
        }
        Cursor c1=db.query(TBLEEXPENSE,null,null,null,null,null,null);
        if(c1.getCount()>0)
        {
            c1.moveToFirst();
            do {
                 Income income=getIncome(c1.getLong(c1.getColumnIndex(COLINCOMEEXPENSEID)));
                 if(income.getCard().trim().equalsIgnoreCase(name1)&&income.getCurrency().trim().equalsIgnoreCase(name2))
                 {
                     sum2=sum2+c1.getDouble(c1.getColumnIndex(COLEXA));
                 }
            }while (c1.moveToNext());
        }
        return sum1-sum2;
    }
}
