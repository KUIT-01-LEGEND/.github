package zzoni.ch02.quiz;

import java.util.Arrays;
import java.util.List;

/**
 * 퀴즈 2-1 유연한 prettyPrintApple 메서드 구현하기
 */

public class quiz2_1 {
    public static void main(String[] args) {
        List<Apple> inventory = Arrays.asList(
                new Apple(80, "green"),
                new Apple(155, "green"),
                new Apple(120, "red")
        );

        prettyPrintApple(inventory, new AppleWeightPrinter());
        prettyPrintApple(inventory, new AppleColorAndWeightPrinter());
    }

    public static void prettyPrintApple(List<Apple> inventory, ApplePrinter p) {
        for (Apple apple: inventory) {
            String output = p.print(apple);
            System.out.println(output);
        }
    }

    public interface ApplePrinter {
        String print(Apple apple);
    }

    public static class AppleWeightPrinter implements ApplePrinter{
        @Override
        public String print(Apple apple) {
            return "apple의 무게는 " +apple.getWeight() +"g 입니다.";
        }
    }

    public static class AppleColorAndWeightPrinter implements ApplePrinter{
        @Override
        public String print(Apple apple) {
            return "apple의 무게는 " +apple.getWeight() +"g 이고, 색상은 "+apple.getColor()+"입니다.";
        }
    }
}
