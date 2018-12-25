package com.example.kiran.sourcemanagement;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.List;

/**
 * Created by Kiran on 8/17/2017.
 */
public class ViewPagerDisplayAdapter extends FragmentStatePagerAdapter
{
    List<String> data;
    ActDatabase helper;
    Context context;
    ConversionModel model=new ConversionModel();
    android.support.v4.app.FragmentManager manager;
    public ViewPagerDisplayAdapter(android.support.v4.app.FragmentManager fragmentManager,Context context)
    {
        super(fragmentManager);
        manager=fragmentManager;
        this.context=context;
        helper=new ActDatabase(context,ActDatabase.DBNAME,null,ActDatabase.DBVERSION);
        data=helper.getAllCardNames();
    }

    @Override
    public Fragment getItem(int position) {
        String card=data.get(position);
        double bal=helper.getBalance(card);
        bal=model.getTruncate(bal);
        ViewPagerDisplayFragment fragment=new ViewPagerDisplayFragment();
        Bundle bundle=new Bundle();
        bundle.putString("CARD_NAME",card);
        bundle.putString("BALANCE",String.valueOf(bal));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return data.size();
    }
}
