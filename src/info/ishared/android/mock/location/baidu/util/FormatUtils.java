package info.ishared.android.mock.location.baidu.util;

import java.text.DecimalFormat;

/**
 * Created with IntelliJ IDEA.
 * User: Seven
 * Date: 13-4-10
 * Time: PM1:40
 */
public class FormatUtils {
    private static final DecimalFormat decimalFormatter=new DecimalFormat("#.00");


    public static String formatLatLngNumber(double number){
        return decimalFormatter.format(number);
    }
}
