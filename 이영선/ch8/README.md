# 8장. 컬렉션 API 개선
## 8-1. 컬렉션 팩토리

- ArrayList의 팩토리 메서드 : `Arrays.asList(e1, e2, e3)`
    - 요소 개선/수정(set)은 가능하지만, 추가(add)는 안된다!

      (UnsupportedOperaionException)

- Set의 팩토리 메서드는 없다.

  대신, List를 인수로 받아서 HaseSet으로 만들어준다 : `new HashSet<>(Arrays.asList(e1, e2, e3))`

  아님 스트림 생성 → Set으로 변환 : `Stream.of(e1, e2, e3).collect(Collectors.toSet())`


### 컬렉션을 만드는 새로운 방법을 알아보자!

1. **리스트 List**
- 새로운 컬렉션 팩토리 메서드 : `List.of(e1, e2, e3)`
    - 요소 개선/수정, 추가 모두 불가능 (불변 객체를 만들 수 있다👍🏻)
    - 스트림보다 구현이 더 단순하여 데이터를 변환할 필요가 없을 때 사용 권장
- 스트림 API : `Stream.of(e1, e2, e3).collect(Collectors.toList())`

1. **집합 Set**
- 새로운 컬렉션 팩토리 메서드 : `Set.of(e1, e2, e3)`
- 스트림 API

1. **맵 Map**
- 새로운 컬렉션 팩토리 메서드 : `Map.of(key1, value1, key2, value2, key3, value3)`
- 키-값 쌍이 10개 이상인 경우 : `Map.ofEntries(entry(key1, value1), entry(key2, value2), entry(key3, value3))`

  Map.entry() : Map.Entry의 새로운 팩토리 메서드


## 8-2. List와 Set 처리

**`removeIf({프레디케이트})`** : 프레디케이트를 만족하는 컬렉션의 요소 제거 - List, Set

**replaceAll({UnaryOperator})`** : 리스트의 모든 요소를 바꿔준다 - List

UnaryOperator : T → T
(인수와 리턴값이 동일한 타입을 가진 함수형 인터페이스)

**`sort()`** : 리스트를 정렬 - List

## 8-3. Map 처리

Map 인터페이스에 새롭게 추가된 미폴트 메서드

1. **`forEach({BiConsumer})`**

   BiConsumer : 키, 값 → void

2. 정렬 메서드
    - **`Entry.comparingByValue`**
    - **`Entry.comparingByKey`**

   → 사용법 `tempMap.stream().sorted(Entry.comparingByValue)`

3. **`getOrDefault({찾는 키}, {기본값})`**

   : 요청하는 키가 없을 경우 자동으로 생성해주는 메서드 (NPE방지)

맵에 **키가 존재** → 키 반환  //  **존재X** → 기본값 반환

1. 계산 패턴

   : 키 유무에 따라 실행할 동작이 다를 때

- **`computeIfAbsent({찾는 키}, {키가 없을 때 실행할 동작})`** : ex. 정보 캐시할 때 사용

  맵에 키가 존재 → 계산X (계산할 필요 없음)

  키가 존재X → 키를 이용해 새 값을 계산 & 맵에 추가

- **`computeIfPresent({찾는 키}, {키가 있을 때 실행할 동작})`** : `computeIfAbsent()`과 반대

  맵에 키가 존재 → 새 값 계산 & 맵에 추가

  키가 존재X → 계산X

- **`compute()`** : 제공된 키로 값 계산 & 맵에 추가


1. 삭제 패턴 : **`remove({키}, {값})`**
- 기존 **`remove({키})`** : 키에 해당하는 맵 항목을 제거
- 새로운 **`remove({키}, {값})`** : 키가 특정 값과 연관되었을 때만 맵 항목을 제거

1. 교체 패턴
- **`replaceAll({BiFunction})`** : 모든 항목의 값을 바꾼다.

  BiFunction : 키, 값 → 리턴타입

- **`Replace({키}, {값})`** : 키가 존재하면 맵의 값을 바꾼다.


1. 합침
- **`putAll({추가할 맵})`** : 중복된 키가 있으면 안됨. (이때는 merge 사용)
- **`merge({키}, {값}, {BiFunction})`** :

  BiFunction : 같은 키가 맵에 이미 존재할 때, 어떻게 처리할 지를 정의


## 8-4. 개선된 ConcurrentHashMap

:동시성 친화된 HashMap 버전

1. 리듀스와 검색 → 객체, 값, 순서에 의존하지 않아야 하는 함수들!!
    - 키, 값으로 연산 : `forEach`, `reduce`, `search`
    - 키로 연산 : `forEachKey`, `reduceKeys`, `searchKeys`
    - 값으로 연산 : `forEachValue`, `reduceValues`, `searchValues`
    - Map.Entry 객체로 연산 : `forEachEntry`, `reduceEntries`, `searchEntries`
2. 개수

   `mappingCount` : 맵의 매핑 개수를 반환

3. 집합뷰

   `keySet` : map → set으로 바꿔주는 메서드