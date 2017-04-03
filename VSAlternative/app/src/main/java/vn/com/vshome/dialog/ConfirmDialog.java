package vn.com.vshome.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import in.workarounds.typography.Button;
import in.workarounds.typography.TextView;
import vn.com.vshome.R;
import vn.com.vshome.callback.DialogCallback;

/**
 * Created by anlab on 10/6/16.
 */
public class ConfirmDialog extends DialogFragment {

    private TextView mTitle, mContent;
    private Button mConfirm, mCancel;

    private String title = "", content = "";

    public void setTitle(String title){
        this.title = title;
    }

    public void setContent(String content){
        this.content = content;
    }

    private DialogCallback callback;

    public void setCallback(DialogCallback callback){
        this.callback = callback;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_dialog_confirm, container,
                false);

        initView(rootView);

        return rootView;
    }

    private void initView(View view){
        mTitle = (TextView) view.findViewById(R.id.dialog_confirm_title);
        mTitle.setText(title);

        mContent = (TextView) view.findViewById(R.id.dialog_confirm_content);
        mContent.setText(Html.fromHtml(content.toString()));

        mCancel = (Button) view.findViewById(R.id.dialog_confirm_cancel_button);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog.this.dismiss();
            }
        });
        mConfirm = (Button) view.findViewById(R.id.dialog_confirm_confirm_button);
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onConfirm();
                }
                ConfirmDialog.this.dismiss();
            }
        });
    }
}
