package com.example.kiran.sourcemanagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Kiran on 7/17/2017.
 */
public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.MyViewHolder> implements ExpenseOnclickDialog.MyOnclickExpense,ExpenseDeleteDialog.MyDeleteExpense
{
    List<String> data;
    Context context;
    String eName,eName1,eName2;
    String iName,date;
    int amt;
    int code;
    ActDatabase helper;
    Income income;
    ConversionModel model=new ConversionModel();
    public ExpenseAdapter(Context context,List<String> data)
    {
        this.data=data;
        this.context=context;
        helper=new ActDatabase(context,ActDatabase.DBNAME,null,ActDatabase.DBVERSION);
    }
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView;
        MyViewHolder(View v)
        {
            super(v);
            textView=(TextView)v.findViewById(R.id.textExp1);
        }
    }
    public void swap(List<String> dt)
    {
        data.clear();
        data.addAll(dt);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position)
    {
        String name1=data.get(position);
        eName=name1;
        holder.textView.setText(name1.toUpperCase());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos=holder.getAdapterPosition();
                eName1=data.get(pos);
                FragmentManager manager = ((Activity) context).getFragmentManager();
                ExpenseOnclickDialog dialog = new ExpenseOnclickDialog();
                dialog.setListener(ExpenseAdapter.this);
                dialog.show(manager, "MY_EXPENSE");
            }
        });
        holder.textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int pos = holder.getAdapterPosition();
                eName2 = data.get(pos);
                deleteDialog(eName2);
                return true;
            }
        });
    }
    public void deleteDialog(String name)
    {
        eName2=name;
        code=1;
        JobTask task=new JobTask();
        task.execute(0);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.expense_holder, parent, false);
        MyViewHolder holder=new MyViewHolder(v);
        return holder;
    }
    public void resultOnclickExpense(Income income,int a,String date)
    {
        iName=income.getName();
        amt=a;
        this.date=date;
        this.income=income;
        code=0;
        JobTask task=new JobTask();
        task.execute(0);

    }
    public void resultDeleteExpense(String s)
    {
    }
    private class JobTask extends AsyncTask<Integer,Void,String>
    {
        ProgressDialog dialog;
        int status;
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(context);
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
                case 0:addExpense();
                        break;
                case 1:removeDialog(eName2);
                       break;
                default:Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show();
            }
        }
    }
    public void removeDialog(String name)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("Deletion....").setMessage("Do you want to delete "+name).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sure(eName2);
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
    public void sure(final String s)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("Expense:"+s).setMessage("Are You Sure ?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                helper.deleteExpense(s);
                swap(helper.getAllExpenseName());
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
    public void addExpense()
    {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dt1=model.performDate(date);
        String dt2=income.getDate();
        try {
            if (amt <= 0||amt>helper.getBalance(income.getCard(),income.getCurrency())||sdf.parse(dt1).before(sdf.parse(dt2))) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setTitle("Invalid Data...Please Check").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                AlertDialog dialog=builder.create();
                dialog.show();
            } else {
                    Expense expense = new Expense();
                    expense.seteName(eName1);
                    Currency currency=helper.getCurrency(income.getCurrency());
                    amt=amt*currency.getVal()/SettingAct.set_currency_value;
                    expense.setAmt(amt);
                    expense.seteDate(date);
                    expense.setIid(income.getId());
                    helper.addExpense(expense);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
