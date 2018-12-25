package com.example.kiran.sourcemanagement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IncomeDetailsAct extends AppCompatActivity {

    LayoutInflater inflater;
    View v;
    AlertDialog.Builder builder;
    String name,amt,cur,card,date;
    TextView tv1,tv2,tv3,tv4,tv5;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent in=getIntent();
        name=in.getStringExtra("NAME");
        amt=in.getStringExtra("AMOUNT");
        cur=in.getStringExtra("CURRENCY");
        card=in.getStringExtra("CARD");
        date=in.getStringExtra("DATE");
        inflater=getLayoutInflater();
        v=inflater.inflate(R.layout.income_delete, null);
        tv1=(TextView)v.findViewById(R.id.text1);
        tv2=(TextView)v.findViewById(R.id.text2);
        tv3=(TextView)v.findViewById(R.id.text3);
        tv4=(TextView)v.findViewById(R.id.text4);
        tv5=(TextView)v.findViewById(R.id.text5);
        tv1.setText("Name : "+name.toUpperCase());
        tv2.setText("Amount : "+amt);
        tv3.setText("Currency : "+cur.toUpperCase());
        tv4.setText("Card : "+card.toUpperCase());
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date d = sdf.parse(date);
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
            tv5.setText(sb.toString());
        }
        catch (ParseException E)
        {
            E.printStackTrace();
        }
        builder=new AlertDialog.Builder(this);
        builder.setTitle("  INCOME DETAILS...").setView(v).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent1=new Intent(getApplicationContext(),IncomeAct.class);
                startActivity(intent1);
                dialog.dismiss();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent2=new Intent(getApplicationContext(),IncomeAct.class);
                startActivity(intent2);
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
