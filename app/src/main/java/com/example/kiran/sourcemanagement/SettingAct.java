package com.example.kiran.sourcemanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SettingAct extends AppCompatActivity implements CurrencyNewDialog.MyCurrencyResult,CurrencyDeleteDialog.MyDeleteCurrency,CurrencySetDialog.MySetCurrency
{
    public static int theme_decider=0;
    public static String set_currency_name="RUPEES";
    public static String set_currency_symbol="\u20B9";
    public static int set_currency_value=1;
    ArrayAdapter<String> adapter;
    List<String> alTheme;
    String cName,cSymbol;
    int code,val;
    Spinner sp1;
    ArrayAdapter<String> adapter1;
    List<String> al;
    Spinner sp2;
    List<String> alCurrency;
    ArrayAdapter<String> adapter2;
    ActDatabase helper;
    ConversionModel model;
    List<Report> reports=new ArrayList<>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewMethod(theme_decider);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        model=new ConversionModel();
        helper=new ActDatabase(this,ActDatabase.DBNAME,null,ActDatabase.DBVERSION);
        sp1 = (Spinner) findViewById(R.id.spinner1);
        sp2=(Spinner)findViewById(R.id.spinner2);
        alTheme = model.getAllTheme();
        alCurrency=model.listAllCurrency();
        adapter1 = new ArrayAdapter<String>(this, R.layout.my_spinner_setting_item, alTheme)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v=super.getView(position, convertView, parent);
                ((TextView)v).setTextSize(20);
                ((TextView)v).setGravity(Gravity.CENTER);
                return v;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v=super.getDropDownView(position, convertView, parent);
                ((TextView) v).setGravity(Gravity.START);
                return v;
            }
        };
        adapter2 = new ArrayAdapter<String>(this, R.layout.my_spinner_setting_item, alCurrency)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v=super.getView(position, convertView, parent);
                ((TextView)v).setTextSize(20);
                ((TextView)v).setGravity(Gravity.CENTER);
                return v;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v=super.getDropDownView(position, convertView, parent);
                ((TextView) v).setGravity(Gravity.START);
                return v;
            }
        };
        sp1.setAdapter(adapter1);
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    theme_change(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp2.setAdapter(adapter2);
        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0)
                    doCurrency(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_act_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.aboutApp:Intent intent1=new Intent(this,AboutAppAct.class);
                               startActivity(intent1);
                               break;
            case R.id.aboutAuthor:AuthorDialog dialog=new AuthorDialog();
                                  dialog.show(getFragmentManager(),"MY_AUTHOR");
                                  break;
            case R.id.feedback:Intent intent2=new Intent(this,FeedbackAct.class);
                               startActivity(intent2);
                               break;
            case R.id.lg:logout();
                         break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void resultCurrency(String s1,String s2,int v)
    {
        if(s1==null||s2==null||s1.trim().equals("")||s2.trim().equals(""))
        {
            sp2.setAdapter(adapter2);
        }
        else {
            cName = s1;
            cSymbol = s2;
            val=v;
            code = 0;
            JobTask task = new JobTask();
            task.execute(0);
        }
    }
    public void logout()
    {
        Intent intent=new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    private class JobTask extends AsyncTask<Integer,Void,String>
    {
        ProgressDialog dialog;
        int status;
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(SettingAct.this);
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
                case 0: addCurrency(cName,cSymbol,val);
                        code=2;
                        break;
                case 1: CurrencyListDialog dialog1=new CurrencyListDialog();
                        dialog1.show(getFragmentManager(), "MY_CURRENCY");
                        code=2;
                        sp2.setAdapter(adapter2);
                        break;
                default:Intent intent=new Intent(getApplicationContext(),SourceMainAct.class);
                       startActivity(intent);
                       break;

            }
        }
    }
    public void addCurrency(String s1,String s2,int val)
    {
        Currency cur=new Currency(s1,s2,val);
        boolean t=helper.addCurrency(cur);
        if(t)
            Toast.makeText(this,"Successfully Added..",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this,"Failed to Add..",Toast.LENGTH_LONG).show();
        sp2.setAdapter(adapter2);
    }
    public void theme_change(int i)
    {
        theme_decider=i;
        code=2;
        JobTask task=new JobTask();
        task.execute(0);
    }
    public void setContentViewMethod(int i)
    {
        switch (i)
        {
            case 0:setContentView(R.layout.activity_setting);
                   break;
            case 1:setContentView(R.layout.activity_setting);
                   break;
            case 2:setContentView(R.layout.activity_settings_blue);
                   break;
            case 3:setContentView(R.layout.activity_setting_yellow);
                  break;
            case 4:setContentView(R.layout.activity_setting_green);
                   break;
            case 5:setContentView(R.layout.activity_setting_pink);
                   break;
            default:setContentView(R.layout.activity_setting);
        }
    }
    public void doCurrency(int pos)
    {
        switch (pos)
        {
            case 1: CurrencySetDialog dialog2=new CurrencySetDialog();
                    dialog2.show(getFragmentManager(),"MY_CURRENCY");
                    break;
            case 2: CurrencyNewDialog dialog=new CurrencyNewDialog();
                    dialog.show(getFragmentManager(), "MY_CURRENCY");
                    break;
            case 3: CurrencyDeleteDialog dialog1=new CurrencyDeleteDialog();
                    dialog1.show(getFragmentManager(), "MY_CURRENCY");
                    break;
            case 4: JobTask task=new JobTask();
                    task.execute(0);
                    code=1;
                    break;
            default:Toast.makeText(this,"Invalid Choice",Toast.LENGTH_SHORT).show();
        }
    }
    public void resultDeleteCurrency(String s)
    {
        boolean t=helper.deleteCurrency(s);
        if(t&&set_currency_name.trim().equalsIgnoreCase(s)) {
            Toast.makeText(this, s.toUpperCase() + " Successfully Deleted", Toast.LENGTH_LONG).show();
            set_currency_name="RUPEES";
            set_currency_symbol="\u20B9";
            set_currency_value=1;
        }
        else
            Toast.makeText(this,"Failed to Delete "+s,Toast.LENGTH_LONG).show();
        sp2.setAdapter(adapter2);
    }
    public void resultSetCurrency(String s)
    {
        Currency currency=helper.getCurrency(s);
        if(currency==null)
        {
            set_currency_name="RUPEES";
            set_currency_symbol="\u20B9";
            set_currency_value=1;
        }
        else
        {
            set_currency_name=currency.getCurName();
            set_currency_symbol=currency.getCurSymbol();
            set_currency_value=currency.getVal();
        }
        sp2.setAdapter(adapter2);
    }
}
