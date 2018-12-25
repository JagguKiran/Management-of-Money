package com.example.kiran.sourcemanagement;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BalanceAct extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    ListView lv;
    TextView tv;
    DrawerLayout dl;
    List<String> data;
    ActDatabase helper;
    ViewPager viewPager;
    ConversionModel model;
    ViewPagerDisplayAdapter adapter1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewMethod(SettingAct.theme_decider);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        model=new ConversionModel();
        viewPager=(ViewPager)findViewById(R.id.vp1);
        adapter1=new ViewPagerDisplayAdapter(getSupportFragmentManager(),this);
        viewPager.setAdapter(adapter1);
        helper=new ActDatabase(this,ActDatabase.DBNAME,null,ActDatabase.DBVERSION);
        dl = (DrawerLayout) findViewById(R.id.dlName);
        lv = (ListView) findViewById(R.id.lvName);
        tv = (TextView) findViewById(R.id.tvName);
        data = helper.getAllCardNames();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = data.get(position);
                performOperation(s);
            }
        });
    }
    public void performOperation(String s)
    {
        double bal=helper.getBalance(s);
        bal=model.getTruncate(bal);
        Card card=helper.getCard(s);
        double incomeBal=helper.getTotalIncomeByCard(s);
        incomeBal=model.getTruncate(incomeBal);
        double expenseBal=helper.getTotalExpenseByCard(s);
        expenseBal=model.getTruncate(expenseBal);
        Intent intent=new Intent(this,BalanceDetailsAct.class);
        intent.putExtra("CARD_NAME",s.toUpperCase());
        intent.putExtra("CARD_NUMBER",card.getCardNumber().toUpperCase());
        intent.putExtra("INCOME_BAL",String.valueOf(incomeBal));
        intent.putExtra("EXPENSE_BAL",String.valueOf(expenseBal));
        intent.putExtra("BALANCE",String.valueOf(bal));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.balance);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        NotificationCompat.Builder n = new NotificationCompat.Builder(this);
        n.setContentIntent(pendingIntent);
        n.setSmallIcon(android.R.drawable.btn_plus);
        n.setLargeIcon(bitmap);
        n.setAutoCancel(false);
        n.setContentTitle("BALANCE IN " + s.toUpperCase());
        if(SettingAct.set_currency_name.trim().equalsIgnoreCase("RUPEES"))
            n.setContentText(SettingAct.set_currency_symbol+" "+model.convertDoubleToWord(bal)+"("+SettingAct.set_currency_symbol+bal+")");
        else
            n.setContentText(SettingAct.set_currency_symbol+" "+model.convertDoubleToDollar(bal)+"("+SettingAct.set_currency_symbol+bal+")");
        n.build();
        NotificationManager manager = (NotificationManager) this.getSystemService(Activity.NOTIFICATION_SERVICE);
        manager.notify(0, n.build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.balance_act_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.lg:Intent intent=new Intent(this,MainActivity.class);
                         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
            case 0:setContentView(R.layout.activity_balance);
                break;
            case 1:setContentView(R.layout.activity_balance);
                break;
            case 2:setContentView(R.layout.activity_balance_blue);
                break;
            case 3:setContentView(R.layout.activity_balance_yellow);
                break;
            case 4:setContentView(R.layout.activity_balance_green);
                break;
            case 5:setContentView(R.layout.activity_balance_pink);
                break;
            default:setContentView(R.layout.activity_balance);
        }
    }
}


