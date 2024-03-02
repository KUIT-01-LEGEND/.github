# 3장: 람다 표현식

# 람다 표현식

> 익명 함수를 단순화한 것.
> 

### 특징

- 익명
- 함수
    - 특정 클래스에 종속되지 않음 (VS 메서드: 메서드는 클래스에 종속)
    - 파라미터 리스트, 바디, 반환 형식, 예외를 포함 (메서드와 공통)
- 전달
    - 람다 표현식을 메서드 인수로 전달하거나 변수로 저장 가능.
- 간결성

### 예시

```docker
// No lambda
Comparator<Apple> byWeight = new Comparator<Apple>() {
		public int compare(Apple a1, Apple a2) {
				return a1.getWeight().compareTo(a2.getWeight());
		}
}

// Use lambda
Comparator<Apple> byWeight =
		(Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight());
```

### 람다의 구성 요소

- `파라미터 리스트` : `(Apple a1, Apple a2)`
    - 파라미터가 없는 경우 : `() -> 42` 형태의 람다 가능.
- `화살표(->)` : 파라미터 리스트와 바디를 구분
- `람다 바디` : `a1.getWeight().compareTo(a2.getWeight());`

### 스타일

- 표현식 스타일 : `(parameters) -> expression`
- 블록 스타일 : `(parameters) -> { statements; }`

# 어디에, 어떻게 사용할 것인가?

람다를 함수형 인터페이스에서 사용할 수 있다.

## 함수형 인터페이스

> 정확히 하나의 추상 메서드를 지정하는 인터페이스.
> 

아무리 많은 디폴트 메서드가 존재하더라도 추상 메서드가 오직 하나면 함수형 인터페이스이다.

- 디폴트 메서드: 인터페이스의 메서드를 구현하지 않은 클래스를 고려해서 기본 구현을 제공하는 바디를 포함하는 메서드

람다 표현식으로 함수형 인터페이스의 추상 메서드 구현을 직접 전달할 수 있으므로 **전체 표현식을 인터페이스의 인스턴스로 취급**할 수 있다.

- Predicate<T> 는 함수형 인터페이스다.
- 자바 API의 함수형 인터페이스는 Comparator, Runnable 등이 있다.

```java
// lambda
Runnable r1 = () -> System.out.println("Hello 1");

// 익명 클래스
Runnable r2 = new Runnable() {
		public void run() {
				sout("Hello 2);
		}
}

public static void process(Runnable r){
		r.run()
}

process(r1);
process(r2);
process(() -> sout("Hello 3")); // 람다 직접 전달

```

### 함수 디스크립터

함수형 인터페이스의 추상 메서드를 람다로 구현하였다면, 둘의 표현식 시그니처는 같다. 람다 표현식의 시그니처를 서술하는 메서드를 함수 디스크립터라고 부른다.

- Comparator의 `compare` 메서드의 함수 디스크립터는 `(T, T) → int`

<aside>
💡 퀴즈3-3: 어디에 람다를 사용할 수 있는가?

다음 중 람다 표현식을 올바로 사용한 코드는?

```java
// 1. 
execute(() -> {});
public void execute(Runnable r) {
		r.run(); 
}

//2.
public Callable<String> fetch() { 
		return () -> "Tricky example ;-)";
}

//3. 
Predicate<Apple> p = (Apple a) -> a.getWeight();
```

- 정답
    - 1, 2번
    
    1. 람다 표현식`() →{}` 의 시그니처: `() -> void` 
        
        이는 Runnable의 추상 메서드 `run` 의 시그니처와 동일.
        
    2. `Callable<String>` 메서드의 시그니처 : `() -> String`
    3. Predicate의 test 메서드의 시그니처:
        
        `Predicate<Apple> : (Apple) -> boolean`
        
        3번의 람다 표현식의 시그니처와 일치하지 않음.
        
</aside>

### 실행 어라운드 패턴

실제 자원을 처리하는 코드를 설정과 정리 두 과정이 둘러싸는 형태를 가지는 코드

- 초기화/준비 코드 → 작업 수행 → 정리 마무리 코드

### Predicate

`(T) -> boolean` : 불리언 표현

```java
@FunctionalInterface
public interface Predicate<T> {
		boolean test(T t);
}

pubic <T> List<T> filter(List<T> list, Predicate<T> p) {
		List<T> results = new ArrayList<>();
		for(T t: list) {
				if(p.test(t)) {
						results.add(t);
				}
		}
		return results;
}

Predicate<String> nonEmptyStringPredicate = (String s) -> !s.isEmpty();

List<String> nonEmpty = filter(listOfStrings, nonEmptyStringPredicate);
```

