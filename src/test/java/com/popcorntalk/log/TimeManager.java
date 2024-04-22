package com.popcorntalk.log;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class TimeManager {
    //시간 힘수
    static long beforeTime_EachTest;
    static long afterTime_EachTest;
    static int testnum = 1;
    //
    @BeforeEach
    void setBeforeTime_EachTest(){
        beforeTime_EachTest = System.currentTimeMillis();
    }
    @AfterEach
    void setAfterTime_EachTest(){
        afterTime_EachTest = System.currentTimeMillis();
        System.out.println("이번 실험 : " + testnum + " 번, 걸린 시간 : " + (afterTime_EachTest-beforeTime_EachTest));
        testnum++;
    }
}
