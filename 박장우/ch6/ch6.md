# 6장: 스트림으로 데이터 수집

# 용어 정리

- 컬렉션 `Collection` : 객체들의 그룹을 나타내는 인터페이스이다. 자바에서 컬렉션은 데이터를 저장하고 관리하는데 사용된다. `List, Set, Queue` 등이 있다.
- 컬렉터 `Collector` : Java 스트림 API에서 사용되는 인터페이스이다. 스트림의 요소를 수집하여 다양한 종류의 결과를 생성하는 데 사용된다. 컬렉터는 스트림의 요소를 수집하고 그룹화, 변환, 집계 등을 수행할 수 있다. `supplier(), accumulator(), combiner(), finisher()` 등이 있다.
    - 요약: `collect` 메서드의 인수
- `collect` : Java 스트림 API에서 제공하는 최종 연산 중 하나이다. 스트림의 요소를 수집하여 원하는 형식으로 변환하고, 수집 과정에서 컬렉터를 사용한다.

# 컬렉터

collect으로 결과를 수집하는 과정을 간단하면서도 유연한 방식으로 정의할 수 있다.

스트림에 collect를 호출하면 스트림의 요소에 컬렉터로 파라미터화된 리듀싱 연산이 수행된다.

collect에서는 리듀싱 연산을 이용해서 스트림의 각 요소를 방문하면서 컬렉터가 작업을 처리한다.

## 미리 정의된 컬렉터

`Collectors` 클래스에서 제공하는 팩토리 메서드의 기능

- 스트림 요소를 하나의 값으로 리듀스하고 요약
- 요소 그룹화
- 요소 분할
    - Predicate를 그룹화 함수로 사용한다.

## 리듀싱과 요약

컬렉터로 스트림의 항목을 하나의 결과로 합칠 수 있다.

- `counting()`
    
    ```java
    long howManyDishes = menu.stream().collect(Collectors.counting());
    
    // static import 하면 코드를 줄일 수 있다.
    import static java.util.stream.Collectors.*;
    long howManyDishes = menu.stream().collet(counting());
    
    long howManyDishes = menu.stream().count();
    ```
    

### 최대, 최소

`maxBy` , `minBy`

`Comparator` 를 인수로 받는다.

```java
Comparator<Dish> dishCaloriesComparator = 
		Comparator.comparingInt(Dish::getCalories);

Optional<Dish> mostCalorieDish = 
		menu.stream().collect(maxBy(dishCaloriesComparator));
```

### 요약 연산

Collectors 클래스는 `Collectors.summingInt` 라는 요약 팩토리 메서드를 제공한다.

- 객체를 int로 매핑하는 함수를 인수로 받는다.
- 이 인수로 전달된 함수는 객체를 int로 매핑한 컬렉터를 반환한다.
- `summingInt` 는 총합을 계산한다.

이외에도 `summingLong(), summingDouble(), averagingInt(), averagingLong(), ...`  등이 있다.

**두 개 이상의 연산을 한 번에 수행할 때**

```java
IntSummaryStatistics menuStat = 
		menu.stream().collect(summarizingInt(Dish::getCalories));

IntSummaryStatistics{count=9, sum=4500, min=120, average=477.77778, max=800}
```

메서드 `summarizing___`, 클래스 `___SummaryStatistics` 를 사용하면 된다.

### 문자열 연결

`joining` 팩토리 메서드 : 스트림의 각 객체에 `toString` 을 호출 → 모든 문자열을 하나의 문자열로 연결해서 반환

```java
String shortMenu = menu.stream().map(Dish::getName).collect(joining());

// Dish 클래스에 name을 반환하는 toString 메서드가 들어 있다면 map 생략 가능.
String shortMenu = menu.stream.collect(joining());

// 구분자를 넣을 수도 있다.
String shortMenu = menu.stream().map(Dish::getName).collect(joining(", "));

```

- 내부적으로 StringBuilder를 이용.

### 범용 리듀싱 요약 연산

`reducing` 팩토리 메서드로 범용적인 리듀싱 연산 적용 가능.

```java
int totalCal = menu.stream()
		.collect(reducing(0, Dish::getCalories, (i, j) -> i + j));
```

reducing의 인수는 세 개 이다.

- 첫 번째: 리듀싱 연산의 시작값
    - 스트림에 인수가 없을 때는 반환값
    - 숫자 합계에서는 인수가 없을 때  반환값으로 0이 적합
- 두 번째: 변환 함수
- 세 번째: `BinaryOperator` , 두 항목을 하나의 값으로.

