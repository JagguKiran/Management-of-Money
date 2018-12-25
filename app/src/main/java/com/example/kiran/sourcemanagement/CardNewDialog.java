package com.example.kiran.sourcemanagement;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Kiran on 7/18/2017.
 */
public class CardNewDialog extends DialogFragment
{
    LayoutInflater inflater;
    View v;
    EditText et1,et2;
    String s1,s2;
    TextView tv;
    public interface MyNewCard
    {
        void resultNewCard(String s1,String s2);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflater=getActivity().getLayoutInflater();
        v=inflater.inflate(R.layout.card_onclick, null);
        et1=(EditText)v.findViewById(R.id.ec1);
        et2=(EditText)v.findViewById(R.id.ec2);
        tv=(TextView)v.findViewById(R.id.tc1);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                help();
            }
        });
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(v).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                s1=et1.getText().toString();
                s2=et2.getText().toString();
                if((s1.trim().equals("")||s1==null)&&(s2.trim().equals("")||s2==null))
                {
                    dialog.dismiss();
                }
                else {
                    MyNewCard card = (MyNewCard) getActivity();
                    card.resultNewCard(s1, s2);
                    dialog.dismiss();
                }
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
    public void help()
    {
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View v=inflater.inflate(R.layout.card_number_dialog,null);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setMessage("RESTRICTION OF CARD NUMBER").setView(v).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }
}
