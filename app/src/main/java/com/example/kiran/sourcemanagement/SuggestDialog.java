package com.example.kiran.sourcemanagement;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Kiran on 7/31/2017.
 */
public class SuggestDialog extends DialogFragment
{
    LayoutInflater inflater;
    View v;
    TextView tv;
    AlertDialog.Builder builder;
    public interface ResultInterface
    {
        void result(int a);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflater=getActivity().getLayoutInflater();
        v=inflater.inflate(R.layout.suggest_dialog, null);
        tv=(TextView)v.findViewById(R.id.text);
        tv.setText("Have you already read the Frequent Asked Questions ?");
        builder=new AlertDialog.Builder(getActivity());
        builder.setView(v).setPositiveButton("DONE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ResultInterface res=(ResultInterface)getActivity();
                res.result(1);
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
