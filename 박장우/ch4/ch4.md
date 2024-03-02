# 4장: 스트림

# 스트림

> `데이터 처리 연산`을 지원하도록 `소스`에서 추출된 `연속된 요소`
> 
- `연속된 요소`
    - 컬렉션과 비교했을 때 공통점: 스트림은 특정 요소로 이루어진 연속된 값의 인터페이스를 제공.
    - 차이점
        - 컬렉션: 시간, 공간 복잡도와 관련된 요소 저장 및 접근 연산. 주체가 데이터
        - 스트림: 표현 계산식 (filter, map, sorted). 주체가 계산
- `소스` : 스트림은 컬렉션, 배열, I/O 자원 등 데이터 제공 소스로부터 데이터를 소비.
    - 리스트로 스트림을 만들면 순서는 리스트의 순서 그대로.
- `데이터 처리 연산` : 함수형 프로그래밍 언어 연산과 매우 비슷.

### 특성

- 파이프라이닝: 대부분의 스트림 연산은 스트림 자신을 반환한다.
    - 스트림 연산끼리 연결해서 커다란 파이프라인을 구성할 수 있게 함.
    - SQL 쿼리와 비슷한 맥락
- 내부 반복

기존 자바 7 코드

```java
List<Dish> lowCaloricDishes = new ArrayList<>();
for(Dish dish: menu) {  // 누적자로 요소 필터링
		if(dish.getCalories() < 400) {
				lowCaloricDishes.add(dish);
		}
}

Collections.sort(lowCaloricDishes, new Comparator<Dish>() { // 익명 클래스로 정렬
		public int compare(Dish dish1, Dish dish2) {
				return Integer.compare(dish1.getCalories(), dish2.getCalories());
		}	
});

List<String> lowCaloricDishesName = new ArrayList<>();
for(Dish dish: lowCaloricDishes) {
		lowCaloricDishesName.add(dish.getName()); // 정렬된 리스트 처리
}
```

이 때 `lowCaloricDishes` 는 가비지 변수이다. 컨테이너 역할만 하는 중간 변수이다.

최신 자바8 코드

```java
List<String> lowCaloricDishesName = 
				menu.parallelStream()
						.filter(d -> d.getCalories() < 400)   // 400칼로리 이하의 요리 선택
						.sorted(comparing(Dish::getCalories)  // 칼로리로 요리 정렬
						.map(Dish::getName)                   // 요리명 추출
						.collect(tolist());                   // 모든 요리명을 리스트에 저장
```

`parallelStream` : 멀티코어에서 병렬적으로 처리 가능.

### 스트림 연산

`filter` : 람다를 인수로 받아 스트림에서 특정 요소를 제외시킨다.

`map` : 람다를 이용해서 한 요소를 다른 요소로 변환하거나 정보를 추출.

`limit` : 스트림 크기를 축소한다. ex) limit(3): 선착순 3개만 선택

`collect` : 스트림을 다른 형식으로 변환한다.

- `collect(toList())` : 스트림을 리스트로 변환해서 저장.

# 컬렉션 VS 스트림

> 데이터를 `언제` 계산하느냐의 차이.
> 
- 컬렉션: 현재 자료구조가 포함하는 **모든** 값을 메모리에 저장하는 자료구조.
    
    → 컬렉션의 모든 요소는 컬렉션에 추가하기 전에 계산되어야 한다.
    
    - 컬렉션에 요소를 추가하거나 요소를 삭제할 수 있다.
    - 삽입/삭제 연산이 일어날 때마다 모든 요소를 메모리에 저장해야 한다.
- 스트림: 요청할 때만 요소를 계산하는 고정된 자료구조.
    - 스트림에 요소를 추가하거나 삭제할 수 없다.
    
    → 생산자 - 소비자 관계.
    

### 스트림은 한번만 탐색 가능하다

탐색된 스트림의 요소는 소비된다. 한 번 탐색한 요소를 다시 탐색하려면 초기 데이터 소스에서 새로운 스트림을 만들어야 한다. 

- 이 때 데이터 소스가 I/O 채널이라면 소스를 반복해서 사용하지 못하므로 새로운 스트림을 만들 수 없다.

### 외부 반복과 내부 반복

- 외부 반복: 컬렉션으로 사용자가 직접 요소를 반복하기
    - while, for-each, Iterator 객체
- 내부 반복: 스트림으로 알아서 반복을 처리하고 결과를 어디엔가 저장.
    
    ![image](https://gist.github.com/assets/79884688/f7843bb3-7724-46ea-9394-ce0933b0473f)
    

## 스트림 연산

- 중간 연산: filter, map, limit 등 서로 연결되어 파이프라인을 형성
- 최종 연산: 파이프라인을 닫는 연산
    - collect, count, forEach

### 중간 연산

여러 중간 연산을 연결해 쿼리를 만들 수 있다.

- 단말 연산을 스트림 파이프라인에 실행하기 전까지는 아무 연산을 실행하지 않는다.
    - lazy
    - 쇼트 서킷: limit 연산으로 최적화
    - 루프 퓨전: 서로 다른 연산이지만 한 과정으로 병합.

### 최종 연산

스트림 파이프라인에서 결과를 도출.

- forEach: 각 요소에 람다를 적용하고 void를 반환.

```java
menu.stream().forEach(System.out::println);
```

![image]([https://gist.github.com/assets/79884688/0fe5b6f8-360b-40c8-83c5-540439686e8d](https://gist.github.com/assets/79884688/0fe5b6f8-360b-40c8-83c5-540439686e8d))