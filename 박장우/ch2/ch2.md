# 2장: 동작 파라미터화

# 동작 파라미터화 코드 전달하기

자주 바뀌는 요구사항에 대응하기 위한 방법. 동작 파라미터화에서는 메서드 내부적으로 다양한 동작을 수행할 수 있도록 코드를 메서드 인수로 전달한다.

### 정의

동작 파라미터화란 아직은 어떻게 실행할 것인지 결정하지 않은 코드 블럭을 의미한다.

- 코드 블럭의 실행은 나중으로 미뤄진다.

## 동작 파라미터화 방법

필터링 과정에서 객체의 어떤 속성에 기초해서 Boolean 값을 반환하는 방법을 채용한다.

- 사과가 녹색인가? (다른 색상도)
- 사과가 150그램 이상인가?

### 클래스 기반

1. 선택 조건을 결정하는 인터페이스 정의
    
    참 거짓을 반환하는 함수를 **`Predicate`** 라고 한다. 이를 인터페이스로 정의한다.
    
    ```java
    public interface ApplePredicate {
    		boolean test(Apple apple);
    }
    ```
    
2. 속성에 따라 구현체를 작성
    
    ```java
    static class AppleWeightPredicate implements ApplePredicate {
    
        @Override
        public boolean test(Apple apple) {
            return apple.getWeight() > 150;
        }
    
    }
    
    static class AppleColorPredicate implements ApplePredicate {
    
        @Override
        public boolean test(Apple apple) {
            return apple.getColor() == Color.GREEN;
        }
    
    }
    
    static class AppleRedAndHeavyPredicate implements ApplePredicate {
    
        @Override
        public boolean test(Apple apple) {
            return apple.getColor() == Color.RED && apple.getWeight() > 150;
        }
    
    }
    ```
    
    다양한 선택 조건을 대표하는 여러 버전의 ApplePredicate를 정의한다.
    
    - 이를 `전략 디자인 패턴` 이라고 한다.
        - 각 전략(알고리즘)을 **캡슐화**하는 알고리즘 패밀리를 정의해둔 다음 런타임에 알고리즘을 선택하는 기법.
        - 알고리즘 패밀리 - ApplePredicate
        - 전략 - AppleHeavyWeightPredicate 등
3. 필터 메서드 정의 후 사용.
    
    ```java
    public static List<Apple> filterApples(List<Apple> inventory, ApplePredicate p){
    		
    		List<Apple> result = new ArrayList<>();
    		for (Apple apple: inventory) {
    				if(p.test(apple)) {
    						result.add(apple);
    				}
    		}
    		return result;
    }
    
    List<Apple> greenApples = filterApples(inventory, new AppleGreenColorPredicate());
    ```
    

메서드는 객체만 인수로 받으므로 `test` 메서드를 `ApplePredicate` 로 감싸서 전달해야 한다.

### 익명 클래스 기반

`익명 클래스` : 즉석에서 필요한 구현을 만들어서 사용하는 개념.

```java
List<Apple> redApples = filterApples(inventory, new ApplePredicate() {
		public boolean test(Apple apple) {
				return "red".equals(apple.getColor());
		}
});

```

~~코틀린 마렵다~~

### 람다 기반

```java
List<Apple> result = filterApples(inventory, 
		(Apple apple) -> "red".equals(apple.getColor()));
```

![Untitled](2%E1%84%8C%E1%85%A1%E1%86%BC%20%E1%84%83%E1%85%A9%E1%86%BC%E1%84%8C%E1%85%A1%E1%86%A8%20%E1%84%91%E1%85%A1%E1%84%85%E1%85%A1%E1%84%86%E1%85%B5%E1%84%90%E1%85%A5%E1%84%92%E1%85%AA%203e71fcd6df734871a120bdb61d6992eb/Untitled.png)

## 추상화

Predicate도 추상화하면 Apple 뿐만 아니라 바나나, 오렌지, 정수, 문자열 등의 리스트에 필터 메서드를 적용 가능.

```java
public interface Predicate<T> {
		boolean test(T t);
}

public static <T> List<T> filter(List<T> list, Predicate<T> p) {
		List <T> result = new ArrayList<>();
		for(T e: list){
				if(p.test(e)){
						result.add(e);
				}
		}
		return result;
}
```

## 실전 예제

- Comparator로 정렬
- Runnable로 코드 블럭 실행
- Callable을 결과로 반환
- GUI 이벤트 처리

모두 람다로 처리 가능하다.

## 느낀점

개발자가 해야 할 일:  중복되는 코드 없애기