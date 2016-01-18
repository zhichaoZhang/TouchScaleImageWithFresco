package com.zzc.android.touchsaclewithfresco;

import android.graphics.PointF;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;

/**
 * 支持双击缩放的控制器
 *
 * Created by zczhang on 15/12/19.
 */
public class MyZoomableController extends DefaultZoomableController {
    private static final String LOG_TAG = "MyZoomableController";
    private OnClickListener clickListener;
    private static final int MAX_INTERVAL_FOR_CLICK = 250;
    private static final int MAX_DISTANCE_FOR_CLICK = 100;
    private static final int MAX_DOUBLE_CLICK_INTERVAL = 400;
    int mDownX = 0;
    int mDownY = 0;
    int mTempX = 0;
    int mTempY = 0;
    boolean mIsWaitUpEvent = false;
    boolean mIsWaitDoubleClick = false;

    private boolean isCurrentBig = false;//当前是否处于放大状态.用户处理双击缩放


    public MyZoomableController(TransformGestureDetector gestureDetector) {
        super(gestureDetector);
        setRotationEnabled(false);
        setMinScaleFactor(0.5f);
        setMaxScaleFactor(5.0f);
    }

    /**
     * 处理双击缩放
     */
    private void doubleScale() {
        if (isCurrentBig) {
            System.out.println("-------缩小-----");
            System.out.println("-------mDownX-----"+mDownX);
            System.out.println("-------mDownY-----"+mDownY);
            isCurrentBig = false;
            zoomToImagePoint(1.0f, new PointF(mDownX, mDownY));
        } else {
            System.out.println("-------放大----");
            isCurrentBig = true;
            zoomToImagePoint(2.0f, new PointF(mDownX, mDownY));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!mIsWaitUpEvent && event.getAction() != MotionEvent.ACTION_DOWN) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) event.getX();
                mDownY = (int) event.getY();
                mIsWaitUpEvent = true;
                handler.postDelayed(mTimerForUpEvent, MAX_INTERVAL_FOR_CLICK);
                break;
            case MotionEvent.ACTION_MOVE:
                mTempX = (int) event.getX();
                mTempY = (int) event.getY();
                if (Math.abs(mTempX - mDownX) > MAX_DISTANCE_FOR_CLICK
                        || Math.abs(mTempY - mDownY) > MAX_DISTANCE_FOR_CLICK) {
                    mIsWaitUpEvent = false;
                    handler.removeCallbacks(mTimerForUpEvent);
                    Log.d(LOG_TAG, "The move distance too far:cancel the click");
                }
                break;
            case MotionEvent.ACTION_UP:
                mTempX = (int) event.getX();
                mTempY = (int) event.getY();
                if (Math.abs(mTempX - mDownX) > MAX_DISTANCE_FOR_CLICK
                        || Math.abs(mTempY - mDownY) > MAX_DISTANCE_FOR_CLICK) {
                    mIsWaitUpEvent = false;
                    handler.removeCallbacks(mTimerForUpEvent);
                    Log.d(LOG_TAG,
                            "The touch down and up distance too far:cancel the click");
                    break;
                } else {
                    mIsWaitUpEvent = false;
                    handler.removeCallbacks(mTimerForUpEvent);
                    onSingleClick();
                    return super.onTouchEvent(event);
                }
            case MotionEvent.ACTION_CANCEL:
                mIsWaitUpEvent = false;
                handler.removeCallbacks(mTimerForUpEvent);
                Log.d(LOG_TAG, "The touch cancel state:cancel the click");
                break;
            default:
                Log.d(LOG_TAG, "irrelevant MotionEvent state:" + event.getAction());
        }

        return super.onTouchEvent(event);

    }


    public void zoom(float scale) {
        zoomToImagePoint(scale, new PointF(mDownX, mDownY));
    }

    public void setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    /**
     * 自己判断是否是点击和双击
     */
    public interface OnClickListener {
        void onClick();

        void onDoubleClick();
    }

    Runnable mTimerForSecondClick = new Runnable() {
        @Override
        public void run() {
            if (mIsWaitDoubleClick) {
                Log.d(LOG_TAG,
                        "The mTimerForSecondClick has executed,so as a singleClick");
                mIsWaitDoubleClick = false;
                // at here can do something for singleClick!!
                if (clickListener != null) {
                    clickListener.onClick();
                }
            } else {
                Log.d(LOG_TAG,
                        "The mTimerForSecondClick has executed, the doubleclick has executed ,so do nothing");
//                if (clickListener != null) {
//                    clickListener.onDoubleClick();
//                }
//                doubleScale();
            }
        }
    };

    public void onSingleClick() {
        if (mIsWaitDoubleClick) {
            onDoubleClick();
            mIsWaitDoubleClick = false;
            handler.removeCallbacks(mTimerForSecondClick);
        } else {
            mIsWaitDoubleClick = true;
            handler.postDelayed(mTimerForSecondClick, MAX_DOUBLE_CLICK_INTERVAL);
        }
    }

    public void onDoubleClick() {
        Log.d(LOG_TAG, "we can do sth for double click here");
//        if (clickListener != null) {
//            clickListener.onDoubleClick();
//        }
        doubleScale();

    }

    Runnable mTimerForUpEvent = new Runnable() {
        public void run() {
            if (mIsWaitUpEvent) {
                Log.d(LOG_TAG,
                        "The mTimerForUpEvent has executed, so set the mIsWaitUpEvent as false");
                mIsWaitUpEvent = false;
            } else {
                Log.d(LOG_TAG,
                        "The mTimerForUpEvent has executed, mIsWaitUpEvent is false,so do nothing");
            }
        }
    };

    Handler handler = new Handler();
}
