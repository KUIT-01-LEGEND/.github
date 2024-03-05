package stream;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class StreamMain {
    final static List<Student> studentList = Arrays.asList(
            new Student("이동현", 10L),
            new Student("김병렬", 20L),
            new Student("임람다", 37L),
            new Student("김대한", 69L),
            new Student("김민국", 75L),
            new Student("임하늘", 80L),
            new Student("임바다", 40L),
            new Student("이강현", 95L),
            new Student("윤명주", 100L)
    );

    public static void main(String[] args) {
        streamAndParallel();
        midAndFinal();
        filteringAndSlicing();
        flatMap();
        reducing();

    }

    // -------- STREAM and PARALLEL-STREAM --------
    public static void streamAndParallel() {
        List<String> list = Arrays.asList("홍길동", "신용권", "김자바", "김병렬", "람다식");

        // 순차 처리
        list.stream().forEach(StreamMain::print);
        System.out.println();

        // 병렬 처리
        list.parallelStream().forEach(StreamMain::print);

        System.out.println();
        System.out.println();
    }

    // -------- 중간/최종 처리 --------
    public static void midAndFinal() {
        double avg = studentList.stream()
                // 중간 처리 (학생 객체를 점수로 매핑)
                .mapToLong(Student::getScore)
                // 최종 처리 (평균 점수)
                .average()
                .getAsDouble();

        System.out.println("평균 점수 = " + avg);
    }

    //filtering & slicing
    public static void filteringAndSlicing() {
        List<Student> slicedStudents = studentList.stream()
                .filter(s -> s.getScore() > 70L)
                .sorted(Comparator.comparing(Student::getScore))
                .skip(2)
                .toList();
    }

    public static void flatMap() {
        //flatMap 사용 전 -> List<String[]>으로 반환
        List<String> studentNames = studentList.stream()
                .map(Student::getName)
                .toList();
        List<String[]> list = studentNames.stream()
                .map(name -> name.split(""))
                .distinct()
                .toList();

        //flatMap 사용
        List<String> alphabetsInNames = studentNames.stream()
                .map(name -> name.split(""))
                .flatMap(Arrays::stream)
                .distinct()
                .toList();
        System.out.println("alphabetsInNames.toString() = " + alphabetsInNames.toString());
    }

    public static void reducing() {
        Integer reduce = studentList.stream()
                .map(s -> 1)
                .reduce(0, Integer::sum);
        System.out.println("reduce = " + reduce);
    }

    public static void print(String str) {
        System.out.println(str + ": " + Thread.currentThread().getName());
    }
}
