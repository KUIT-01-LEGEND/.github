### **동작 파라미터화**
: 아직은 어떻게 실행할 것인지 결정하지 않은 코드 블럭

→ 변화하는 요구사항에 유연하게 대응할 수 있도록 코드 작성 가능

<br>

# 변화하는 요구사항에 대응하기
## 𝟏 녹색 사과 필터링
모든 사과 각각에 대하여 녹색인지 검사하고 녹색인 사과만을 추출하는 코드를 작성할 수 있음

```java
public static List<Apple> filterGreenApples(List<Apple> inventory) {
		List<Apple> result = new ArrayList<>(); //녹색 사과 리스트
		for (Apple apple : inventory) {
				if (GREEN.equals(apple.getColor()) {
						result.add(apple);
				}
		}
		return result;
}
```

### 다른색 사과의 필터링 요구에 대한 적절한 대응 불가능
요구하는 색이 변화하면, `filterRedApples()`, `filterGrayApples()`와 같은 새로운 메서드 생성 필요

색상을 제외한 모든 코드가 중복됨 **→ 코드 추상화하기**

<br>

## 𝟐 색 파라미터화
색을 파라미터화하면, 반복되는 코드 없이 조금 더 유연한 대응이 가능

```java
public static List<Apple> filterApplesByColor(List<Apple> inventory, **Color color**) {
		List<Apple> result = new ArrayList<>(); //색 필터링 사과 리스트
		for (Apple apple : inventory) {
				if (**apple.getColor().equals(color)**) {
						result.add(apple);
				}
		}
		return result;
}
```

### 새로운 필터링, 무게
새로운 조건으로 필터링하고 싶다면, 위 코드와 유사하게 무게로 필터링하는 코드를 작성할 수 있음
하지만, 이 역시 색과 무게라는 큰 주제*(if 문)*만 다를 뿐 거의 대부분의 코드가 중복됨
> DRY, don’t repeat yourself 원칙 위배

<br>

## 𝟑 가능한 모든 속성으로 필터링 👎🏻

필터링 속성이 증가하면, 코드의 대부분이 중복되는 문제 발생

→ filter 메서드를 합치고 어떤 기준으로 필터링할 것인지에 대한 flag를 추가할 수 있음

*🚨 사용하지 말 것*

### 불필요한 코드 작성의 필요성 / 변화에 대응 불가능

50가지 필터링 조건이 있을 때, 단 한 가지 조건으로만 필터링 하고 싶을 때에도 50개의 모든 인수를 작성해줘야 하는 문제 발생

→ **어떤 기준으로 사과를 필터링할 것인지를 전달하는 방법 모색하기**

<br>

---

# 동작 파라미터화
## 필터링 조건에 따른 전략 캡슐화
### [전략 패턴이란? (클릭)](https://roel-yomojomo.tistory.com/20)

### 참/거짓을 반환하는 Predicate을 사용
ApplePredicate 인터페이스를 정의한 후, 원하는 필터링 조건에 대한 참/거짓을 반환하는 여러 구현 가능
<img width="694" alt="image" src="https://github.com/KUIT-01-LEGEND/modern-java-in-action/assets/96233738/bad73bfa-9f2d-43de-ae0d-eb4125a5dce6">

<br>

## 𝟒 추상적 조건으로 필터링
ApplePredicate 객체를 받아 필터링 조건을 검사하도록 filterApples() 메서드 수정

```java
xpublic static List<Apple> filterApples(List<Apple> inventory, **ApplePredicate predicate**) {
    List<Apple> result = new ArrayList<>();
    for (Apple apple : inventory) {
        if (predicate.test(apple)) {
            result.add(apple);
        }
    }
    return result;
    
    //**filterApples(inventory, new GreenColorPredicate());** 와 같이 사용 가능
}
```

### → ApplePredicate을 구현하는 여러 클래스 정의 후, 인스턴스화여 사용
번거로움..

<br>

### Quiz 2-1
[Code](https://github.com/ro-el-c/Java_Practice/tree/main/src/main/java/modern_java_in_action/quiz/ch2)

<br>

---

# 간소화
## 익명 클래스
> 이름이 없는 클래스 → 클래스 선언과 인스턴스화를 동시에

### 𝟓 filterApples()를 호출하는 시점에 ApplePredicate를 구현하는 익명 클래스 작성
많은 공간을 차지함

```java
filterApples(inventory, **new ApplePredicate() {
		public boolean test(Apple apple) {
				return apple.getColor().equals(RED);
		}
}**);
```

<br>

### Quiz 2-2
다음 코드를 실행한 결과는 4, 5, 6, 42 중 어느 것일까?
**A. 5**

```java
public class MeaningOfThis {
		public final int value = 4;
		public void doIt() {
				int value = 6;
				Runnable r = new Runnable() {
						public final int value = 5;
						public void run() {
								int value = 10;
								System.out.println(this.value);
						}
				};
				r.run();
		}

		public static void main(String[] args) {
				MeaninOfThis m = new MeaningOfThis();
				m.doIt(); // Q. 결과는?
		}
}
```

<br>

## 𝟔 람다 표현식 사용
코드를 훨씬 간결하게 구현 가능

```java
List<Apple> result = filterApples(inventory, (Apple apple) -> apple.getColor().equals(RED));
```

### 지금까지의 내용
<img width="686" alt="image" src="https://github.com/KUIT-01-LEGEND/modern-java-in-action/assets/96233738/8f004df2-632a-4434-8293-c73e1d96a40b">


<br>

## 모든 사물에 필터링 적용
필터링을 모든 물체에 적용하고 싶으면?

### 𝟕 리스트 형식으로 추상화
형식 파라미터*(T)*로 추상화 → 유연 & 간결

```java
public interface Predicate<T> {
		boolean test(T t);
}

public static <T> List<T> filter(List<T> list, Predicate<T> p) {
		List<T> result = new ArrayList<>();
		for(T e : list) {
				if(p.test(e)) {
						result.add(e);
				}
		}
		return result;
}

//사용 예제
List<Apple> redApples = filter(inventory, (Apple apple) -> RED.equals(apple.getColor()));
List<Integer> evenNumbers = filter(numbers, (Integer i) -> i % 2 == 0);
```
