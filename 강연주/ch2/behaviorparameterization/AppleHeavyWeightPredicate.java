package 강연주.ch2.behaviorparameterization;

import 강연주.ch2.Apple;

public class AppleHeavyWeightPredicate implements ApplePredicate{
    @Override
    public boolean test(Apple apple) {
        return apple.getWeight()>150;
    }
}
