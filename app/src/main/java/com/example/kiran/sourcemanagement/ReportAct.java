package com.example.kiran.sourcemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ReportAct extends AppCompatActivity implements FromToDateDialog.MyFromToDate
{
    Spinner sp1;
    RecyclerView rc1;
    List<String> al1;
    List<String> al2;
    Toolbar tb;
    ArrayAdapter<String> adapter1;
    ReportAdapter adapter2;
    TextView counter;
    ReportModel rm;
    List<Report> data;
    ActDatabase helper;
    String fromDate,toDate;
    ConversionModel model;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewMethod(SettingAct.theme_decider);
        tb = (Toolbar) findViewById(R.id.toolbar);
        counter=(TextView)findViewById(R.id.text11);
        counter.setVisibility(View.GONE);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        model=new ConversionModel();
        helper=new ActDatabase(this,ActDatabase.DBNAME,null,ActDatabase.DBVERSION);
        sp1=(Spinner)findViewById(R.id.spinner1);
        rc1=(RecyclerView)findViewById(R.id.rec1);
        al1=model.getPeriod();
        adapter1=new ArrayAdapter<String>(this,R.layout.report_spinner_item,al1)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v=super.getView(position, convertView, parent);
                if(position==0)
                {
                    ((TextView)v).setText(al1.get(0)+"\u21d3");
                }
                ((TextView)v).setGravity(Gravity.CENTER);
                ((TextView)v).setTextSize(25);
                return v;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v=super.getDropDownView(position, convertView, parent);
                ((TextView)v).setGravity(Gravity.START);
                ((TextView)v).setTextSize(22);
                return v;
            }
        };
        sp1.setAdapter(adapter1);
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                performOperation(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        al2=model.getPeriod();
        rm=new ReportModel(helper.getAllIncomeByDate(),helper.getAllExpenseByDate(),helper.getAllCurrency(),helper.getAllIncome());
        data=rm.getAllReports();
        adapter2=new ReportAdapter(data,this);
        rc1.setLayoutManager(new LinearLayoutManager(this));
        rc1.setAdapter(adapter2);
        rc1.setHasFixedSize(true);
    }
    public void setContentViewMethod(int i)
    {
        switch(i)
        {
            case 0:setContentView(R.layout.activity_report);
                break;
            case 1:setContentView(R.layout.activity_report);
                break;
            case 2:setContentView(R.layout.activity_report_blue);
                break;
            case 3:setContentView(R.layout.activity_report_yellow);
                break;
            case 4:setContentView(R.layout.activity_report_green);
                break;
            case 5:setContentView(R.layout.activity_report_pink);
                break;
            default:setContentView(R.layout.activity_report);
        }
    }
    public void performOperation(int i)
    {
        switch(i)
        {
            case 0:rm=new ReportModel(helper.getAllIncomeByDate(),helper.getAllExpenseByDate(),helper.getAllCurrency(),helper.getAllIncome());
                   data=rm.getAllReports();
                   adapter2=new ReportAdapter(data,this);
                   rc1.setAdapter(adapter2);
                   rc1.setLayoutManager(new LinearLayoutManager(this));
                   rc1.setHasFixedSize(true);
                   break;
            case 1:rm=new ReportModel(helper.getAllTodaysIncome(),helper.getAllTodayExpense(),helper.getAllCurrency(),helper.getAllIncome());
                   data=rm.getAllReports();
                   adapter2=new ReportAdapter(data,this);
                   rc1.setAdapter(adapter2);
                   rc1.setLayoutManager(new LinearLayoutManager(this));
                   rc1.setHasFixedSize(true);
                   break;
            case 2:FromToDateDialog dialog=new FromToDateDialog();
                   dialog.show(getFragmentManager(), "MY_DATE");
                   break;
            default:
                onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.report_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.pieChart:Intent intent1=new Intent(this,PieAct.class);
                               startActivity(intent1);
                               break;
            case R.id.barChart:Intent intent2=new Intent(this,BarAct.class);
                               startActivity(intent2);
                               break;
            case R.id.lg:logout();
                         break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void logout()
    {
        Intent intent=new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    public void resultDate(String s1,String s2)
    {
        fromDate=s1;
        toDate=s2;
        rm=new ReportModel(helper.getAllIncomeGivenDate(fromDate,toDate),helper.getAllExpenseGivenDate(fromDate,toDate),helper.getAllCurrency(),helper.getAllIncome());
        data=rm.getAllReports();
        adapter2=new ReportAdapter(data,this);
        rc1.setLayoutManager(new LinearLayoutManager(this));
        rc1.setAdapter(adapter2);
        rc1.setHasFixedSize(true);
    }
}
