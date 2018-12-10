package com.asiainfo;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author king-pan
 * @date 2018/12/10
 * @Description ${DESCRIPTION}
 */
public class ListTest {


    @Test
    public void listTest(){

        List<String> result = new ArrayList<>(10);
        System.out.println(result.size());
    }
}
