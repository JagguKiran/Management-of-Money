package com.example.kiran.sourcemanagement;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kiran on 8/15/2017.
 */
public class ReportModel
{
    List<Income> incomes;
    List<Expense> expenses;
    List<Currency> currencies;
    List<Income> ins;
    public ReportModel(List<Income> incomes,List<Expense> expenses,List<Currency> currencies,List<Income> ins)
    {
        this.incomes=incomes;
        this.expenses=expenses;
        this.currencies=currencies;
        this.ins=ins;
    }
    public List<Report> getAllReports()
    {
        List<Report> al=new ArrayList<>();
        int code=0,code1=0;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            if(expenses.size()==0)
            {
                for(int i=0;i<incomes.size();i++)
                {
                    Report report=new Report();
                    Income income=incomes.get(i);
                    report.setName1("Income Name     :: " + income.getName().toUpperCase());
                    report.setName2("Income Amount :: " + findCurrency(income.getCurrency())+ income.getAmt());
                    report.setName3("Credited In           :: " + income.getCard().toUpperCase());
                    report.setName4(income.getDate());
                    al.add(report);
                }
            }
            else if(incomes.size()==0)
            {
                for(int i=0;i<expenses.size();i++)
                {
                    Report report=new Report();
                    Expense expense=expenses.get(i);
                    Income income1=new Income();
                    for(int j=0;j<ins.size();j++)
                    {
                        if(ins.get(j).getId()==expense.getIid())
                        {
                            income1=ins.get(j);
                            break;
                        }
                    }
                    report.setName1("Expense Name     :: " + expense.geteName().toUpperCase());
                    report.setName2("Expense Amount :: " + findCurrency(income1.getCurrency())+ expense.getAmt());
                    report.setName3("Through Income  :: " + income1.getName().toUpperCase());
                    report.setName4(expense.geteDate());
                    al.add(report);
                }
            }
            else if (incomes.size() < expenses.size()) {
                for(int i=0,j=0;i<incomes.size();)
                {
                    Report report=new Report();
                    Income income=incomes.get(i);
                    Expense expense=expenses.get(j);
                    if(sdf.parse(income.getDate()).before(sdf.parse(expense.geteDate()))||sdf.parse(income.getDate()).equals(sdf.parse(expense.geteDate())))
                    {
                        report.setName1("Income Name     :: " + income.getName().toUpperCase());
                        report.setName2("Income Amount :: " + findCurrency(income.getCurrency())+ income.getAmt());
                        report.setName3("Credited In           :: " + income.getCard().toUpperCase());
                        report.setName4(income.getDate());
                        i++;
                        code1=i;
                        al.add(report);
                        if(i==incomes.size())
                        {
                            break;
                        }
                    }
                    else
                    {
                        Income income1=new Income();
                        for(int k=0;k<ins.size();k++)
                        {
                            if(expense.getIid()==ins.get(k).getId())
                            {
                                income1=ins.get(k);
                                break;
                            }
                        }
                        report.setName1("Expense Name     :: " + expense.geteName().toUpperCase());
                        report.setName2("Expense Amount :: " + findCurrency(income1.getCurrency())+ expense.getAmt());
                        report.setName3("Through Income  :: " + income1.getName().toUpperCase());
                        report.setName4(expense.geteDate());
                        j++;
                        code=j;
                        al.add(report);
                        if(j==expenses.size())
                        {
                            break;
                        }
                    }
                }
                if(code<expenses.size()) {
                    for (int i = code; i < expenses.size(); i++) {
                        Report report = new Report();
                        Expense expense = expenses.get(i);
                        Income income1 = new Income();
                        for (int j = 0; j < ins.size(); j++) {
                            if (ins.get(j).getId() == expense.getIid()) {
                                income1 = ins.get(j);
                            }
                        }
                        report.setName1("Expense Name     :: " + expense.geteName().toUpperCase());
                        report.setName2("Expense Amount :: " + findCurrency(income1.getCurrency()) + expense.getAmt());
                        report.setName3("Through Income  :: " + income1.getName().toUpperCase());
                        report.setName4(expense.geteDate());
                        al.add(report);
                    }
                }
                if(code1<incomes.size())
                {
                    for(int i=code1;i<incomes.size();i++)
                    {
                        Report report=new Report();
                        Income income1=incomes.get(i);
                        report.setName1("Income Name :: "+income1.getName().toUpperCase());
                        report.setName2("Income Amount :: "+findCurrency(income1.getCurrency())+income1.getAmt());
                        report.setName3("Credited In         :: "+income1.getCard().toUpperCase());
                        report.setName4(income1.getDate());
                        al.add(report);
                    }
                }
            }
            else
            {
                for(int i=0,j=0;i<expenses.size();)
                {
                    Report report=new Report();
                    Expense expense=expenses.get(i);
                    Income income=incomes.get(j);
                    if(sdf.parse(income.getDate()).before(sdf.parse(expense.geteDate())))
                    {
                        report.setName1("Income Name     :: " + income.getName().toUpperCase());
                        report.setName2("Income Amount :: " + findCurrency(income.getCurrency())+ income.getAmt());
                        report.setName3("Credited In           :: " + income.getCard().toUpperCase());
                        report.setName4(income.getDate());
                        j++;
                        code1=j;
                        al.add(report);
                        if(j==incomes.size())
                        {
                            break;
                        }
                    }
                    else
                    {
                        Income income1=new Income();
                        for(int k=0;k<ins.size();k++)
                        {
                            if(ins.get(k).getId()==expense.getIid())
                            {
                                income1=ins.get(k);
                                break;
                            }
                        }
                        report.setName1("Expense Name     :: "+expense.geteName().toUpperCase());
                        report.setName2("Expense Amount :: " + findCurrency(income1.getCurrency())+ expense.getAmt());
                        report.setName3("Through Income  :: " + income1.getName().toUpperCase());
                        report.setName4(expense.geteDate());
                        i++;
                        code=i;
                        al.add(report);
                        if(i==expenses.size())
                        {
                            break;
                        }
                    }
                }
                if(code1<incomes.size()) {
                    for (int i = code1; i < incomes.size(); i++) {
                        Report report = new Report();
                        Income income1 = incomes.get(i);
                        report.setName1("Income Name     :: " + income1.getName().toUpperCase());
                        report.setName2("Income Amount :: " + findCurrency(income1.getCurrency()) + income1.getAmt());
                        report.setName3("Credited In           :: " + income1.getCard().toUpperCase());
                        report.setName4(income1.getDate());
                        al.add(report);
                    }
                }
                if(code<expenses.size()) {
                    for (int i = code; i < expenses.size(); i++) {
                        Report report = new Report();
                        Expense expense = expenses.get(i);
                        Income income1 = new Income();
                        for (int k = 0; k < ins.size(); k++) {
                            if (ins.get(k).getId() == expense.getIid()) {
                                income1 = ins.get(k);
                                break;
                            }
                        }
                        report.setName1("Expense Name     :: " + expense.geteName().toUpperCase());
                        report.setName2("Expense Amount :: " + findCurrency(income1.getCurrency()) + expense.getAmt());
                        report.setName3("Through Income  :: " + income1.getName().toUpperCase());
                        report.setName4(expense.geteDate());
                        al.add(report);
                    }
                }
            }
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            Log.d("KIRANKUMARUPPIN", "Something went really wrong kirankumaruppinjagadevi");
        }
        return al;
    }
    public String findCurrency(String s)
    {
        String currency="";
        for(int x=0;x<currencies.size();x++)
        {
            Currency cur=currencies.get(x);
            if(cur.getCurName().trim().equalsIgnoreCase(s))
            {
                currency=cur.getCurSymbol();
                break;
            }
        }
        return currency;
    }
}
