package vn.com.vshome.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;

import in.workarounds.typography.Button;
import in.workarounds.typography.TextView;
import vn.com.vshome.R;

/**
 * Created by anlab on 7/5/16.
 */
public class Utils {

    public static void showErrorDialog(String title, String message,
                                       Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_error);

        TextView mTitle = (TextView) dialog.findViewById(R.id.dialog_error_title);
        mTitle.setText(title);
        TextView mContent = (TextView) dialog.findViewById(R.id.dialog_error_content);
        mContent.setText(message);
        Button mCancel = (Button) dialog.findViewById(R.id.dialog_error_cancel_button);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public static void showErrorDialog(int title, int message,
                                       Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_error);

        TextView mTitle = (TextView) dialog.findViewById(R.id.dialog_error_title);
        mTitle.setText(context.getResources().getString(title));
        TextView mContent = (TextView) dialog.findViewById(R.id.dialog_error_content);
        mContent.setText(context.getResources().getString(message));
        Button mCancel = (Button) dialog.findViewById(R.id.dialog_error_cancel_button);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public final static int Byte2Unsigned(byte a) {
        return a & 0xFF;
    }

    public final static int Int2Unsigned(int a) {
        return a & 0xFF;
    }
}
