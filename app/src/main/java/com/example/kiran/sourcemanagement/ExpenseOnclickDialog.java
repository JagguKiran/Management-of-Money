package com.example.kiran.sourcemanagement;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Kiran on 7/18/2017.
 */
public class ExpenseOnclickDialog extends DialogFragment implements Calculator.CalculatorResult
{
    LayoutInflater inflater;
    View v;
    Button button;
    ImageView imageView;
    String date;
    int amt;
    EditText et1;
    Spinner sp1;
    ActDatabase helper;
    ExpenseOnclickDialog dialog;
    List<Income> data;
    List<String> inData;
    Income selectedIncome,income1;
    ArrayAdapter<String> adapter;
    private MyOnclickExpense dialog1;
    DateFormat dateFormat=DateFormat.getDateTimeInstance();
    Calendar calendar=Calendar.getInstance();
    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener()
    {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            update();
        }
    };
    public void update()
    {
        date=dateFormat.format(calendar.getTime());
    }
    public void setListener(MyOnclickExpense d)
    {
        dialog1=d;
    }
    public ExpenseOnclickDialog()
    {
        dialog=this;
    }
    public interface MyOnclickExpense
    {
        void resultOnclickExpense(Income s1,int a,String date);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        inflater=getActivity().getLayoutInflater();
        View v=inflater.inflate(R.layout.expense_onclick_dialog,null);
        helper=new ActDatabase(getActivity(),ActDatabase.DBNAME,null,ActDatabase.DBVERSION);
        data=helper.getAllIncome();
        inData=helper.getAllIncomeName();
        et1=(EditText)v.findViewById(R.id.etamt);
        imageView=(ImageView)v.findViewById(R.id.imge);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calculator cal=new Calculator(getActivity(),dialog);
                cal.calculate();
            }
        });
        button=(Button)v.findViewById(R.id.bt1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(),d,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        sp1=(Spinner)v.findViewById(R.id.spinner);
        if(inData.size()==0)
            inData.add("NO INCOME ");
        else
        {
            inData.add(0,"CHOOSE INCOME");
        }
        adapter=new ArrayAdapter(getActivity(),R.layout.my_spinner_items,inData)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v=super.getView(position, convertView, parent);
                ((TextView)v).setTextSize(15);
                ((TextView)v).setGravity(Gravity.CENTER);
                return v;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v=super.getDropDownView(position, convertView, parent);
                ((TextView) v).setGravity(Gravity.START);
                return v;
            }
        };
        sp1.setAdapter(adapter);
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    int pos = -1;
                    String name = inData.get(position);
                    position--;
                    selectedIncome = helper.getIncome(name);
                    if (selectedIncome != null) {
                        for (int i = 0; i < data.size(); i++) {
                            if (data.contains(selectedIncome) && i <= position)
                                pos++;
                        }
                        income1 = data.get(pos);
                    } else {
                        Toast.makeText(getActivity(), "Empty", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(v).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String amount = et1.getText().toString();
                try {
                    amt = Integer.parseInt(amount);
                } catch (Exception e) {
                    e.printStackTrace();
                    amt=0;
                }

                update(income1, amt,date);
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
    public void update(Income income,int amt,String date)
    {
        if(dialog1!=null)
            dialog1.resultOnclickExpense(income,amt,date);
    }
    public void resultCalculator(String s)
    {
        et1.setText(s);
    }
}
