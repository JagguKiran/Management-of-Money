package com.example.kiran.sourcemanagement;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Kiran on 7/26/2017.
 */
public class FromToDateDialog extends DialogFragment
{
    LayoutInflater inflater;
    View v;
    AlertDialog.Builder builder;
    Button b1,b2;
    String fsDate,tsDate;
    Date fDate,tDate;
    int set=0;
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
    public interface MyFromToDate
    {
        void resultDate(String fsDate,String tsDate);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflater=getActivity().getLayoutInflater();
        v=inflater.inflate(R.layout.from_to_date_dialog, null);
        b1=(Button)v.findViewById(R.id.bt1);
        b2=(Button)v.findViewById(R.id.bt2);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fDate(v);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tDate(v);
            }
        });
        builder=new AlertDialog.Builder(getActivity());
        builder.setView(v).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               updateDate();
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
    public void fDate(View v)
    {
        set=1;
        new DatePickerDialog(getActivity(),d,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    public void tDate(View v)
    {
        set=2;
        new DatePickerDialog(getActivity(),d,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    public void update()
    {
        switch (set)
        {
            case 1:fDate=calendar.getTime();
                   fsDate=dateFormat.format(calendar.getTime());
                   break;
            case 2:tDate=calendar.getTime();
                   tsDate=dateFormat.format(calendar.getTime());
                   break;
            default:
                Toast.makeText(getActivity(),"Invalid",Toast.LENGTH_LONG).show();
        }
    }
    public void updateDate()
    {
        try {
             if (fDate == null || tDate == null || fDate.compareTo(tDate) > 0)
             {
                 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                 builder.setTitle("Invalid Date...").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                MyFromToDate date=(MyFromToDate)getActivity();
                date.resultDate(fsDate,tsDate);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }

    }
}
