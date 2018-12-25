package com.example.kiran.sourcemanagement;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class AboutAppAct extends AppCompatActivity {

    TextView tv1,tv2;
    RecyclerView rv;
    CustomAdapter adapter;
    List<String> al;
    ConversionModel model;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewMethod(SettingAct.theme_decider);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        model=new ConversionModel();
        tv1=(TextView)findViewById(R.id.text1);
        tv2=(TextView)findViewById(R.id.text2);
        tv1.setText("This application is designed to organize your income and expenses,you can register your money movements by date and then review the results in reports.");
        tv2.setText("Remember that organising your expenses allows you to have better control over your money.");
        rv=(RecyclerView)findViewById(R.id.rec1);
        al=model.getAllApp();
        adapter=new CustomAdapter(this,al);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);
    }
    public void setContentViewMethod(int i)
    {
        switch(i)
        {
            case 0:setContentView(R.layout.activity_about_app);
                break;
            case 1:setContentView(R.layout.activity_about_app);
                break;
            case 2:setContentView(R.layout.activity_about_app_blue);
                break;
            case 3:setContentView(R.layout.activity_about_app_yellow);
                break;
            case 4:setContentView(R.layout.activity_about_app_green);
                break;
            case 5:setContentView(R.layout.activity_about_app_pink);
                break;
            default:setContentView(R.layout.activity_about_app);
        }
    }
}
