# 5장: 스트림 활용

# 필터링

---

### Predicate

스트림의`filter` 메서드는 predicate를 인수로 받아서 일치하는 모든 요소를 포함하는 스트림을 반환한다.

```java
List<Dish> vegMenu = menu.stream()
													.filter(Dish::isVegetarian)
													.collect(toList());
```

### Distinct

`distinct` 메서드 : 고유 요소로 이루어진 스트림을 반환.

고유 여부는 스트림에서 만든 객체의 hashCode, equals로 결정된다.

```java
List<Integer> numbers = Arrays.asList(1,2,1,3,3,3,2,4);
numbers.stream()
		.filter(i -> i%2 == 0) // 2, 2, 4
		.distinct()            // 2, 4
		.forEach(System.out::println); // 출력
```

# 예제

프로젝트에 적용한 부분

```java
private List<Date> filterAndSortByYearAndMonth(String year, String month, List<Date> takeDates) {
    return takeDates.stream()
            .filter(takeDate -> takeDate.toString().split(DATE_DELIMITER)[0].equals(year))
            .filter(takeDate -> takeDate.toString().split(DATE_DELIMITER)[1].equals(month))
            .sorted()
            .toList();
}
```

List<Date> takeDates: [2024-02-26, 2024-02-27] 과 같은 Date 데이터를 담음.

해당 데이터를 스트림으로 만들고, 연도와 월 데이터가 같은 항목만 추출한 다음 정렬해서 리스트로 만든 후 리턴한다.

# 슬라이싱

---

### Predicate

filter 연산을 적용하면 전체 스트림을 반복하면서 각 요소에 predicate를 적용한다.

만약 리스트가 정렬되어 있고 특정 조건을 만족하는 경우 반복 작업을 중단하려면?

→ `takeWhile` 연산을 사용하자.

320칼로리 이하의 요리를 선택하는 예제

```java
// specialMenu는 calories 순으로 정렬되어 있다.
List<Dish> filteredMenu = 
		specialMenu.stream()
						.filter(dish -> dish.getCalories() < 320)
						.collect(toList());

// 320칼로리보다 크거나 같은 요리가 나왔을 때 반복 작업을 중단.
List<Dish> slicedMenu1 = 
		specialMenu.stream()
						.**takeWhile**(**dish -> dish.getCalories() < 320**)
						.collect(toList());
```

`dropWhile` : takeWhile과 정반대의 연산. predicate가 처음으로 거짓이 되는 지점까지 발견된 요소를 버리고 남은 모든 요소를 반환.

- 위 예제에서 dropWhile을 사용하면 320칼로리보다 큰 요소를 탐색.

### 스트림 축소

`limit(n)` : 스트림이 정렬되어 있으면 최대 요소 n개를 반환할 수 있다.

주어진 값 이하의 크기를 갖는 새로운 스트림을 반환한다.

### 요소 건너뛰기

`skip(n)` : n만큼 건너뛰고 나머지 요소를 반환한다.

# 매핑

---

### 스트림의 각 요소에 함수 적용하기

`map` : 함수를 인수로 받아 새로운 요소로 매핑된 스트림을 반환한다. 

- 기본형 요소에 대한 mapTo*Type* 메서드도 지원한다 (mapToInt, mapToLong, mapToDouble).

### 스트림 평면화

<aside>
💡 고유 문자로 이루어진 리스트:

`["Hello", "World"]` 리스트를 
`["H", "e", "l", "o", "W", "r", "d"]` 로 만드려면?

</aside>

`flatMap` : 평면화된 스트림을 반환. 인수로 `Arrays::stream` 을 넣을 수 있다.

- `Arrays.stream()` : 문자열을 받아 스트림을 만듦.

```java
words.stream()
		.map(word -> word.split(""))
		.flatMap(Arrays::stream)
		.distinct()
		.collect(toList());
```

# 검색과 매칭

### `anyMatch`

Predicate가 주어진 스트림에서 적어도 한 요소와 일치하는지 확인할 때 사용.

```java
if(memu.stream().anyMatch(Dish::isVegetarian)){
		//...
}
```

### `allMatch`

Predicate가 모든 요소와 일치하는지 검사.

메뉴의 모든 요리가 1000 칼로리 이하면 건강식일 때, 메뉴가 건강식인지 확인하는 코드

