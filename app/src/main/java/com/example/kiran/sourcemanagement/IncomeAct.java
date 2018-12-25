package com.example.kiran.sourcemanagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class IncomeAct extends AppCompatActivity implements IncomeAmountDialog.MyDialogFragmentListener,IncomeNameDialog.MyDialogFragmentListener1,IncomeCurrencyDialog.MyDialogFragmentListener2,IncomeCardDialog.MyDialogFragmentListener3
{
    String sdate;
    String amt;
    String name;
    String cur;
    String card;
    int amount;
    Income income;
    ActDatabase helper;
    ConversionModel model;
    DateFormat dateFormat=DateFormat.getDateTimeInstance();
    Calendar calendar=Calendar.getInstance();
    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener()
    {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            update();
        }
    };
    public void update()
    {
        sdate=dateFormat.format(calendar.getTime());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewMethod(SettingAct.theme_decider);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        model=new ConversionModel();
        helper=new ActDatabase(this,ActDatabase.DBNAME,null,ActDatabase.DBVERSION);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.income_act_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.show:Intent intent=new Intent(this,IncomeDisplay.class);
                           startActivity(intent);
                           break;
            case R.id.lg:   Intent intent1=new Intent(this,MainActivity.class);
                            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent1);
                            finish();
                            break;
            case R.id.add:Intent intent2=new Intent(this,IncomeAct.class);
                          startActivity(intent2);
                          break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void amount(View v)
    {
        IncomeAmountDialog dialog=new IncomeAmountDialog();
        dialog.show(getFragmentManager(), "MY_AMOUNT_DIALOG");

    }
    public void name(View v)
    {
        IncomeNameDialog inDialog=new IncomeNameDialog();
        inDialog.show(getFragmentManager(), "MY_NAME_DIALOG");
    }
    public void idate(View v)
    {
        new DatePickerDialog(this,d,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    public void icur(View v)
    {
        IncomeCurrencyDialog dialog=new IncomeCurrencyDialog();
        dialog.show(getFragmentManager(), "MY_CURRENCY_DIALOG");
    }
    public void icard(View v)
    {
        IncomeCardDialog dialog=new IncomeCardDialog();
        dialog.show(getFragmentManager(), "MY_CARD_DIALOG");
    }
    public void onResultViewAmount(String n)
    {
        if(n==null&&n.trim().equals("")) {
            amount = 0;
        }
        else {
            try {
                amount = Integer.parseInt(n);
            } catch (Exception e) {
                e.printStackTrace();
                amount=0;
            }
        }
    }
    public void onResultViewName(String n)
    {
        name=n;
    }
    public void onResultViewCurrency(String n)
    {
        cur=n;
    }
    public void onResultViewCard(String n)
    {
        card=n;
    }
    public void submit(View v) {
        if (amount<=0||name == null ||helper.getAllCard().size()==0||helper.getAllCurrency().size()==0||(cur==null&&!helper.getAllCurrencyName().contains("RUPEES"))||(card==null&&!helper.getAllCardNames().contains("BY CASH"))) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Invalid Data....").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog=builder.create();
            dialog.show();
        }
        else {
            income = null;
            try {
                income = new Income(amount, name, sdate, cur, card);
            } catch (Exception e) {
                Toast.makeText(this, "Invalid Income " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            helper.addIncome(income);
            JobTask task = new JobTask();
            task.execute(0);
        }
    }
    private class JobTask extends AsyncTask<Integer,Void,String>
    {
        ProgressDialog dialog;
        int status;
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(IncomeAct.this);
            dialog.setProgress(status);
            dialog.setMax(100);
            dialog.setIndeterminate(true);
            dialog.show();
            dialog.setContentView(R.layout.wait_dialog);
        }

        @Override
        protected String doInBackground(Integer... params) {
            int st = params[0];
            while (st < 100) {
                st = st + 100;
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (st >= 100) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            notificationMethod();
        }
    }
    public void notificationMethod()
    {
        Intent intent=new Intent(this,IncomeDetailsAct.class);
        intent.putExtra("AMOUNT",String.valueOf(income.getAmt()));
        intent.putExtra("NAME",income.getName());
        intent.putExtra("DATE",income.getDate());
        intent.putExtra("CURRENCY",income.getCurrency());
        intent.putExtra("CARD", income.getCard());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        Drawable drawable= ContextCompat.getDrawable(this, R.drawable.income);
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        builder.setLargeIcon(bitmap).setSmallIcon(android.R.drawable.btn_plus);
        builder.setAutoCancel(false).setContentIntent(pendingIntent).setContentTitle("INCOME FROM " + name);
        if(income.getCurrency().trim().equalsIgnoreCase("RUPEES"))
            builder.setContentText(SettingAct.set_currency_symbol+" "+model.convertDoubleToWord(amount)).setAutoCancel(true);
        else
            builder.setContentText(SettingAct.set_currency_symbol+" "+model.convertDoubleToDollar(amount)).setAutoCancel(true);
        NotificationManager manager=(NotificationManager)this.getSystemService(Activity.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
    public void setContentViewMethod(int i)
    {
        switch(i)
        {
            case 0:setContentView(R.layout.activity_income);
                break;
            case 1:setContentView(R.layout.activity_income);
                break;
            case 2:setContentView(R.layout.activity_income_blue);
                break;
            case 3:setContentView(R.layout.activity_income_yellow);
                break;
            case 4:setContentView(R.layout.activity_income_green);
                break;
            case 5:setContentView(R.layout.activity_income_pink);
                break;
            default:setContentView(R.layout.activity_income);
        }
    }
}

