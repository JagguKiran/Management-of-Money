package com.example.kiran.sourcemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class FeedbackAct extends AppCompatActivity implements SuggestDialog.ResultInterface {

    TextView tv1,tv2;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewMethod(SettingAct.theme_decider);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tv1=(TextView)findViewById(R.id.text1);
        tv1.setText("  Before sending an gmail, Please read the Frequently Asked Questions.");
        tv2=(TextView)findViewById(R.id.text2);
        tv2.setText("  To report bugs or suggestions on the application.");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feedback_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.lg:Intent intent=new Intent(this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void setContentViewMethod(int i)
    {
        switch(i)
        {
            case 0:setContentView(R.layout.activity_feedback);
                break;
            case 1:setContentView(R.layout.activity_feedback);
                break;
            case 2:setContentView(R.layout.activity_feedback_blue);
                break;
            case 3:setContentView(R.layout.activity_feedback_yellow);
                break;
            case 4:setContentView(R.layout.activity_feedback_green);
                break;
            case 5:setContentView(R.layout.activity_feedback_pink);
                break;
            default:setContentView(R.layout.activity_feedback);
        }
    }
    public void knowQuestions(View v)
    {

    }
    public void suggest(View v)
    {
        SuggestDialog dialog=new SuggestDialog();
        dialog.show(getFragmentManager(),"MY_SUGGEST");
    }
    public void result(int a)
    {
        if(a==1)
        {
            GmailSendDialog dialog=new GmailSendDialog();
            dialog.show(getFragmentManager(),"MY_SEND");
        }
    }
}
