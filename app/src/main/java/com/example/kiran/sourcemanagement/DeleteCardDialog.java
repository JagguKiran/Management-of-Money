package com.example.kiran.sourcemanagement;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Kiran on 7/21/2017.
 */
public class DeleteCardDialog extends DialogFragment {
    LayoutInflater inflater;
    View v;
    String str1,str2;
    EditText et1,et2;
    public interface MyDeleteCardDialog {
        void resultDelete(String s1, String s2);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflater=getActivity().getLayoutInflater();
        v=inflater.inflate(R.layout.card_onclick, null);
        et1=(EditText)v.findViewById(R.id.ec1);
        et2=(EditText)v.findViewById(R.id.ec2);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Deletion...").setView(v).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                str1=et1.getText().toString();
                str2=et2.getText().toString();
                MyDeleteCardDialog dialog1=(MyDeleteCardDialog)getActivity();
                dialog1.resultDelete(str1,str2);
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