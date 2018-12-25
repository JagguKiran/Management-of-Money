package com.example.kiran.sourcemanagement;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.List;

/**
 * Created by Kiran on 8/15/2017.
 */
public class ExpenseIncomeNameDialog extends DialogFragment
{
    List<String> al;
    ActDatabase helper;
    String selection;
    AlertDialog.Builder builder;
    public interface MyIncomeExpenseName
    {
        void resultIncomeExpenseName(String s);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        helper = new ActDatabase(getActivity(), ActDatabase.DBNAME, null, ActDatabase.DBVERSION);
        al = helper.getAllIncomeName();
        if (al.size() == 0) {
            builder=new AlertDialog.Builder(getActivity());
            builder.setMessage("No Income Data Available Sorry..").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            return builder.create();
        } else {
            String[] as = new String[al.size()];
            for (int i = 0; i < al.size(); i++)
                as[i] = al.get(i);
            builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Choose Income Name").setSingleChoiceItems(as, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selection = al.get(which);
                }
            }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MyIncomeExpenseName result = (MyIncomeExpenseName) getActivity();
                    result.resultIncomeExpenseName(selection);
                    dialog.dismiss();
                }
            }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            return dialog;
        }
    }
}
