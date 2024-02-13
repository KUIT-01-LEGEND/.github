# chapter2

동작 파라미터화 : 실행 방식이 결정되지 않은 코드 블럭을 파라미터를 통해 전달 받아 실행되는 것. 즉 코드 내용의 선언을 외부로 위임하여 해당 메서드가 실행되는 시점에서 실행 방식을 결정.

### 예제 : 사과 필터링

1. 기본적인 필터링

```java
public static List<Apple> filterGreenApples(List<Apple> inventory) {
	List<Apple> result = new ArrayList<>(); 
	for (Apple apple: inventory) {
		if (GREEN.equals(apple.getColorO) {
			result.add(apple);
		}
	}
	return result;
}
```

- 가장 기본적인 형태다. 메서드 내부에서 필터링 방식(빨간 사과)을 선언하여 사용한다.
1. 파라미터화 

```java
public static List<Apple> filterApplesByColor(List<Apple> inventory, Color color) {
	List<Apple> result = new ArrayList<>();
		for (Apple apple: inventory) {
			if ( apple.getColor().equals(color) ) {
			result.add(apple);
		}
	}
	return result;
}
```

- 필터링 조건이었던 색상에 대한 정보를 파라미터로 전달 받아 사용한다. 색상에 대한 정보는 추상화 되었기에 보다 나은 코드가 되었지만 다른 조건에 대한 필터링 방식은 추상화되지 않았다.

```java
public static List<Apple> filterApplesByWeight(List<Apple> inventory, int weight) {
	List<Apple> result = new ArrayList<>();
		for (Apple apple: inventory) {
			if ( apple.getWeight() > weight ) {
			result.add(apple);
		}
	}
	return result;
}
```

- 결국 다른 조건에 대한 필터링을 하기 위해선 새로운 메서드를 정의해야 한다. 모든 조건을 파라미터화한다면, 새로운 요구사항 마다 마찬가지로 수정이 발생하고 많은 파라미터는 코드 가독성을 저해한다. 이러한 문제를 해결하기 위한 기법이 `동작 파라미터화`이다.
1. Predicate 인터페이스 정의
- Predicate : 참 또는 거짓을 반환하는 함수

```java
public interface ApplePredicate {
	boolean test (Apple apple);
}

public class AppleHeavyWeightPredicate implements ApplePredicate {
	public boolean test(Apple apple) {
		return apple.getWeight() > 150;
	}
}

public class AppleGreenColorPredicate implements ApplePredicate
	public boolean test(Apple apple) {
		return GREEN.equals(apple.getColor());
	}
}
```

- Predicate 인터페이스를 구현하고 구현체로 각 조건을 만족하면 true를 반환하도록 했다. 필터링 조건에 대한 부분을 Predicate 인터페이스로 추상화했으므로 필터링 메서드에서 `ApplePredicate` 를 파라미터로 주입 받아 사용할 수 있다.
1. 추상적 조건으로 필터링

```java
public static List<Apple> filterApples(List<Apple> inventory, ApplePredicate p) {
	List<Apple> result = new ArrayList<>();
	for(Apple apple: inventory) {
		if(p.test(apple)) {
			result.add(apple); 
		}
	}
	return result;
}
```

- 필터링 조건은 `ApplePredicate` 에 추상화 되어있기 때문에 해당 메서드는 필터링 조건과의 의존성은 사라졌다. 필터링 조건이 무엇인지 상관없이 해당 조건에 만족하는 사과만 List로 반환하면 그만이다. 즉 `ApplePredicate` 구현체의 변화와 상관없이 해당 메서드 고유의 역할만 수행하며 객체지향원칙인 DIP, OCP 등을 준수하게 되는 것이다.
- 하지만 필터링 조건이 추가되면 구현체를 새로 정의해주어야 한다. 마찬가지로 피곤한 일이다. 익명 클래스로 전환해보자.
1. 익명 클래스 활용

```java
new ApplePredicate() {
	public boolean test(Apple apple){ 
		return RED. equals(apple. getColor()); 
}
```

- 인터페이스 구현체를 정의하지 않고 코드 상에서 익명 클래스를 만들어 사용할 수 있다. 덕분에 모든 조건에 따른 구현체를 등록하는 수고를 덜었다. 하지만 그래도 여전히 의미없는 `public boolean test(Apple apple){ ...` 등의 코드는 남아있다.
1. 람다 표현식 사용

```java
List<Apple> result = filterApples(inventoryz (Apple apple) -> RED.equals(apple.getColor()));
```

- 익명 클래스를 람다 표현식으로 대체하는 것은 함수형 인터페이스에서 사용 가능하다. 함수형 인터페이스는 추상 메서드가 단 한 개인 인터페이스를 의미한다.

생각 정리

- 변경 가능성이 있는 내용을 외부로 위임하고 인터페이스로 추상화하여 사용한다는 개념은 동작 파라미터화와 OOP가 비슷하다. → 결국 객체지향
- 기존에는 2번처럼 단순히 값을 외부에서 주입하는 정도로 사용했었는데 코드 블럭을 추상화 한다는 개념은 새로웠다.
- 근데 이걸 내가 어디에 쓸지 떠오르지 않는다…
