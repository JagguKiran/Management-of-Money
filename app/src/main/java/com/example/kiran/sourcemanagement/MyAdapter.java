package com.example.kiran.sourcemanagement;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Kiran on 7/16/2017.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>
{
    List<String> data;
    Context context;
    int prev=0;
    public MyAdapter(Context ctx,List<String> data)
    {
        this.data=data;
        context=ctx;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv;
        public MyViewHolder(View v)
        {
            super(v);
            tv=(TextView)v.findViewById(R.id.tv11);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View row=inflater.inflate(R.layout.source_main_layout,parent,false);
        MyViewHolder holder=new MyViewHolder(row);
        return holder;
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        String name=data.get(position);
        final String naam=name;
        holder.tv.setText(name);
        if(position>prev)
        {
            AnimatorUtil.animate(holder,true);
        }
        else
        {
            AnimatorUtil.animate(holder,false);
        }
        prev=position;
        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch(naam)
                {
                    case "INCOME":Intent incomeIntent=new Intent(context,IncomeAct.class);
                        context.startActivity(incomeIntent);
                        break;
                    case "EXPENSE":Intent expenseIntent=new Intent(context,ExpenseAct.class);
                        context.startActivity(expenseIntent);
                        break;
                    case "CARDS":Intent cardsIntent=new Intent(context,CardAct.class);
                        context.startActivity(cardsIntent);
                        break;
                    case "SETTINGS":Intent settingsIntent=new Intent(context,SettingAct.class);
                                    context.startActivity(settingsIntent);
                                    break;
                    case "BALANCE":Intent balIntent=new Intent(context,BalanceAct.class);
                                   context.startActivity(balIntent);
                                   break;
                    case "REPORT":Intent reportIntent=new Intent(context,ReportAct.class);
                                  context.startActivity(reportIntent);
                                  break;
                    default:
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
