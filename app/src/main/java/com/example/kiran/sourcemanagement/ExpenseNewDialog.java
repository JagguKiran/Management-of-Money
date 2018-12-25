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
 * Created by Kiran on 7/17/2017.
 */
public class ExpenseNewDialog extends DialogFragment implements Calculator.CalculatorResult
{

    View v;
    LayoutInflater inflater;
    EditText et1,et2;
    Spinner sp1;
    Button bt;
    List<String> incomeNames;
    List<Income> data;
    String eName,date;
    double amt;
    Income selectedName,income1;
    ArrayAdapter<String> adapter;
    ImageView imageView;
    ExpenseNewDialog dialog;
    ActDatabase helper;
    AlertDialog.Builder builder;
    DateFormat dateFormat=DateFormat.getDateTimeInstance();
    Calendar calendar=Calendar.getInstance();
    public ExpenseNewDialog()
    {
        dialog=this;
    }
    public interface MyNewExpense
    {
        void resultNewExpense(String s,double amt,String str,Income income);
    }
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
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        helper=new ActDatabase(getActivity(),ActDatabase.DBNAME,null,ActDatabase.DBVERSION);
        incomeNames=helper.getAllIncomeName();
        builder=new AlertDialog.Builder(getActivity());
        if(incomeNames.size()==0)
        {
            builder.setTitle("No Income Data Available Sorry..").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            return builder.create();
        }
        else {
            inflater = getActivity().getLayoutInflater();
            v = inflater.inflate(R.layout.expense_new_dialog, null);
            et1 = (EditText) v.findViewById(R.id.exname);
            et2 = (EditText) v.findViewById(R.id.examt);
            bt = (Button) v.findViewById(R.id.bt1);
            sp1 = (Spinner) v.findViewById(R.id.spinner);

            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DatePickerDialog(getActivity(), d, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
            imageView = (ImageView) v.findViewById(R.id.imge);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calculator cal = new Calculator(getActivity(), dialog);
                    cal.calculate();
                }
            });
            incomeNames.add(0, "CHOOSE INCOME");
            adapter = new ArrayAdapter(getActivity(), R.layout.my_spinner_items, incomeNames) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    ((TextView) v).setTextSize(15);
                    ((TextView) v).setGravity(Gravity.CENTER);
                    return v;
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View v = super.getDropDownView(position, convertView, parent);
                    ((TextView) v).setGravity(Gravity.START);
                    return v;
                }
            };
            sp1.setAdapter(adapter);
            sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    if (position != 0)
                    {
                        int pos = -1;
                        String name = incomeNames.get(position);
                        position--;
                        selectedName = helper.getIncome(name);
                        data=helper.getAllIncome();
                        if (selectedName != null)
                        {
                            for (int i = 0; i < data.size(); i++)
                            {
                                if (data.contains(selectedName) && i <= position) {
                                    pos++;
                                }
                            }
                            income1 = data.get(pos);
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Empty", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            builder.setView(v).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    eName = et1.getText().toString();
                    String amount = et2.getText().toString();
                    try {
                        amt = Double.parseDouble(amount);
                    } catch (Exception e) {
                        e.printStackTrace();
                        amt = 0.0;
                    }
                    MyNewExpense exp = (MyNewExpense) getActivity();
                    exp.resultNewExpense(eName, amt, date, income1);
                    dialog.dismiss();
                }
            }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            return dialog;
        }
    }
    public void resultCalculator(String s)
    {
        et2.setText(s);
    }
}
