[자세한 정리 노션 링크](https://ro-el.notion.site/ch3-8a5a9563c4dc40d3a1dc7f0661546f08?pvs=4)

<br>

# 람다란?
[람다 표현식](https://www.notion.so/960b24858fee41b899483241905bef9e?pvs=21)
> 메서드로 전달할 수 있는 익명 함수를 단순화한 것

### 예시
```java
Comparator<Apple> byWeight = new Comparator<Apple>() {
		public int compare(Apple a1, Apple a2) {
				return a1.getWeight().compareTo*a2.getWeight());
		}
};

//to

Comparator<Apple> byWeight = (Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight());
```

## 특징
- **익명**
    - 이름이 없음
- **함수**
    - 특정 클래스에 종속되지 않음
    - *but* 메서드처럼 파라미터 리스트, 바디, 반환 형식, 가능한 예외 리스트를 포함
- **전달**
    - 메서드 인수로 전달하거나 변수로 저장할 수 있음
- **간결성**
    - 자질구레한 코드 구현 불필요

## 구조
- **파라미터 리스트**: 메서드의 파라미터
- **화살표**: 리스트와 바디의 구분자
- **람다 바디**: 실제 로직, 람다의 반환값에 해당하는 표현식(return이 함축되어 있음)
    - void 리턴 가능
    - `{}` 여러 행의 문장 포함 가능

<br>
<details>
  <summary>expression & statement</summary>
    <div>
      <h4>expression: 표현식, 수식</h4>
      <p>- 하나의 값으로 표현<br>`(parameters) -> expression`</p>
      <h4> statement: 문장, 구문, 상태</h4>
      <p>- 작업을 수행하는 문장<br>`(paramaters) -> { statement; }`</p>
    </div>
</details>

<br>

## Quiz 3-1
앞에서 설명한 람다 규칙에 맞지 않는 람다 표현식을 고르시오.

**A. 4, 5**

1. () → {}
    - parameter X, void return
2. () → "Raoul"
    - parameter X, String return
3. () → { return "Mario"; }
    - parameter X, String return
4. (Integer i) → return "Alan" + i;
    - return은 흐름 제어문, return을 감싸는 {} 필요
    - ⇒ (Integer i) → { return "Alan" + i; }
5. (String s) → { "Iron Man"; }
    - “Iron Man”은 구문이 아닌 표현식, return을 명시하거나 {}를 생략해야 함
    - ⇒ (String s) → { return "Iron Man"; } 또는 (String s) → "Iron Man"
**정답: 4, 5**

<br>

---

# 어디에, 어떻게 사용할까?
## 함수형 인터페이스
> 정확히 하나의 추상 메서드를 지정하는 인터페이스
*ex. Comparator, Runnable, Callable*

람다 표현식으로 함수형 인터페이스의 추상 메서드 구현을 직접 전달 가능
→ 전체 표현식을 함수형 인터페이스의 인스턴스로 취급 가능

### [@FunctionalInterface 어노테이션](https://www.notion.so/FunctionalInterface-05bcac05d94b493aba6e9c4176eb3b2a?pvs=21)
함수형 인터페이스임을 가리키는 어노테이션으로, 컴파일 시점에 체킹 및 오류 반환

### Quiz 3-2
다음 인터페이스 중 함수형 인터페이스는 어느 것 인가?

**A. Adder**
```java
public interface Adder {
		int add(int ,a int b);
}

public interface SmartAdder extends Adder { //상속받은 add 메서드도 포함
		int add(double a, double b);
}

public interface Nothing { //추상 메서드가 없음
}
```
**정답: Adder**

<br>

## 함수 디스크립터

> 람다 표현식의 시그니처*(메소드의 이름과 매개변수 이름을 제외한 나머지)*를 서술하는 메서드

### 람다 표현식의 시그니처

> 받아들이는 매개변수의 타입과 개수, 반환하는 값의 타입에 대한 정의
매개변수 목록, 반환 타입, 람다 표현식의 형태를 명시적으로 설명
> 

ex. 람다 표현식 `s -> s.length() > 10`의 시그니처 = `(String) -> boolean`

<br>

### Quiz 3-3
다음 중 람다 표현식을 올바로 사용한 코드는?
**A. 1, 2**
1.
```java
execute(() -> {});
public void execute(Runnable r) {
		r.run();
}
```

2.
```java
public Calable<String> fetch() {
		return () -> "Tricky example ;-)";
}
```

3.
```java
Predicate‹Apple> p = (Apple a) -> a.getWeight();
```

**정답: 1, 2**
1. 람다 표현식의 시그니처가 `() → void`, Runnable 추상 메서드 run의 시그니처와 일치
2. 람다 표현식의 시그니처가 `() → String`, fetch()의 반환 형식이 Callable<String>이므로 시그니처 일치
3. 람다 표현식의 시그니처는 `(Apple) → String`이지만, Predicate의 test 메서드의 시그니처는 `(Apple) → boolean`으로 일치하지 않음. XX

<br>

---

# 함수형 인터페이스 사용
[표준 API의 함수적 인터페이스](https://www.notion.so/API-fea49d9e599c442fa99d66b7a704830f?pvs=21) 

| 종류 | 추상 메서드 특징 |
| --- | --- |
| Consumer<T> | 매개값: O / 리턴값: X<br>- void accept(T t);s |
| Function<T, R> | 매개값: O / 리턴값: O<br>- R apply(T t);<br>- 주로 매개값을 리턴값으로 매핑 (타입 변환) |
| Predicate<T> | 매개값: O / 리턴값: O (boolean)<br>- boolean test(T t);<br>- 매개값을 조사하여 true/false 반환 |

<br>

## 제네릭 파라미터에 참조형만 사용 가능
자바의 모든 형식은 참조형과 기본형에 해당하지만, 제네릭 파라미터에는 참조형만 사용할 수 있음

참조형: Byte, Integer, Object, List

기본형: int, double, byte, char

<br>

## 기본형 특화
### 박싱
**박싱 ↔ 언박싱**
> 박싱: 기본형을 참조형으로 변환
언박싱: 참조형을 기본형으로 변환

**오토박싱**
> 박싱과 언박싱이 자동으로 이루어지는 기능
```java
List<Integer> list = new ArrayList<>();
for (int i = 300; i < 400; i++) {
		list.add(i);
} //int -> Integer
```

**문제점: 비용 소모**
박싱한 값 = 기본형을 감싸는 래퍼, 힙에 저장

→ 메모리를 더 소비하여 기본형을 가져올 때도 메로리를 탐색하는 과정이 필요함

<br>

### 자바 8에서 제공하는 함수형 인터페이스
오토박싱 동작을 피할 수 있도록 함수형 인터페이스 제공

- `IntPredicate` 인터페이스 / 추상 메서드 : `boolean test(int t)` - 박싱 없음
- Predicate<Integer>는 박싱 O

<br>

### Quiz 3-4
다음과 같은 함수형 디스크립터(즉, 람다 표현식의 시그니처)가 있을 때 어떤 함수형 인터페이스를 사용할 수 있는가? [표 3-2]에서 대부분의 해답을 찾을 수 있다. 또한 이들 함수형 인터페이스에 사용할 수 있는 유효한 람다 표현식을 제시하시오.
1. T → R
    
    **A. Function**
    
2. (int, int) → int
    
    **A. IntBinaryOperator**
    
3. T → void
    
    **A. Consumer**
    
4. () → T
    
    **A. Supplier**
    
5. (T, U) → R
    
    **A. BiFunction**
    
<br>

## 예외, 람다, 함수형 인터페이스
함수형 인터페이스 → 확인된 예외를 던지는 동작 허용 X

예외를 선언하는 함수형 인터페이스를 직접 정의하거나, 람다를 try/catch 블럭으로 감싸기

<br>

---

# 형식 검사, 형식 추론, 제약
- 컴파일러가 람다의 형식을 검사하는 방법
- 피해야 하는 것
- 등

## 형식 검사
context → 추론 → type

### 대상 형식
> 어떤 컨텍스트에서 제공 것이라 기대되는 람다 표현식의 형식
*컨텍스트: 람다가 전달될 메서드 파라미터, 람다가 할당되는 변수 등*

### 형식 확인 과정
1. 람다가 사용된 컨텍스트는 무엇인가? → 메서드의 정의 확인
2. (파라미터를 통해) 대상 형식 파악
3. 대상 형식의 추상 메서드와 함수 디스크립터 파악
4. 람다의 시그니처와 일치하는지 파악
5. 검사 종료

**example**
```java
List<Apple> heavierThan150g = filter(inventory, (Apple apple) -> apple.getWeight() > 150);
```

1. 메서드의 선언 확인
2. 두 번째 파라미터로 `Predicate<Apple>` 형식(대상 형식)을 기대
    - `Predicate<Apple>`은 test라는 한 개의 추상 메서드를 정의하는 함수형 인터페이스
    - test 메서드 함수 디스크립터: Apple → boolean
3. 전달된 인수는 위와 같은 요구사항을 만족해야 함

<br>

### Quiz 3-5
다음 코드의 문제를 해결하시오.
**A.
람다 표현식의 시그니처가 `() → void`
대상 형식인 Object는 함수형 인터페이스가 아니므로 ()→void 형식의 함수 디스크립터를 가지는 대상 형식으로 바꿔야 함 = Runnable
`Runnable r = () → { System.out.println("Tricky example"); };`**

```java
Object o = () -> { System.out.println("Tricky example"); };
```
**정답:
또는 Runnable로 캐스팅 `(Runnable) () → { System.out.println("Tricky example"); };`**

같은 함수형 디스크립터를 가진 두 함수형 인터페이스를 갖는 메서드를 오버로딩할 때, 캐스팅 기법 활용 가능

*ex. Runnable과 Action의 함수 디스크립터가 동일함
execute(() → {})라는 람다 표현식이 둘 중 누구를 가리키는지 명확하지 않음
⇒ 캐스팅을 통해 호출 대상을 명확히 할 수 있음*

<br>

## 같은 람다, 다른 함수형 인터페이스
‘대상 형식’이라는 특징 때문에, 같은 람다 표현식이라도 다른 함수형 인터페이스로 사용될 수 있음

*ex. Callable, PrivilegedAction은 () → T의 함수를 정의함*

### void 호환 규칙
람다 바디에 일반 표현식이 있으면 void를 반환하는 함수 디스크립터와 호환됨 (파라미터 리스트도 호환되어야 함)
```java
// boolean 반환값
Predicate<String> p = s -> list.add(s);

// void 반환값
Consumer<String> b = s -> list.add(s);
```

<br>

## 형식 추론
대상 형식을 이용하여 람다 표현식과 관련된 함수형 인터페이스를 추론함

→ 함수 디스크립터를 알 수 있으므로 컴파일러는 람다의 시그니처도 추론할 수 있음

⇒ 컴파일러는 람다 파라미터 형식을 추론할 수 있음

```java
//형식 추론 X
Comparator<Apple> c = (Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight());

//형식 추론
Comparator<Apple> c = (a1, a2) -> a1.getWeight().compareTo(a2.getWeight());
```

<br>

## 지역 변수 사용
### 람다 캡처링
> 파라미터로 넘겨진 변수가 아닌 외부에서 정의된 변수인 자유 변수를 활용할 수 있음

### 제약
람다는 인스턴스 변수와 정적 변수를 자유롭게 캡처할 수 있지만, 지역 변수는 명시적으로 final 선언이 되어 있어야 함. 또는, 실질적으로 final로 선언된 변수와 똑같이 사용되어야 함

**why?**

인스턴스 변수는 힙에, 지역 변수는 스택에 위치

→ 람다에서 지역 변수에 바로 접근할 수 있다고 가정할 때,
람다가 스레드에서 실행됐을 때 변수를 할당한 스레드가 사라진다면?
변수 할당이 해제되었는데 람다가 여전히 변수에 접근하려고 함

→ 자바 구현에서) 원래 변수에 접근을 허용하는 것이 아닌 자유 지역 변수의 복사본을 제공
⇒ 따라서, 복사본의 값이 바뀌면 안 됨. 값을 한 번만 할당(실질적 final)해야 하는 제약이 생김

<br>

---

# 메서드 참조
```java
inventory.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));
//to
inventory.sort(comparing(Apple::getWeight());
```

## 요약
> 특정 메서드만을 호출하는 람다의 축약형

### 만드는 방법
**1. 정적 메서드 참조**

ex. `Integer::parseInt`

**2. 다양한 형식의 인스턴스 메서드 참조**

ex. `String::length`

**3. 기존 객체의 인스턴스 메서드 참조**

ex. Transaction 객체를 할당 받은 `expensiveTransaction` 지역 변수가 있고, Transaction 객체에 getValue 메서드가 있을 때, `expensiveTransaction::getValue`

<br>

### Quiz 3-6
다음의 람다 표현식과 일치하는 메서드 참조를 구현하시오.
```java
//1.
ToIntFunction<String> stringToInt = (String s) -> Integer.parseInt(s);
**//A.**
ToIntFunction<String> stringToInt = Integer::parseInt;

//2.
BiPredicate<List<String>, String> contains = (list, element) -> list.contains(element);
**//A.**
BiPredicate<List<String>, String> contains = List::contains;

//3.
Predicat<String> startsWithNumber = (String string) -> this.startsWithNumber(string);
**//A.**
Predicat<String> startsWithNumber = this::startsWithNumber;
```

<br>

## 생성자 참조
> `ClassName::new`

### 기본 생성자, Supplier<T>
> () → T

Supplier<Apple> a = Apple::new;

### 생성자, Function<T, R>
> T → R

무게를 인수로 Apple 생성
Function<Integer, Apple> a = Apple::new;

```java
List<Integer> weights = Arrays.asList(7, 3, 4, 10);
List<Apple> apples = map(weights, **Apple::new**);
public List<Apple> map(List<Integer> weights, **Function<Integer, Apple>** f) {
		List<Apple> result = new ArrayList<>();
		for (Integer weight : weights) {
				result.add(**f.apply(weight)**);
		}
		return result;
}
```

<br>

### Quiz 3-7
지금까지 인수가 없거나, 하나 또는 둘인 생성자를 생성자 참조로 바꾸는 방법을 살펴봤다. Color(int, int, int)처럼 인수가 세 개인 생성자의 생성자 참조를 사용하려면 어떻게 해야 할까?

A.
람다의 시그니처와 일치하는 디스크립터를 가지는 함수형 인터페이스를 생성한다.

```java
public interface TriFunction<T, U, R, V> {
		R apply(T t, U u, R r);
}
```

<br>

---

# 예제 코드
## stream `.map()` DTO mapping

Function이 대상 형식이며, 새로운 Stream을 반환

<img width="540" alt="Untitled 4" src="https://github.com/KUIT-01-LEGEND/modern-java-in-action/assets/96233738/f3b20d8a-bec7-4c5f-8197-ff29d6909595">

람다 표현식의 시그니처는 `T → R`으로, 매개 값으로 바디 내에서 새로운 객체를 생성하여 리턴 값으로 사용
함수형 인터페이스 Function의 함수 디스크립터와 일치

<img width="921" alt="Untitled 5" src="https://github.com/KUIT-01-LEGEND/modern-java-in-action/assets/96233738/6b821aeb-16cb-482e-a807-e5d6fb01b149">

<br>

### .sorted()에 메서드 참조
PlanMemberDTO에 정의한 getter 메서드만을 호출하므로 메서드 참조로 축약 가능

<img width="704" alt="Untitled 6" src="https://github.com/KUIT-01-LEGEND/modern-java-in-action/assets/96233738/4990a79c-865a-4e35-a5ad-94ba112fe3c5">

<br>

## orElseThrow
Supplier가 대상 형식이며, 확인된 오류를 throw 할 수 있는 메서드

<img width="770" alt="Untitled 7" src="https://github.com/KUIT-01-LEGEND/modern-java-in-action/assets/96233738/5e1ddbc4-566f-4147-9850-79a8876dba68">

람다 표현식의 시그니처는 `() → T(Exception)`로, 전달되는 파라미터가 없고 새로운 객체가 생성됨
함수형 인터페이스 Supplier의 디스크립터와 일치

<img width="649" alt="Untitled 8" src="https://github.com/KUIT-01-LEGEND/modern-java-in-action/assets/96233738/d830b94a-1e1f-4625-8858-1e723aebb2cf">
