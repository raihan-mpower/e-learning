package mpower.org.elearning_module.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.LinearLayout;

/**
 * Created by raihan on 8/22/17.
 */

public class squareGrid extends LinearLayout {


    public squareGrid(Context context) {
        super(context);
    }

    public squareGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public squareGrid(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public squareGrid(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
