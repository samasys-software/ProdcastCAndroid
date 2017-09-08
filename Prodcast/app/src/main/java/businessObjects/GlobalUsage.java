package businessObjects;

import java.text.NumberFormat;

/**
 * Created by nandhini on 07/09/17.
 */

public class GlobalUsage {


  private static NumberFormat numberFormat=NumberFormat.getNumberInstance();

    public static NumberFormat getNumberFormat() {

            numberFormat.setMaximumFractionDigits(2);
            numberFormat.setMinimumFractionDigits(2);

        return numberFormat;
    }
}
