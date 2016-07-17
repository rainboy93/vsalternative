package vn.com.vshome.view;

import vn.com.vshome.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class ProgressHUD extends Dialog {
    private static ProgressHUD progressHUD;

    public static void showLoading(final Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideLoading(activity);
                progressHUD = show(activity);
            }
        });
    }

    public static void hideLoading(Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressHUD != null) {
                    progressHUD.dismiss();
                    progressHUD = null;
                }
            }
        });
    }

    public ProgressHUD(Context context) {
        super(context);
    }

    public ProgressHUD(Context context, int theme) {
        super(context, theme);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        ImageView imageView = (ImageView) findViewById(R.id.spinnerImageView);
        AnimationDrawable spinner = (AnimationDrawable) imageView
                .getBackground();
        spinner.start();
    }

    public void setMessage(CharSequence message) {
        if (message != null && message.length() > 0) {
            findViewById(R.id.message).setVisibility(View.VISIBLE);
            TextView txt = (TextView) findViewById(R.id.message);
            txt.setText(message);
            txt.invalidate();
        }
    }

    private static ProgressHUD show(Context context) {
        ProgressHUD dialog = new ProgressHUD(context, R.style.ProgressHUD);
        dialog.setTitle("");
        dialog.setContentView(R.layout.progress_hud);
        dialog.findViewById(R.id.message).setVisibility(View.GONE);
        dialog.setCancelable(false);
//		dialog.setOnCancelListener(new OnCancelListener() {
//			@Override
//			public void onCancel(DialogInterface dialog) {
//
//			}
//		});
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.0f;
        dialog.getWindow().setAttributes(lp);
        // dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.show();
        return dialog;
    }
}
