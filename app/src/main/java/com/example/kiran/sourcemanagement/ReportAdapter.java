package com.example.kiran.sourcemanagement;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
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
 * Created by Kiran on 7/26/2017.
 */
public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyViewHolder>
{
    Context context;
    List<Report> dt;
    ReportAct act;
    List<Report> data;
    public ReportAdapter(List<Report> data,Context context)
    {
        this.context=context;
        this.data=data;
        act=(ReportAct)context;
        dt=new ArrayList<>();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv1,tv2,tv3,tv4;
        CardView cd;
        CheckBox cb;
        public MyViewHolder(View v)
        {
            super(v);
            tv1=(TextView)v.findViewById(R.id.text1);
            tv2=(TextView)v.findViewById(R.id.text2);
            tv3=(TextView)v.findViewById(R.id.text3);
            tv4=(TextView)v.findViewById(R.id.text4);
            cd=(CardView)v.findViewById(R.id.cardView1);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = getAdapterPosition();
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Report report=data.get(position);
        holder.tv1.setText(report.getName1());
        holder.tv2.setText(report.getName2());
        holder.tv3.setText(report.getName3());
        SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try
        {
            Date dd = sdf1.parse(report.getName4());
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
            holder.tv4.setText(sb.toString());
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.card_report_view, parent, false);
        MyViewHolder holder=new MyViewHolder(v);
        return holder;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
