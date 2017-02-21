package com.thilinas.mapp.custom;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.thilinas.mapp.R;

/**
 * Created by Thilina on 21-Feb-17.
 */

public class WaveProgressDialog extends ProgressDialog {
    private static WaveProgressDialog Instance;
    private ProgressBar progressBar;
    private Context context;
    private TextView msg;
    static String messageBody ="";
    public WaveProgressDialog(Context context) {
        super(context);
        this.context = context;
    }
    public WaveProgressDialog(Context context,String message) {
        super(context);
        this.context = context;
        messageBody = message;
    }
    public WaveProgressDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    public WaveProgressDialog(Context context,int theme,String y) {
        super(context, theme);
        this.context = context;
    }

    public synchronized static WaveProgressDialog getInstance(Context context,String message) {
        if ( Instance == null ){
            Instance = new WaveProgressDialog(context);
            Instance.setIndeterminate(true);
            Instance.setCancelable(false);
            messageBody = message;
            Instance.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            Instance.show();
        }else{
            messageBody = message;
            Instance.show();
        }
        return Instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_progress);
        msg = (CustomTextView) findViewById(R.id.cstText);
        if(messageBody.length()>0){
            msg.setText(messageBody);
            msg.setVisibility(View.VISIBLE);
        }else{
            msg.setVisibility(View.GONE);
        }

      /*  progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Wave anim = new Wave();
        anim.setColor(ContextCompat.getColor(context, R.color.lime_green_1));
        progressBar.setIndeterminateDrawable(anim);*/
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
