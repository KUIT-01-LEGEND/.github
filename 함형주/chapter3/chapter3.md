# chapter3

람다 표현식 : 메서드로 전달할 수 있는 익명 함수를 단순하게 표현한 것.  파라미터, 바디, 반환형식, 예외 리스트를 가질 수 있음.

람다를 활용하면 익명 클래스의 선언 등을 간단하게 정의할 수 있음.

```java
// 기존 코드
Comparator<Apple> byWeight = new Comparator<Apple>() {
	public int compare(Apple a1, Apple a2) {
		return a1,getWeight().compareTo(a2.getWeight());
	}
}；

// 람다 활용
Comparator<Apple> byWeight =
(Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight());
```

### 람다 표현식의 구성

- 파라미터 리스트 : 위 예제에서 `(Apple a1, Apple a2)` 에 해당
- 화살표 : 파라미터 리스트와 바디를 구분하는 역할
- 람다 바디 : 실행되는 코드. `a1.getWeight().compareTo(a2.getWeight())` 에 해당.  `{ }` 처럼 코드 블럭 안에 여러 줄의 바디를 구성할 수 있음. 이 때 리턴값이 존재하면 `return` 을 명시적으로 사용해야 함.

### 람다의  사용

- 함수형 인터페이스를 익명클래스로 생성하는 과정에서 사용. → 람다 표현식을 함수형 인터페이스의 인스턴스로 취급 가능

```java
Runnable r1 = () -＞ System.out.println("Hello World 1")；
```

- 람다는 파라미터 참조값 말고도 외부 자유변수도 바디에 사용이 가능하다. 하지만 지역 변수를 사용하기 위해선 final로 선언되어 있거나 실질적으로 final 명시된 것 처럼 값의 변화가 없어야 한다.

### 함수 디스크립터

- 시그니처 : 메서드의 형식을 지정 ex) `int add(int a, int b)`
- 함수 디스크립터 : 람다 표현식의 시그니처(함수형 인터페이스의 추상 메서드 시그니처)를 서술하는 메서드

### 람다 활용 : 실행 어라운드 패턴

- 실행 어라운드 패턴(Execute Around Pattern) : 디자인패턴 중 하나. 자원을 오픈하고 닫는 작업을 수행하는 코드를 감싸는 패턴. 코드 중복을 방지하고 자원 누수를 방지하기 위해 사용

```java
// ex) Execute Around Pattern 
connection.open();
logic();
connection.close();
```

- try-with-resources 구문으로 사용 가능

```java
public String processFile() throws IOException {
	try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
		return br.readLine(); < _| 실제 필요한 작업을 하는 행이다.
	}
}
```

1. 동작 파라미터화
- processFIle() 함수의 기능을 파라미터화
- 함수형 인터페이스 정의

```java
public interface BufferedReaderProcessor {
	String process(BufferedReader b) throws IOException;
}

public String processFile(BufferedReaderProcessor p) throws IOException {
	p.process();
}
```

- 람다식 사용

```java
String oneLine = processFile((BuffeedReader br) -> br.readLine());
String twoLines = processFile((BufferedReader br) -> br.readLine() + br.readLine());
```

### 함수형 인터페이스

함수형 인터페이스 | 함수 디스크립터

- Predicate<T> | T → boolean
- Consumer<T> | T -> void
- Function<T, R> | T -> R
- Supplier<T> () -> T

### 기본형 특화 함수형 인터페이스

- 함수형 인터페이스를 사용하면서 기본형(primitive type)을 사용할 일 이 생긴다. 하지만 제네릭 파리미터를 사용해야 하기에 필연적으로 박싱 언박싱이 필요하다. 성능 저하의 요소다. 때문에 자바에선 기본형 특화 함수형 인터페이스를 제공한다.
- IntPredicate, LongConsumer, IntFunction<R>, IntToDoubleFunction, 등이 있다.

### 형식 검사

1. filter 메서드의 선언을 확인한다.
2. filter 메서드는 두 번째 파라미터로 Predicate<Apple> 형식 (대상 형식)을 기대한다.
3. Predicate〈Apple>은 test라는 한 개의 추상 메서드를 정의하는 함수형 인터페이스다.
4. test 메서드는 Apple을 받아 boolean을 반환하는 함수 디스크립 터를 묘사한다.
5. filter 메서드로 전달된 인수는 이와 같은 요구사항을 만족해야 한다.

### 형식 추론

- 자바 컴파일러는 람다 표현식이 사용된 콘텍스트(대상 형식)를 이용해서 람다 표현식과 관련된 함수형 인터페이스를 추론한다

```java
// 형식 추론 x
List<Apple> greenApples = 
	filter(inventory, (Apple apple) -> GREEN.equals(apple.getColor()))；

// 형식 추론 o
List<Apple> greenApples = 
	filter(inventory, apple -> GREEN.equals(apple.getColor()))；
```

### 메서드 참조

```java
// 메서드 참조 활용식
inventory,sort(comparing(Apple：:getWeight));
```

- 가독성을 높일 수 있다. 그게 끝이다.
1. 정적 메서드 참조
예를 들어 Integer의 parselnt 메서드는 Integer::parselnt로 표현할 수 있다.
2. 다양한 형식의 인스턴스 메서드 참조
예를 들어 String의 length 메서드는 String::length로 표현할 수 있다.
3. 기존 객체의 인스턴스 메서드 참조
예를 들어 Transaction 객체를 할당받은 expensiveTransaction 지역 변수가 있고,
Transaction 객체에는 getValue 메서드가 있다면，이를 expensiveTransaction::getValue라고 표현할 수 있다.
4. 생성자 참조
Supplier<Apple> c1 = Apple::new; // 인수가 없는 경우
Function<Integer, Apple> c2 = Apple::new; // 인수 Integer 활용 

### 람다 표현식을 조합할 수 있는 메서드

- 정렬 시 Comparator.compare()에선 두개의 파라미터가 사용되었지만 정적 메서드 `Comparator.comparing()` 을 사용하여 인수를 줄일 수 있다. 
`Comparator<Apple> c = Comparator.comparing(Apple::getWeight);` 처럼 인수를 줄여 사용 가능
또한 역정렬 메서드 `reversed()` 를 사용하거나 `thenComparing()` 을 사용해 `Comparator` 를 연결할 수 있다.
- Predicate 도 비슷하게 `negate, and, or` 3개의 메서드를 활용해 반전, &&, || 등 활용할 수 있다.
- Function은 `andThen, compose` 두 개의 메서드를 제공한다.
f.andThen(g)  = g(f(x)), f.compose(g) = f(g(x))
