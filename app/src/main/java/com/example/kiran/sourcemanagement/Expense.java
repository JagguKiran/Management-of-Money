package com.example.kiran.sourcemanagement;

import java.util.Date;

/**
 * Created by Kiran on 8/12/2017.
 */
public class Expense{
    private long eid;
    private String eName;
    private double amt;
    private String eDate;
    private long iid;

    public long getEid() {
        return eid;
    }

    public void setEid(long eid) {
        this.eid = eid;
    }

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        if(eName==null||eName.trim().equals(""))
            throw new IllegalArgumentException("Invalid Expense Name");
        this.eName = eName;
    }

    public double getAmt() {
        return amt;
    }

    public void setAmt(double amt) {
        if(amt<0)
            throw new IllegalArgumentException("Amount should not be negative");
        String amount=String.valueOf(amt);
        if(amount.substring(amount.indexOf('.')).length()>5) {
            amount = amount.substring(0, amount.indexOf('.') + 5);
        }
            try {
                amt = Double.parseDouble(amount);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.amt = amt;
    }

    public String geteDate() {
        return eDate;
    }

    public void seteDate(String eDate) {
        String dt=checkDate(eDate);
        this.eDate = dt;
    }

    public long getIid() {
        return iid;
    }

    public void setIid(long iid) {
        this.iid = iid;
    }

    public Expense(long eid, String eName, double amt, String eDate, long iid) {
        this.eid = eid;
        if(eName==null||eName.trim().equals(""))
            throw new IllegalArgumentException("Invalid Expense Name");
        this.eName = eName;
        if(amt<0)
            throw new IllegalArgumentException("Amount should not be negative");
        String amount=String.valueOf(amt);
        if(amount.substring(amount.indexOf('.')).length()>5) {
            amount = amount.substring(0, amount.indexOf('.') + 5);
        }
        try {
            amt = Double.parseDouble(amount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.amt = amt;
        String dt=checkDate(eDate);
        this.eDate = dt;
        this.iid = iid;
    }

    public Expense(String eName, double amt, String eDate, long iid) {
        if(eName==null||eName.trim().equals(""))
            throw new IllegalArgumentException("Invalid Expense Name");
        this.eName = eName;
        if(amt<0)
            throw new IllegalArgumentException("Amount should not be negative");
        String amount=String.valueOf(amt);
        if(amount.substring(amount.indexOf('.')).length()>5) {
            amount = amount.substring(0, amount.indexOf('.') + 5);
        }
        try {
            amt = Double.parseDouble(amount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.amt = amt;
        String dt=checkDate(eDate);
        this.eDate = dt;
        this.iid = iid;
    }
    public Expense()
    {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Expense expense = (Expense) o;

        if (eid != expense.eid) return false;
        if (Double.compare(expense.amt, amt) != 0) return false;
        if (iid != expense.iid) return false;
        if (eName != null ? !eName.equals(expense.eName) : expense.eName != null) return false;
        return !(eDate != null ? !eDate.equals(expense.eDate) : expense.eDate != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (eid ^ (eid >>> 32));
        result = 31 * result + (eName != null ? eName.hashCode() : 0);
        temp = Double.doubleToLongBits(amt);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (eDate != null ? eDate.hashCode() : 0);
        result = 31 * result + (int) (iid ^ (iid >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "eid=" + eid +
                ", eName='" + eName + '\'' +
                ", amt=" + amt +
                ", eDate='" + eDate + '\'' +
                ", iid=" + iid +
                '}';
    }

    public String checkDate(String date)
    {
        StringBuilder sb=new StringBuilder();
        if(date==null||date.trim().equals(""))
        {
            String str=new Date().toString();
            String[] as=str.split(" ");
            sb.append(as[5]);
            sb.append("-");
            sb.append(performMoth(as[1]));
            sb.append("-");
            sb.append(performDay(as[2]));
            sb.append(" ");
            sb.append(as[3]);
            return sb.toString();
        }
        else if(date.split(" ").length!=2)
        {
            String[] as=date.split(" ");
            sb.append(as[2]);

            sb.append("-");
            sb.append(performMoth(as[0]));

            sb.append("-");
            sb.append(performDay(as[1]));

            sb.append(" ");
            sb.append(as[3]);

            return sb.toString();
        }
        else
            return date;
    }
    public String performMoth(String str)
    {
        if(str.trim().equals("Jan"))
            return "01";
        else if(str.trim().equals("Feb"))
            return "02";
        else if(str.trim().equals("Mar"))
            return "03";
        else if(str.trim().equals("Apr"))
            return "04";
        else if(str.trim().equals("May"))
            return "05";
        else if(str.trim().equals("Jun"))
            return "06";
        else if(str.trim().equals("Jul"))
            return "07";
        else if(str.trim().equals("Aug"))
            return "08";
        else if(str.trim().equals("Sep"))
            return "09";
        else if(str.trim().equals("Oct"))
            return "10";
        else if(str.trim().equals("Nov"))
            return "11";
        else if(str.trim().equals("Dec"))
            return "12";
        else
            return "00";
    }
    public String performDay(String s)
    {
        String[] s1=s.split(",");
        String s2=s1[0];
        if(s2.trim().equals("1"))
            return "01";
        if(s2.trim().equals("2"))
            return "02";
        if(s2.trim().equals("3"))
            return "03";
        if(s2.trim().equals("4"))
            return "04";
        if(s2.trim().equals("5"))
            return "05";
        if(s2.trim().equals("6"))
            return "06";
        if(s2.trim().equals("7"))
            return "07";
        if(s2.trim().equals("8"))
            return "08";
        if (s2.trim().equals("9"))
            return "09";
        else
            return s2;
    }
}
