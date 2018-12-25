package com.example.kiran.sourcemanagement;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CardDisplay extends AppCompatActivity implements CardDisplayAdapter.MyDisplayCardAdapter,CardDeleteDialog.MyCardDeleteResult,View.OnLongClickListener
{

    RecyclerView recyclerView;
    LinearLayoutManager llm;
    CardDisplayAdapter adapter;
    List<Card> al;
    Card cards;
    String str1,str2;
    int si;
    int code;
    List<Card> dtt=new ArrayList<>();
    boolean isAction=false;
    int count=0;
    TextView counter;
    Toolbar tb;
    ActDatabase helper;
    String cName;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewMethod(SettingAct.theme_decider);
        tb= (Toolbar) findViewById(R.id.toolbar);
        counter=(TextView)findViewById(R.id.text11);
        counter.setVisibility(View.GONE);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        helper=new ActDatabase(this,ActDatabase.DBNAME,null,ActDatabase.DBVERSION);
        recyclerView=(RecyclerView)findViewById(R.id.recy5);
        llm=new LinearLayoutManager(getApplicationContext());
        al=helper.getAllCard();
        adapter=new CardDisplayAdapter(this,al,recyclerView);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        ItemTouchHelper.SimpleCallback callback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.showDeleteDetail(viewHolder,recyclerView);
            }
        };
        ItemTouchHelper helper=new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.card_display_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.listByName:break;
            case R.id.delCard:CardDeleteDialog dialog=new CardDeleteDialog();
                              dialog.show(getFragmentManager(),"MY_CARD");
                              break;
            case R.id.lg:Intent intent=new Intent(this,MainActivity.class);
                         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                         startActivity(intent);
                         finish();
                         break;
            case R.id.delete:adapter.updateAdapter(dtt);
                      clearActionBar();
                       break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void resultCardDisplay(Card c,int i)
    {
        cards=c;
        si=i;
        code=0;
        JobTask task=new JobTask();
        task.execute(0);

    }
    public void resultDelete(String s1,String s2)
    {
        code=1;
        str1=s1;
        str2=s2;
        JobTask task=new JobTask();
        task.execute(0);


    }
    public void sure(final int i)
    {
        AlertDialog.Builder builder1=new AlertDialog.Builder(this);
        builder1.setTitle("Are You Sure ?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (i)
                {
                    case 1:boolean t=helper.updateCard(cards);
                           adapter.swap(helper.getAllCard());
                           if(t)
                               Toast.makeText(getApplicationContext(),"Successfully Updated..",Toast.LENGTH_LONG).show();
                           else
                               Toast.makeText(getApplicationContext(),"Unuccessful Operation..",Toast.LENGTH_LONG).show();
                           break;
                    case 2:Card c=helper.getCard(cName);
                           boolean tt=helper.deleteCard(c.getId());
                           adapter.swap(helper.getAllCard());
                        if(tt)
                            Toast.makeText(getApplicationContext(),"Successfully Deleted..",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getApplicationContext(),"Unuccessful Operation..",Toast.LENGTH_LONG).show();
                        break;
                    default:Toast.makeText(getApplicationContext(),"Unsuccessful",Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog1=builder1.create();
        dialog1.show();
    }
    private class JobTask extends AsyncTask<Integer,Void,String>
    {
        ProgressDialog dialog;
        int status;
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(CardDisplay.this);
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
                case 0:notificationMethod(cards, si);
                       break;
                case 1:sure(2);
                       break;
                default:Toast.makeText(CardDisplay.this,"",Toast.LENGTH_LONG).show();
            }

        }
    }
    public void notificationMethod(Card c,int i)
    {
        cards=c;
        LayoutInflater inflater=this.getLayoutInflater();
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View v=inflater.inflate(R.layout.card_text_onclick,null);
        final TextView tv1=(TextView)v.findViewById(R.id.tc1);
        final TextView tv2=(TextView)v.findViewById(R.id.tc2);
        tv1.setText("Card Name : "+c.getcName());
        tv2.setText("Card Number : " + c.getCardNumber());
        switch (i)
        {
            case 0:builder.setTitle("DETAILED INFORMATION").setView(v).setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    updateMethod(cards);
                }
            }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
                break;
            case 1:
                builder.setTitle("Deletion...").setView(v).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sure(2);
                        dialog.dismiss();
                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                break;
            default:Toast.makeText(this,"Something went really wrong",Toast.LENGTH_LONG).show();
        }
        AlertDialog dialog=builder.create();
        dialog.show();
    }
    public void deleteCard(String s1)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Are you sure?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sure(2);
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
    public void updateMethod(final Card c)
    {
        cards=c;
        LayoutInflater inflater=this.getLayoutInflater();
        View v=inflater.inflate(R.layout.card_update, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        final EditText et1=(EditText)v.findViewById(R.id.ec1);
        final EditText et2=(EditText)v.findViewById(R.id.ec2);
        et1.setText(c.getcName());
        et2.setText(c.getCardNumber());
        builder.setTitle("Updating..").setView(v).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cards.setcName(et1.getText().toString());
                cards.setCardNumber(et2.getText().toString());
                sure(1);
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
            case 0:setContentView(R.layout.activity_card_display);
                break;
            case 1:setContentView(R.layout.activity_card_display);
                break;
            case 2:setContentView(R.layout.activity_card_display_blue);
                break;
            case 3:setContentView(R.layout.activity_card_display_yellow);
                break;
            case 4:setContentView(R.layout.activity_card_display_green);
                break;
            case 5:setContentView(R.layout.activity_card_display_pink);
                break;
            default:setContentView(R.layout.activity_card_display);
        }
    }
    public boolean onLongClick(View v)
    {
        tb.getMenu().clear();
        tb.inflateMenu(R.menu.custom_toolbar);
        counter.setVisibility(View.VISIBLE);
        isAction=true;
        adapter.notifyDataSetChanged();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        return false;
    }
    public void clearActionBar()
    {
        isAction=false;
        tb.getMenu().clear();
        tb.inflateMenu(R.menu.card_display_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        counter.setVisibility(View.GONE);
        counter.setText("0 Item Selected");
        count=0;
        dtt.clear();
    }
    public void prepare_selection(View v,int pos)
    {
        if(((CheckBox)v).isChecked()) {
            dtt.add(al.get(pos));
            count = count + 1;
            updateCount(count);
        }
        else
        {
            dtt.remove(al.get(pos));
            count=count-1;
            updateCount(count);
        }
    }
    public void updateCount(int count)
    {
        if(count==0)
        {
            counter.setText("0 Item Selected");
        }
        else
        {
            counter.setText(count + " Item Selected");
        }
    }
    public void onBackPressed()
    {

        if(isAction)
        {
            clearActionBar();
            adapter.notifyDataSetChanged();
        }
        else
            super.onBackPressed();
    }
    public void resultDeleteCard(String s)
    {
        cName=s;
        code=1;
        JobTask task=new JobTask();
        task.execute(0);

    }
}
