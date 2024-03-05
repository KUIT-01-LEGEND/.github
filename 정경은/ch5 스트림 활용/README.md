# 필터링
## Predicate로 필터링
> filter 메서드는 프레디케이트(boolean 반환 함수)를 인수로 받아,
일치하는 모든 요소를 포함하는 스트림을 반환
<img width="646" alt="Untitled" src="https://github.com/KUIT-01-LEGEND/modern-java-in-action/assets/96233738/f9e77ae0-85ac-4019-9099-8a1c67a69332">

<br>

## 고유 요소 필터링 - distinct
> 고유 여부는 스트림에서 만든 객체의 hashCode, equals로 결정됨
<img width="731" alt="Untitled 1" src="https://github.com/KUIT-01-LEGEND/modern-java-in-action/assets/96233738/a6c70b17-29a6-42a6-946a-61b88714d539">

---

# 슬라이싱
## Predicate 이용
자바 9 → 새로운 메서드 지원

### takeWhile
> 모든 스트림에 프레디케이트를 적용하여 슬라이싱 가능

**specialMenu**라는 리스트가 **이미 칼로리 순으로 정렬**되어 있다면,
<br>320 칼로리보다 크거나 같은 요리가 처음 나왔을 때 반복 작업을 중단하면 됨
```java
List<Dish> filteredMenu = specialMenu.stream()
				.filter(d -> d.getCalories() < 320)
				.collect(toList());
```

→ 아주 많은 요소를 포함하는 큰 스트림에서는 중단하는 것이 상당한 차이를 일으킬 수 있음

```java
List<Dish> slicedMenu = specialMenu.stream()
				.**takeWhile**(d -> d.getCalories() < 320)
				.collect(toList());
```

### dropWhile
> 프레디케이트가 처음으로 거짓이 되는 지점까지 발견된 요소를 버림
takeWhile과 정반대 작업 수행

<br>

## 축소
`limit(n)`: 주어진 값 이하의 크기를 갖는 새로운 스트림을 반환

## 요소 건너뛰기
`skip(n)`: 처음 n개 요소 제외 스트림 반환

limit ↔ skip 상호 보완적 연산 수행

<br>

---

# 매핑
> 특정 데이터를 선택하는 작업

## map()
1. 인수로 함수 제공
2. 각 요소에 함수 적용
3. 새로운 요소들이 매핑됨 

→ 여러 데이터를 가지는 클래스가 있고 해당 클래스를 여러 개 가지는 리스트가 있을 때,
리스트에 속한 모든 객체에서 특정 값만 추출하여 리스트로 뽑아낼 때

## 평면화 - flatMap
[”Hello”, “World”] 리스트가 있을 때, 결과로 [”H”, “e”, “l”, “o”, “W”, “r”, “d”]를 원함

즉, 고유 문자로 이루어진 리스트로 반환하고 싶을 때

### 문제점
`Stream<String>`을 의도했지만, `List<String[]>`이 반환됨

### map과 Arrays.stream 활용
문자열을 split한 문자열 배열을 받아 스트림으로 만들고, distinct() 적용

→ 결국 List<String<String>>을 만드는 것

### flatMap 사용
스트림을 하나의 스트림으로 평면화,
<br>즉, `Stream<String[]>`을 `Stream<String>`으로 평면화
```java
List<String> studentNames = studentList.stream()
        .map(Student::getName)
        .toList();

//flatMap 사용 전 -> List<String[]>으로 반환
List<String[]> list = studentNames.stream()
        .map(name -> name.split(""))
        .distinct()
        .toList();

//flatMap 사용
List<String> alphabetsInNames = studentNames.stream()
        .map(name -> name.split(""))
        .flatMap(Arrays::stream)
        .distinct()
        .toList();
```

## Quiz 5-2
```java
//1.
List<Integer> square = numbers.stream()
				.map(n -> n*n)
				.toList();

//2.
List<int[]> = numbers1.stream()
				.flatMap(n1 -> numbers2.stream()
				.map(n2 -> new int[]{n1, n2}))**
				.toList();

//3.
List<int[]> = numbers1.stream()
				.flatMap(n1 -> numbers2.stream()
				.filter(n2 -> (n1 + n2) % 3 == 0)
				.map(n2 -> new int[]{n1, n2}))
				.toList();
```

