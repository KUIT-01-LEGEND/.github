# 5장

- 스트림의 기능을 다루는 듯 싶다.

## 필터링

1. Predicate로 필터링 → boolean 값을 기반으로 필터링
- distinct() →  고유 요소(중복 없이) 필터링

### 슬라이싱

- 스트림의 요소를 조절 - predicate 활용
- takewhile() → predicate.test() 의 결과가 true인 스트림 요소를 만나면 나머지 요소를 버림
- dropWhile() → 위의 반대. 해당 요소까지의 값을 버림
    - 해당 메서드는 정렬된 스트림 값을 다룰 때 유용
- limit() → 정수개의 스트림 요소를  반환
- skip() → limit의 반대. n개의 나머지 요소를 반환

### 매핑

- 스트림의 요소를 기반으로 T를 만들어냄.
- map()

### 평면화

- 여러 스트림을 하나로 ex) [”asd”, “qwe”] 를 [”a”, “s”,“d”,“q”,“w”,“e”] 로 만들기
- map 만을 사용하면 Stream<String[ ]> 을 반환하게 된다.
- flatMap() →  여러 스트림을 하나로 만듦
    - 위 상황의 경우 Arrays.Stream() 을 인자로 사용하여 문자열 배열을 스트림으로 만들 수 있음.

### 검색

- allMatch, anyMatch, noneMatch, findFirst, findAny - 딱 봐도 대충 알겠죠?
- 쇼트 서킷 : 여러 표현식이 엮여 있을 때 특정 표현식 결과에 따라 값이 정해지는 경우 - 스트림에선 이를 활용해 스트림 탐색 길이를 줄임.
- 

## 리듀싱

- 쇼트 서킷과 반대의 개념으로 모든 스트림 요소를 방문하여 처리하는 연산
- reduce : Bi.. 연산 수행. 초깃값을 인자로 받거나 그렇지 않을 경우 Optional 반환

## 기본형 특화 스트림

- IntStream, LongStream… → 오토박싱으로 인한 성능 저하를 줄임
- mapToInt 등으로 변환 가능. boxed()로 복원 가능

## 스트림 만들기

- of() → 값으로 만들기, ofNullable()
- Files.lines(Path.get(filename)) → 파일의 한 행을 기반으로 스트림 생성

- iterate → 초깃값과 람다를 인자로 받아 값을 생성. 초깃값을 연속적으로 계산하여 무한스트림 생성 가능
- generate → supplier를 인수로 받아 새로운 값 생성
