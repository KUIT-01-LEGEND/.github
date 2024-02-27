# 6장

## 컬렉터

- 스트림 요소의 반환 정보를 지정 (리스트, 맵 등으로 반환)
- collect() → Collector 인터페이스를 인자로 받음. Collectors 유틸클래스 활용

## 요약

- maxBy, minBy, summingInt 등으로 객체 값을 계산
- joining → toString을 사용하여 문자열 연결
- reducing() → 3개의 인자를 사용
    1. 연산의 시작값 or 반환값 : 리듀싱 연산의 인수가 없으면 반환값
    2. 변환 함수
    3. BinaryOperator 

## 그룹화

- groupingBy : 맵 형식으로 반환
- 해당 메서드 인자로 supplier를 받아 그룹화 된 스트림을 조작할 수 있음
- 다수준 그룹화 가능 (다중 그룹화)

## 분할

- partitioningBy → predicate 를 사용하여 boolean 값을 기준으로 맵 분할

## Collector 인터페이스 구현

```java
public interface Collector<T, A, R> {
	Supplier<A> supplier();
	BiConsumer<A, T> accumulator();
	Function<A, R> finisher();
	BinaryOperator<A> combiner();
	Set<Characteristics> characteristics();
}
```

- T : 수집될 스트림 요소
- A : 누적자
- R : 결과 객체의 형식

- supplier() : empty 상태의 Supplier 반환
- accumulator() : 결과 컨테이너에 요소 추가
- finisher() : 누적자 객체를 최종 결과로 반환
- combiner() : 두 컨테이너 병합
- characteristics() : 컬렉터의 속성을 정의.
