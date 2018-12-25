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
public class IncomeCurrencyDialog extends DialogFragment
{
    String[] as;
    List<String> al;
    String selection;
    ActDatabase helper;
    public interface MyDialogFragmentListener2
    {
        void onResultViewCurrency(String s);
    }
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        helper=new ActDatabase(getActivity(),ActDatabase.DBNAME,null,ActDatabase.DBVERSION);
        al=helper.getAllCurrencyName();
        if(al.size()==0) {
            AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
            builder.setTitle("No Currency Available Sorry..").setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
        builder.setTitle("Choose Currency..").setSingleChoiceItems(as, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selection=al.get(which);
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyDialogFragmentListener2 act=(MyDialogFragmentListener2)getActivity();
                act.onResultViewCurrency(selection);
            }
        });
        AlertDialog dialog=builder.create();
        return dialog;
    }
}
