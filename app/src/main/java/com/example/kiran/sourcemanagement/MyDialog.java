package com.example.kiran.sourcemanagement;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Kiran on 7/16/2017.
 */
public class MyDialog extends DialogFragment
{
    LayoutInflater inflater;
    View v;
    Button bt1,bt2;
    TextView et;
    EditText et1,et2;
    public MyDialog()
    {
    }
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        inflater=getActivity().getLayoutInflater();
        v=inflater.inflate(R.layout.user_login_detail, null);
        et1=(EditText)v.findViewById(R.id.userId);
        et2=(EditText)v.findViewById(R.id.pwd);
        et=(TextView)v.findViewById(R.id.et1);
        bt1=(Button)v.findViewById(R.id.b1);
        bt2=(Button)v.findViewById(R.id.b2);
        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performAction();
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOperation();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelOperation();
            }
        });
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(v);
        return builder.create();
    }
    public void performAction()
    {
        Intent inn1=new Intent(getActivity(),RegisterAct.class);
        startActivity(inn1);
    }
    public void saveOperation()
    {
        String name=et1.getText().toString();

        String pass=et2.getText().toString();


        //business logic that means authenatication process

        Intent inn=new Intent(getActivity(),SourceMainAct.class);
        startActivity(inn);
    }
    public void cancelOperation()
    {
        Toast.makeText(getActivity(),"You clicked cancel button",Toast.LENGTH_LONG).show();
    }
}
