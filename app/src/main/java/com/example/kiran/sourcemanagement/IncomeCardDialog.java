package com.example.kiran.sourcemanagement;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kiran on 7/16/2017.
 */
public class IncomeCardDialog extends DialogFragment {
    String selection;
    List<String> al;
    String[] as;
    ActDatabase helper;
    public interface MyDialogFragmentListener3
    {
        void onResultViewCard(String s);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        helper=new ActDatabase(getActivity(),ActDatabase.DBNAME,null,ActDatabase.DBVERSION);
        al=helper.getAllCardNames();
        if(al.size()==0) {
            AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
            builder.setTitle("No Card Available Sorry..").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog=builder.create();
            return dialog;
        }
        else
        {
            as=new String[al.size()];
            for (int i = 0; i < al.size(); i++) {
                as[i] = al.get(i);
            }
        }
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose your card..").setSingleChoiceItems(as, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selection=al.get(which);
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyDialogFragmentListener3 act=(MyDialogFragmentListener3)getActivity();
                act.onResultViewCard(selection);
            }
        });
        AlertDialog dialog=builder.create();
        return dialog;
    }
}
