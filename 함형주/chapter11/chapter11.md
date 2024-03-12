# 11장

## 왜 Optional 인가

### null의 문제

- 에러의 근원이다 : NullPointerException은 자바에서 가장 흔히 발생하는 에러다.
- 코드를 어지럽힌다 : 때로는 중첩된 null 확인 코드를 추가해야 하므로 null 때문에 코
드 가독성이 떨어진다.
- 아무 의미가 없다 : null은 아무 의미도 표현하지 않는다. 특히 정적 형식 언어에서 값이
없음을 표현하는 방법으로는 적절하지 않다.
- 자바 철학에 위배된다 : 자바는 개발자로부터 모든 포인터를 숨겼다. 하지만 예외가 있는
데 그것이 바로 null 포인터다.
- 형식 시스템에 구멍을 만든다 : null은 무형식이며 정보를 포함하고 있지 않으므로 모든
참조 형식에 null을 할당할 수 있다. 이런 식으로 null이 할당되기 시작하면서 시스템의
다른 부분으로 null이 퍼졌을 때 애초에 null이 어떤 의미로 사용되었는지 알 수 없다.

## Optional

- Otional은 선택형 값을 캡슐화하는 클래스 → 래퍼클래스랑 비슷하지만 존재하지 않을 수도 있는 객체를 표현하기 위해 사용

## 메서드

![image](https://github.com/KUIT-01-LEGEND/modern-java-in-action/assets/80512150/26b7dd69-f0c5-455c-9a25-8ff841907ad6)

### 팩토리 메서드

- of..() 로 시작.

### 추출 메서드

- get.. 이나 orElse.., map.. 으로 사용
- get은 무작정 값을 꺼내오므로 null 인 Optional 에서는 NoSuch… 예외 발생 → 객체의 존재가 보장되는 상황이 아니라면 사용 x. 애초에 get을 사용하면 Optional을 사용하는 이유가 없으므로 최대한 사용을 지양하자.
- orElse.. : get이랑 유사하지만 값이 없는 경우를 처리할 수 있음.
    - orElse() : 인자로 설정한 기본 값을 대신 반환
    - orElseGet() : 인자로 Supplier를 전달하여 해당 값을 반환
    - orElseThrow() : Supplier로 생성된 예외 발생

### Map

- map()을 사용하면 Function을 사용해 T를 통해 Optional로 포장된 R 값을 가져올 수 있다.
    
    ```java
    Optional<Insurance> optlnsurance = Optional.ofNullable(insurance);
    Optional<String> name = optlnsurance.map(Insurance::getName);
    ```
    
- 위 예제에서 만약 [Insurance.name](http://Insurance.name) 이 null 이라면, 빈 Optional을 반환한다.

### FlatMap

- 만약 map에서 반환하는 값이 이미 Optional로 포장되어 있는 경우는 flatMap으로 처리한다. (스트림과 같은 맥락)
- 위 경우, map으로 해당 값을 접근한다면 Optional<Optional<객체>> 형식이 되므로 이차원 Optional을 1차원으로 평탄화 해주는 작업이 필요하다.
- map과 flatMap 은 둘 다 Optional을 반환하므로, 메서드 체이닝 기업으로 핸들링 할 수 있다.
    
    ```java
    public String getCarInsuranceName(Optional<Person> person) {
    	return person.flatMap(Person::getCar)
    		.flatMap(Car::getlnsurance)
    		.map(Insurance::getName)
    		,orElse("Unknown");
    }
    ```
    
- flatMap으로 두 Optional 합치기
    
    ```java
    public Optional<Insurance> nullSafeFindCheapestInsurance
    			(Optional<Person> person, Optional<Car> car) {
    		return person.flatMap(p -> car.map(c -> findCheapestInsurance(p, c)));
    }
    ```
    
- 위 예제처럼 두 Optional을 처리할 때 언랩이나 조건문을 사용하지 않고 flatMap으로 하나의 Optional로 처리할 수 있다.
- 즉 Person과 Car이 null 이거나 두 객체에 대한 Insurance가 null이면 빈 optional을 반환

## Optional Stream

- Stream<Optional<객체>> 형태의 스트림을 편하게 다루기 위해 [Optional.stream](http://Optional.stream) 메서드를 제공한다.
    
    ```java
    public Set<String> getCarlnsuranceNames(List<Person> persons) {
    
    	****return persons.stream()
    		.map(Person::getCar)
    		.map(optCar -> optCar.flatMap(Car::getlnsurance))
    		.map(optlns -> optlns.map(Insurance::getName))
    		.flatMap(Optional::stream)
    		.collect(toSet())；
    }
    ```
    
- 위 예시처럼 Optional의 stream 메서드를 사용해서 값이 존재하는 Optional 에 대해서만 stream으로 반환할 수 있다.

## 참고 : Optional은 직렬화가 불가능하다.

- 직렬화 : 자바 상의 Object를 다른 시스템에서 사용할 수 있도록 연속적인 데이터(바이트 스트림 등)으로 변환하는 작업.(반대는 역직렬화) 주로 객체를 바이트 형태로 변환하여 디비나 파일 등에 저장하기 위해 사용.
- 자바에서 직렬화 하기 위한 객체는 Serializable 를 상속받아야 함. Serializable 은 단순 마커인터페이스로 아무 기능을 하지 않지만, 직렬화 api에서 Serializable 를 상속받지 않은 객체가 사용된다면 NotSerializableException  예외가 발생한다.
- Optional은 Serializable 인터페이스를 상속받지 않으므로 직렬화가 불가능하다.
- Optional의 최초 용도가 선택형 반환값을 지원하는 것이었기에 직렬화가 고려되지 않았다. (필드 형식으로 사용하는 것을 가정하지 않음)

> 아래 글과 링크의 차이…
> 

https://www.baeldung.com/java-optional-return

![image](https://github.com/KUIT-01-LEGEND/modern-java-in-action/assets/80512150/481ea7f1-f086-470e-8101-485ad40177e8)

- jpa를 사용하면 그냥 optional을 멀리하는게 좋을 지도..?
- ++ 추가로 스프링빈은 싱글톤으로 유지됨 → 상태를 가지는 필드를 가지면 안됨 → 그럼 여기도 Optional 필드를 허용하지 않는게 좋지 않을까?
- 스프링 프로젝트에서 http 통신이나 IO 작업 외에 어디에 사용할 수 있을까

---

- 기본형 특화 Optional도 있지만 stream 처럼 대량 연산을 하지 않기에 성능 향상을 기대할 수 없고 심지어 다른 기본형 Optional이나 심지어 기본 Optional과도 호환되지 않기에 사용하는 의미가 없다.

[https://homoefficio.github.io/2019/10/03/Java-Optional-바르게-쓰기/](https://homoefficio.github.io/2019/10/03/Java-Optional-%EB%B0%94%EB%A5%B4%EA%B2%8C-%EC%93%B0%EA%B8%B0/)

참고하도록 하자.
