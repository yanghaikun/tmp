package net.rlair.flight.support.util;

import net.rlair.flight.support.log.Log;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Kevin on 2017/8/26.
 */
public class UtilsTest {
    @Test
    public void getDateTest() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse("2017-08-26");
        Date endDate = sdf.parse("2017-10-26");
        //获取每周一，三，七
        List<Integer> schedule = Arrays.asList(1, 3, 7);
        List<Date> result = Utils.getDateMachDayOfWeek(startDate, endDate, schedule);

        for(Date d : result){
            Log.FLIGHT.info(sdf.format(d));
        }
    }
}
