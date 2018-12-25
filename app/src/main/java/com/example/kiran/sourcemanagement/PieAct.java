package com.example.kiran.sourcemanagement;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.EventLogTags;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class PieAct extends AppCompatActivity {

    PieChart pieChart;
    List<Income> incomeName;
    List<Float> incomeAmount;
    List<Expense> expenseName;
    List<Float> expenseAmount;
    Description descr;
    int code=0;
    ActDatabase helper;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewMethod(SettingAct.theme_decider);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        helper=new ActDatabase(this,ActDatabase.DBNAME,null,ActDatabase.DBVERSION);
        pieChart=(PieChart)findViewById(R.id.pieChart);
        incomeName=helper.getAllIncome();
        incomeAmount=helper.getAllIncomeAmount();
        expenseName=helper.getAllExpense();
        expenseAmount=helper.getAllExpenseAmount();
        descr=new Description();
        descr.setTextColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        descr.setTextSize(15f);
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(5);
        pieChart.setTransparentCircleRadius(10f);
        pieChart.setCenterTextSize(12);
        pieChart.setDrawEntryLabels(true);
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                String[] as = h.toString().split(" ");
                String str1 = as[4];
                str1=str1.substring(0,str1.length()-1);
                String str2=as[2];
                str2=str2.substring(0,str2.indexOf('.'));
                int pos = 0;
                switch (code) {

                    case 1:
                        try {
                                Float num = Float.parseFloat(str1);
                                for (int i = 0; i < incomeAmount.size(); i++) {
                                    if (num==(float)(incomeAmount.get(i))) {
                                        pos = Integer.parseInt(str2);
                                        break;
                                    }
                            }
                        } catch (Exception ee) {
                            Log.d("KIRANKUMARUPPIN","Error Caught");
                            ee.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), "Selected item is " + incomeName.get(pos).getName(), Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        try {
                            Float num = Float.parseFloat(str1);
                            for (int i = 0; i < expenseAmount.size(); i++) {
                                if (num==(float)(expenseAmount.get(i))) {
                                    pos = Integer.parseInt(str2);
                                    break;
                                }
                            }
                        } catch (Exception ee) {
                            Log.d("KIRANKUMARUPPIN","Error Caught");
                            ee.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), "Selected item is " + expenseName.get(pos).geteName(), Toast.LENGTH_SHORT).show();
                        break;
                    default:Toast.makeText(getApplicationContext(),"WRONG SELECTED",Toast.LENGTH_SHORT).show();
                }
            }
            public void onNothingSelected() {

            }
        });
    }
    public void incomeChart(View v)
    {
        code=1;
        descr.setText("Income Details..");
        pieChart.setDescription(descr);
        pieChart.setCenterText("Income Details..");
        List<PieEntry> al1=new ArrayList<>();
        for(int i=0;i<incomeAmount.size();i++)
        {
            al1.add(new PieEntry(incomeAmount.get(i),i));
        }
        PieDataSet pds=new PieDataSet(al1,"Income Details..");
        pds.setSliceSpace(2f);
        pds.setValueTextSize(12f);

        List<Integer> colors=new ArrayList<>();
        colors.add(Color.GRAY);
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        colors.add(Color.RED);
        pds.setColors(colors);

        Legend lg=pieChart.getLegend();
        lg.setForm(Legend.LegendForm.CIRCLE);
        lg.setFormSize(10f);
        lg.setTextSize(12f);
        lg.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        lg.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        lg.setOrientation(Legend.LegendOrientation.VERTICAL);
        lg.setDrawInside(false);
        lg.setXEntrySpace(7f);
        lg.setYEntrySpace(0f);
        lg.setYOffset(0f);

        PieData pieData=new PieData(pds);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }
    public void expenseChart(View v)
    {
        code=2;
        descr.setText("Expense Details..");
        pieChart.setDescription(descr);
        pieChart.setCenterText("Expense Details..");
        List<PieEntry> al1=new ArrayList<>();
        for(int i=0;i<expenseAmount.size();i++)
        {
            al1.add(new PieEntry(expenseAmount.get(i),i));
        }
        PieDataSet pds=new PieDataSet(al1,"Expense Details..");
        pds.setSliceSpace(2f);
        pds.setValueTextSize(12f);

        List<Integer> colors=new ArrayList<>();
        colors.add(Color.GRAY);
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        colors.add(Color.RED);
        colors.add(Color.BLACK);
        colors.add(Color.BLUE);
        pds.setColors(colors);

        Legend lg=pieChart.getLegend();
        lg.setForm(Legend.LegendForm.CIRCLE);
        lg.setFormSize(10f);
        lg.setTextSize(12f);
        lg.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        lg.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        lg.setOrientation(Legend.LegendOrientation.VERTICAL);
        lg.setDrawInside(false);
        lg.setXEntrySpace(7f);
        lg.setYEntrySpace(0f);
        lg.setYOffset(0f);

        PieData pieData=new PieData(pds);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }
    public void setContentViewMethod(int i)
    {
        switch(i)
        {
            case 0:setContentView(R.layout.activity_pie);
                break;
            case 1:setContentView(R.layout.activity_pie);
                break;
            case 2:setContentView(R.layout.activity_pie_blue);
                break;
            case 3:setContentView(R.layout.activity_pie_yellow);
                break;
            case 4:setContentView(R.layout.activity_pie_green);
                break;
            case 5:setContentView(R.layout.activity_pie_pink);
                break;
            default:setContentView(R.layout.activity_pie);
        }
    }
}
