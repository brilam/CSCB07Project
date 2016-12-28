package cs.b07.cscb07project.backend.databases;

import cs.b07.cscb07project.backend.constants.Constants.DataConstants;
import cs.b07.cscb07project.backend.data.Itinerary;

import java.util.Comparator;

/**
 * Class allowing comparison of Itineraries based on cost.
 */
public class CostComparator implements Comparator<Itinerary> {

  @Override
  public int compare(Itinerary o1, Itinerary o2) {
    if ((o1.getCost()) < (o2.getCost())) {
      return DataConstants.LESS_THAN_VALUE;
    } else if ((o1.getCost()) > (o2.getCost())) {
      return DataConstants.GREATER_THAN_VALUE;
    } else {
      return DataConstants.EQUAL_VALUE;
    }
  }

}
