package com.example.kiran.sourcemanagement;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

/**
 * Created by Kiran on 8/15/2017.
 */
public class ExpenseNameDialog extends DialogFragment
{
    String selection;
    ActDatabase helper;
    List<String> al;
    public interface MyExpenseName
    {
        void resultExpenseName(String s);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        helper=new ActDatabase(getActivity(),ActDatabase.DBNAME,null,ActDatabase.DBVERSION);
        al=helper.getAllExpenseName();
        if(al.size()==0)
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
            builder.setMessage("No Expense Data Available Sorry..").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog=builder.create();
            return dialog;
        }
        else {
            String[] as = new String[al.size()];
            for (int i = 0; i < al.size(); i++)
                as[i] = al.get(i);
            AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
            builder.setTitle("Choose Expense").setSingleChoiceItems(as, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selection = al.get(which);
                }
            }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MyExpenseName result = (MyExpenseName) getActivity();
                    result.resultExpenseName(selection);
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
