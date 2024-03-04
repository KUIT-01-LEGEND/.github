# 스트림이란?
> 자바 8 API에 새로 추가된 기능

## 선언형으로 컬렉션 데이터 처리 가능
### 기존 코드
```java
List<Dish> lowCaloricDishes = new ArrayList<>();
for (Dish dish : menu) {
		if (dish.getCalories() < 400) {
				lowCaloricDishes.add(dish);
		}
}
Collections.sort(lowCaloricDishes, new Comparator<Dish>() {
		public int compare(Dish dish1, Dish dish2) {
				return Integer.compare(dis1.getClories(), dish2.getCalories());
		}
});
List<String> lowCaloricDishesName = new ArrayList<>(); //컨테이너 역할의 가비지 변수
for (Dish dish : lowCaloricDishes) {
		lowCaloricDishesName.add(dish.getName());
}
```

### 자바 8 이후 코드
```java
List<String> lowCaloricDishesName = menu.stream()
			.filter(d -> d.getCalories() < 400)
			.sorted(comparing(Dish::getCalories))
			.map(Dish::getName)
			.collect(toList());
```

<br>

## 이점
- 선언형으로 코드 구현 가능
    - 변하는 요구사항에 쉽게 대응 가능
    - 필터링 코드 쉽게 구현 가능
- 조립 가능 → 유연성 증가
- 블럭 연산 연결 → 복잡한 데이터 처리 파이프라인 만들 수 있음

<br>

# 스트림 시작하기
> 데이터 처리 연산을 지원하도록 소스에서 추출된 연속된 요소, Sequence of elements
## 정의
1. 연속된 요소: 특정 요소 형식으로 이루어진 연속된 값 집합의 인터페이스 제공
    - filter, sorted, map과 같은 표현 계산식이 주를 이룸
    - 컬렉션의 주제: 데이터
    - 스트림의 주제: 계산
2. 소스: 데이터 제공 소스로부터 데이터를 소비
    - 정렬된 컬렉션으로 스트림 생성 → 정렬 유지
3. 데이터 처리 연산: 함수형 프로그래밍 언어에서 일반적으로 지원하는 연산과 데이터베이스와 비슷한 연산을 지원
    - 연산 순차/병렬 실행 모두 가능

### 두 가지 중요 특징
1. 파이프라이닝: 스트림 연산끼리 연결하여 커다란 파이프 라인 구성할 수 있도록 자신을 반환
    - 게으름(laziness), 쇼트서킷(short-circuiting) 같은 최적화 얻을 수 있음
    - 데이터베이스 질의와 유사
2. 내부 반복

<br>

# 스트림 vs 컬렉션
## 데이터 계산 시점
### 컬렉션
> 요소는 컬렉션에 추가되기 전 계산을 마쳐야 함

컬렉션에 요소를 추가/삭제할 때마다 컬렉션의 모든 요소를 메모리에 저장해야 하며, 컬렉션에 추가하려는 요소는 미리 계산되어야 함

### 스트림
> 요청할 때만 요소를 계산

스트림에 요소를 추가/제거 불가능

사용자가 요청하는 값만 추출 → 생산자와 소비자 관계 형성

<br>

## 탐색 횟수
### 스트림
> 반복자처럼 한 번만 탐색 가능 (소비형)

<br>

## 내/외부 반복
### 컬렉션 - 외부 반복
> 사용자가 직접 요소 반복해야 함

### 스트림 - 내부 반복
> 반복을 알아서 처리, 결과 스트림 값을 어딘가에 저장

수행해야 하는 작업을 지정만하면 알아서 처리됨

<br>

### Quiz 4-1
```java
List<String> highCaloricDishes = menu.stream()
				.filter(d -> d.getCalories() > 300)
				.map(Dish::getName)
				.collect(toList());
```

<br>

# 스트림 연산
## 종류
- 중간 연산: 연결할 수 있는 스트림 연산
- 최종 연산: 스트림을 닫는 연산

<br>

## 중간 연산
> 다른 스트림을 반환
⭐️ 단말 연산을 스트림 파이프라인에 실행하기 전까지 아무 연산도 수행하지 않음, 게으름

→ 합쳐진 중간 연산을 최종 연산으로 한 번에 처리

## 최종 연산
> 결과 도출

### 중간/최종 연산 종류
<img width="742" alt="Untitled" src="https://github.com/KUIT-01-LEGEND/modern-java-in-action/assets/96233738/3c6ebce8-fd17-4dd9-9342-10f50b73d5de">

<br>

## Quiz 4-2

중간 연산: filter, distinct, limit

최종 연산: count

<br>

# 스트림 이용
## 세 가지
- 질의 수행할 데이터 소스 *ex. 컬렉션*
- 스트림 파이프라인을 구성할 중간 연산 연결
- 스트림 파이프라인을 실행하고 결과를 만들 최종 연산

-> 빌더 패턴과 유사
