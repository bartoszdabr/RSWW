package reservation.costcalculator;

import reservation.AgeRange;

import java.util.Map;

public interface CostCalculator {

    Double calculateOfferCost(Map<AgeRange, Long> ageGroupsSize, Long numOfDays);
}
