package com.example.project.smartsilent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ashrin Tamim on 1/14/2017.
 */

public class TimeDateProvider {

    public static List<TimeDate> TimeList ;
    public static Map<String, TimeDate> TimedataMap;








    static {
        TimeList = new ArrayList<>();
        TimedataMap = new HashMap<>();
    }







    private static void addTimeData(TimeDate Data) {
        TimeList.add(Data);
        TimedataMap.put(Data.getId(),Data);

         TimeController.getInstance().saveTimeGson(Data);

    }





    public static void settimevalue(int silentHour, int silentMinute,int unsilentHour,int unsilentMinute)
    {
        addTimeData(new TimeDate(null,silentHour, silentMinute, unsilentHour, unsilentMinute));

    }
}
