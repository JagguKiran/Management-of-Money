package com.example.kiran.sourcemanagement;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Kiran on 7/17/2017.
 */
public class ExpenseDisplayAdapter extends RecyclerView.Adapter<ExpenseDisplayAdapter.MyViewHolder>
{
    Context context;
    List<Expense> data;
    RecyclerView rv;
    int p;
    List<Expense> dt;
    ExpenseDisplay act;
    ActDatabase helper;
    List<Income> ins;
    Income income;
    public ExpenseDisplayAdapter(Context context,List<Expense> data,RecyclerView rv)
    {
        this.context=context;
        this.data=data;
        this.rv=rv;
        act=(ExpenseDisplay)context;
        dt=new ArrayList<>();
        helper=new ActDatabase(context,ActDatabase.DBNAME,null,ActDatabase.DBVERSION);
    }
    public interface MyExpenseDisplayResult
    {
        void resultExpenseDisplay(Expense e,int i);
    }
    public void swap(List<Expense> d)
    {
        data.clear();
        data.addAll(d);
        notifyDataSetChanged();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView tv1,tv2,tv3,tv4,tv5;
        CardView cd;
        CheckBox cb;
        public MyViewHolder(View v)
        {
            super(v);
            tv1=(TextView)v.findViewById(R.id.text1);
            tv2=(TextView)v.findViewById(R.id.text2);
            tv3=(TextView)v.findViewById(R.id.text3);
            tv4=(TextView)v.findViewById(R.id.text4);
            tv5=(TextView)v.findViewById(R.id.text5);
            cd=(CardView)v.findViewById(R.id.cardView);
            cb=(CheckBox)v.findViewById(R.id.cb1);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int ii=0;
                    int i=getAdapterPosition();
                    Expense ex=data.get(i);
                    MyExpenseDisplayResult result=(MyExpenseDisplayResult)context;
                    result.resultExpenseDisplay(ex,ii);
                }
            });
            cd.setOnLongClickListener(act);
            cb.setOnClickListener(this);
        }
        public void onClick(View v)
        {
            act.prepare_selection(v,getAdapterPosition());
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Expense e=data.get(position);
        ins=helper.getAllIncome();
        for(int i=0;i<ins.size();i++)
        {
            if(e.getIid()==ins.get(i).getId())
            {
                income=ins.get(i);
                break;
            }
        }
        Currency currency=helper.getCurrency(income.getCurrency());
        double amount=e.getAmt()*currency.getVal()/SettingAct.set_currency_value;
        e.setAmt(amount);
        holder.tv1.setText("Expense Name     :: "+e.geteName().toUpperCase());
        holder.tv2.setText("Expense Amount :: "+SettingAct.set_currency_symbol+e.getAmt());
        holder.tv3.setText("Expense By            :: "+ income.getCard().toUpperCase());
        holder.tv4.setText("Through Income  :: "+income.getName().toUpperCase());
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date d = sdf.parse(e.geteDate());
            String str=d.toString();
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

            holder.tv5.setText(sb.toString());
        }
        catch (ParseException E)
        {
            E.printStackTrace();
        }
        if(!act.isAction)
        {
            holder.cb.setVisibility(View.GONE);
        }
        else
        {
            holder.cb.setVisibility(View.VISIBLE);
            holder.cb.setChecked(false);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.expense_card_view_display, parent, false);
        MyViewHolder holder=new MyViewHolder(v);
        return holder;
    }
    public void updateAdapter(List<Expense> d)
    {
        for(Expense e:d)
        {
            data.remove(e);
            helper.deleteExpense(e);
        }
        swap(helper.getAllExpense());
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void showDeleteDetail(final RecyclerView.ViewHolder holder,final RecyclerView rv)
    {
        p=holder.getAdapterPosition();
        JobTask task=new JobTask();
        task.execute(0);

    }
    private class JobTask extends AsyncTask<Integer,Void,String>
    {
        ProgressDialog dialog;
        int status;

        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
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
            notificationMethod();
        }
    }
    public void notificationMethod()
    {
        final Expense ex=data.get(p);
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v=inflater.inflate(R.layout.expense_delete,null);
        final TextView tv1=(TextView)v.findViewById(R.id.text1);
        final TextView tv2=(TextView)v.findViewById(R.id.text2);
        final TextView tv3=(TextView)v.findViewById(R.id.text3);
        final TextView tv4=(TextView)v.findViewById(R.id.text4);
        final TextView tv5=(TextView)v.findViewById(R.id.text5);
        final TextView tv6=(TextView)v.findViewById(R.id.text6);
        tv1.setText("Income Name : "+income.getName());
        tv2.setText("Expense Name : "+ex.geteName());
        tv3.setText("Expense Amount : "+ex.getAmt());
        tv4.setText("Currency : "+income.getCurrency());
        tv5.setText("Card : "+income.getCard());
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
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        data.remove(p);
        notifyItemRemoved(p);
        rv.scrollToPosition(p);
        dt.add(ex);
        builder.setTitle("DELETE...").setView(v).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean t=helper.deleteExpense(ex);
                swap(helper.getAllExpense());
                dialog.dismiss();
            }
        }).setNegativeButton("UNDO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                data.add(p,ex);
                notifyItemInserted(p);
                rv.scrollToPosition(p);
                dt.remove((dt.size()-1));
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }
}
