package vn.com.vshome.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;

import in.workarounds.typography.Button;
import in.workarounds.typography.TextView;
import vn.com.vshome.R;
import vn.com.vshome.callback.DialogCallback;

/**
 * Created by anlab on 10/6/16.
 */
public class DeleteDialog extends DialogFragment {

    private TextView mContent;
    private Button mConfirm;
    private ImageButton mCancel;
    private ImageView mIcon;

    private int iconId;

    private CharSequence content = "";

    public void setContent(CharSequence content) {
        this.content = content;
    }

    private DialogCallback callback;

    public void setCallback(DialogCallback callback) {
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
        View rootView = inflater.inflate(R.layout.view_dialog_delete_scene, container,
                false);

        initView(rootView);

        return rootView;
    }

    private void initView(View view) {
        mContent = (TextView) view.findViewById(R.id.dialog_delete_scene_text);
        mContent.setText(Html.fromHtml(content.toString()));

        mIcon = (ImageView) view.findViewById(R.id.dialog_delete_scene_icon);
        if (iconId != 0) {
            mIcon.setImageResource(iconId);
        }

        mCancel = (ImageButton) view.findViewById(R.id.dialog_delete_scene_button_cancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteDialog.this.dismiss();
            }
        });
        mConfirm = (Button) view.findViewById(R.id.dialog_delete_scene_button_confirm);
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteDialog.this.dismiss();
                if (callback != null) {
                    callback.onConfirm();
                }
            }
        });
    }

    public void setIcon(int id) {
        iconId = id;
    }
}
