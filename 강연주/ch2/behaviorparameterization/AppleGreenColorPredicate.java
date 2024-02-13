package 강연주.ch2.behaviorparameterization;

import 강연주.ch2.Apple;

import static 강연주.ch2.Color.GREEN;

public class AppleGreenColorPredicate implements ApplePredicate{
    @Override
    public boolean test(Apple apple) {
        return GREEN.equals(apple.getColor());
    }
}
