package businessObjects.font_design;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by nandhini on 09/09/17.
 */

public class NewTextView extends android.support.v7.widget.AppCompatTextView {
    public NewTextView(Context context) {
        super(context);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/GothamBook.ttf");
        this.setTypeface(tf);
    }

    public NewTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/GothamBook.ttf");
        this.setTypeface(tf);
    }

    public NewTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/GothamBook.ttf");
        this.setTypeface(tf);
    }


}
