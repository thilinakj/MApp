package com.thilinas.mapp.custom;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.ProgressBar;

import com.thilinas.mapp.R;

/**
 * Created by Thilina on 21-Feb-17.
 */

public class WaveProgressDialog extends ProgressDialog {
    private ProgressBar progressBar;
    private Context context;
    public WaveProgressDialog(Context context) {
        super(context);
        this.context = context;
    }
    public WaveProgressDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    public WaveProgressDialog(Context context,int theme,String y) {
        super(context, theme);
        this.context = context;
    }

    public static WaveProgressDialog ctor(Context context) {
        WaveProgressDialog dialog = new WaveProgressDialog(context);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        return dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_progress);
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
