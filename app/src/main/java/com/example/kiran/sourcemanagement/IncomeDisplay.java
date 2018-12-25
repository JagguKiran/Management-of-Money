package com.example.kiran.sourcemanagement;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class IncomeDisplay extends AppCompatActivity implements MyIncomeDisplayAdapter.MyIncomeDisplayResult,View.OnLongClickListener {

    LinearLayoutManager llm;
    List<Income> data;
    RecyclerView recyclerView;
    MyIncomeDisplayAdapter adapter;
    String name,cur,crd,date,amt;
    double amount,ammount;
    Income income=new Income();
    Income oldIncome=new Income();
    int si;
    int code;
    ConversionModel model=new ConversionModel();
    boolean isAction;
    Toolbar tb;
    List<Income> dt=new ArrayList<>();
    int count=0;
    TextView counter;
    ActDatabase helper;
    long id;
    DateFormat dateFormat=DateFormat.getDateTimeInstance();
    Calendar calendar=Calendar.getInstance();
    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener()
    {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH,monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            update();
        }
    };
    public void update()
    {
        date=dateFormat.format(calendar.getTime());
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewMethod(SettingAct.theme_decider);
        tb= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        helper=new ActDatabase(this,ActDatabase.DBNAME,null,ActDatabase.DBVERSION);
        data=helper.getAllIncome();
        counter=(TextView)findViewById(R.id.text11);
        counter.setVisibility(View.GONE);
        recyclerView=(RecyclerView)findViewById(R.id.recy1);
        llm=new LinearLayoutManager(getApplicationContext());
        adapter=new MyIncomeDisplayAdapter(data,this,recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(llm);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.income_display_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.print:adapter.swap(helper.getAllIncome());
                            break;
            case R.id.byAmount:adapter.swap(helper.getAllIncomeByAmount());
                             break;
            case R.id.byDate:data=helper.getAllIncomeByDate();
                             adapter.swap(data);
                             break;
            case R.id.lg:Intent intent=new Intent(this,MainActivity.class);
                         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                         startActivity(intent);
                         finish();
                         break;
            case R.id.delete:adapter.updateAdapter(dt);
                              clearActionMode();
                              break;
            default:onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    public void resultIncomeDisplay(Income in,int i)
    {
        JobTask task=new JobTask();
        task.execute(0);
        income=in;
        oldIncome=in;
        id=in.getId();
        si=i;
        code=0;

    }
    public void sure(final int i)
    {
        AlertDialog.Builder builder1=new AlertDialog.Builder(this);
        builder1.setTitle("Are You Sure ?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (i)
                {
                    case 1:
                           Income income=new Income(id,amount,name,date,cur,crd);
                           helper.updateIncome(income);
                           adapter.swap(helper.getAllIncome());
                           break;
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
            dialog = new ProgressDialog(IncomeDisplay.this);
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
                case 0:notificationMethod(income,si);
                       break;
            }
        }
    }
    public void notificationMethod(Income in,int i)
    {
        income=in;
        LayoutInflater inflater = getLayoutInflater();
        View v;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        v = inflater.inflate(R.layout.income_delete, null);
        final TextView tv1 = (TextView) v.findViewById(R.id.text1);
        final TextView tv2 = (TextView) v.findViewById(R.id.text2);
        final TextView tv3 = (TextView) v.findViewById(R.id.text3);
        final TextView tv4 = (TextView) v.findViewById(R.id.text4);
        final TextView tv5 = (TextView) v.findViewById(R.id.text5);
        ammount=in.getAmt();
        Currency currency=helper.getCurrency(in.getCurrency());
        ammount=ammount*SettingAct.set_currency_value/currency.getVal();
        ammount=model.getTruncate(ammount);
        tv1.setText("Name : " + in.getName());
        tv2.setText("Amount : " + String.valueOf(ammount));
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
               Date dd =sdf1.parse(in.getDate());
                String str = dd.toString();
                StringBuilder sb = new StringBuilder();
                String[] as = str.split(" ");
                sb.append("On ");
                sb.append(as[0]);
                sb.append(",");
                sb.append(as[1]);
                sb.append(" ");
                sb.append(as[2]);
                sb.append(",");
                sb.append(as[5]);
                sb.append(" at ");
                String[] sa = as[3].split(":");
                sb.append(sa[0]);
                sb.append(":");
                sb.append(sa[1]);
                tv5.setText(sb.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            tv4.setText("Card : " + in.getCard());
            tv3.setText("Currency : " + in.getCurrency());
            switch (i) {
                case 0:
                    builder.setTitle("Detailed Information").setView(v).setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            updateMethod(income);
                            dialog.dismiss();
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
                case 2:Toast.makeText(this,"Successfully Deleted All...",Toast.LENGTH_LONG).show();
                       break;
                default:
                    Toast.makeText(this, "Something went really wrong", Toast.LENGTH_LONG).show();
            }
            AlertDialog dialog = builder.create();
            dialog.show();
    }
    public void updateMethod(Income in)
    {
        LayoutInflater inflater=getLayoutInflater();
        View v=inflater.inflate(R.layout.income_update, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        final EditText et1=(EditText)v.findViewById(R.id.ext1);
        final EditText et2=(EditText)v.findViewById(R.id.ext2);
        final Spinner spr1=(Spinner)v.findViewById(R.id.sp1);
        final Spinner spr2=(Spinner)v.findViewById(R.id.sp2);
        final Button bt=(Button)v.findViewById(R.id.bt1);
        et1.setText(in.getName());
        et2.setText(String.valueOf(ammount));
        final List<String> al1=new ArrayList<>();
        final List<String> al2=new ArrayList<>();
        al1.add("CHOOSE CARD");
        al2.add("CHOOSE CURRENCY");
        al1.addAll(helper.getAllCardNames());
        al2.addAll(helper.getAllCurrencyName());
        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(this,R.layout.my_spinner_items,al1)
        {
            public View getView(int position, View convertView,ViewGroup parent)
            {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextSize(16);
                ((TextView)v).setGravity(Gravity.CENTER);
                return v;
            }
            public View getDropDownView(int position, View convertView,ViewGroup parent) {

                    View v = super.getDropDownView(position, convertView, parent);
                    ((TextView) v).setGravity(Gravity.START);
                    return v;
                }
        };
        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(this,R.layout.my_spinner_items,al2)
        {
            public View getView(int position, View convertView,ViewGroup parent)
            {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextSize(16);
                ((TextView)v).setGravity(Gravity.CENTER);
                return v;
            }
            public View getDropDownView(int position, View convertView,ViewGroup parent) {
                View v = super.getDropDownView(position, convertView,parent);
                ((TextView) v).setGravity(Gravity.START);
                return v;
            }
        };
        spr1.setAdapter(adapter1);
        spr2.setAdapter(adapter2);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDate();
            }
        });

        builder.setTitle("Updating..").setView(v).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                name = et1.getText().toString();
                amt = et2.getText().toString();
                try {
                    amount = Double.parseDouble(amt);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int i = spr1.getSelectedItemPosition();
                crd = al1.get(i);
                if (crd.trim().equalsIgnoreCase("CHOOSE CARD")) {
                    crd = oldIncome.getCard();
                }
                int j = spr2.getSelectedItemPosition();
                cur = al2.get(j);
                if (cur.trim().equalsIgnoreCase("CHOOSE CURRENCY")) {
                    cur = oldIncome.getCurrency();
                }
            if(date==null)
            {
                date=oldIncome.getDate();
            }

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
    public void updateDate()
    {
        new DatePickerDialog(this,d,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    public void setContentViewMethod(int i)
    {
        switch(i)
        {
            case 0:setContentView(R.layout.activity_income_display);
                break;
            case 1:setContentView(R.layout.activity_income_display);
                break;
            case 2:setContentView(R.layout.activity_income_display_blue);
                break;
            case 3:setContentView(R.layout.activity_income_display_yellow);
                break;
            case 4:setContentView(R.layout.activity_income_display_green);
                break;
            case 5:setContentView(R.layout.activity_income_display_pink);
                break;
            default:setContentView(R.layout.activity_income_display);
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
    public void clearActionMode()
    {
        isAction=false;
        tb.getMenu().clear();
        tb.inflateMenu(R.menu.income_display_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        counter.setVisibility(View.GONE);
        counter.setText("0 Item Selected");
        count=0;
        dt.clear();
    }
    public void prepare_selection(View v,int pos)
    {
        if(((CheckBox)v).isChecked())
        {
            dt.add(data.get(pos));
            count=count+1;
            updateCount(count);
        }
        else
        {
            dt.remove(data.get(pos));
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
    public void onBackPressed() {
        if(isAction)
        {
            clearActionMode();
            adapter.notifyDataSetChanged();
        }
        else
            super.onBackPressed();
    }
}