```java
boolean isHealthy = menu.stream().allMatch(dish -> dish.getCalories < 1000);
```

### `NoneMatch`

Predicate와 일치하는 요소가 없는지 확인한다.

### 쇼트서킷

자바의 `&&` , `||` 과 같은 연산.

표현식에서 하나라도 거짓(참) 이라는 결과가 나오면 나머지 표현식의 결과와 상관없이 전체 결과도 거짓(참)으로 정하는 것.

### 요소 검색: `findAny`

현재 스트림에서 임의의 요소를 반환.

```java
Optional<Dish> dish = menu.stream().filter(Dish::isVeg).findAny();
```

<aside>
💡 Optional

값의 존재나 부재 여부를 표현하는 컨테이너 클래스. NPE 방지

`isPresent()` : 값이 존재하면 true
`ifPresent(Consumer<T> block)` : 값이 있으면 주어진 블록 실행.
`T get()` : 값이 존재하면 값을 반환, 값이 없으면 NoSuchElementException을 일으킴.
`T orElse(T other)` : 값이 있으면 값을 반환, 없으면 기본값을 반환.

</aside>

### 첫 번째 요소 찾기: `findFirst`

현재 스트림에서 첫 번째 요소 반환. 순서가 정해져 있을 때 사용.

- 병렬 실행에서는 첫 번째 요소를 찾기 어려우므로 요소의 반환 순서가 상관없다면 findAny를 사용한다.

# Reducing

### `Reduce`

모든 스트림 요소를 BinaryOperator로 처리해서 **값으로 도출**한다. 

```java
T reduce(T identity, BinaryOperator<T> accumulator);

int sum = numbers.stream().reduce(0, Integer::sum);
int product = numbers.stream().reduce(1, (a, b) -> a * b);

// 초기값(identity)가 없으므로 아무 요소가 없을 때를 위해 Optional<T>를 반환한다.
Optional<T> reduce(BinaryOperator<T> accumulator);

Optional<Integer> min = numbers.stream().reduce(Integer::min);

<U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner);
```

### Map-Reduce 패턴

map과 reduce를 연결하는 기법.

스트림의 각 요소를 1로 매핑한 다음 reduce로 이들의 합계를 계산하는 방식으로 스트림의 요소 수를 센다.

```java
// map reduce pattern
int count = menu.stream().map(d -> 1).reduce(0, Integer::sum);

// 그냥 count 사용해보기
long count = menu.stream().count()
```

<aside>
💡 reduce를 왜 쓰나요?

- reduce를 사용하면 내부 반복이 추상화되면서 내부적으로 병렬로 reduce를 실행할 수 있게 된다.

- 외부 반복에서는 sum 변수를 공유해야 하기 때문에 병렬화가 어렵다.

- 단, reduce로 병렬적인 실행을 위해서는 reduce에 넘겨준 람다의 상태가 바뀌지 않아야 하며, 연산이 어떤 순서로 실행되더라도 결과가 바뀌지 않는 구조여야 한다.

</aside>

# 숫자형 스트림

### 기본형 특화 스트림

- IntStream
- DoubleStream
- LongStream

숫자 관련 reducing 연산 수행 메서드를 제공한다. (min, max, average 등)

필요한 경우 다시 객체 스트림으로 복원하는 기능도 제공.

### 스트림 → 특화 스트림

`mapToInt`, `mapToDouble` , `mapToLong` 

각각 특화된 스트림을 반환한다.

```java
int calories = menu.stream().mapToInt(Dish::getCalories).sum();
```

스트림이 비어있으면 sum은 기본값 0을 반환.

### 객체 스트림으로 복원

`boxed` 사용

```java
Stream<Integer> boxed(); // in IntStream
```

### 숫자 범위

특정 범위의 숫자를 이용해야 할 때:

- `range` : 열린 구간
- `rangeClosed`  : 닫힌 구간

# 스트림 만들기

### 값으로 스트림 만들기

`Stream.of()`  : 정적 메서드, 스트림을 생성.

`Stream.empty()` : 스트림 비우기

### Nullable Stream

자바 9부터 `Stream.ofNullable()` 로 nullable한 객체를 포함하는 스트림을 생성 가능.

### 배열로 스트림 만들기

`Arrays.stream()` 

### 파일 스트림

`Files.lines` 로 파일의 각 행 요소를 반환하는 스트림 생성 가능.

