package com.sandeepani.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sandeepani.view.R;

/**
 * Created by MAST_HODC\vramz on 3/28/15.
 */
public class SwitchChildView extends RelativeLayout {
    public ImageView personIV;
    public TextView childNameTV;
    public Button switchChildBT;

    public SwitchChildView(Context context) {
        super(context);
    }

    public SwitchChildView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwitchChildView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void initSwitchChildBar() {
        inflateHeader();
    }

    private void inflateHeader() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.switch_child, this);

        personIV = (ImageView) findViewById(R.id.child_icon);
        childNameTV = (TextView) findViewById(R.id.child_name);
        switchChildBT = (Button) findViewById(R.id.switch_child);
    }
}
