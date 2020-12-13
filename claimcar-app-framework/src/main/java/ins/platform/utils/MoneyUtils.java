package ins.platform.utils;

import ins.platform.utils.DataUtils;

import java.text.DecimalFormat;

public class MoneyUtils
{
  protected static final String DOLLARS = "DOLLARS";
  protected static final String DOLLAR = "DOLLAR";
  protected static final String CENTS = "CENTS";
  protected static final String CENT = "CENT";
  protected static final String BILLION = "BILLION";
  protected static final String MILLION = "MILLION";
  protected static final String THOUSAND = "THOUSAND";
  protected static final String HUNDRED = "HUNDRED";
  protected static final String ZERO = "ZERO";
  protected static final String ONLY = "ONLY";
  protected static final int ONE_BILLION = 1000000000;
  protected static final int ONE_MILLION = 1000000;
  protected static final int ONE_THOUSAND = 1000;
  
  public static String toChinese(int iFee, String iCurrency)
  {
    return toChinese(iFee, iCurrency);
  }
  
  public static String toChinese(long iFee, String iCurrency)
  {
    return toChinese(iFee, iCurrency);
  }
  
  public static String toChinese(double iFee, String iCurrency)
  {
    String strChineseMoney = "";
    String strNumber = "              ";
    String strFee = "";
    String strThat = "";
    int intLength = 0;
    int i = 0;int j = 0;
    if ((iCurrency == null) || (iCurrency.length() == 0)) {
      iCurrency = "CNY";
    }
    if (iFee < 0.0D) {
      throw new IllegalArgumentException("金额不能为负");
    }
    if (iFee == 0.0D) {
      return strChineseMoney;
    }
    strFee = new DecimalFormat("0").format(iFee * 100.0D);
    intLength = strFee.length();
    if (intLength > 14) {
      throw new IllegalArgumentException("金额超出范围");
    }
    strNumber = strNumber.substring(0, 14 - intLength) + strFee;
    for (i = 14 - intLength; i < 14; i++)
    {
      j = new Integer(strNumber.substring(i, i + 1)).intValue();
      if (j > 0)
      {
        strChineseMoney = strChineseMoney.trim() + strThat.trim() + getUpperChineseDigit(j).trim() + getUpperChineseUnit(i, iCurrency);
        
        strThat = "";
      }
      else if (strChineseMoney.length() != 0)
      {
        if (i == 11) {
          strChineseMoney = strChineseMoney + getUpperChineseUnit(11, iCurrency);
        } else if ((i == 7) && (!strNumber.substring(4, 8).equals("0000"))) {
          strChineseMoney = strChineseMoney + "万";
        } else if ((i == 3) && (!strNumber.substring(0, 4).equals("0000"))) {
          strChineseMoney = strChineseMoney + "亿";
        }
        if ((i < 11) || (i == 12)) {
          strThat = getUpperChineseDigit(0);
        }
      }
    }
    if (strChineseMoney.endsWith("拾")) {
      strChineseMoney = strChineseMoney + "分";
    }
    if (strChineseMoney.endsWith("圆")) {
      strChineseMoney = strChineseMoney + "整";
    }
    if (strChineseMoney.endsWith("角")) {
      strChineseMoney = strChineseMoney + "整";
    }
    return strChineseMoney;
  }
  
  public static String getUpperChineseDigit(int iDigit)
  {
    String strUpperChineseDigit = "";
    String strUpperChineseChar = "零壹贰叁肆伍陆柒捌玖";
    if (iDigit > 9) {
      throw new IllegalArgumentException("金额超出范围");
    }
    if (iDigit < 0) {
      throw new IllegalArgumentException("金额不能为负");
    }
    strUpperChineseDigit = strUpperChineseChar.substring(iDigit, iDigit + 1);
    return strUpperChineseDigit;
  }
  
