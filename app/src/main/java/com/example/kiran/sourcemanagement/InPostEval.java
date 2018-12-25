package com.example.kiran.sourcemanagement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kiran on 8/5/2017.
 */
public class InPostEval
{
    private Stack theStack;
    private String input;
    private String output = "";
    List<String> al=new ArrayList<>();
    StringBuilder sb=new StringBuilder();
    public InPostEval(String in)
    {

        input = in;
        int stackSize = input.length();
        theStack = new Stack(stackSize);

    }
    public String doTrans()
    {
        boolean check=false;
        for (int j = 0; j<input.length(); j++)
        {
            char ch = input.charAt(j);
            switch (ch)
            {
                case '+':
                case '-':check=true;
                    gotOper(ch, 1);
                    break;
                case '*':
                case '/':check=true;
                    gotOper(ch, 2);
                    break;
                case '(': check=true;
                    theStack.push(ch);
                    break;
                case ')': check=true;
                    gotParen(ch);
                    break;
                default:
                    output = output + ch;
                    break;
            }
            if(check==false)
            {
                sb.append(ch);
            }
            else
            {
                if(!sb.toString().trim().equals(""))
                    al.add(sb.toString());
                check=false;
                sb=new StringBuilder();
            }
        }
        if(!sb.toString().trim().equals(""))
            al.add(sb.toString());
        while (!theStack.isEmpty())
        {
            output = output + theStack.pop();
        }
        return output;
    }
    public void gotOper(char opThis, int prec1)
    {
        while (!theStack.isEmpty())
        {
            char opTop = theStack.pop();
            if (opTop == '(')
            {
                theStack.push(opTop);
                break;
            }
            else
            {
                int prec2;
                if (opTop == '+' || opTop == '-')
                    prec2 = 1;
                else
                    prec2 = 2;
                if (prec2<prec1)
                {
                    theStack.push(opTop);
                    break;
                }
                else
                    output = output + opTop;
            }
        }
        theStack.push(opThis);
    }
    public void gotParen(char ch)
    {
        while (!theStack.isEmpty())
        {
            char chx = (char)theStack.pop();
            if (chx == '(')
                break;
            else
                output = output + chx;
        }
    }
    public double postfixEvaluate(String e)
    {
        double number1;
        double number2;
        double result=0;
        int max=e.length();
        Stack1 s=new Stack1(max);
        String[] tokens=e.split("");
        StringBuilder sb1=new StringBuilder();
        double token=0;
        for(int j = 0; j < tokens.length; j++)
        {
            if (!"+".equals(tokens[j])&& !"-".equals(tokens[j])&&!"*".equals(tokens[j])&&!"/".equals(tokens[j]))
            {
                sb1.append(tokens[j]);
                if(al.contains(sb1.toString())) {
                    int pos = al.indexOf(sb1.toString());
                    if (pos == 0) {
                        al.remove(sb1.toString());
                        try {
                            token = Double.parseDouble(sb1.toString());
                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }
                        s.push(token);
                        sb1 = new StringBuilder();
                    }
                }
            }
            else
            {
                String op = tokens[j];
                number1 = s.pop();
                number2 = s.pop();
                if (op.equals("/")){
                    result = number2 / number1;}
                else if(op.equals("*")){
                    result = number1 * number2;}
                else if(op.equals("+")){
                    result = number1 + number2;}
                else if(op.equals("-")){
                    result = number2 - number1;}
                else throw new IllegalArgumentException("Wrong value");
                s.push(result);
            }
        }
        result=s.pop();
        return result;
    }
}
class Stack
{
    int max;
    char[] as;
    int top=-1;
    public Stack(int m)
    {
        max=m;
        as=new char[m];


    }
    public void push(char c)
    {
        as[++top]=c;
    }
    public char pop()
    {
        return as[top--];
    }
    public char peek()
    {
        return as[top];
    }
    public boolean isEmpty()
    {
        return (top==-1);
    }
}
class Stack1
{
    int max;
    double[] as;
    int top=-1;
    public Stack1(int m)
    {
        max=m;
        as=new double[m];
    }
    public void push(double c)
    {
        as[++top]=c;
    }
    public double pop()
    {
        return as[top--];
    }
    public double peek()
    {
        return as[top];
    }
    public boolean isEmpty()
    {
        return (top==-1);
    }
}