한 개의 인수를 갖는 reducing

- BinaryOperator만 인수로 가지는 경우
- 스트림의 첫 번째 요소가 시작 요소.
- 두 번째 인수: 자신을 그대로 반환하는 `항등 함수`
- Optional을 반환: 빈 스트림을 받는 경우 시작값이 설정되지 않을 수 있다.

# 그룹화

`Collectors.groupingBy` : 데이터 집합을 그룹화하기

```java
Map<Dish.Type, List<Dish>> dishesByType = 
		menu.stream().collect(groupingBy(Dish::getType));

// 결과
{FISH=[salmon, prawns], OTHER=[value,...], MEAT=[value,...]}
```

Dish.Type와 일치하는 모든 요리를 추출하는 함수를 groupingBy 메서드로 전달.

![Untitled](6%E1%84%8C%E1%85%A1%E1%86%BC%20%E1%84%89%E1%85%B3%E1%84%90%E1%85%B3%E1%84%85%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%B3%E1%84%85%E1%85%A9%20%E1%84%83%E1%85%A6%E1%84%8B%E1%85%B5%E1%84%90%E1%85%A5%20%E1%84%89%E1%85%AE%E1%84%8C%E1%85%B5%E1%86%B8%207c9a0a4dc20f4a6f9a49b2d64bef3148/Untitled.png)

## 그룹화된 요소 조작

요소를 그룹화 한 다음에는 각 결과 그룹의 요소를 조작하는 연산이 필요하다.

`filtering` 메서드

```java
Map<Dish.Type, List<Dish>> caloricDishesByType = 
		menu.stream()
				.collect(groupingBy(Dish::getType, 
								filtering(dish -> dish.getCalories > 500, toList())));
```

`mapping`

```java
Map<Dish.Type, List<String>> dishNamesByType = 
		menu.stream()
				.collect(groupingBy(Dish::getType, mapping(Dish::getName, toList())));
```

`groupingBy` 메서드의 두번째 Collector에 필터링, 매핑을 적용할 수 있다.

## 다수준 그룹화

`Collectors.groupingBy` 는 두 인수를 받을 수 있다. 

```java
public static <T, K, A, D>
Collector<T, ?, Map<K, D>> groupingBy(
					Function<? super T, ? extends K> classifier,
          Collector<? super T, A, D> downstream) {
    return groupingBy(classifier, HashMap::new, downstream);
}
```

예시

```java
Map<Dish.Type, Map<CaloricLevel, List<Dish>>> dishes = 
menu.stream().collect(
		groupingBy(Dish::getType,              // 첫 번째 분류 함수
				groupingBy(dish -> {               // 두 번째 분류 함수
						if(dish.getCalories() <= 400) 
								return CaloricLevel.DIET;
						else if (dish.getCalories() <= 700)
								return CaloricLevel.NORMAL;
						else return CaloricLevel.FAT;
				})
		)
);

// 결과
{MEAT={DIET=[chicken], NORMAL=[beef], FAT=[pork]},
FISH={DIET=[prawns],NORMAL=[salmon]},
OTHER={DIET=[rice, seasonal fruit], NORMAL=[french fries, pizza]}
}
```

최종적으로 두 수준의 맵은 첫 번째 키와 두 번째 기의 기준에 부합하는 요소 리스트를 값으로 갖는다.

### 요약

groupingBy의 연산은 버킷(양동이) 개념이다.

첫 번째 groupingBy는 각 키의 버킷을 만든다. 그리고 각각의 준비된 버킷을 서브스트림 컬렉터로 채워가기를 반복하면서 n수준 그룹화를 달성한다.

## 서브그룹으로 데이터 수집

첫 번째 groupingBy 로 넘겨주는 Collector 의 형식은 제한이 없다.

분류 함수 한 개의 인수를 갖는 groupingBy(f)는 컬렉터에 `toList()` 가 들어간다.

```java
public static <T, K> Collector<T, ?, Map<K, List<T>>>
groupingBy(Function<? super T, ? extends K> classifier) {
    return groupingBy(classifier, toList());
}
```

컬렉터로 counting(), maxBy() 등등이 들어간다.

### collectingAndThen

적용할 컬렉터와 변환 함수를 인수로 받아 다른 컬렉터를 반환한다.

```java
public static<T,A,R,RR> Collector<T,A,RR> collectingAndThen(Collector<T,A,R> downstream,
                                                            Function<R,RR> finisher) {
```