<br>

---

# 검색과 매칭
## 쇼트서킷 기법 사용
*쇼트서킷 기법 → 자바의 &&, || 같은 연산 활용*

모든 요소를 처리하지 않고도 결과를 반환할 수 있음, 원하는 요소를 찾으면 즉시 결과 반환 가능

### anyMatch - 적어도 한 요소와 일치하는가
### allMatch - 모든 요소와 일치하는가
### noneMatch - allMatch와 반대 연산

## +
### findAny - 임의의 요소 검색, 반환
병렬 실행에서는 첫 번재 요소를 찾기 어려우므로,
순서가 상관 없는 상황이라면 병렬 스트림에서는 제약이 적은 findAny를 사용

### findFirst - 첫 번째 요소

<br>

## Optional이란?
> 값의 존재/부재 여부를 표현하는 컨테이너 클래스

→ null 확인 관련 버그 대처 가능

### 값 처리 기능 제공

- isPresent(): Optional이 값을 포함하면 true
- ifPresent(Consumer<T> block): Optional에 값이 있으면 주어진 블럭을 실행
- T get(): 값이 존재하면 반환, 없으면 NoSuchElementException 일으킴
- T orElse(T other): 값이 있으면 반환, 없으면 기본값 반환

### ex
```java
menu.stream()
  .filter(Dish::isVegetarian)
  .findAny()
  .ifPresent(dish -> System.out.println(dish.getName());
//값이 있으면 출력, 없으면 아무 일도 일어나지 않음
```

<br>

---

# 리듀싱
> 모든 스트림 요소를 처리해서 값으로 도출
*fold*

## 요소의 합
numbers라는 리스트 안에 든 숫자의 합을 구하는 과정

```java
int sum = numbers.stream().reduce(0, (a, b) -> a + b);
//초기값 = 0 -> 값을 더하면 누적값
```
<img width="853" alt="Untitled 2" src="https://github.com/KUIT-01-LEGEND/modern-java-in-action/assets/96233738/f3113f64-3952-4b97-8254-20d5192e8810">

### 메서드 참조 이용으로 코드 간결화
```java
int sum = numbers.stream().reduce(0, Integer::sum);
```

### 초기값을 받지 않도록 오버로드된 reduce
```java
Optional<Integer> sum = numbers.stream().reduce((a, b) -> (a + b));
```

**Optional을 반환하는 이유**
스트림에 아무 요소도 없는 경우, 초기값이 없으므로 합계 반환 불가능

→ 합계가 없음을 나타내도록 Optional 객체로 감쌈

<br>

## 최댓값, 최솟값
```java
Optional<Integer> max = numbers.stream().reduce(Integer::max);
Optional<Integer> min = numbers.stream().reduce(Integer::min);
```

## Quiz 5-3
```java
int count = menu.stream()
		.map(d -> 1)
		.reduce(0, Integer::sum);
```

<br>

---

# p.177 문제

1. 2011년에 일어난 모든 트랜잭션을 찾아 값을 오름차순으로 정리하시오.
    
    ```java
    List<Transaction> = transactions.stream()
    		.filter(t -> t.getYear() == 2011)
    		.sorted(comparing(Transaction::getValue)
    		.toList();
    ```
    
2. 거래자가 근무하는 모든 도시를 중복 없이 나열하시오.
    
    ```java
    List<String> cities = transaction.stream()
    		.map(t -> t.getTrader().getCity())
    		.distinct()
    		.toList();
    ```
    
3. 케임브리지에서 근무하는 모든 거래자를 찾아서 이름순으로 정렬하시오.
    
    ```java
    List<Trader> = transactions.stream()
    		.map(Transaction::getTrader)
    		.filter(t -> t.getCity().equals("Cambridge"))
    		.sorted(comparing(Trader::getName))
    		.toList();
    ```
    
4. 모든 거래자의 이름을 알파벳순으로 정렬해서 반환하시오.
    
    ```java
    //정렬해서 리스트로 반환하라는 줄 알았는데,,, 이름을 걍 다 더해서 나열하라고 한 거구나..
    String name = transactions.stream()
    		.map(t -> t.getTrader().getName())
    		.distinct()
    		.sorted()
    		.reduce("", (name1, name2) -> name1 + name2);
    ```
    
