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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CardAct extends AppCompatActivity implements CardDeleteDialog.MyCardDeleteResult,CardNewDialog.MyNewCard,CardAdapter.MyCardResult
{

    LinearLayoutManager llm;
    RecyclerView recyclerView;
    CardAdapter adapter;
    List<String> al;
    List<Card> al1;
    Card card;
    int si,code;
    String cName,cNumber;
    ActDatabase helper;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewMethod(SettingAct.theme_decider);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView=(RecyclerView)findViewById(R.id.recy4);
        helper=new ActDatabase(this,ActDatabase.DBNAME,null,ActDatabase.DBVERSION);
        al=helper.getAllCardNames();
        llm=new LinearLayoutManager(getApplicationContext());
        adapter=new CardAdapter(this,al);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.card_act_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.newCard:CardNewDialog dialog1=new CardNewDialog();
                              dialog1.show(getFragmentManager(),"MY_CARD");
                              break;
            case R.id.delCard:CardDeleteDialog dialog=new CardDeleteDialog();
                              dialog.show(getFragmentManager(),"MY_CARD");
                              break;
            case R.id.list:Intent intent1=new Intent(this,CardDisplay.class);
                           startActivity(intent1);
                           break;
            case R.id.lg:logout();
                         break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void resultDeleteCard(String s)
    {
        cName=s;
        si=1;
        JobTask task=new JobTask();
        task.execute(0);

    }
    public void resultNewCard(String s1,String s2)
    {
        cName=s1;
        cNumber=s2;
        si=2;
        JobTask task=new JobTask();
        task.execute(0);
    }
    public void logout()
    {
        Intent intent=new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    public void resultCard(int i,int j)
    {
        si=0;
        code=j;
        al1=helper.getAllCard();
        Card c=al1.get(i);
        card=c;
        JobTask task=new JobTask();
        task.execute(0);

    }
    public void sure(final int i)
    {
        AlertDialog.Builder builder1=new AlertDialog.Builder(this);
        builder1.setTitle("Are You Sure ?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (si == 4) {
                    switch (i) {
                        case 0:
                            boolean t = helper.updateCard(card);
                            si = 4;
                            adapter.swap(helper.getAllCardNames());
                            break;
                        case 1:
                            helper.deleteCard(card.getId());
                            adapter.swap(helper.getAllCardNames());
                            break;
                        default:
                            Toast.makeText(getApplicationContext(), "Sorry no operation", Toast.LENGTH_LONG).show();

                    }
                    JobTask task = new JobTask();
                    task.execute(0);
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
            dialog=new ProgressDialog(CardAct.this);
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
            switch (si)
            {
                case 0:notifyDeleteUpdate(card, code);
                       break;
                case 1:notifyDelete(cName);
                       break;
                case 2:notifyNewCard(cName,cNumber);
                       break;
                default:Toast.makeText(CardAct.this,"Successful",Toast.LENGTH_LONG).show();
            }

        }
    }
    public void notifyNewCard(String s1,String s2)
    {
        boolean check=false;
        String str="";
        try {
            card = new Card(s1, s2);
            check=true;
        }catch (Exception e)
        {
            str=e.getMessage();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(check) {
            builder.setTitle("Successfully added..").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    helper.addCard(card);
                    adapter.swap(helper.getAllCardNames());
                    dialog.dismiss();
                }
            });
        }
        else
        {
            builder.setMessage(str+" Check Help Button").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void notifyDeleteUpdate(Card c,final int i)
    {
        card=c;
        LayoutInflater inflater=this.getLayoutInflater();
        View v;
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        switch (i)
        {
            case 0:v=inflater.inflate(R.layout.card_onclick,null);
                final EditText et1=(EditText)v.findViewById(R.id.ec1);
                final EditText et2=(EditText)v.findViewById(R.id.ec2);
                final TextView tv=(TextView)v.findViewById(R.id.tc1);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        help();
                    }
                });
                et1.setText(c.getcName());
                et2.setText(c.getCardNumber());
                builder.setTitle("Updating...").setView(v).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        card.setcName(et1.getText().toString());
                        card.setCardNumber(et2.getText().toString());
                        si=4;
                        sure(i);
                        dialog.dismiss();
                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                break;
            case 1:v=inflater.inflate(R.layout.card_text_onclick,null);
                final TextView tv1=(TextView)v.findViewById(R.id.tc1);
                final TextView tv2=(TextView)v.findViewById(R.id.tc2);
                tv1.setText(c.getcName());
                tv2.setText(c.getCardNumber());
                builder.setTitle("DELETION").setView(v).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        si=4;
                        sure(i);
                        dialog.dismiss();
                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                break;
            default:Toast.makeText(this,"Something went wrong",Toast.LENGTH_LONG).show();
        }
        AlertDialog dialog=builder.create();
        dialog.show();
    }
    public void notifyDelete(String s)
    {
        final Card c=helper.getCard(s);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Deleting..."+s).setMessage("Are you sure...").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                boolean t=helper.deleteCard(c.getId());
                adapter.swap(helper.getAllCardNames());
                if(t)
                    Toast.makeText(getApplicationContext(), "Successfully deleted...", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(),"Unsuccessful Sorry...",Toast.LENGTH_LONG).show();
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
            case 0:setContentView(R.layout.activity_card);
                break;
            case 1:setContentView(R.layout.activity_card);
                break;
            case 2:setContentView(R.layout.activity_card_blue);
                break;
            case 3:setContentView(R.layout.activity_card_yellow);
                break;
            case 4:setContentView(R.layout.activity_card_green);
                break;
            case 5:setContentView(R.layout.activity_card_pink);
                break;
            default:setContentView(R.layout.activity_card);
        }
    }
    public void help()
    {
        LayoutInflater inflater=getLayoutInflater();
        View v=inflater.inflate(R.layout.card_number_dialog,null);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("RESTRICTION OF CARD NUMBER").setView(v).setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
        adapter.swap(helper.getAllCardNames());
    }
}
