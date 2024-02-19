package zzoni.ch03;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import static java.util.Comparator.comparing;

public class ch03ex {

    @FunctionalInterface
    interface TriFunction<T,U,V,R>{
        R apply(T t, U u, V v);
    }

    @Getter
    @AllArgsConstructor
    public static abstract class Person{
        String name;
        String major;
        abstract String info();
    }
    @Getter
    public static class Student extends Person {
        Integer age;
        public Student(Integer age, String name, String major) {
            super(name, major);
            this.age = age;
        }

        @Override
        public String info(){
            return "학생 "+name+"의 나이는 "+age+"이며, 전공은 "+major+" 입니다.";
        }
    }

    @Getter
    public static class Teacher extends Person{
        public Teacher(String name, String major) {
            super(name, major);
        }

        @Override
        public String info(){
            return name+" 교수의 나이는 비밀이며, 전공은 "+major+" 입니다.";
        }

        @Override
        public String toString() {
            return info();
        }
    }



    public static void main(String[] args){
        /**
         * 생성자 참조 활용
         */
        TriFunction<Integer, String, String, Student> student = Student::new;
        BiFunction<String, String, Teacher> teacher = Teacher::new;

        List<Student> students = Arrays.asList(
                student.apply(24, "강지윤", "컴퓨터공학부"),
                student.apply(24, "강지윧", "소프트웨어공학과"),
                student.apply(23, "강지율", "철학과")
        );

        List<Teacher> teachers = Arrays.asList(
                teacher.apply("진현욱", "철학과"),
                teacher.apply("김욱희", "컴퓨터공학부"),
                teacher.apply("유준범", "컴퓨터공학부")
        );

        /**
         * Stream.map 활용
         */
        // 1. 람다 표현식
        // stream의 map 메서드는 함수형 인터페이스인 Function을 필요로 함
        List<String> studentsInfo = students.stream()
                .map(student1 -> student1.info())
                .toList();

        // 2. 메서드 참조
        List<String> teachersInfo = teachers.stream()
                .map(Teacher::info)
                .toList();


        /**
         * ArrayList.sort 활용
         */
        ArrayList<Student> studentList = new ArrayList<>(students);
        ArrayList<Teacher> teacherList = new ArrayList<>(teachers);

        // 1. 코드 전달
        class PersonComparator implements Comparator<Person> {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.getName().compareTo(o2.getName());
            }
        }
        studentList.sort(new PersonComparator());
        teacherList.sort(new PersonComparator());

        // 2. 익명클래스
        studentList.sort(new Comparator<Person>() {
                @Override
                public int compare(Person o1, Person o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            }
        );
        teacherList.sort(new Comparator<Person>() {
                 @Override
                 public int compare(Person o1, Person o2) {
                     return o1.getName().compareTo(o2.getName());
                 }
             }
        );

        // 3. 람다 표현식
        studentList.sort((Person o1, Person o2) -> o1.getName().compareTo(o2.getName()));
        teacherList.sort((Person o1, Person o2) -> o1.getName().compareTo(o2.getName()));

        // 4. 메서드 참조
        studentList.sort(comparing(Person::getName));
        teacherList.sort(comparing(Person::getName));

        /**
         * Predicate 활용
         */

        Predicate<Person> CSEPerson = person -> person.getMajor().equals("컴퓨터공학부");
        Predicate<Person> CSEorSW = CSEPerson.or(person -> person.getMajor().equals("소프트웨어공학과"));

        List<Teacher> CSETeacher = teacherList.stream()
                .filter(CSEPerson)
                .toList();

        System.out.println(CSETeacher);
    }
}
