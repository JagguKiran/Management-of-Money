package com.example.kiran.sourcemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

public class SourceMainAct extends AppCompatActivity {

    RecyclerView rv;
    LinearLayoutManager llm;
    MyAdapter adapter;
    List<String> data;
    ActDatabase helper;
    ConversionModel model;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewMethod(SettingAct.theme_decider);
        Toolbar bar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rv=(RecyclerView)findViewById(R.id.rv1);
        model=new ConversionModel();
        data=model.getData();
        adapter=new MyAdapter(this,data);
        llm=new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);
    }
    public void setContentViewMethod(int i)
    {
        switch(i)
        {
            case 0:setContentView(R.layout.activity_source_main);
                   break;
            case 1:setContentView(R.layout.activity_source_main);
                   break;
            case 2:setContentView(R.layout.activity_source_main_blue);
                   break;
            case 3:setContentView(R.layout.activity_source_main_yellow);
                   break;
            case 4:setContentView(R.layout.activity_source_main_green);
                   break;
            case 5:setContentView(R.layout.activity_source_main_pink);
                   break;
            default:setContentView(R.layout.activity_source_main);
        }
    }
}