### 함수로 무한 스트림 만들기

`Stream.iterate` 

```java
Stream.iterate(0, n -> n + 2)
		.limit(10)
		.forEach(System.out::println)

// 피보나치 수열 생성기
Stream.iterate(new int[]{0, 1},
			t -> new int[]{t[1], t[0] + t[1]})
			.limit(20)
			.map(t -> t[0])
			.forEach(System.out::println);

```

# 실전 연습 (책 177p)

1. 2011년에 일어난 모든 트랜잭션을 찾아 값을 오름차순으로 정리하시오.
    
    ```java
    transactions.stream()
            .filter(transaction -> transaction.getYear() == 2011)
            .sorted(Comparator.comparing(Transaction::getValue))
            .forEach(System.out::println);
    ```
    
    ![Untitled](5%E1%84%8C%E1%85%A1%E1%86%BC%20%E1%84%89%E1%85%B3%E1%84%90%E1%85%B3%E1%84%85%E1%85%B5%E1%86%B7%20%E1%84%92%E1%85%AA%E1%86%AF%E1%84%8B%E1%85%AD%E1%86%BC%20385ba14fc04840b58667418fdfce0669/Untitled.png)
    
2. 거래자가 근무하는 모든 도시를 중복 없이 나열하시오.
    
    ```java
    transactions.stream()
            .map(transaction -> transaction.getTrader().getCity())
            .distinct()
            .forEach(System.out::println);
    ```
    
    ![Untitled](5%E1%84%8C%E1%85%A1%E1%86%BC%20%E1%84%89%E1%85%B3%E1%84%90%E1%85%B3%E1%84%85%E1%85%B5%E1%86%B7%20%E1%84%92%E1%85%AA%E1%86%AF%E1%84%8B%E1%85%AD%E1%86%BC%20385ba14fc04840b58667418fdfce0669/Untitled%201.png)
    
3. 케임브리지에서 근무하는 모든 거래자를 찾아서 이름순으로 나열하시오.
    
    ```java
    transactions.stream()
            .map(Transaction::getTrader)
            .distinct()
            .filter(trader -> trader.getCity().equals("Cambridge"))
            .sorted(Comparator.comparing(Trader::getName))
            .forEach(System.out::println);
    
    // 정답
    
    List<Trader> traders =
    transactions.stream()
            .map(Transaction::getTrader)
            .filter(trader -> trader.getCity().equals("Cambridge"))
            .distinct()
            .sorted(Comparator.comparing(Trader::getName))
            .forEach(System.out::println);
    ```
    
    ![Untitled](5%E1%84%8C%E1%85%A1%E1%86%BC%20%E1%84%89%E1%85%B3%E1%84%90%E1%85%B3%E1%84%85%E1%85%B5%E1%86%B7%20%E1%84%92%E1%85%AA%E1%86%AF%E1%84%8B%E1%85%AD%E1%86%BC%20385ba14fc04840b58667418fdfce0669/Untitled%202.png)
    
4. 모든 거래자의 이름을 알파벳순으로 정렬해서 반환하시오.
    - 몰라서 정답 확인
    - 문제에서 원하는 답이 뭔지 모르겠다
    
    ```java
    String reduce = transactions.stream()
            .map(transaction -> transaction.getTrader().getName())
            .distinct()
            .sorted()
            .reduce("", (n1, n2) -> n1 + n2);
    System.out.println(reduce);
    ```
    
    ![Untitled](5%E1%84%8C%E1%85%A1%E1%86%BC%20%E1%84%89%E1%85%B3%E1%84%90%E1%85%B3%E1%84%85%E1%85%B5%E1%86%B7%20%E1%84%92%E1%85%AA%E1%86%AF%E1%84%8B%E1%85%AD%E1%86%BC%20385ba14fc04840b58667418fdfce0669/Untitled%203.png)
    
5. 밀라노에 거래자가 있는가?
    
    ```java
    transactions.stream()
            .filter(transaction -> transaction.getTrader().getCity().equals("Milan"))
            .findAny()
            .ifPresentOrElse(System.out::println, () -> System.out.println("No"));
    // 정답
    boolean milanBased = transactions.stream()
            .anyMatch(transaction -> transaction
                    .getTrader()
                    .getCity()
                    .equals("Milan"));
    System.out.println(milanBased);
    ```
    
    ![Untitled](5%E1%84%8C%E1%85%A1%E1%86%BC%20%E1%84%89%E1%85%B3%E1%84%90%E1%85%B3%E1%84%85%E1%85%B5%E1%86%B7%20%E1%84%92%E1%85%AA%E1%86%AF%E1%84%8B%E1%85%AD%E1%86%BC%20385ba14fc04840b58667418fdfce0669/Untitled%204.png)
    