5. 밀라노에 거래자가 있는가?
    
    ```java
    boolean milan = transactions.stream()
    		.anyMatch(t -> t.getTrader().getCity().equals("Milan");
    ```
    
6. 케임브리지에 거주하는 거래자의 모든 트랜잭션값을 출력하시오.
    
    ```java
    transactions.stream()
    		.filter(t -> t.getTrader().getCity().equals("Cambridge"))
    		.map(Transaction::getValue)
    		.forEach(System.out::println);
    ```
    
7. 전체 트랜잭션 중 최댓값은 얼마인가?
    
    ```java
    Optional<Integer> maxValue = transactions.stream()
    		.map(Transaction::getValue)
    		.reduce(Integer::max);
    ```
    
8. 전체 트랜잭션 중 최솟값은 얼마인가?
    
    ```java
    Optional<Integer> minValue = transactions.stream()
    		.map(Transaction::getValue)
    		.reduce(Integer::min);
    ```
    
    ```java
    Optional<Transaction> smallestTransaction = transactions.stream()
    		.min(comparing(Transaction::getValue));
    ```

<br>

---

# 숫자형 스트림
## 기본형 특화 스트림
기본형인 Integer로 매핑한 이후에 sum() 메서드 직접적으로 호출 가능

`.mapToInt(Transaction::getValue).sum();`

mapToInt는 Stream<Integer>가 아닌 IntStream을 반환

### 객체 스트림으로 복원하기
`boxed` 메서드를 이용하여 특화 스트림을 일반 스트림으로 변환

### ex
```java
IntStream intStream = menu.stream().mapToInt(Dish::getCalories);
Stream<Integer> stream = intStream.boxed();
```

### 기본값: OptionalInt
실제 최댓값이 0인 상황 `vs` 스트림에 요소가 없어서 0이 반환된 상황

→ 어떻게 구별?

`OptionalInt`를 이용하여 최댓값이 없는 상황에 사용할 기본값을 명시적으로 정의할 수 있음

```java
OptionalInt maxCalories = menu.stream()
				.mapToInt(Dish::getCalories)
				.max();
int max = maxCalories.orElse(1); //값이 없을 때 기본 최댓값을 1로 지정
```

## 숫자 범위
`IntStream`과 `LongStream`에서 정적 메서드 `range`, `rangeClosed`를 제공함

수학에서 범위를 나타낼 때 (n1, n2) / [n1, n2] 이 느낌

### range
> 시작값과 종료값이 결과에 포함되지 않음

### rangeClosed
> 시작값과 종료값이 결과에 포함됨

<br>

---

# 스트림 만들기
## Code
```java
//값으로 스트림 생성
Stream<String> stream = Stream.of("Modern", "Java", "in", "Action");
stream.map(String::toUpperCase).forEach(System.out::println);

//스트림 비우기
Stream.empty();

//null이 될 수 있는 객체로 스트림 만들기
String homeValue = System.getProperty("home");
Stream<String> homeValueStream = homeValue == null ? Stream.empty() : Stream.of(value);
//Stream.ofNullable 이용
Stream<String> homeValueStream = Stream.ofNullable(System.getProperty("home"));
//flatMap 함께 사용
Stream<String> values = Stream.of("config", "home", "user")
    .flatMap(key -> Stream.ofNullable(System.getProperty(key)));

//배열로 스트림 만들기
int[] numbers = {2, 3, 5, 7, 11, 13};
int sum = Arrays.stream(numbers).sum();
```

### 등
파일로 스트림 만들기, 함수로 무한 스트림 만들기는 책에서 확인

<br>

## 무한 스트림(언바운드 스트림)
> 무한 스트림을 만들 수 있는 두 정적 메서드 Stream.iterate과 Stream.generate

### Stream.iterate
> 연속된 일련의 값을 만들 때 사용

작업을 언제 중단해야 하는지 알 수 없도록 사용할 수는 없음

### Stream.generate
> `Supplier<T>`를 인수로 받아 새로운 값을 생산

연속적으로 값을 계산하지 않음
