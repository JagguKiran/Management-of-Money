package com.example.kiran.sourcemanagement;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Kiran on 8/17/2017.
 */
public class ViewPagerDisplayFragment extends Fragment
{
    TextView textView1;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater inflater1=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater1.inflate(R.layout.view_pager_view,null);
        textView1=(TextView)view.findViewById(R.id.tv1);
        Bundle bundle=getArguments();
        String name1=bundle.getString("CARD_NAME");
        String name2=bundle.getString("BALANCE");
        textView1.setText("Balance In " + name1+" : "+SettingAct.set_currency_symbol+name2);
        return view;
    }
}
