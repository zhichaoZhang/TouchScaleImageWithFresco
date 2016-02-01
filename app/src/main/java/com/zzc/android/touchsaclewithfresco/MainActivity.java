package com.zzc.android.touchsaclewithfresco;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zzc.android.library.ScaleImagePopWindow;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.dv_small)
    SimpleDraweeView dvSmall;
    @InjectView(R.id.root)
    RelativeLayout rlRootView;

    private String picUrl = "http://img.taopic.com/uploads/allimg/130102/240404-13010223360642.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //fresco图片框架初始化
        Fresco.initialize(this.getApplicationContext());
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        dvSmall.setImageURI(Uri.parse(picUrl));
    }

    @OnClick(R.id.dv_small)
    void clickPic() {
        ScaleImagePopWindow scaleImagePopWindow = new ScaleImagePopWindow(this, picUrl);
        scaleImagePopWindow.showAtLocation(rlRootView, Gravity.CENTER, 0, 0);
    }
}
