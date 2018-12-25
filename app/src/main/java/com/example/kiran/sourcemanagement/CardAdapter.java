package com.example.kiran.sourcemanagement;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Kiran on 7/18/2017.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {
    List<String> data;
    Context context;
    String eName;
    ActDatabase helper=new ActDatabase(context,ActDatabase.DBNAME,null,ActDatabase.DBVERSION);
    public interface MyCardResult
    {
        void resultCard(int i,int j);
    }
    public void swap(List<String> data1)
    {
        data.clear();
        data.addAll(data1);
        notifyDataSetChanged();
    }
    public CardAdapter(Context context, List<String> data) {
        this.data = data;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        MyViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.textExp1);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int ii=0;
                    int i=getAdapterPosition();
                    MyCardResult result=(MyCardResult)context;
                    result.resultCard(i,ii);
                }
            });
            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int ii=1;
                    int i=getAdapterPosition();
                    MyCardResult result=(MyCardResult)context;
                    result.resultCard(i,ii);
                    return false;
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String name1 = data.get(position);
        eName = name1;
        holder.textView.setText(name1.toUpperCase());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.card_holder, parent, false);
        MyViewHolder holder=new MyViewHolder(v);
        return holder;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}