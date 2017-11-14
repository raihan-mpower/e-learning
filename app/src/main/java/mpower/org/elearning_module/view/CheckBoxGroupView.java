package mpower.org.elearning_module.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sabbir on 11/14/17.
 */

public class CheckBoxGroupView extends GridLayout {

    List<CheckBox> checkBoxes=new ArrayList<>();

    public CheckBoxGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void put(CheckBox checkBox) {
        checkBoxes.add( checkBox);
        invalidate();
        requestLayout();
    }

    public void remove(Integer id) {
        // TODO: Remove items from ArrayList
    }

    public List<?> getCheckboxesChecked(){

        List<CheckBox> checkeds = new ArrayList<>();
        for (CheckBox c : checkBoxes){
            if(c.isChecked())
                checkeds.add(c);
        }

        return checkeds;
    }

    public List<Object> getCheckedIds(){

        List<Object> checkeds = new ArrayList<>();
        for (CheckBox c : checkBoxes){
            if(c.isChecked())
                checkeds.add(c.getTag());
        }
        return checkeds;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        for (CheckBox c:checkBoxes){
            addView(c);
        }
        invalidate();
        requestLayout();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }
}
