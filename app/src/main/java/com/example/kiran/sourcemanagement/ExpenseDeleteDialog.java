package com.example.kiran.sourcemanagement;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

/**
 * Created by Kiran on 7/17/2017.
 */
public class ExpenseDeleteDialog extends DialogFragment
{
    String[] as;
    String selection;
    List<String> al;
    ActDatabase helper;
    private MyDeleteExpense del;
    public interface MyDeleteExpense
    {
        void resultDeleteExpense(String s1);
    }
    void setListener(MyDeleteExpense d)
    {
        del=d;
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
            return builder.create();
        }
        else {
            as = new String[al.size()];
            for (int i = 0; i < al.size(); i++) {
                as[i] = al.get(i);
            }
        }
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("DELETE EXPENSE").setSingleChoiceItems(as, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selection=al.get(which);
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(selection!=null) {
                    MyDeleteExpense exp = (MyDeleteExpense) getActivity();
                    exp.resultDeleteExpense(selection);
                }
                dialog.dismiss();
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog=builder.create();
        return dialog;
    }
}
