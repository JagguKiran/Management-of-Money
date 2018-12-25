package com.example.kiran.sourcemanagement;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Kiran on 7/31/2017.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>
{
    List<String> data;
    Context context;
    public CustomAdapter(Context c,List<String> al)
    {
        data=al;
        context=c;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv1;
        TextView tv2;
        ImageView im;
        public MyViewHolder(View v)
        {
            super(v);
            tv1=(TextView)v.findViewById(R.id.text1);
            tv2=(TextView)v.findViewById(R.id.text2);
            im=(ImageView)v.findViewById(R.id.image);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v=inflater.inflate(R.layout.card_about_app,null);
        MyViewHolder holder=new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String str=data.get(position);
        String[] as=str.split(":");
        holder.tv1.setText("   "+as[0]);
        holder.tv2.setText(as[1]);
        switch (position)
        {
            case 0:holder.im.setImageResource(R.drawable.income);
                   break;
            case 1:holder.im.setImageResource(R.drawable.expense);
                   break;
            case 2:holder.im.setImageResource(R.drawable.report);
                   break;
            case 3:holder.im.setImageResource(R.drawable.balance);
                   break;
            case 4:holder.im.setImageResource(R.drawable.card);
                  break;
            case 5:holder.im.setImageResource(R.drawable.settings);
                  break;
            default:holder.im.setImageResource(R.drawable.income);
        }
    }
    public int getItemCount() {
        return data.size();
    }
}
