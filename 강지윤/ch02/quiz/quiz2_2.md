
### Quiz 2-2 익명 클래스 문제
- 다음 코드의 실행 결과는 4, 5, 6, 42 중 어느 것일까?

    ``` java
    public class MeaningOfThis {
    	public final int value = 4;
    	public void doIt(){
    		int value =6;
    		Runnable r = new Runnable() {
    			public final int value = 5;
    			public void run(){
    				int value = 10; 
    				System.out.printIn(this.value);
    			}
    		};
    		r.run(); 
    	}
    	public static void main(String...args) {
    		// QUIZ : 이 행의 출력 결과는?
    		MeaningOfThis m=new MeaningOfThis(); m.doIt(); 
    	}
    }
    ```
  
---

- 예상 : 5 <br>
> 정답 : 5 <br>
해설 : 코드에서 this는 MeaningOfThis가 아니라 Runnable을 참조하기 때문.
