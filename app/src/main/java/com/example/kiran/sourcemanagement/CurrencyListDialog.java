package com.example.kiran.sourcemanagement;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Kiran on 7/18/2017.
 */
public class CurrencyListDialog extends DialogFragment
{
    LayoutInflater inflater;
    View v;
    LinearLayoutManager llm;
    List<Currency> al;
    CurrencyAdapter adapter;
    ActDatabase helper;
    RecyclerView recyclerView;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflater=getActivity().getLayoutInflater();
        v=inflater.inflate(R.layout.currency_all_list, null);
        recyclerView=(RecyclerView)v.findViewById(R.id.recy6);
        helper=new ActDatabase(getActivity(),ActDatabase.DBNAME,null,ActDatabase.DBVERSION);
        al=helper.getAllCurrency();
        adapter=new CurrencyAdapter(getActivity(),al);
        llm=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(v).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog=builder.create();
        return dialog;
    }
}
