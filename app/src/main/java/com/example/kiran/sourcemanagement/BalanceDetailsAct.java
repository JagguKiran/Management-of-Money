package com.example.kiran.sourcemanagement;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class BalanceDetailsAct extends AppCompatActivity {

    TextView tv1,tv2,tv3,tv4,tv5,tv6;
    Intent intent;
    AlertDialog.Builder builder;
    LayoutInflater inflater;
    ActDatabase helper;
    List<Currency> currencyList;
    double val;
    ConversionModel model;
    String cname,cnum,incomeBal,expenseBal,balance,selection;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater=getLayoutInflater();
        View v=inflater.inflate(R.layout.content_balance_details,null);
        helper=new ActDatabase(this,ActDatabase.DBNAME,null,ActDatabase.DBVERSION);
        tv1=(TextView)v.findViewById(R.id.text1);
        tv2=(TextView)v.findViewById(R.id.text2);
        tv3=(TextView)v.findViewById(R.id.text3);
        tv4=(TextView)v.findViewById(R.id.text4);
        tv5=(TextView)v.findViewById(R.id.text5);
        tv6=(TextView)v.findViewById(R.id.text6);
        model=new ConversionModel();
        intent=getIntent();
        cname=intent.getStringExtra("CARD_NAME");
        cnum=intent.getStringExtra("CARD_NUMBER");
        incomeBal=intent.getStringExtra("INCOME_BAL");
        expenseBal=intent.getStringExtra("EXPENSE_BAL");
        balance=intent.getStringExtra("BALANCE");
        tv1.setText(" Card Name       :: "+cname);
        tv2.setText(" Card Number   :: "+cnum);
        tv3.setText(" Total Income   :: "+SettingAct.set_currency_symbol+incomeBal);
        tv4.setText(" Total Expense :: "+SettingAct.set_currency_symbol+expenseBal);
        tv5.setText(" Available Balance:: "+SettingAct.set_currency_symbol+balance);;
        builder=new AlertDialog.Builder(this);
        builder.setTitle(cname.toUpperCase()+" DETAILS..").setView(v).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent1=new Intent(getApplicationContext(),BalanceAct.class);
                startActivity(intent1);
                dialog.dismiss();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }
    public void showOther(View view)
    {
        currencyList=helper.getAllCurrency();
        String[] as=new String[currencyList.size()];
        for(int i=0;i<currencyList.size();i++)
            as[i]=currencyList.get(i).getCurName();
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Choose Currency").setSingleChoiceItems(as, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selection=currencyList.get(which).getCurName();
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                convertOperation(selection);
                dialog.dismiss();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }
    public void convertOperation(String selection)
    {
        if(currencyList!=null) {
            for (int i = 0; i < currencyList.size(); i++) {
                if(currencyList.get(i).getCurName().trim().equalsIgnoreCase(selection))
                {
                    Currency currency=currencyList.get(i);
                    try
                    {
                        val=Double.parseDouble(balance);
                        val=val*SettingAct.set_currency_value/currency.getVal();
                        val=model.getTruncate(val);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    AlertDialog.Builder builder=new AlertDialog.Builder(this);
                    if(currency.getCurName().trim().equalsIgnoreCase("RUPEES"))
                    {
                        builder.setTitle(currency.getCurSymbol() + val);
                        builder.setMessage(currency.getCurSymbol()+model.convertDoubleToWord(val));
                    }
                    else {
                        builder.setTitle(currency.getCurSymbol() + val);
                        builder.setMessage(currency.getCurSymbol() + model.convertDoubleToDollar(val));
                    }
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog=builder.create();
                    dialog.show();
                }
            }
        }
    }
}