6. 케임브리지에 거주하는 거래자의 모든 트랜잭션값을 출력하시오.
    
    ```java
    transactions.stream()
            .filter(transaction -> transaction.getTrader().getCity().equals("Cambridge"))
            .map(Transaction::getValue)
            .forEach(System.out::println);
    
    // 정답
    transactions.stream()
            .filter(t -> "Cambridge".equals(t.getTrader().getCity()))
            .map(Transaction::getValue)
            .forEach(System.out::println);
    ```
    
    ![Untitled](5%E1%84%8C%E1%85%A1%E1%86%BC%20%E1%84%89%E1%85%B3%E1%84%90%E1%85%B3%E1%84%85%E1%85%B5%E1%86%B7%20%E1%84%92%E1%85%AA%E1%86%AF%E1%84%8B%E1%85%AD%E1%86%BC%20385ba14fc04840b58667418fdfce0669/Untitled%205.png)
    
    ### 정답 코드가 더 적절한 이유
    
    ```java
    // 1
    transaction -> transaction.getTrader().getCity().equals("Cambridge")
    
    // 2
    t -> "Cambridge".equals(t.getTrader().getCity())
    ```
    
    1번 코드보다 2번 코드가 더 적절한 이유는 2번 코드가 Null safe 하기 때문이다.
    
    - 1번 코드에서 `transaction.getTrader().getCity()` 가 `null` 일 경우 코드는 `NullPointerException` 을 일으킨다.
        - 명시적으로 null을 검사하거나 `Objects.equals()` 와 같은 null 안전한 방법으로 비교해야 한다.
    - 2번 코드에서는 문자열 상수를 객체의 메서드로 호출하는 방식이다.
        
        만약 `getCity()` 가 `null`을 리턴한다면 이는 `"Cambridge".equals(null)` 로 해석된다. 
        
        `equals()`  는 `null`  을 인자로 받을 수 있고, 해당 경우에 항상 `false` 를 리턴한다.
        
        따라서 이 방법으로 추가적인 명시적 널 체크 없이 안전하게 널을 처리할 수 있다.
        
    
7. 전체 트랜잭션 중 최댓값은 얼마인가?
    
    ```java
    OptionalInt max = transactions.stream()
            .mapToInt(Transaction::getValue).max();
    System.out.println(max.getAsInt());
    
    // 정답
    Optional<Integer> highestValue = transactions.stream()
    				.map(Transaction::getValue)
            .reduce(Integer::max);
    System.out.println(highestValue.get());
    ```
    
    ![Untitled](5%E1%84%8C%E1%85%A1%E1%86%BC%20%E1%84%89%E1%85%B3%E1%84%90%E1%85%B3%E1%84%85%E1%85%B5%E1%86%B7%20%E1%84%92%E1%85%AA%E1%86%AF%E1%84%8B%E1%85%AD%E1%86%BC%20385ba14fc04840b58667418fdfce0669/Untitled%206.png)
    
8. 전체 트랜잭션 중 최솟값은 얼마인가?
    
    ```java
    OptionalInt min = transactions.stream()
            .mapToInt(Transaction::getValue).min();
    System.out.println(min.getAsInt());
    
    // 정답 코드, 마지막 코드가 가장 적절하다.
    Optional<Transaction> smallestTransaction = transactions.stream()
    				.reduce((t1, t2) -> t1.getValue() < t2.getValue() ? t1 : t2);
    
    Optional<Transaction> min1 = transactions.stream()
    				.min(Comparator.comparing(Transaction::getValue));
    ```
    
    ![Untitled](5%E1%84%8C%E1%85%A1%E1%86%BC%20%E1%84%89%E1%85%B3%E1%84%90%E1%85%B3%E1%84%85%E1%85%B5%E1%86%B7%20%E1%84%92%E1%85%AA%E1%86%AF%E1%84%8B%E1%85%AD%E1%86%BC%20385ba14fc04840b58667418fdfce0669/Untitled%207.png)