![Untitled](6%E1%84%8C%E1%85%A1%E1%86%BC%20%E1%84%89%E1%85%B3%E1%84%90%E1%85%B3%E1%84%85%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%B3%E1%84%85%E1%85%A9%20%E1%84%83%E1%85%A6%E1%84%8B%E1%85%B5%E1%84%90%E1%85%A5%20%E1%84%89%E1%85%AE%E1%84%8C%E1%85%B5%E1%86%B8%207c9a0a4dc20f4a6f9a49b2d64bef3148/Untitled%201.png)

## 프로젝트 적용 예시

```java
//Timestamp 를 key 로 갖는 BoolTake Map / 을 value 로 갖는 UserProductId가 Key 인 Map
//ex) userProductId가 X인 boolTake 들 중 (1차 조회) / 29일 오후 2시에 먹은 boolTake (2차 조회)
Map<Long, Map<Timestamp, BoolTake>> timestampBoolTakeMap = 
		boolTakeList.map(list -> list.stream()
                .collect(Collectors.groupingBy(
                        boolTake -> boolTake.getUserProduct().getId(),
                        Collectors.toMap(
                                BoolTake::getTakeDateTime,
                                boolTake -> boolTake
                        )
                )))
				        .orElse(Collections.emptyMap());
```

- BoolTake: 섭취 여부를 관리하는 클래스
- boolTakeList → Optional<List<BoolTake>> 타입.
1. 섭취 여부 리스트를 스트림으로 만듦
2. collect의 인수로 groupingBy를 사용 → Map을 생성.
3. Key는 Long 타입의 id.
4. Value는 Map<Timestamp, BoolTake>
5. orElse로 boolTakeList가 비어있을 경우 처리.

# 분할

분할 함수: Predicate를 분류 함수로 사용하는 특수한 그룹화 기능.

- 분할 함수가 불리언을 반환하므로 맵의 키는 boolean 타입.
    
    → True: ~~~ / False: ~~~ 형식으로 맵이 구성된다.
    

```java
Map<Boolean, List<Dish>> menu = 
		menu.stream().collect(partitioningBy(Dish::isVegtarian));

List<Dish> vegDishes = menu.get(true);
List<Dish> vegDishes2 = menu.stream().filter(Dish::isVegtarian)
		.collect(toList());	
```

### 장점

- true, false 두 가지 요소의 스트림 리스트를 모두 유지한다
- 컬렉터를 두 번째 인수로 전달할 수 있는 오버로드된 `partitioningBy` 메서드 존재.

```java
public static <T, D, A>
Collector<T, ?, Map<Boolean, D>> partitioningBy(
								Predicate<? super T> predicate,
                Collector<? super T, A, D> downstream) {
```

# Collector 인터페이스

```java
public interface Collector<T, A, R> {
		Supplier<A> supplier();
    BiConsumer<A, T> accumulator();
    BinaryOperator<A> combiner();
    Function<A, R> finisher();
    Set<Characteristics> characteristics();
}
```

- T : 수집될 스트림 항목의 제네릭 형식
- A: 누적자. 수집 과정에서 중간 결과를 누적하는 객체의 형식
- R: 수집 연산 결과 객체의 형식(대개 컬렉션)

### supplier()

새로운 결과 컨테이너 만들기.

supplier 메서드는 빈 결과로 이루어진 Supplier를 반환해야 한다.

```java
public Supplier <List<T>> supplier(){
		// return () -> new ArrayList<T>();
		return ArrayList::new;
}
```

### accumulator()

결과 컨테이너에 요소 추가하기.

리듀싱 연산을 수행하는 함수를 반환한다.

누적자와 스트림의 n번째 요소를 함수에 적용한다.

```java
public BiConsumer<List<T>, T> accumulator() {
		return (list, item) -> list.add(item);
//		return List::add
}
```

### finisher()

최종 변환 값을 결과 컨테이너로 적용. 

스트림 탐색을 끝내고 누적자 객체를 최종 결과로 변환하면서 누적 과정을 끝낼 때 호출할 함수를 반환해야 한다.

```java
public Function<List<T>, List<T>> finisher() {
		return Function.identity();
}
```

### combiner()

두 결과 컨테이너를 병합한다.

```java
public BinaryOperator<List<T>> combiner() {
		return (list1, list2) -> {
				list1.addAll(list2);
				return list1;
		}
}
```

### Characteristics

컬렉터의 연산을 정의하는 Characteristics 형식의 불변 집합을 변환.

- UNORDERED
- CONCURRENT
- IDENTITY_FINISH