package 강연주.ch2.genericlist;

import java.util.ArrayList;
import java.util.List;

public class FilterApple {

    public <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        final var result = new ArrayList<T>();
        for (T e : list){
            if (predicate.test(e)) {
                result.add(e);
            }
        }
        return result;
    }

}
