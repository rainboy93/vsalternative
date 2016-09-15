package vn.com.vshome.view;

import android.content.Context;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;

import in.workarounds.typography.TextView;

/**
 * Created by anlab on 9/14/16.
 */
public class AutoFitTextView extends TextView {
    public AutoFitTextView(Context context) {
        super(context);
        setSingleLine();
        setEllipsize(TextUtils.TruncateAt.END);
    }

    public AutoFitTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSingleLine();
        setEllipsize(TextUtils.TruncateAt.END);
    }

    public AutoFitTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setSingleLine();
        setEllipsize(TextUtils.TruncateAt.END);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final Layout layout = getLayout();
        if (layout != null) {
            final int lineCount = layout.getLineCount();
            if (lineCount > 0) {
                final int ellipsisCount = layout.getEllipsisCount(lineCount - 1);
                if (ellipsisCount > 0) {
                    final float textSize = getTextSize();
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, (textSize - 1));
                    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                }
            }
        }
    }
}
