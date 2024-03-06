# chapter8

## 컬렉션 팩토리 메서드

- 컬렉션 인터페이스는 불변 컬렉션을 생성하는 팩토리 메서드를 제공
    - Arrays.asList() → 불변 사이즈의 리스트 생성.
        - Set 같은 경우 HashSet의 인자로 list를 넘겨 생성 가능
    - List.of(), Set.of(),…→ 불변 아이템, 불변 사이즈의 컬렉션
    - Map.of() → key, value, key value… 형태로 생성
    - Map..ofEntry() → Map.entry() 를 인자로 생성

## 리스트와 집합 처리

### List, Set

- removeIf : 프레디케이트를 만족하는 요소를 제거
    - 리스트를 for-each로 접근했을 때 내부적으로 iterator 를 사용한다. 즉 컬렉션에 대해 iterator가 컬렉션을 관리하므로 동시성 문제가 발생할 수 있다.
    
    ```java
    for (Transaction transaction : transactions) {
    	if(Character.isDigit(
    				transaction.getReferenceCode().charAt(O))) {
    		transactions.remove(transaction); // iterator와 동기화 문제
    	}
    }
    ```
    
    - removeIf 를 통해 동시성 문제 없이 사용 가능
- replaceAll : 리스트 전용. UnaryOperator 사용해서 요소 변경
- sort : 리스트 정렬

## Map 처리

- map을 for-each로 키와 밸류를 확인하는 작업은 비효율적
- BiConsumer 으로 키와 밸류를 받아 for-each 구문 수행 가능
    
    ```java
    map.forEach((key, value) -> System.out.println
    	(key+ " is " + value+ " value"));
    ```
    

### 정렬

- Entry.comparingByValue or .comparingByKey 로 정렬 가능
    
    ```java
    map.entrySet()
    	.stream()
    	.sorted(Entry.comparingByKey())
    	.forEachOrdered(System.out::printin);
    ```
    

### Null 값 핸들링

- 맵을 사용하다보면 존재하지 않는 key에 대해서 null 반환 → getOrDefault 메서드 활용 (Optional.orElse 랑 비슷)
    - 키가 존재하지 않으면 기본값 리턴.
    - 하지만 키에 해당하는 value가 null 인경우는 null 리턴

### 계산패턴

- 키의 존재 여부에 따라 동작 실행
- computelfAbsent : 제공된 키에 해당하는 값이 없으면(값이 없거나 널), 키를 이용해 새 값을 계산하고 맵에 추가한다.
- computelfPresent : 제공된 키가 존재하면 새 값을 계산하고 맵에 추가한다.
- compute : 제공된 키로 새 값을 계산하고 맵에 저장한다.
- replaceAll : BiFunction을 적용한 결과로 각 항목의 값을 교체한다
- Replace : 키가 존재하면 맵의 값을 바꾼다
- putAll : 중복된 키가 없다면 두 맵을 합친다.
- merge : 중복된 키에서 발생하는 충돌을 핸들링한다.
    
    ```java
    friends.forEach((k, v) ->
    	everyone.merge(k, v, 
    		(movie1, movie2) -> movie1 + " & " movie2)); 
    // 중복된 키의 경우 값을 연결하는 로직
    ```
    

## ConcurrentHashMap

- 동시성 친화적 map
    - forEach, reduce,
    - search : null이 아닌 값을 반환할 때 까지 함수 실행
- 키 값으로 연산(forEach, reduce, search)
- 키로 연산(forEachKey, reduceKeys, searchKeys)
- 값으로 연산(forEachValue. reduceValues, searchvalues)
- Map.Entry 객체로 연산(forEachEntry, reduceEntries, searchEntries)
- mappingCount : 매핑 개수 반환 (long)
