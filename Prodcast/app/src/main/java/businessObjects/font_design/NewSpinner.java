package businessObjects.font_design;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Spinner;

/**
 * Created by nandhini on 09/09/17.
 */

public class NewSpinner extends android.support.v7.widget.AppCompatSpinner {
    public NewSpinner(Context context) {
        super(context);
    }

    public NewSpinner(Context context, int mode) {
        super(context, mode);
    }

    public NewSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NewSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
        //Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/GothamBook.ttf");

    }




}
