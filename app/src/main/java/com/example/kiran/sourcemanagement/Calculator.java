package com.example.kiran.sourcemanagement;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Kiran on 8/3/2017.
 */
public class Calculator
{
    IncomeAmountDialog d;
    ExpenseOnclickDialog d1;
    ExpenseNewDialog d2;
    Context context;
    TextView tv;
    StringBuilder sb=new StringBuilder();
    double result;
    String str="";
    Button b1,b2,b3,b4,b5,b6,b7,b8,b9,b0,bp,bm,bs,bd,bc,be,bclr,bop,bcs,bdot;
    public interface CalculatorResult
    {
        void resultCalculator(String result);
    }
    public Calculator(Context c,IncomeAmountDialog dialog)
    {
        d=dialog;
        context=c;
    }
    public Calculator(Context c,ExpenseNewDialog d2)
    {
        this.d2=d2;
        context=c;
    }
    public Calculator(Context c,ExpenseOnclickDialog dialog)
    {
        d1=dialog;
        context=c;
    }
    public Calculator(Context c)
    {
        context=c;

    }
    public void calculate()
    {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v=inflater.inflate(R.layout.calculator, null);
        find(v);
        bdot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.append(".");
                tv.setText(str.concat(sb.toString()));
            }
        });
        b0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.append("0");
                if(result!=0)
                    result=0;
                tv.setText(str.concat(sb.toString()));
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.append("1");
                if(result!=0)
                    result=0;
                tv.setText(str.concat(sb.toString()));
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.append("2");
                if(result!=0)
                    result=0;
                tv.setText(str.concat(sb.toString()));
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.append("3");
                if(result!=0)
                    result=0;
                tv.setText(str.concat(sb.toString()));
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.append("4");
                if(result!=0)
                    result=0;
                tv.setText(str.concat(sb.toString()));
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.append("5");
                if(result!=0)
                    result=0;
                tv.setText(str.concat(sb.toString()));
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.append("6");
                if(result!=0)
                    result=0;
                tv.setText(str.concat(sb.toString()));
            }
        });
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.append("7");
                if(result!=0)
                    result=0;
                tv.setText(str.concat(sb.toString()));
            }
        });
        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.append("8");
                if(result!=0)
                    result=0;
                tv.setText(str.concat(sb.toString()));
            }
        });
        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.append("9");
                if(result!=0)
                    result=0;
                tv.setText(str.concat(sb.toString()));
            }
        });
        bp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(result!=0)
                {
                    sb.append(result);
                }
                sb.append("+");
                tv.setText(str.concat(sb.toString()));
            }
        });
        bm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(result!=0)
                    sb.append(result);
                sb.append("-");
                tv.setText(str.concat(sb.toString()));
            }
        });
        bs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(result!=0)
                    sb.append(result);
                sb.append("*");
                tv.setText(str.concat(sb.toString()));
            }
        });
        bd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(result!=0)
                    sb.append(result);
                sb.append("/");
                tv.setText(str.concat(sb.toString()));
            }
        });
        bop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.append("(");
                tv.setText(str.concat(sb.toString()));
            }
        });
        bcs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.append(")");
                tv.setText(str.concat(sb.toString()));
            }
        });
        bc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sb.length() != 0) {
                    sb.deleteCharAt(sb.length() - 1);
                    tv.setText(sb.toString());
                }
            }
        });
        be.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sb.length() != 0) {
                    try {

                        InPostEval in = new InPostEval(sb.toString());
                        String str = in.doTrans();
                        result=in.postfixEvaluate(str);
                        sb=new StringBuilder();
                        tv.setText(String.valueOf(result));
                    }
                    catch (Exception e)
                    {

                        tv.setText(String.valueOf(result));
                        e.printStackTrace();
                    }
                }
            }
        });
        bclr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb=new StringBuilder("");
                tv.setText(sb.toString());
            }
        });
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setView(v).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                addinterfaceMethod(tv);
                dialog.dismiss();
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }
    public void addinterfaceMethod(View v)
    {
        TextView tv=(TextView)v;
        if (sb.length() != 0) {
            try {

                InPostEval in = new InPostEval(sb.toString());
                String str = in.doTrans();
                result=in.postfixEvaluate(str);
                sb=new StringBuilder();
                tv.setText(String.valueOf(result));
            }
            catch (Exception e)
            {
                tv.setText(String.valueOf(result));
                e.printStackTrace();
            }
        }
        if(d!=null) {
            CalculatorResult res = (CalculatorResult) d;
            String str = tv.getText().toString();
            res.resultCalculator(str);
        }
        if(d1!=null)
        {
            CalculatorResult res = (CalculatorResult) d1;
            String str = tv.getText().toString();
            res.resultCalculator(str);
        }
        if(d2!=null)
        {
            CalculatorResult res = (CalculatorResult) d2;
            String str = tv.getText().toString();
            res.resultCalculator(str);
        }
    }
    public void find(View v)
    {
        tv=(TextView)v.findViewById(R.id.et1);
        bdot=(Button)v.findViewById(R.id.bdot);
        b0=(Button)v.findViewById(R.id.b0);
        b1=(Button)v.findViewById(R.id.b1);
        b2=(Button)v.findViewById(R.id.b2);
        b3=(Button)v.findViewById(R.id.b3);
        b4=(Button)v.findViewById(R.id.b4);
        b5=(Button)v.findViewById(R.id.b5);
        b6=(Button)v.findViewById(R.id.b6);
        b7=(Button)v.findViewById(R.id.b7);
        b8=(Button)v.findViewById(R.id.b8);
        b9=(Button)v.findViewById(R.id.b9);
        bp=(Button)v.findViewById(R.id.bp);
        bm=(Button)v.findViewById(R.id.bm);
        bs=(Button)v.findViewById(R.id.bs);
        bd=(Button)v.findViewById(R.id.bd);
        bc=(Button)v.findViewById(R.id.bc);
        be=(Button)v.findViewById(R.id.be);
        bclr=(Button)v.findViewById(R.id.bclr);
        bop=(Button)v.findViewById(R.id.op);
        bcs=(Button)v.findViewById(R.id.cs);
    }
}
