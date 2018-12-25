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
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Kiran on 7/17/2017.
 */
public class CardDisplayAdapter extends RecyclerView.Adapter<CardDisplayAdapter.MyViewHolder>
{
    Context context;
    List<Card> data;
    List<Card> dt;
    RecyclerView rv;
    int p;
    ActDatabase helper;
    CardDisplay act;
    public interface MyDisplayCardAdapter
    {
        void resultCardDisplay(Card c,int i);
    }
    public void swap(List<Card> d)
    {
        data.clear();
        data.addAll(d);
        notifyDataSetChanged();
    }
    public CardDisplayAdapter(Context context,List<Card> data,RecyclerView recyclerView)
    {
        this.context=context;
        this.data=data;
        rv=recyclerView;
        act=(CardDisplay)context;
        dt=new ArrayList<>();
        helper=new ActDatabase(context,ActDatabase.DBNAME,null,ActDatabase.DBVERSION);
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView tv1,tv2,tv3;
        CardView cd;
        CheckBox cb;
        public MyViewHolder(View v)
        {
            super(v);
            tv1=(TextView)v.findViewById(R.id.text1);
            tv2=(TextView)v.findViewById(R.id.text2);
            tv3=(TextView)v.findViewById(R.id.text3);
            cd=(CardView)v.findViewById(R.id.cardView);
            cb=(CheckBox)v.findViewById(R.id.cb1);
            cd.setOnLongClickListener(act);
            cb.setOnClickListener(this);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int ii = 0;
                    int i = getAdapterPosition();
                    Card c = data.get(i);
                    MyDisplayCardAdapter cardAdapter = (MyDisplayCardAdapter) context;
                    cardAdapter.resultCardDisplay(c, ii);
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
        Card c=data.get(position);
        holder.tv1.setText("Card Name     :: "+c.getcName());
        holder.tv2.setText("Card Number :: "+c.getCardNumber());
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date d = new Date();
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
            holder.tv3.setText(sb.toString());
        }
        catch (Exception E)
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
        View v=inflater.inflate(R.layout.card_card_view_display, parent, false);
        MyViewHolder holder=new MyViewHolder(v);
        return holder;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public void showDeleteDetail(final RecyclerView.ViewHolder holder,final RecyclerView recyclerView)
    {
        p=holder.getAdapterPosition();
        final Card c=data.get(p);
        JobTask task=new JobTask();
        task.execute(0);
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
            notificationMethod();
        }
    }
    public void notificationMethod()
    {
        final Card c=data.get(p);
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View v=inflater.inflate(R.layout.card_text_onclick,null);
        final TextView tv1=(TextView)v.findViewById(R.id.tc1);
        final TextView tv2=(TextView)v.findViewById(R.id.tc2);
        tv1.setText("Card Name : "+c.getcName());
        tv2.setText("Card Number : " + c.getCardNumber());
        data.remove(p);
        notifyItemRemoved(p);
        rv.scrollToPosition(p);
        dt.add(c);
        builder.setTitle("DELETE...").setView(v).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean t=helper.deleteCard(c.getId());
                swap(helper.getAllCard());
                if(t)
                    Toast.makeText(context,"Successfully Deleted..",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(context,"Unsuccessful...",Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }).setNegativeButton("UNDO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                data.add(p,c);
                notifyItemInserted(p);
                rv.scrollToPosition(p);
                dt.remove((dt.size()-1));
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }
    public void updateAdapter(List<Card> card)
    {
        for(Card c:card)
        {
            data.remove(c);
            helper.deleteCard(c.getId());
        }
        swap(helper.getAllCard());
        notifyDataSetChanged();
    }
}
