package com.zzc.android.library;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;

import butterknife.ButterKnife;


/**
 * Created by zczhang on 15/12/18.
 */
public class ScaleImagePopWindow extends PopupWindow implements MyZoomableController.OnClickListener {
    private static final int SCALE_STATUS_NORMAL = 0;//正常显示
    private static final int SCALE_STATUS_MATCH_WIDTH_OR_HEIGHT = 1;//横向或纵向充满屏幕
    private static final int SCALE_STATUS_ORIGIN_PIC = 2;//显示图片原尺寸
    ZoomableDraweeView dvBigImage;
    private String picUrl;//图片地址
    private Context context;
    private int screenWidth = 0;
    private int screenHeight = 0;
    private int currentScaleStatus = SCALE_STATUS_NORMAL;
    private int picWidth = 0;
    private int picHeight = 0;
    private float screenRatio = 0f;//屏幕高宽比

    private boolean isCrossMap = false;//是否是横图

    private float normaScale = 1.0f;
    private float matchScreenScale = 0f;
    private float recoveryOriginScale = 0f;

    private MyZoomableController myZoomableController;

    public ScaleImagePopWindow(Context context, String picUrl) {
        this.context = context;
        this.picUrl = picUrl;
        init();
    }

    public ScaleImagePopWindow(Context context, String picUrl, int picHeight, int picWidth) {
        this.context = context;
        this.picUrl = picUrl;
        this.picHeight = picHeight;
        this.picWidth = picWidth;
//        screenWidth = ScreenUtil.getScreenWidth(context);
//        screenHeight = ScreenUtil.getScreenHeight(context);
        init();
    }

    private void init() {
        //全屏
        ((Activity) context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View view = LayoutInflater.from(context).inflate(R.layout.view_scale_image_popwindow, null);
        dvBigImage = (ZoomableDraweeView)view.findViewById(R.id.dv_big_image);
        setContentView(view);
        ButterKnife.inject(this, view);
        if (TextUtils.isEmpty(picUrl)) {
            Toast.makeText(context, "Picture url can not be empty", Toast.LENGTH_SHORT).show();
        } else {

//            screenRatio = Float.parseFloat(MoneyUtil.divide(String.valueOf(screenHeight), String.valueOf(screenWidth)));//屏幕宽高比
//            String picVerticalRatio = MoneyUtil.divide(String.valueOf(picHeight), String.valueOf(picWidth));//图片高宽比
//            String picHorRatio = MoneyUtil.divide(String.valueOf(picWidth), String.valueOf(picHeight));//图片宽高比
//
//            //图片的高宽比大于屏幕的高宽比就按宽度算,否则按高度算
//            isCrossMap = Float.parseFloat(picVerticalRatio) < screenRatio;//判断是否是横图
//            if (Float.parseFloat(picVerticalRatio) < screenRatio) {//如果是横屏
//                recoveryOriginSacle = Float.parseFloat(MoneyUtil.divide(String.valueOf(picWidth), String.valueOf(screenWidth)));
//                matchScreenScale = Float.parseFloat(MoneyUtil.divide(String.valueOf(screenHeight), MoneyUtil.multiply(picVerticalRatio, String.valueOf(screenWidth))));
//            } else {
//                recoveryOriginSacle = Float.parseFloat(MoneyUtil.divide(String.valueOf(picHeight), String.valueOf(screenHeight)));
//                matchScreenScale = Float.parseFloat(MoneyUtil.divide(String.valueOf(screenWidth), MoneyUtil.multiply(picHorRatio, String.valueOf(screenHeight))));
//            }

            //设置下载控制器
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(Uri.parse(picUrl))
                    .setOldController(dvBigImage.getController())
                    .build();
            //设置显示控制器
            GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(context.getResources())
                    .setActualImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE)
                    .build();
            dvBigImage.setHierarchy(hierarchy);
            dvBigImage.setController(controller);
            //设置缩放控制器
            myZoomableController = new MyZoomableController(TransformGestureDetector.newInstance());
            myZoomableController.setClickListener(this);
            dvBigImage.setZoomableController(myZoomableController);
        }
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        dvBigImage.setFocusable(true);
        setFocusable(true);
        setOutsideTouchable(true);
        setTouchable(true);
        setBackgroundDrawable(context.getResources().getDrawable(android.R.color.black));

//        setAnimationStyle(R.style.popwin_scale_image_style);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.reset(this);
    }

    public void setPicWidth(int picWidth) {
        this.picWidth = picWidth;
    }

    public void setPicHeight(int picHeight) {
        this.picHeight = picHeight;
    }

    @Override
    public void onClick() {
        dismiss();
    }

    @Override
    public void onDoubleClick() {
//        if (currentScaleStatus == SCALE_STATUS_NORMAL) {
//            myZoomableController.zoom(matchScreenScale);
//            currentScaleStatus = SCALE_STATUS_MATCH_WIDTH_OR_HEIGHT;
//            return;
//        }
//
//        if (currentScaleStatus == SCALE_STATUS_MATCH_WIDTH_OR_HEIGHT) {
//            myZoomableController.zoom(recoveryOriginScale);
//            currentScaleStatus = SCALE_STATUS_ORIGIN_PIC;
//            return;
//        }
//
//        if (currentScaleStatus == SCALE_STATUS_ORIGIN_PIC) {
//            myZoomableController.zoom(normaScale);
//            currentScaleStatus = SCALE_STATUS_NORMAL;
//            return;
//        }
    }
}
