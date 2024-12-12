package com.security.springsecurity.test;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

public class test {
    public static void main(String[] args) {

//        Set<String> stringSet=new HashSet<>();
//        stringSet.add("tezera");
//        stringSet.add("demissie");
//        stringSet.add("seifu");
//        stringSet.add("nika");
//        stringSet.add("jabye");
//        stringSet.clear();
//        System.out.println(stringSet.contains("tezera"));

//        for(String names:stringSet){
//            System.out.println(names);
//        }

//        Iterator iterator=stringSet.iterator();
//        while(iterator.hasNext()){
//            System.out.println(iterator.next());
//        }

//     stringSet.forEach(()->println);

   HashMap<String,Integer> hashMap=new HashMap<>();
        hashMap.put("tezera",1234);
        hashMap.put("zinash",12345);
        hashMap.put("mihret",123456);
        System.out.println(hashMap);
        System.out.println(hashMap.get("tezera"));

        hashMap.remove("tezera");
        System.out.println(hashMap);





    }
}


