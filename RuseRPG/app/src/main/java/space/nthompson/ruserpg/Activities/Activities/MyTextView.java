package space.nthompson.ruserpg.Activities.Activities;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Nick on 11/30/15.
 */
public class MyTextView extends TextView {


    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "ARCADEPI.TTF"));
    }
}
