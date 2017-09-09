package businessObjects.font_design;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by nandhini on 09/09/17.
 */

public class NewEditText extends android.support.v7.widget.AppCompatEditText {
    public NewEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public NewEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NewEditText(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/GothamBook.ttf");
            setTypeface(tf);
        }
    }
}
