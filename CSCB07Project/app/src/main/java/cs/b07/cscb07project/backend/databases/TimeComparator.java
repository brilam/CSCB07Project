package cs.b07.cscb07project.backend.databases;


import cs.b07.cscb07project.backend.constants.Constants.DataConstants;
import cs.b07.cscb07project.backend.constants.Constants.DateConstants;
import cs.b07.cscb07project.backend.data.Itinerary;

import java.util.Comparator;


/**
 * Class allowing comparison of Itineraries based on time.
 */
public class TimeComparator implements Comparator<Itinerary> {

  @Override
  public int compare(Itinerary o1, Itinerary o2) {
    String[] itinTimeOne = o1.getTotalTime().split(":");
    String[] itinTimeTwo = o2.getTotalTime().split(":");
    // Splits the hours and minutes from the duration of each Itinerary and parses each
    int itinOneHours = Integer.parseInt(itinTimeOne[DateConstants.HOUR_INDEX]);
    int itinOneMinutes = Integer.parseInt(itinTimeOne[DateConstants.MINUTE_INDEX]);
    int itinTwoHours = Integer.parseInt(itinTimeTwo[DateConstants.HOUR_INDEX]);
    int itinTwoMinutes = Integer.parseInt(itinTimeTwo[DateConstants.MINUTE_INDEX]);


    if ((itinOneHours) < (itinTwoHours)) {
      return DataConstants.LESS_THAN_VALUE;
    } else if ((itinOneHours) > (itinTwoHours)) {
      return DataConstants.GREATER_THAN_VALUE;
    } else {
      if (itinOneMinutes == itinTwoMinutes) {
        return DataConstants.EQUAL_VALUE;
      } else if ((itinOneMinutes) < (itinTwoMinutes)) {
        return DataConstants.LESS_THAN_VALUE;
      } else {
        return DataConstants.GREATER_THAN_VALUE;
      }

    }
  }

}
