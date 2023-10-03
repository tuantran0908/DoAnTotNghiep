package llq.fw.cm.common;

import java.util.Calendar;
import java.util.Date;

public class AddDate {

    public static Date addTime(Date fromDate, Integer times, String type) {
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(fromDate);
        // int numDays = c.getActualMaximum(Calendar.DATE);
        switch (type) {
            case Constants.Prequency.NGAY:
                c.add(Calendar.DATE, times);
                dt = c.getTime();
                break;
            case Constants.Prequency.TUAN:
                c.add(Calendar.DATE, times * 7);
                dt = c.getTime();
                break;
            case Constants.Prequency.THANG:
                c.add(Calendar.DATE, times * 31);
                dt = c.getTime();
                break;
            case Constants.Prequency.QUY:
                c.add(Calendar.DATE, times * 93);
                dt = c.getTime();
                break;
            default:
                c.add(Calendar.DATE, times * 356);
                dt = c.getTime();
                break;
        }

        return dt;

    }
}
