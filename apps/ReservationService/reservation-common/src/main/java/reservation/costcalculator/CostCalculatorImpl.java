package reservation.costcalculator;

import reservation.AgeRange;

import java.util.Map;

public class CostCalculatorImpl implements CostCalculator{

    private final Integer BASE_OFFER_COST = 100;

    private final Map<AgeRange, Double> discountMap = Map.of(
            AgeRange.LESS_THAN_3_YEARS_OLD, 0.7,
            AgeRange.LESS_THAN_10_YEARS_OLD, 0.8,
            AgeRange.LESS_THAN_18_YEARS_OLD, 0.9,
            AgeRange.ADULT, 1.0
    );

    @Override
    public Double calculateOfferCost(Map<AgeRange, Long> ageGroupsSize, Long numOfDays) {

        return ageGroupsSize
                .entrySet()
                .stream()
                .map(ageGroup -> discountMap.get(ageGroup.getKey()) * BASE_OFFER_COST * numOfDays * ageGroup.getValue())
                .reduce(0.0, Double::sum);
    }
}