  private static String getUpperChineseUnit(int iPoint, String iCurrency)
  {
    String strUpperChineseUnit = "";
    String strUpperChineseUnitChar = "";
    if (iPoint > 13) {
      throw new IllegalArgumentException("金额超出范围");
    }
    if (iPoint < 0) {
      throw new IllegalArgumentException("金额不能为负");
    }
    iCurrency = iCurrency.trim();
    if (iCurrency.equals("CNY")) {
      strUpperChineseUnitChar = "仟佰拾亿仟佰拾万仟佰拾圆角分";
    } else if (iCurrency.equals("HKD")) {
      strUpperChineseUnitChar = "仟佰拾亿仟佰拾万仟佰拾圆角分";
    } else if (iCurrency.equals("JPY")) {
      strUpperChineseUnitChar = "仟佰拾亿仟佰拾万仟佰拾圆角分";
    } else if (iCurrency.equals("GBP")) {
      strUpperChineseUnitChar = "仟佰拾亿仟佰拾万仟佰拾镑先令便士";
    } else {
      strUpperChineseUnitChar = "仟佰拾亿仟佰拾万仟佰拾圆拾分";
    }
    strUpperChineseUnit = strUpperChineseUnitChar.substring(iPoint, iPoint + 1);
    return strUpperChineseUnit;
  }
  
  public static String getUpperEnglishDigit(int iFee)
  {
    String[] strTemp = { "ZERO", "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN", "ELEVEN", "TWELVE", "THIRTEEN", "FOURTEEN", "FIFTEEN", "SIXTEEN", "SEVENTEEN", "EIGHTEEN", "NINETEEN", "TWENTY", "THIRTY", "FORTY", "FIFTY", "SIXTY", "SEVENTY", "EIGHTY", "NINETY" };
    if ((iFee > 27) || (iFee < 0)) {
      throw new IllegalArgumentException("Array index overflow!");
    }
    return strTemp[iFee];
  }
  
  private static double getInt(double iFee, int iLen)
  {
    String strFee = "";
    int index = 0;
    strFee = new DecimalFormat("0.00").format(iFee);
    index = strFee.indexOf(".");
    strFee = strFee.substring(0, index - iLen);
    return Double.parseDouble(strFee);
  }
  
  private static double getDecimal(double iFee, int iLen)
  {
    String strFee = "";
    int index = 0;
    strFee = new DecimalFormat("0.00").format(iFee);
    index = strFee.indexOf(".");
    strFee = strFee.substring(index - iLen);
    return Double.parseDouble(strFee);
  }
  
  private static String format_99(int iFee)
  {
    String strOutFee = "";
    int i = 0;
    int j = 0;
    if ((iFee > 0) && (iFee < 100))
    {
      if (iFee <= 20)
      {
        strOutFee = getUpperEnglishDigit(iFee);
      }
      else
      {
        j = new Double(iFee / 10).intValue();
        strOutFee = getUpperEnglishDigit(j + 18);
        i = iFee - 10 * j;
        if (i != 0) {
          strOutFee = strOutFee + " " + getUpperEnglishDigit(i);
        }
      }
    }
    else {
      strOutFee = "";
    }
    return strOutFee;
  }
  
  private static String format_999(double iFee)
  {
    String strOutFee = "";
    int i = 0;
    int j = 0;
    if ((iFee >= 0.0D) && (iFee < 1000.0D))
    {
      i = new Double(iFee / 100.0D).intValue();
      j = new Double(iFee).intValue() % 100;
      strOutFee = format_99(j);
      if (i != 0)
      {
        if ((strOutFee.length() > 0) && 
          (!strOutFee.substring(0, 1).equals(" "))) {
          strOutFee = " AND " + strOutFee;
        }
        strOutFee = getUpperEnglishDigit(i) + " " + "HUNDRED" + strOutFee;
      }
    }
    else
    {
      strOutFee = "";
    }
    return strOutFee;
  }
  
