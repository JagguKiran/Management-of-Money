package com.example.kiran.sourcemanagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExpenseAct extends AppCompatActivity implements ExpenseNewDialog.MyNewExpense,ExpenseDeleteDialog.MyDeleteExpense
{
    RecyclerView recyclerView;
    List<String> data;
    ExpenseAdapter adapter;
    LinearLayoutManager llm;
    String iName,eName,amounts,date,cards,curs;
    int code;
    double amt;
    Income income;
    ActDatabase helper;
    ConversionModel model;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewMethod(SettingAct.theme_decider);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        model=new ConversionModel();
        helper=new ActDatabase(this,ActDatabase.DBNAME,null,ActDatabase.DBVERSION);
        recyclerView=(RecyclerView)findViewById(R.id.recy2);
        llm=new LinearLayoutManager(getApplicationContext());
        data=helper.getAllExpenseName();
        adapter=new ExpenseAdapter(this,data);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.expense_act_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.list:Intent intent=new Intent(this,ExpenseDisplay.class);
                           startActivity(intent);
                           break;
            case R.id.newExpense:ExpenseNewDialog dialog=new ExpenseNewDialog();
                                 dialog.show(getFragmentManager(),"MY_EXPENSE");
                                break;
            case R.id.delExpense:ExpenseDeleteDialog dialog1=new ExpenseDeleteDialog();
                                 dialog1.show(getFragmentManager(),"MY_EXPENSE");
                                 break;
            case R.id.lg:logout();
                         break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void resultNewExpense(String s1,double a,String dt,Income in)
    {
        JobTask task=new JobTask();
        task.execute(0);
        dt=model.performDate(dt);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try
        {
            if(s1==null||s1.trim().equalsIgnoreCase("")||a<=0||in==null||a>helper.getBalance(in.getCard(),in.getCurrency())||sdf.parse(dt).before(sdf.parse(in.getDate())))
            {
                code=2;
            }
            else {
                eName = s1;
                amounts = String.valueOf(a);
                date = dt;
                income = in;
                iName = income.getName();
                cards = income.getCard();
                curs = income.getCurrency();
                code = 0;
                amt = a;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void resultDeleteExpense(String s)
    {
        eName=s;
        JobTask task=new JobTask();
        task.execute(0);
        code=1;
    }
    public void logout()
    {
        Intent intent=new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    public void notificationMethod(String s1,double amt,String date,Income income)
    {
        Intent intent=new Intent(this,ExpenseDetailsAct.class);
        intent.putExtra("INAME",income.getName().toUpperCase());
        intent.putExtra("ENAME",s1.toUpperCase());
        intent.putExtra("AMOUNT",String.valueOf(amt));
        intent.putExtra("CURRENCY",income.getCurrency().toUpperCase());
        intent.putExtra("CARD",income.getCard().toUpperCase());
        intent.putExtra("DATE", date);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent=PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        Drawable drawable= ContextCompat.getDrawable(this, R.drawable.expense);
        Bitmap bitmap=((BitmapDrawable)drawable).getBitmap();
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        builder.setContentIntent(pendingIntent).setLargeIcon(bitmap).setSmallIcon(android.R.drawable.ic_delete).setContentTitle("EXPENSE");
        if(income.getCurrency().trim().equalsIgnoreCase("RUPEES"))
            builder.setContentText(SettingAct.set_currency_symbol+" "+model.convertDoubleToDollar(amt)).setAutoCancel(true);
        else
            builder.setContentText(SettingAct.set_currency_symbol+" "+model.convertDoubleToDollar(amt)).setAutoCancel(true);
        NotificationManager manager=(NotificationManager)this.getSystemService(Activity.NOTIFICATION_SERVICE);
        manager.notify(1,builder.build());
    }
    private class JobTask extends AsyncTask<Integer,Void,String>
    {
        ProgressDialog dialog;
        int status;
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(ExpenseAct.this);
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
            switch (code)
            {
                case 0:sure(1);
                       break;
                case 1:sure();
                       break;
                case 2: AlertDialog.Builder builder=new AlertDialog.Builder(ExpenseAct.this);
                    builder.setTitle("Invalid Data...Please Check").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog=builder.create();
                    dialog.show();
                       break;
                default:
                    Toast.makeText(ExpenseAct.this,"Something went really wrong",Toast.LENGTH_LONG).show();
            }
        }
    }
    public void sure()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(eName).setMessage("Are You Sure ?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean t = helper.deleteExpense(eName);
                if (t) {
                    Toast.makeText(getApplicationContext(), eName.toUpperCase() + " Successfully Deleted", Toast.LENGTH_LONG).show();
                    adapter.swap(helper.getAllExpenseName());
                } else {
                    Toast.makeText(getApplicationContext(), eName.toUpperCase() + " Failed to Delete", Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }
    public void setContentViewMethod(int i)
    {
        switch(i)
        {
            case 0:setContentView(R.layout.activity_expense);
                break;
            case 1:setContentView(R.layout.activity_expense);
                break;
            case 2:setContentView(R.layout.activity_expense_blue);
                break;
            case 3:setContentView(R.layout.activity_expense_yellow);
                break;
            case 4:setContentView(R.layout.activity_expense_green);
                break;
            case 5:setContentView(R.layout.activity_expense_pink);
                break;
            default:setContentView(R.layout.activity_expense);
        }
    }
    public void sure(final int i)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Are You Sure ?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try
                {
                    Expense expense=new Expense(eName,amt,date,income.getId());
                    helper.addExpense(expense);
                    adapter.swap(helper.getAllExpenseName());
                    notificationMethod(eName,amt,date,income);
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.swap(helper.getAllExpenseName());
    }

}
