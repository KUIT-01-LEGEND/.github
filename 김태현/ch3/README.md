# 3. 람다 표현식

람다 표현식이 등장한 배경: 2장에서 설명해던 것과 같이, 동적 파라미터의 형식을 구현하기 위하여 장황한 코드 작성이 필요하였음.
메서드로 전달할 수 있는 익명 함수인 람다표현식을 통하여 장황한 코드를 간결하게 표현하여 코드의 가독성을 높일 수 있음.

람다표현식의 구조: 파라미터, 화살표, 바디

```java
(Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight());
// 람다 파라미터     화살표             람다 바디
// 또는 블록 스타일로
(parameters) -> {statements;}
```

함수형 인터페이스: 하나만의 추상 메서드를 지정하는 인터페이스

람다 표현식을 통하여 표현식을 함수형 인터페이스의 인스턴스로 취급 가능

함수 디스크립터: 람다 표현식의 시그니처를 서술하는 메서드
예시
```java
(Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight());
// (Apple, Apple) -> int 이 위 람다 표현식의 함수 디스크립터
```

람다 표현식은 실행 어라운드 패턴등에서 유용하게 사용 가능.

자바 8 라이브러리에서는 java.util.function 패키지를 만들어 다양한 함수형 인터페이스들을 제공하여 직접 함수형 인터페이스를 만들 필요없이 사용할수있게 해준다.
Predicate, Consumer, Function 등이 있다.자주 사용하니 외워두면 유용.

그러나 위와 같은 함수형 인터페이스들은 제너릭 파라미터를 이용하다 보니 참조형만 사용할 수 있어 기본형을 사용하려면 참조형으로 변환하는 박싱 과정이 필요하며,
이러한 사실은 성능의 하락할 수 있음. 이를 위해 특별한 함수형 인터페이스를 제공해줌. 보통은 기존 함수 인터페이스에서 앞에 기본형 타입을 붙여주면 됨.
예시: intPredicate, DoublePredicate 등과 같이

컴파일러가 람다 표현식을 검증하기 위해, 콘텍스트에 따라 기대되는 람다 표현식의 형식인 대상 형식을 확인하기 위해 컨텍스트를 통해 형식 확인 과정을 거침.
형식 추론을 통하여 코드를 더욱 간결하게 작성 가능.
```java
(Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight());
(a1, a2) -> a1.getWeight().compareTo(a2.getWeight());
// 위 아래 동일
```

람다 표현식은 외부 지역 변수를 참조 가능하나, 이를 변경하는 것은 불가능. 또한 외부 지역 변수는 final 형식을 가지고 있어야함.

### 메서드 참조
람다 표현식의 축약형. 더욱더 코드의 간결성을 증가시킴
정적 메서드, 다양한 형식의 인스턴스 메서드, 기존 객체의 인스턴스 메서드 모두 참조할수 있음.
사용 방법은 예시를 참고
```java
// (args)->ClassName.staticMethod(args)
// ClassName::staticMethod
// (arg0,rest)->arg0.instanceMethod(rest)
// ClassName::instanceMethod (arg0은 ClassName 형식)
// (args)->expr.instanceMethod(rest)
// expr::instanceMethod (arg0은 ClassName 형식)
```

메서드 참조를 ClassName::new와 같이 생성자 참조 등과 같이 이용 가능.

추가적으로 람다 표현식을 조합하면서 유용하게 활용하기 위해 몇몇 함수형 인터페이스들은 다양한 유틸리티 메서드를 제공.
함수형 인터페이스는 하나만의 추상 메서드를 가지면 되는데, 그렇다면 이 함수형 인터페이스가 제공하는 유틸리티 메서드는 무엇이냐, 
바로 디폴트 메서드에 해당한다.

해당하는 메서드들은 교재 참고.

## 결론 및 의견: 
결국 자바 8에서 등장한 람다 표현식과 메서드 참조는 코드의 가독성을 높이기 위해, 따라서 개발자들의 생산성을 높이기 위해 제작된 기능들이다.
기존 방식에서 람다 표현식을 이용하며 혁명적인 반복적인 코드 감소 및 가독성을 증가 시킬 수 있다. 상황에 맞는 사용이 아닌 무작정 남용한다면
오히려 코드의 가독성을 떨어 뜨릴수 있는 결과로도 이어질 수 있다고 생각함. 따라서 상황에 맞게 적절히 사용해보자.

## 예제 코드
```java
Thread thread = new Thread(new Runnable() {

    @Override
    public void run() {
          System.out.println("Start Thread");
          Thread.sleep(1000);
          System.out.println("End Thread");
   }
});
// 람다 표현식을 통해 가독성 업
Thread thread = new Thread(() -> {
          System.out.println("Start Thread");
          Thread.sleep(1000);
          System.out.println("End Thread");
});
```
