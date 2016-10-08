package vn.com.vshome.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
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
public class ErrorDialog extends DialogFragment {

    private TextView mTitle, mContent;
    private Button mCancel;

    private String title = "", content = "";

    public void setTitle(String title){
        this.title = title;
    }

    public void setContent(String content){
        this.content = content;
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
        View rootView = inflater.inflate(R.layout.view_dialog_error, container,
                false);

        initView(rootView);

        return rootView;
    }

    private void initView(View view){
        mTitle = (TextView) view.findViewById(R.id.dialog_error_title);
        mTitle.setText(title);

        mContent = (TextView) view.findViewById(R.id.dialog_error_content);
        mContent.setText(content);

        mCancel = (Button) view.findViewById(R.id.dialog_error_cancel_button);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ErrorDialog.this.dismiss();
            }
        });
    }
}
