package net.rlair.flight.support.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Kevin on 2017/8/26.
 */
public class Utils {

    /**
     * 根据给定的开始和结束日期以及星期几，返回符合条件的日期
     * @param startDate
     * @param endDate
     * @param daysOfWeek
     * @return
     */
    public static List<Date> getDateMachDayOfWeek(Date startDate, Date endDate, List<Integer> daysOfWeek){
        List<Date> result = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        while (cal.getTime().getTime() <= endDate.getTime()){
            int w = getDayOfWeek(cal.getTime());
            if(daysOfWeek.contains(w)){
                result.add(cal.getTime());
            }
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return result;
    }

    public static int getDayOfWeek(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w =cal.get(Calendar.DAY_OF_WEEK) - 1;
        if(w <= 0){
            w = 7;
        }
        return w;
    }
}