### Consumer

`T -> void` : 객체에서 소비

```java
@FunctionalInterface
public interface Consumer<T> {
		void accept(T t);
}
```

### Function

`T -> R` : 입력을 출력으로 매핑하는 람다를 정의할 때 활용. 객체에서 선택/추출

```java
@FunctionalInterface
public interface Function<T, R> {
		R apply(T t);
}
public <T, R> List<R> map(List<T> list, Function<T, R> f) {
		List<R> result = new ArrayList<>();
		for(T t : list){
				result.add(f.apply(t));
		}
		return result;
}

List<Integer> l = map(
				Arrays.asList("lambdas", "in", "action"),
				(String s) -> s.length()
);
```

### Supplier

`() -> T` : 객체 생성

```java
@FunctionalInterface
public interface Supplier<T> {
	T get();
}
```

### 박싱과 언박싱

자바의 모든 형식은 참조형 아니면 기본형이다.

- 참조형: Byte, Integer, Object, List 등
- 기본형: int, double, byte, char

제네릭 파라미터에는 참조형만 사용할 수 있다.(Consumer<T> 의 T)

- 박싱 : 자바가 기본적으로 기본형 → 참조형으로 변환하는 과정
- 언박싱: 참조형 → 기본형
- 오토박싱: 박싱과 언박싱이 자동
    - 비용 소모

따라서 오토박싱에 들어가는 연산이 필요 없게 하는 함수형 인터페이스가 존재.

`IntPredicate`, `DoubleConsumer` 등.

- `IntPredicate` 과 `Predicate<Integer>` 의 차이.

## 메서드 참조

> 기존의 메서드 정의를 재활용해서 람다처럼 전달하기 → 가독성 증가.
> 

특정 메서드만을 호출하는 람다의 축약형.

```java
// Before
inventory.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight());

// After
inventory.sort(comparing(Apple::getWeight));
```

메서드명 앞에 구분자 `::` 를 붙이는 방식으로 메서드 참조를 화룡한다.

`Apple::getWeight` : Apple 클래스에 정의된 `getWeight` 의 메서드 참조.

- 실제로 메서드를 호출하는게 아니므로 괄호는 필요 X
- 람다 표현식 `(Apple a) -> a.getWeight()` 를 축약한 것.

### 메서드 참조를 만들기

1. 정적 메서드 참조
    
    `Integer::parseInt` 
    
2. 다양한 형식의 인스턴스 메서드 참조
    
    `String::length` 
    
    `(String s) -> s.toUpperCase()` → `String::toUpperCase`
    
3. 기존 객체의 인스턴스 메서드 참조
    
    ```java
    Transaction expensiveTransaction = new Transaction();
    
    // 람다 식
    () -> expensiveTransaction.getValue()
    
    // 축약형
    expensiveTransaction::getValue
    ```
    

### 메서드 참조 활용

```java
// 비공개 헬퍼 메서드
private boolean isValidName(String str) {
		return Character.isUpperCase(string.charAt(0));
}

// Predicate<String> 이 필요한 경우에 활용
filter(words, this::isValidName)
```

## 생성자 참조

클래스명과 new 키워드를 이용하여 생성자의 참조를 만들 수 있다.

`ClassName::new`

# 예제

프로젝트에서 사용한 코드. 

### orElseThrow의 인자로 람다 사용하기

JPARepository에서 기본적으로 제공하는 `findById` 는 `Optional`를 리턴한다. 이 때 Optional의 `orElseThrow` 는 Supplier를 받아 값이 존재하지 않을 경우 Throw할 대상을 Supplier로부터 제공받는다. 

```java
Address newAddress1 = 
		addressRepository.findById(request.getAddressId())
					.orElseThrow(**() -> new NotFoundAddressException()**);

Address newAddress2 = 
		addressRepository.findById(request.getAddressId())
					.orElseThrow(**NotFoundAddressException::new**);
```

`NotFoundAddressException` 의 생성자를 람다로 표현한 식을 생성자 참조로 축약할 수 있다. 

다음은 Optional의 `orElseThrow` 의 정의. 매개변수로 Supplier를 받는다.

```java
public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
    if (value != null) {
        return value;
    } else {
        throw exceptionSupplier.get();
    }
}
```