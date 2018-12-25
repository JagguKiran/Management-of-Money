package com.example.kiran.sourcemanagement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.Date;

public class ExpenseDetailsAct extends AppCompatActivity {

    LayoutInflater inflater;
    View v;
    TextView tv1,tv2,tv3,tv4,tv5,tv6;
    AlertDialog.Builder builder;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater=getLayoutInflater();
        v=inflater.inflate(R.layout.expense_delete, null);
        tv1=(TextView)v.findViewById(R.id.text1);
        tv2=(TextView)v.findViewById(R.id.text2);
        tv3=(TextView)v.findViewById(R.id.text3);
        tv4=(TextView)v.findViewById(R.id.text4);
        tv5=(TextView)v.findViewById(R.id.text5);
        tv6=(TextView)v.findViewById(R.id.text6);
        Intent intent=getIntent();
        tv1.setText("Income Name : "+intent.getStringExtra("INAME"));
        tv2.setText("Expense Name : "+intent.getStringExtra("ENAME"));
        tv3.setText("Expense Amount : "+intent.getStringExtra("AMOUNT"));
        tv4.setText("Currency : "+intent.getStringExtra("CURRENCY"));
        tv5.setText("Card : "+intent.getStringExtra("CARD"));
        Date d=new Date();
        String str=d.toString();
        StringBuilder sb=new StringBuilder();
        String[] as=str.split(" ");
        sb.append("On ");
        sb.append(as[0]);
        sb.append(",");
        sb.append(as[1]);
        sb.append(" ");
        sb.append(as[2]);
        sb.append(",");
        sb.append(as[5]);
        sb.append(" at ");
        String[] sa=as[3].split(":");
        sb.append(sa[0]);
        sb.append(":");
        sb.append(sa[1]);
        tv6.setText(sb.toString());
        builder=new AlertDialog.Builder(this);
        builder.setTitle("  EXPENSE DETAILS").setView(v).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent in1=new Intent(getApplicationContext(),ExpenseAct.class);
                startActivity(in1);
                dialog.dismiss();
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent in2=new Intent(getApplicationContext(),ExpenseAct.class);
                startActivity(in2);
                dialog.dismiss();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }
}
