package com.example.kiran.sourcemanagement;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.List;

/**
 * Created by Kiran on 7/18/2017.
 */
public class CardDeleteDialog extends DialogFragment
{
    String selection;
    List<String> al;
    String[] as;
    AlertDialog.Builder builder;
    ActDatabase helper;
    public interface MyCardDeleteResult
    {
        void resultDeleteCard(String s);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        helper = new ActDatabase(getActivity(), ActDatabase.DBNAME, null, ActDatabase.DBVERSION);
        al = helper.getAllCardNames();
        if (al.size() == 0) {
                builder=new AlertDialog.Builder(getActivity());
            builder.setMessage("No Card Available Sorry..").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            return builder.create();
        } else {
            as = new String[al.size()];
            for (int i = 0; i < al.size(); i++) {
                as[i] = al.get(i).toUpperCase();
            }
            builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("DELETE CARD").setSingleChoiceItems(as, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selection = al.get(which);
                }
            }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (selection != null) {
                        MyCardDeleteResult card = (MyCardDeleteResult) getActivity();
                        card.resultDeleteCard(selection);
                    }
                }
            });
            AlertDialog dialog = builder.create();
            return dialog;
        }
    }
}
