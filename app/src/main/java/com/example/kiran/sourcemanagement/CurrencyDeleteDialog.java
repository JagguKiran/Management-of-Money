package com.example.kiran.sourcemanagement;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kiran on 8/13/2017.
 */
public class CurrencyDeleteDialog extends DialogFragment {
    ActDatabase helper;
    AlertDialog.Builder builder;
    String selection;
    List<String> al = new ArrayList<>();
    String[] as;

    public interface MyDeleteCurrency {
        void resultDeleteCurrency(String s);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        helper = new ActDatabase(getActivity(), ActDatabase.DBNAME, null, ActDatabase.DBVERSION);
        al = helper.getAllCurrencyName();
        if (al.size() == 0)
        {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
            builder1.setMessage("Sorry No Currency Available..").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            return builder1.create();
        }
        else
        {
            as = new String[al.size()];
            for (int i = 0; i < al.size(); i++) {
                as[i] = al.get(i);
            }
            builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("DELETION").setSingleChoiceItems(as, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selection = al.get(which);
                }
            }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(selection!=null) {
                        MyDeleteCurrency res = (MyDeleteCurrency) getActivity();
                        res.resultDeleteCurrency(selection);
                    }
                    dialog.dismiss();
                }
            });
            return builder.create();
        }
    }
}
