package com.aissit.otpdemo.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aissit.otpdemo.R;


public class UpdateDialog extends Dialog implements DialogInterface.OnDismissListener {
    private Window mWindow;
    private TextView mDialogContent;
    private OnItemClickListener onItemClickListener;

    public UpdateDialog(@NonNull Context context) {
        this(context, 0);
    }

    public UpdateDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    protected UpdateDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView(context);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }

    private void initView(Context context) {
        mWindow = getWindow();
        setBackgroundIsTransparent(false);
        setContentView(View.inflate(context, R.layout.update_progress_dialog, null));
        setCancelable(false);
        setOnDismissListener(this);
        View btn_cancel = findViewById(R.id.dialog_btn_close);
        mDialogContent = findViewById(R.id.dialog_content);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onCancelUpdate();
                }
            }
        });
    }

    public void setContent(String content) {
        mDialogContent.setText(content);
    }

    public void showAndDismissDelay(String prompt, boolean hideCancel, OnItemClickListener listener) {
        onItemClickListener = listener;
        setContent(prompt);
        if (hideCancel) {
            findViewById(R.id.dialog_btn_close).setVisibility(View.GONE);
        }
        show();
        startTimer();
    }

    private final Handler mH = new Handler(Looper.getMainLooper());
    //    private int mDelaySecond = 3;
    private final Runnable mDismissRunnable = new Runnable() {
        @Override
        public void run() {
//            mDelaySecond--;
//            if (mDelaySecond >= 0) {
//                startTimer();
//            } else {
            if (onItemClickListener != null) {
                onItemClickListener.onConfirmUpdate();
            }
            dismiss();
//            }
        }
    };

    private void startTimer() {
//        setContent(String.format(getContext().getString(R.string.delay_start_whatsapp_desc), mDelaySecond));
        mH.postDelayed(mDismissRunnable, 3000);
    }

    /**
     * 设置背景框颜色是否透明，透明为完全透明，不透明为半透明
     *
     * @param isTransparent 是否透明
     */
    public void setBackgroundIsTransparent(boolean isTransparent) {
        if (isTransparent) {
            mWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mWindow.setDimAmount(0);
        } else {
            mWindow.setBackgroundDrawable(new ColorDrawable());
        }
    }

    //dialog显示时，状态栏显示是因为获取了焦点，在显示在前清除焦点，可避免在状态栏隐藏情况下显示dialog带出状态栏的问题
    private void clearSystemUiVisibility() {
        mWindow.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    private void fullScreenImmersive() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            mWindow.getDecorView().setSystemUiVisibility(uiOptions);
        }
        mWindow.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
//        mDelaySecond = 0;
        if (mH != null) {
            mH.removeCallbacks(mDismissRunnable);
        }
//        if (onItemClickListener != null) {
//            onItemClickListener.onCancelUpdate();
//        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public interface OnItemClickListener {
        void onConfirmUpdate();

        void onCancelUpdate();

    }
}