  private static String formatToEnglish(double iFee)
  {
    String strOutFee = "";
    String strTmpFee = "";
    if (iFee < 0.0D)
    {
      strOutFee = formatToEnglish(-iFee);
      strOutFee = "MINUS " + strOutFee;
      return strOutFee;
    }
    if (iFee < 1.0D)
    {
      strOutFee = "ZERO";
      return strOutFee;
    }
    if (iFee >= 1000000000.0D)
    {
      strOutFee = formatToEnglish(getInt(iFee, 9));
      strTmpFee = formatToEnglish(getDecimal(iFee, 9));
      if (!strTmpFee.equals("ZERO")) {
        strTmpFee = " " + strTmpFee;
      }
      if (strTmpFee.indexOf("MILLION") > -1) {
        strOutFee = strOutFee + " " + "BILLION" + strTmpFee;
      } else {
        strOutFee = strOutFee + " " + "BILLION" + " AND " + strTmpFee;
      }
    }
    else if ((iFee >= 1000000.0D) && (iFee < 1000000000.0D))
    {
      strOutFee = format_999(getInt(iFee, 6));
      strTmpFee = formatToEnglish(getDecimal(iFee, 6));
      if (!strTmpFee.equals("ZERO")) {
        strTmpFee = " " + strTmpFee;
      }
      if (strTmpFee.indexOf("THOUSAND") > -1) {
        strOutFee = strOutFee + " " + "MILLION" + strTmpFee;
      } else {
        strOutFee = strOutFee + " " + "MILLION" + " AND " + strTmpFee;
      }
    }
    else if ((iFee >= 1000.0D) && (iFee < 1000000.0D))
    {
      strOutFee = format_999(getInt(iFee, 3));
      strTmpFee = formatToEnglish(getDecimal(iFee, 3));
      if (!strTmpFee.equals("ZERO")) {
        strTmpFee = " " + strTmpFee;
      }
      if (strTmpFee.indexOf("HUNDRED") > -1) {
        strOutFee = strOutFee + " " + "THOUSAND" + strTmpFee;
      } else {
        strOutFee = strOutFee + " " + "THOUSAND" + " AND " + strTmpFee;
      }
    }
    else if ((iFee >= 1.0D) && (iFee < 1000.0D))
    {
      strOutFee = format_999(iFee);
    }
    return strOutFee;
  }
  
  public static String toEnglish(double iFee)
  {
    String strOutFee = "";
    String strFee = "";
    String strCent = "";
    String strTmpFee = "";
    int i = 0;
    if (iFee < 0.0D) {
      throw new IllegalArgumentException("Money can not be negative!");
    }
    strFee = new DecimalFormat("0").format(iFee * 100.0D);
    i = strFee.length();
    if (i > 14) {
      throw new IllegalArgumentException("Money exceeds its range(fourteen digits)!");
    }
    if (iFee == 0.0D) {
      strOutFee = "ZERO";
    }
    strCent = "CENTS";
    
    strOutFee = formatToEnglish(getInt(iFee, 0));
    
    strTmpFee = formatToEnglish(DataUtils.round(getDecimal(iFee, 0) * 100.0D, 0));
    if (strTmpFee.equals(getUpperEnglishDigit(1))) {
      strCent = "CENT";
    }
    if (!strTmpFee.equals("ZERO")) {
      if (strOutFee.equals("ZERO")) {
        strOutFee = strTmpFee + " " + strCent;
      } else {
        strOutFee = strOutFee + " AND " + strCent + " " + strTmpFee;
      }
    }
    strOutFee = strOutFee + " ONLY";
    return strOutFee;
  }
  
  public static String toEnglish(int iFee)
  {
    return toEnglish(iFee);
  }
  
  public static String toEnglish(long iFee)
  {
    return toEnglish(iFee);
  }
  
  public static String toAccount(String strMoney)
  {
    String formatMoney = "";
    DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
    formatMoney = decimalFormat.format(DataUtils.round(Double.valueOf(strMoney).doubleValue(), 2));
    return formatMoney;
  }
  
//  public static void main(String[] args) {
//	String str = MoneyUtils.toChinese(322212.45d, "CNY");
//	System.out.println(str);
// }
}
