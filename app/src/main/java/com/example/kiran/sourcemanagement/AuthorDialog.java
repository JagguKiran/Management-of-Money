package com.example.kiran.sourcemanagement;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Kiran on 7/31/2017.
 */
public class AuthorDialog extends DialogFragment
{
    LayoutInflater inflater;
    View v;
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflater=getActivity().getLayoutInflater();
        v=inflater.inflate(R.layout.author_dialog, null);
        TextView textView=(TextView)v.findViewById(R.id.text1);
        TextView textView1=(TextView)v.findViewById(R.id.text2);
        textView.setText("If you notice an error in translation,an incompatibility with your device,or any problem in the application,please notiy this gmail:");
        textView1.setPaintFlags(textView1.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),FeedbackAct.class);
                startActivity(intent);
            }
        });
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("About Author").setView(v).setPositiveButton("ACCEPT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog=builder.create();
        return dialog;
    }
}
