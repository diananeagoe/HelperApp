package com.example.apphelper.ui.home;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.apphelper.R;

import sun.bob.mcalendarview.views.BaseCellView;
import sun.bob.mcalendarview.vo.DayData;

public class DateCellView extends BaseCellView {

    public DateCellView(Context context) {
        super(context);
    }

    public DateCellView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setDisplayText(DayData day) {
        String text = day.getText();
        ((TextView) this.findViewById(R.id.id_cell_text)).setText(text);
    }
    /*
    @Override
    public void setDisplayText(String text) {
        ((TextView) this.findViewById(R.id.id_cell_text)).setText(text);
    }

     */
}
