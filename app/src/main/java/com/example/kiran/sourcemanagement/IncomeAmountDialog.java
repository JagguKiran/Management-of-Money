package com.example.kiran.sourcemanagement;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Kiran on 7/16/2017.
 */
public class IncomeAmountDialog extends DialogFragment implements Calculator.CalculatorResult {
    LayoutInflater inflater;
    View v;
    EditText et;
    String str;
    ImageView imageView;
    String result;
    IncomeAmountDialog dialog1;
    public IncomeAmountDialog()
    {
        dialog1=this;
    }
    public interface MyDialogFragmentListener
    {
        void onResultViewAmount(String str);
    }
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        inflater=getActivity().getLayoutInflater();
        v=inflater.inflate(R.layout.income_amount, null);
        et=(EditText)v.findViewById(R.id.editText1);
        imageView=(ImageView)v.findViewById(R.id.image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calculator cal=new Calculator(getActivity(),dialog1);
                cal.calculate();
            }
        });
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(v).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                str=et.getText().toString();
                MyDialogFragmentListener act=(MyDialogFragmentListener)getActivity();
                act.onResultViewAmount(str);
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
    public void resultCalculator(String s)
    {
        et.setText(s);
    }
}
