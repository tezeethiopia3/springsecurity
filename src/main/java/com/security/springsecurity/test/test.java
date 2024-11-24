package com.security.springsecurity.test;

import java.util.ArrayList;
import java.util.Spliterator;

public class test {
    public static void main(String[] args) {
        System.out.println("Hi Tezera are you fine=======");
        ArrayList<String> strings=new ArrayList<>();
        strings.add("one");
        strings.add("two");
        strings.add("three");
        for(int i=0;i<strings.size();i++){
            System.out.println(strings.get(i));
        }
        for(String x:strings){
            System.out.println(x);
        }

        System.out.println(strings.toString());
//        Spliterator<String> stringSpliterator=strings.spliterator();
//
//        while (stringSpliterator.tryAdvance((n)-> System.out.println(n)));

    }
}
