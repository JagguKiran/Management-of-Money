package com.example.kiran.sourcemanagement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.EventLogTags;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class BarAct extends AppCompatActivity{

    List<String> cards,l1;
    List<Float> iamt,eamt,l2;
    BarChart barChart;
    float barWidth=0.3f;
    float groupWidth=0.4f;
    float barSpace=0f;
    Description description;
    List<BarEntry> y1;
    List<BarEntry> y2;
    ActDatabase helper;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        helper=new ActDatabase(this,ActDatabase.DBNAME,null,ActDatabase.DBVERSION);
        cards=helper.getAllCardNames();
        iamt=helper.getAllIncomeAmount();
        eamt=helper.getAllExpenseAmount();
        description=new Description();
        description.setText("Income/Expense Details (In Rupees)");
        description.setTextSize(15f);
        description.setTextColor(Color.BLACK);
        barChart=(BarChart)findViewById(R.id.bar);
        barChart.setDescription(description);
        barChart.setPinchZoom(true);
        barChart.setScaleEnabled(true);
        barChart.setDrawBarShadow(false);
        barChart.setDrawGridBackground(false);
        y1=new ArrayList<>();
        y2=new ArrayList<>();
        addMethod();
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                String[] as = h.toString().split(" ");
                String str1 = as[4];
                str1=str1.substring(0,str1.length()-1);
                String str2=as[2];
                str2=str2.substring(0,str2.indexOf('.'));
                int pos = 0;
                try
                {
                    Float num = Float.parseFloat(str1);
                    for (int i = 0; i < cards.size(); i++) {
                        if (num==(float)(helper.getTotalExpenseByCard(cards.get(i))))
                        {
                            pos = Integer.parseInt(str2);
                            break;
                        }
                    }
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
                individualBarDialog(cards.get(pos));
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    public void addMethod()
    {
        for(int i=0;i<cards.size();i++)
        {
            y1.add(new BarEntry((int)helper.getTotalIncomeByCard(cards.get(i)),(int)helper.getTotalIncomeByCard(cards.get(i))));
        }
        for(int i=0;i<cards.size();i++)
        {
            y2.add(new BarEntry((int)helper.getTotalExpenseByCard(cards.get(i)),(int) helper.getTotalExpenseByCard(cards.get(i))));
        }
        BarDataSet set1,set2;
        set1=new BarDataSet(y1,"Income");
        set1.setColor(Color.GREEN);
        set2=new BarDataSet(y2,"Expense");
        set2.setColor(Color.RED);
        BarData data=new BarData(set1,set2);
        data.setValueFormatter(new LargeValueFormatter());


        barChart.setData(data);
        barChart.getBarData().setBarWidth(barWidth);
        barChart.getBarData().setHighlightEnabled(false);
        barChart.getBarData().getDataSetByIndex(1).setHighlightEnabled(true);
        barChart.getXAxis().setAxisMinimum(0f);
        barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupWidth, barSpace) * cards.size());
        barChart.groupBars(0, groupWidth, barSpace);
        barChart.invalidate();

        Legend l = barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
        l.setYOffset(20f);
        l.setXOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);



        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMaximum(cards.size());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(cards));

        barChart.getAxisRight().setEnabled(false);
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(25f);
        leftAxis.setAxisMinimum(0f);
    }
    public void individualBarDialog(String s)
    {
        LayoutInflater inflater=getLayoutInflater();
        View v=inflater.inflate(R.layout.individualexpensebar, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        PieChart pc=(PieChart)v.findViewById(R.id.pieChart);
        Description desc=new Description();
        desc.setText("Expenses List");
        desc.setTextColor(Color.BLACK);
        desc.setTextSize(15f);
        desc.setTextAlign(Paint.Align.RIGHT);
        pc.setDescription(desc);
        l1 = helper.getExpenseNamesByCard(s);
        l2 = helper.getExpenseAmountsByCard(s);
        pc.setRotationEnabled(true);
        pc.setHoleRadius(25f);
        pc.setTransparentCircleAlpha(5);
        pc.setTransparentCircleRadius(10f);
        pc.setCenterTextSize(12);
        pc.setDrawEntryLabels(true);
        pc.setCenterText(s);
        final List<PieEntry> al=new ArrayList<>();
        for(int i=0;i<l2.size();i++)
        {
            al.add(new PieEntry(l2.get(i),i));
        }
        PieDataSet pds=new PieDataSet(al,"Expense Details of "+s);
        pds.setSliceSpace(2f);
        pds.setValueTextSize(12f);

        List<Integer> colors=new ArrayList<>();
        colors.add(Color.GRAY);
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        colors.add(Color.RED);
        pds.setColors(colors);

        Legend lg=pc.getLegend();
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
        pc.setData(pieData);
        pc.invalidate();
        pc.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                String[] as = h.toString().split(" ");
                String str1 = as[4];
                str1=str1.substring(0,str1.length()-1);
                String str2=as[2];
                str2=str2.substring(0,str2.indexOf('.'));
                int pos=0;
                try {
                    Float num = Float.parseFloat(str1);
                    for (int i = 0; i < l2.size(); i++) {
                        if (num.equals(l2.get(i))) {
                            pos = Integer.parseInt(str2);
                            break;
                        }
                    }
                } catch (Exception ee) {
                    Log.d("KIRANKUMARUPPIN","Error Caught");
                    ee.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "Selected item is " + l1.get(pos), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
        builder.setTitle("This is "+s+" Bar").setView(v).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
}
