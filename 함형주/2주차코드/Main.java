package 이주차;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Main {

    static Trader raoul;
    static Trader mario;
    static Trader alan;
    static Trader brian;

    static List<Transaction> transactions;

    public static void main(String[] args) {

        연습문제5_6_1();
        연습문제5_6_2();
        연습문제5_6_3();
        연습문제5_6_4();
        연습문제5_6_5();
        연습문제5_6_6();
        연습문제5_6_7();
        연습문제5_6_8();
    }

    static {
        raoul = new Trader("Raoul", "Cambridge");
        mario = new Trader("Mario", "Milan");
        alan = new Trader("Alan", "Cambridge");
        brian = new Trader("Brian", "Cambridge");

        transactions = Arrays.asList(
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000),
                new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 710),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950));
    }

    // 2011년에 일어난 모든 트랜잭션을 찾아 값을 오름차순으로 정리하시오.
    private static void 연습문제5_6_1() {
        System.out.println("\n연습문제 5.6.1");
        transactions.stream()
                .filter(transaction -> transaction.getYear() == 2011)
                .sorted(Comparator.comparing(Transaction::getYear))
                .forEach(System.out::println);

    }

    // 거래자가 근무하는 모든 도시를 중복 없이 나열하시오.
    private static void 연습문제5_6_2() {
        System.out.println("\n연습문제 5.6.2");
        transactions.stream()
                .map(transaction -> transaction.getTrader().getCity())
                .distinct()
                .forEach(System.out::println);
    }

    // 케임브리지에서 근무하는 모든 거래자를 찾아서 이름순으로 정렬하시오
    private static void 연습문제5_6_3() {
        System.out.println("\n연습문제 5.6.3");
        transactions.stream()
                .map(Transaction::getTrader)
                .filter(trader -> trader.getCity().equals("Cambridge"))
                .distinct()
                .sorted(Comparator.comparing(Trader::getName))
                .forEach(System.out::println);

    }

    // 모든 거래자의 이름을 알파벳순으로 정렬해서 반환하시오.
    private static void 연습문제5_6_4() {
        System.out.println("\n연습문제 5.6.4");
        transactions.stream()
                .map(transaction -> transaction.getTrader().getName())
                .distinct()
                .sorted()
                .forEach(System.out::println);
    }

    // 밀라노에 거래자가 있는가?
    private static void 연습문제5_6_5() {
        System.out.println("\n연습문제 5.6.5");
        boolean is밀라노 = transactions.stream()
                .anyMatch(transaction -> transaction.getTrader().getCity().equals("Milan"));

        System.out.println("is밀라노 = " + is밀라노);

    }

    // 케임브리지에 거주하는 거래자의 모든 트랜잭션값을 출력하시오.
    private static void 연습문제5_6_6() {
        System.out.println("\n연습문제 5.6.6");
        transactions.stream()
                .filter(transaction -> transaction.getTrader().getCity().equals("Cambridge"))
                .forEach(System.out::println);

    }

    // 전체 트랜잭션 중 최댓값은 얼마인가?
    private static void 연습문제5_6_7() {
        System.out.println("\n연습문제 5.6.7");
        Optional<Integer> 트랜잭션최댓값 = transactions.stream()
                .map(Transaction::getValue)
                .reduce(Integer::max);

        System.out.println("트랜잭션최댓값 = " + 트랜잭션최댓값.get());

    }

    // 전체 트랜잭션 중 최솟값은 얼마인가?
    private static void 연습문제5_6_8() {
        System.out.println("\n연습문제 5.6.8");
        Optional<Integer> 트랜잭션최솟값 = transactions.stream()
                .map(Transaction::getValue)
                .reduce(Integer::min);

        System.out.println("트랜잭션최솟값 = " + 트랜잭션최솟값.get());
    }

}

