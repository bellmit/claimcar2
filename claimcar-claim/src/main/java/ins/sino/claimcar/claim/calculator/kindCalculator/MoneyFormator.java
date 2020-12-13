package ins.sino.claimcar.claim.calculator.kindCalculator;

import java.math.BigDecimal;
import java.text.DecimalFormat;


public class MoneyFormator {
    
    public final static int FORMAT = BigDecimal.ROUND_HALF_UP;
    
    public final static int MONEY_DECIMAL_PLACES = 2;
    
    public final static String MONEY_FORMAT_STRING = "#,##0.00";
    
    public final static int RATE_DECIMAL_PLACES = 3;
    
    public final static String RATE_FORMAT_STRING = "#,##0.00";
    
    /**
     * 格式化数字到小数位后两位小数，以四舍五入方式阶段。金额专用。
     * @param money
     * @return
     */
    public static BigDecimal format(BigDecimal money){
        return money.setScale(MoneyFormator.MONEY_DECIMAL_PLACES , MoneyFormator.FORMAT);
    }
    
    /**
     * 将数字截位后格式化成字符串。金额专用。
     * @param pay
     * @return
     */
    public static String format4Output(double pay) {
        double number = pay;
        if (!Double.isNaN(number)) {
            number = MoneyFormator.format(new BigDecimal(pay)).doubleValue();
        } 
        return new DecimalFormat(MoneyFormator.MONEY_FORMAT_STRING).format(number);
    }
    
    /**
     * 格式化数字到小数位后两位小数，以四舍五入方式阶段。比例专用。
     * @param money
     * @return
     */
    public static BigDecimal format4Rate(BigDecimal money){
        return money.setScale(MoneyFormator.RATE_DECIMAL_PLACES , MoneyFormator.FORMAT);
    }
    
    /**
     * 将数字截位后格式化成字符串。比例专用。
     * @param pay
     * @return
     */
    public static String format4RateOutput(double pay) {
        double number = pay;
        if (!Double.isNaN(number)) {
            number = MoneyFormator.format4Rate(new BigDecimal(pay)).doubleValue();
        } 
        return new DecimalFormat(MoneyFormator.RATE_FORMAT_STRING).format(number);
    }
}
