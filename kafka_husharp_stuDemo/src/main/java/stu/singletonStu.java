package stu;

import java.util.Collections;
import java.util.List;

public class singletonStu {
    public static void main(String[] args) {
        List<String> singletonList = Collections.singletonList("111");
        singletonList.add("222");
        System.out.println(singletonList);
    }
}
