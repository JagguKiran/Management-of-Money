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

/**
 * Created by Kiran on 7/18/2017.
 */
public class CurrencyNewDialog extends DialogFragment
{
    LayoutInflater inflater;
    View v;
    EditText et1,et2,et3;
    String s1,s2,val;
    int value;
    public interface MyCurrencyResult
    {
        void resultCurrency(String s1,String s2,int a);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflater=getActivity().getLayoutInflater();
        View v=inflater.inflate(R.layout.currency_new_dialog,null);
        et1=(EditText)v.findViewById(R.id.ec1);
        et2=(EditText)v.findViewById(R.id.ec2);
        et3=(EditText)v.findViewById(R.id.ec3);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(v).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                s1 = et1.getText().toString();
                s2 = et2.getText().toString();
                if(s2.trim().equalsIgnoreCase("Rs"))
                {
                    s2="\u20B9";
                }
                else if(s1.trim().equalsIgnoreCase("pound")||s1.trim().equalsIgnoreCase("pounds"))
                {
                    s2="\u00A3";
                }
                else if(s1.trim().equalsIgnoreCase("Euro")||s1.trim().equalsIgnoreCase("Euros"))
                {
                    s2="\u20AC";
                }
                val=et3.getText().toString();
                try
                {
                    value=Integer.parseInt(val);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    value=1;
                }
                MyCurrencyResult result=(MyCurrencyResult)getActivity();
                result.resultCurrency(s1,s2,value);
                dialog.dismiss();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog=builder.create();
        return dialog;
    }
}
