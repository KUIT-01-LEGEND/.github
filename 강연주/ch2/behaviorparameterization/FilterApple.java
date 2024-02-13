package 강연주.ch2.behaviorparameterization;

import 강연주.ch2.Apple;
import 강연주.ch2.Color;

import java.util.ArrayList;
import java.util.List;

public class FilterApple {
    public static List<Apple> filterApples(List<Apple> inventory, ApplePredicate applePredicate){
        List<Apple> result = new ArrayList<>();
        for(Apple apple: inventory){
            if(applePredicate.test(apple)){
                result.add(apple);
            }
        }
        return result;
    }
}
