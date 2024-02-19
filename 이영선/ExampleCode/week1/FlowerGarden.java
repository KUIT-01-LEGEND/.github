import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;

// 백준 2457번 : 공주님의 정원
public class FlowerGarden {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        int num = Integer.parseInt(br.readLine());

        Date[] dates = new Date[num];

        // 월, 일 입력 받아서 Date로 생성 후 배열에 저장
        for (int i = 0; i < num; i++) {
            st = new StringTokenizer(br.readLine(), " ");
            int start = Integer.parseInt(st.nextToken()) * 100 + Integer.parseInt(st.nextToken());
            int end = Integer.parseInt(st.nextToken()) * 100 + Integer.parseInt(st.nextToken());
            dates[i] = new Date(start, end);
        }


        // 조건 1. start 오름차순 // 조건 2. end 내림차순
        Arrays.sort(dates, new Comparator<Date>() {
            @Override
            public int compare(Date o1, Date o2) {
                if (o1.start == o2.start) {
                    return o2.end - o1.end;
                }
                return o1.start - o2.start;
            }
        });

        // 람다 표현식으로 변경
        Arrays.sort(dates, (Date d1, Date d2)
                -> d1.start == d2.start? d2.end-d1.end : d1.start-d2.start);

        // 메서드 참조를 사용하기 위해 Date 클래스에 compareTo 메서드 추가
        Arrays.sort(dates, Date::compareTo);

        // 꽃 개수 카운트 하기
        int cnt = 0;
        int beforeEnd = 301;
        int last = 1201;
        int index = 0;
        int nextEnd = 0;

        while (beforeEnd < last) {
            boolean exist = false;

            for (int i = index; i < num; i++) {
                // beforeEnd보다 다음 end값이 더 작으면 아웃 (처음 beforeEnd 값을 301로)
                if (dates[i].start > beforeEnd) {
                    break;
                }

                // 위의 조건을 만족하는 여러 꽃들 중 가장 늦게 지는 꽃을 찾아라!
                if (dates[i].end > nextEnd) {
                    exist = true;
                    nextEnd = dates[i].end;
                    index++;
                }
            }

            if (exist) {
                cnt++;
                beforeEnd = nextEnd;
            } else {
                break;
            }
        }

        if (beforeEnd < last) {
            System.out.println("0");
        } else {
            System.out.println(cnt);
        }
    }

}

class Date implements Comparable<Date> {
    int start, end;

    public Date(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public int compareTo(Date o) {
        if (this.start == o.start) {
            return o.end - this.end;
        }
        return this.start - o.start;
    }
}
