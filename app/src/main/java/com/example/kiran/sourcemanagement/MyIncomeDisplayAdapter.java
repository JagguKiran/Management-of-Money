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
public class MyIncomeDisplayAdapter extends RecyclerView.Adapter<MyIncomeDisplayAdapter.MyViewHolder>
{
    List<Income> data;
    Context context;
    List<Income> dt;
    int poss;
    RecyclerView rv;
    IncomeDisplay act;
    ActDatabase helper;
    public MyIncomeDisplayAdapter(List<Income> data,Context context,RecyclerView recyclerView)
    {
        this.context=context;
        this.data=data;
        rv=recyclerView;
        act=(IncomeDisplay)context;
        dt=new ArrayList<>();
        helper=new ActDatabase(context,ActDatabase.DBNAME,null,ActDatabase.DBVERSION);
    }
    public interface MyIncomeDisplayResult
    {
        void resultIncomeDisplay(Income i,int ii);
    }
    public void swap(List<Income> d)
    {
        data.clear();
        data.addAll(d);
        notifyDataSetChanged();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView tv1,tv2,tv3,tv4;
        CardView cd;
        CheckBox cb;
        public MyViewHolder(View view)
        {
            super(view);
            tv1=(TextView)view.findViewById(R.id.text1);
            tv2=(TextView)view.findViewById(R.id.text2);
            tv3=(TextView)view.findViewById(R.id.text3);
            tv4=(TextView)view.findViewById(R.id.text4);
            cd=(CardView)view.findViewById(R.id.cardView);
            cb=(CheckBox)view.findViewById(R.id.cb1);
            cd.setOnLongClickListener(act);
            cb.setOnClickListener(this);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                        int ii = 0;
                        int i = getAdapterPosition();
                        Income in = data.get(i);
                        MyIncomeDisplayResult result = (MyIncomeDisplayResult) context;
                        result.resultIncomeDisplay(in, ii);
                }
            });
        }
        public void onClick(View v)
        {
            act.prepare_selection(v,getAdapterPosition());
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Income income=data.get(position);
        Currency currency=helper.getCurrency(income.getCurrency());
        double amount=income.getAmt()*currency.getVal()/SettingAct.set_currency_value;
        income.setAmt(amount);
        holder.tv1.setText("Income Name      :: " + income.getName().toUpperCase());
        holder.tv2.setText("Income Amount  :: "+SettingAct.set_currency_symbol+income.getAmt());
        holder.tv3.setText("Income Card        :: "+income.getCard().toUpperCase());
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date d = sdf.parse(income.getDate());
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

            holder.tv4.setText(sb.toString());
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
        View v=inflater.inflate(R.layout.income_card_view_display, parent, false);
        MyViewHolder holder=new MyViewHolder(v);
        return holder;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public void showDeleteDetail(final RecyclerView.ViewHolder holder,final RecyclerView rv)
    {
        final int pos=holder.getAdapterPosition();
        poss=pos;
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
        final Income in=data.get(poss);
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v=inflater.inflate(R.layout.income_delete,null);
        final TextView tv1=(TextView)v.findViewById(R.id.text1);
        final TextView tv2=(TextView)v.findViewById(R.id.text2);
        final TextView tv3=(TextView)v.findViewById(R.id.text3);
        final TextView tv4=(TextView)v.findViewById(R.id.text4);
        final TextView tv5=(TextView)v.findViewById(R.id.text5);
        tv1.setText("Name : " + in.getName());
        tv2.setText("Amount : " + String.valueOf(in.getAmt()));
        SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try
        {
            Date dd = sdf1.parse(in.getDate());
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
            tv5.setText(sb.toString());
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        tv4.setText("Card : " + in.getCard());
        tv3.setText("Currency : " + in.getCurrency());
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        data.remove(poss);
        notifyItemRemoved(poss);
        rv.scrollToPosition(poss);
        dt.add(in);
        builder.setTitle("DELETION..").setView(v).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean t=helper.deleteIncome(in.getId());
                swap(helper.getAllIncome());
                dialog.dismiss();
            }
        }).setNegativeButton("UNDO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                data.add(poss,in);
                notifyItemInserted(poss);
                rv.scrollToPosition(poss);
                dt.remove((dt.size() - 1));
                dialog.dismiss();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }
    public void updateAdapter(List<Income> d)
    {
        for(Income i:d)
        {
            data.remove(i);
            helper.deleteIncome(i.getId());
        }
        swap(helper.getAllIncome());
        notifyDataSetChanged();
    }
}


