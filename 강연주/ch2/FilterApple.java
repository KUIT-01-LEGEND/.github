package 강연주.ch2;

import java.util.ArrayList;
import java.util.List;

import static 강연주.ch2.Color.GREEN;

public class FilterApple {

    //첫 번째 시도: 녹색 사과 필터링
    public static List<Apple> filterGreenApples(List<Apple> inventory){
        List<Apple> result = new ArrayList<>();
        for (Apple apple: inventory){
            if (GREEN.equals(apple.getColor())) {
                result.add(apple);
            }
        }
        return result;
    }

    //두 번째 시도: 색깔로 사과 필터링
    public static List<Apple> filterAppleByColor(List<Apple> inventory, Color color){
        List<Apple> result = new ArrayList<>();
        for (Apple apple: inventory){
            if(apple.getColor().equals(color)){
                result.add(apple);
            }
        }
        return result;
    }

    //무게로 사과 필터링
    public static List<Apple> filterApplesByWeight(List<Apple> inventory,int weight){
        List<Apple> result = new ArrayList<>();
        for(Apple apple: inventory){
            if(apple.getWeight()>weight){
                result.add(apple);
            }
        }
        return result;
    }

    //세 번째 시도: 가능한 모든 속성으로 필터링
    public static List<Apple> filterApples(List<Apple> inventory, Color color, int weight, boolean flag){
        List<Apple> result = new ArrayList<>();
        for(Apple apple: inventory){
            if((flag && apple.getColor().equals(color)) || (!flag && apple.getWeight()>weight)){
                result.add(apple);
            }
        }
        return result;
    }


}
