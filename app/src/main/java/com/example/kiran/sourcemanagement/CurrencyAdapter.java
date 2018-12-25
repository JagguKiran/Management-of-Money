package com.example.kiran.sourcemanagement;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Kiran on 7/18/2017.
 */
public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.MyViewHolder>
{
    Context context;
    List<Currency> data;
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv1,tv2,tv3;
        public MyViewHolder(View v)
        {
            super(v);
            tv1=(TextView)v.findViewById(R.id.txt1);
            tv2=(TextView)v.findViewById(R.id.txt2);
            tv3=(TextView)v.findViewById(R.id.txt3);
        }
    }
    public CurrencyAdapter(Context ctx,List<Currency> data)
    {
        context=ctx;
        this.data=data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.currency_list, parent, false);
        MyViewHolder holder=new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Currency cur=data.get(position);
        holder.tv1.setText(cur.getCurName());
        holder.tv2.setText(cur.getCurSymbol());
        holder.tv3.setText(String.valueOf(cur.getVal()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
