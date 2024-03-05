package stream;

class Student {
    String name;
    Long score;

    public Student(String name, Long score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public Long getScore() {
        return score;
    }
}
