package vn.com.vshome.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;

import in.workarounds.typography.Button;
import in.workarounds.typography.EditText;
import in.workarounds.typography.TextView;
import vn.com.vshome.R;
import vn.com.vshome.callback.DialogCallback;
import vn.com.vshome.callback.InputCallback;

/**
 * Created by anlab on 10/6/16.
 */
public class AddDialog extends DialogFragment {

    private Button mConfirm;
    private EditText mName;
    private ImageButton mCancel;

    private String title = "", content = "";

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private InputCallback callback;

    public void setCallback(InputCallback callback) {
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
        View rootView = inflater.inflate(R.layout.view_dialog_add_scene, container,
                false);

        initView(rootView);

        return rootView;
    }

    private void initView(View view) {
        mName = (EditText) view.findViewById(R.id.dialog_add_scene_name);

        mCancel = (ImageButton) view.findViewById(R.id.dialog_add_scene_button_cancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDialog.this.dismiss();
            }
        });
        mConfirm = (Button) view.findViewById(R.id.dialog_add_scene_button_confirm);
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onConfirm(mName.getText().toString());
                }
                AddDialog.this.dismiss();
            }
        });
    }
}
