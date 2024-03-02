# 1장

자바8의 새로운 기능

# 메서드 참조

디렉터리에서 모든 숨겨진 파일을 필터링해야 하는 상황.

File 클래스의`isHidden` : File 클래스를 인수로 받아 boolean을 반환.

- 자바 8 이전

```java
File[] hiddenFiles = new File(".").listFiles(new FileFilter(){
    @Override
    public boolean accept(File file) {
        return file.isHidden();
    }
});
```

기존에는 FileFilter로 isHidden을 감싸고 FileFilter를 인스턴스화했어야 했다.

- 자바8 `메서드 참조` 사용

```java
File[] hiddenFiles = new File(".").listFiles(File::isHidden);
```

자바 8에서는 메서드는 일급 값이다.

일급 값(일급 시민): 자유롭게 전달할 수 있는 값.

이급 시민: 전달할 수 없는 구조체 

## 코드 넘겨주기

모든 녹색 사과를 선택해서 리스트를 반환하는 프로그램.

```java
public static List<Apple> filterApplesByColor(List<Apple> inventory, Color color) {
    List<Apple> result = new ArrayList<>();
    for (Apple apple : inventory) {
        if (apple.getColor() == color) {
            result.add(apple);
        }
    }
    return result;
}
```

무게로 필터링한다면?`

```java
public static List<Apple> filterApplesByWeight(List<Apple> inventory, int weight) {
    List<Apple> result = new ArrayList<>();
    for (Apple apple : inventory) {
        if (**apple.getWeight() > weight**) {
            result.add(apple);
        }
    }
    return result;
}
```

if 문을 제외하면 모든 코드가 똑같다 → 코드의 중복을 줄이는 방법이 없을까?

자바 8에서는 코드를 인수로 넘겨줄 수 있다.

```java
public static boolean isGreenApple(Apple apple){
		return "green".equals(apple.getColor())
}

public static boolean isHeavyApple(Apple apple){
		return apple.getWeight() > 150;
}

public interface ApplePredicate<T>{
		boolean test(T t);
}

public static List<Apple> filterApples(List<Apple> inventory, Predicate<Apple> p) {
    List<Apple> result = new ArrayList<>();
    for (Apple apple : inventory) {
      if (p.test(apple)) {
        result.add(apple);
      }
    }
    return result;
  }

List<Apple> inventory = Arrays.asList(
    new Apple(80, "green"),
    new Apple(155, "green"),
    new Apple(120, "red")
);

// [Apple{color='green', weight=80}, Apple{color='green', weight=155}]
List<Apple> greenApples = filterApples(inventory, FilteringApples::isGreenApple);
System.out.println(greenApples);

// [Apple{color='green', weight=155}]
List<Apple> heavyApples = filterApples(inventory, FilteringApples::isHeavyApple);
System.out.println(heavyApples);
```

### Predicate

인수로 값을 받아 `true` 나 `false` 를 반환하는 함수.

 

## 람다

predicate 부분을 `isGreenApple`, `isHeavyApple` 메서드로 매번 하기도 귀찮다!

```java
filterApples(inventory, (Apple a) -> "green".equals(a.getColor()));
```

한 번만 사용할 메서드는 따로 정의를 구현할 필요 없다.

# 스트림

for-each: 외부 반복

스트림 API: 내부 반복

### 멀티코어

최신 데스크톱, 노트북은 멀티코어를 장착했다. 즉 CPU가 4~8개 이상의 코어를 가지기 때문에 병렬로 작업을 진행하면 싱글코어보다 빨리 작업을 처리 가능.

### 멀티스레딩

이전에는 멀티스레딩에`synchronized` 를 활용했었다.

하지만 스레드를 잘 제어하지 못하면 원치 않는 방식으로 데이터가 변경될 수 있다.

자바에서 자주 반복되는 패턴: 데이터를 필터링, 추출, 그룹화하는 기능

- 필터링을 빠르게:
    - 컬렉션  → 스트림
    - 병렬로 처리
    - 리스트로 다시 복원.
- `parallelStream` 사용.