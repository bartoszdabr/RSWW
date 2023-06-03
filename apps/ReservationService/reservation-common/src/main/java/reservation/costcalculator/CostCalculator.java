package reservation.costcalculator;

import reservation.AgeRange;

import java.util.HashMap;

public interface CostCalculator {

    Double calculateOfferCost(HashMap<AgeRange, Long> ageGroupsSize, Double stars, Integer numOfDays);
}
