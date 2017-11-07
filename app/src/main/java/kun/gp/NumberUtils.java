package kun.gp;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by kun on 2017-08-29.
 */

public class NumberUtils {
    public static String getMoneyType(String string) {
        // 把string类型的货币转换为double类型。
        Double numDouble = Double.parseDouble(string);
        // 想要转换成指定国家的货币格式
        NumberFormat format = NumberFormat.getNumberInstance();
//        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.CHINA);
        // 把转换后的货币String类型返回
        String numString = format.format(numDouble);
        return numString;
    }
}
