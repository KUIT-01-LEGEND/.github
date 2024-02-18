> > ### Quiz 3-2 함수형 인터페이스
> 다음 인터페이스중 함수형 인터페이스는 어느 것인가?
>
> public interface Adder { int add(int , a int b); }<br>
> public interface SmartAdder extends Adder { int add(double a, double b);}<br>
> public interface Nothing { }
>
> > 예상 : Adder<br>
> >
> > 정답 : Adder만<br>
> > 해설 : SmartAdder는 extends Adder에 의해 2개의 추상 add 메서드를 포함하므로 x <br> Nothing은 추상 메서드가 0개므로 x

<br><br>

> > ### Quiz 3-3 어디에 람다를 사용할 수 있는가?
>
> 다음 중 람다 표현식을 올바로 사용한 코드는?
> 1. execute(() -> {});
     public void **execute**(Runnable r) {
     r.run(); }
> 2. public Callable<String> fetch() {
     return () -> "Tricky example ;-)";}
> 3. Predicate<Apple> p= (Apple a) -> a.getWeight();
> > 해설
> > 1. 유효하다.
       Runnable의 추상 메서드 run의 시그니처와 일치한다. 다만, 람다 바디가 비어있으므로 아무 일도 일어나지 않는다.
> >
> > 2. 유효하다.
       fetch 의 반환 형식은 Callable<String>이다. Callable<String> 메서드의 시그니처는 `()->String` 이 된다.
> >
> > 3. 유효하지 않다.
       Predicate<Apple> 의 시그니처는 (Apple) -> boolean 으로 시그니처가 일치하지 않는다.

<br><br>


> > ### Quiz 3-6 메서드 참조
>
> 1. ToIntFunction<String> stringToInt = (String s) -> Integer.parseInt(s);
> 2. BiPredicate<List‹String>, String> contains = (list, element) -> list.contains (element);
> 3. Predicate‹String> startsWithNumber = (String string) -> this.startsWithNumber(string);
>
> > 정답  `=` 다음으로 들어갈 정답!
> >
> > 1. Integer::parseInt;
> > 2. List::contains;
> > 3. this::startsWithNumber