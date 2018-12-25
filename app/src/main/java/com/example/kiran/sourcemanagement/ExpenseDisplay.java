package com.example.kiran.sourcemanagement;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ExpenseDisplay extends AppCompatActivity implements ExpenseDisplayAdapter.MyExpenseDisplayResult,View.OnLongClickListener,Calculator.CalculatorResult,ExpenseNameDialog.MyExpenseName,ExpenseIncomeNameDialog.MyIncomeExpenseName {

    LinearLayoutManager llm;
    RecyclerView recyclerView;
    ExpenseDisplayAdapter adapter;
    List<Expense> al1;
    List<Income> data;
    Income income,income1,oldIncome;
    double amt,oldamt,ammount;
    Expense expense,oldExpense;
    int si;
    long id;
    String str1,str2,eName,amount,sdate;
    boolean isAction=false;
    List<Expense> dt=new ArrayList<>();
    int count=0;
    TextView counter;
    Toolbar tb;
    ActDatabase helper;
    ArrayAdapter<String> adapter1;
    List<String> data1=new ArrayList<>();
    ConversionModel model=new ConversionModel();
    DateFormat dateFormat=DateFormat.getDateTimeInstance();
    Calendar calendar=Calendar.getInstance();
    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener()
    {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            update();
        }
    };
    public void update()
    {
        sdate=dateFormat.format(calendar.getTime());
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewMethod(SettingAct.theme_decider);
        tb = (Toolbar) findViewById(R.id.toolbar);
        counter=(TextView)findViewById(R.id.text11);
        counter.setVisibility(View.GONE);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView=(RecyclerView)findViewById(R.id.recy3);
        helper=new ActDatabase(this,ActDatabase.DBNAME,null,ActDatabase.DBVERSION);
        llm=new LinearLayoutManager(getApplicationContext());
        al1=helper.getAllExpense();
        adapter=new ExpenseDisplayAdapter(this,al1,recyclerView);
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
        getMenuInflater().inflate(R.menu.expense_display_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.byAll:adapter.swap(helper.getAllExpense());
                            break;
            case R.id.byExpName:ExpenseNameDialog dialog2=new ExpenseNameDialog();
                                dialog2.show(getFragmentManager(),"MY_EXPENSE");
                                break;
            case R.id.byIncomeName:ExpenseIncomeNameDialog dialog3=new ExpenseIncomeNameDialog();
                                   dialog3.show(getFragmentManager(),"MY_INCOME");
                                    break;
            case R.id.byDate:adapter.swap(helper.getAllExpenseByDate());
                             break;
            case R.id.lg:Intent intent=new Intent(this,MainActivity.class);
                          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                          startActivity(intent);
                          finish();
                          break;
            case R.id.delete:adapter.updateAdapter(dt);
                             clearActionBar();
                             break;
            default:onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    public void resultExpenseDisplay(Expense ex,int i)
    {
        JobTask task=new JobTask();
        task.execute(0);
        expense=ex;
        oldExpense=ex;
        id=ex.getEid();
        si=i;
    }
    public void sure()
    {
        AlertDialog.Builder builder1=new AlertDialog.Builder(this);
        builder1.setTitle("Are You Sure ?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                boolean t=false;
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                try
                {
                    if(sdate==null||sdate.trim().equalsIgnoreCase(""))
                        sdate=oldExpense.geteDate();
                    t=sdf.parse(sdate).before(sdf.parse(income1.getDate()));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                Currency currency1=helper.getCurrency(income1.getCurrency());
                int val=currency1.getVal();
                double amt1=helper.getBalance(income1.getCard(), income1.getCurrency())+(oldExpense.getAmt()/val);
                if(eName==null||eName.trim().equalsIgnoreCase("")||amt<=0||income1==null||amt>amt1||t)
                {
                    showMessage();
                }
                else
                {
                    Expense ex1 = new Expense();
                    ex1.setEid(id);
                    eName=eName.toLowerCase();
                    ex1.seteName(eName);
                    ex1.setAmt(amt);
                    ex1.seteDate(sdate);
                    ex1.setIid(income1.getId());
                    helper.updateExpense(ex1);
                    adapter.swap(helper.getAllExpense());
                    dialog.dismiss();
                }
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
    private class JobTask extends AsyncTask<Integer,Void,String> {
        ProgressDialog dialog;
        int status;

        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ExpenseDisplay.this);
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
            notificationMethod(expense, si);
        }
    }
    public void notificationMethod(Expense ex,int i)
    {
        expense=ex;
        oldamt=expense.getAmt();
        LayoutInflater inflater=getLayoutInflater();
        View v;
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        v=inflater.inflate(R.layout.expense_delete,null);
        final TextView tv1=(TextView)v.findViewById(R.id.text1);
        final TextView tv2=(TextView)v.findViewById(R.id.text2);
        final TextView tv3=(TextView)v.findViewById(R.id.text3);
        final TextView tv4=(TextView)v.findViewById(R.id.text4);
        final TextView tv5=(TextView)v.findViewById(R.id.text5);
        final TextView tv6=(TextView)v.findViewById(R.id.text6);
        data=helper.getAllIncome();
        for(int j=0;j<data.size();j++)
        {
            if(ex.getIid()==data.get(j).getId())
            {
                income=data.get(j);
                break;
            }
        }
        Currency currency=helper.getCurrency(income.getCurrency());
        ammount=ex.getAmt()*SettingAct.set_currency_value/currency.getVal();
        ammount=model.getTruncate(ammount);
        tv1.setText("Income Name : " + income.getName());
        tv2.setText("Expense Name : " + ex.geteName());
        tv3.setText("Expense Amount : " + ammount);
        tv4.setText("Currency : " + income.getCurrency());
        tv5.setText("Card : " + income.getCard());
        SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try
        {
            Date dd = sdf1.parse(ex.geteDate());
            String str=dd.toString();
            StringBuilder sb=new StringBuilder();
            String[] as=str.split(" ");
            sb.append("On ");
            sb.append(as[0]);
            sb.append(",");
            sb.append(as[1]);
            sb.append(" ");
            sb.append(as[2]);
            sb.append(",");
            sb.append(as[5]);
            sb.append(" at ");
            String[] sa=as[3].split(":");
            sb.append(sa[0]);
            sb.append(":");
            sb.append(sa[1]);
            tv6.setText(sb.toString());
        }
        catch (ParseException E)
        {
            E.printStackTrace();
        }
        switch (i)
        {
            case 0:builder.setTitle("DETAILED INFORMATION..").setView(v).setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    updateMethod(expense);
                    dialog.dismiss();
                }
            }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
                break;
            case 1:builder.setTitle("Deletion....").setView(v).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sure();
                        dialog.dismiss();
                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                break;
            default:
                Toast.makeText(this,"Something went wrong",Toast.LENGTH_LONG).show();
        }
        AlertDialog dialog=builder.create();
        dialog.show();
    }
    public void updateMethod(Expense ex)
    {
        LayoutInflater inflater=this.getLayoutInflater();
        View v=inflater.inflate(R.layout.expense_update_dialog, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        final EditText et1=(EditText)v.findViewById(R.id.exname);
        final EditText et2=(EditText)v.findViewById(R.id.examt);
        final Button bt=(Button)v.findViewById(R.id.bt1);
        final Spinner sp1=(Spinner)v.findViewById(R.id.spinner);
        et1.setText(ex.geteName());
        et2.setText(String.valueOf(ammount));
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDate();
            }
        });
        data1=helper.getAllIncomeName();
        data1.add(0, "Choose Income");
        adapter1=new ArrayAdapter(this,R.layout.my_spinner_items,data1)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v=super.getView(position, convertView, parent);
                ((TextView)v).setTextSize(15);
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
                    int pos=-1;
                    String name = data1.get(position);
                    position--;
                    income = helper.getIncome(name);
                    data=helper.getAllIncome();
                    if (income!=null) {
                        for(int i=0;i<data.size();i++)
                        {
                            if(data.contains(income)&&i<=position)
                                pos++;
                        }
                        income1 = data.get(pos);
                    } else {
                        Toast.makeText(getApplicationContext(), "Empty", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    long id1=oldExpense.getIid();
                    income1=helper.getIncome(id1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                    long id1=oldExpense.getEid();
                income1=helper.getIncome(id1);
            }
        });
        builder.setTitle("Updating....").setView(v).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                eName = et1.getText().toString();
                amount = et2.getText().toString();
                try {
                    amt = Double.parseDouble(amount);
                } catch (Exception e) {
                    e.printStackTrace();
                    amt=0.0;
                }
                sure();
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
            case 0:setContentView(R.layout.activity_expense_display);
                break;
            case 1:setContentView(R.layout.activity_expense_display);
                break;
            case 2:setContentView(R.layout.activity_expense_display_blue);
                break;
            case 3:setContentView(R.layout.activity_expense_display_yellow);
                break;
            case 4:setContentView(R.layout.activity_expense_display_green);
                break;
            case 5:setContentView(R.layout.activity_expense_display_pink);
                break;
            default:setContentView(R.layout.activity_expense_display);
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
    public void prepare_selection(View v,int pos)
    {
        if(((CheckBox)v).isChecked()) {
            dt.add(al1.get(pos));
            count = count + 1;
            updateCount(count);
        }
        else
        {
            dt.remove(al1.get(pos));
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
    public void clearActionBar()
    {
        isAction=false;
        tb.getMenu().clear();
        tb.inflateMenu(R.menu.expense_display_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        counter.setVisibility(View.GONE);
        counter.setText("0 Item Selected");
        count=0;
        dt.clear();
    }
    public void resultCalculator(String s)
    {
        amount=s;
    }
    public void checkDate()
    {
        new DatePickerDialog(this, d, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    public void showMessage()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Invalid Data..Please Check").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }
    public void resultExpenseName(String s)
    {
        s=s.toLowerCase();
        adapter.swap(helper.getAllExpenseByName(s));
    }
    public void resultIncomeExpenseName(String s)
    {
        data=helper.getAllIncomeByName(s);
        List<Expense> al2=new ArrayList<>();
        for(int i=0;i<data.size();i++)
        {
            al1=helper.getAllExpenseByIncomeId(data.get(i).getId());
            al2.addAll(al1);
        }
        adapter.swap(al2);
    }
}
