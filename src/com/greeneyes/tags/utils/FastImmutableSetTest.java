package com.greeneyes.tags.utils;

import org.junit.Test;

import java.util.*;

import static junit.framework.Assert.assertEquals;

/**
 * Author alex at 19.01.11 16:30
 */
public class FastImmutableSetTest {
    @Test public void testIntersect() throws Exception {
        ArrayBuffer<Integer> buffer = new ArrayBuffer<Integer>(new Integer[10]);
        FastImmutableSet<Integer> s1  = create(1, 2, 3, 4, 5, 6, 7, 8, 9 ,10);
        FastImmutableSet<Integer> s2  = create(1, 2, 3, 4, 5, 6, 7, 8, 9 ,10);
        s1.intersect(s2, buffer);
        assertDone(buffer, 1, 2, 3, 4, 5, 6, 7, 8, 9 ,10);
    }

    @Test public void testNotIntersect() throws Exception {
        ArrayBuffer<Integer> buffer = new ArrayBuffer<Integer>(new Integer[10]);
        FastImmutableSet<Integer> s1  = create(11, 21, 31, 41, 51, 61, 71, 81, 91 ,101);
        FastImmutableSet<Integer> s2  = create(10, 20, 30, 40, 50, 60, 70, 80, 90 ,100);
        s1.intersect(s2, buffer);
        assertDone(buffer);
    }


    @Test public void testIntersectBeginAndEnd() throws Exception {
        ArrayBuffer<Integer> buffer = new ArrayBuffer<Integer>(new Integer[10]);
        FastImmutableSet<Integer> s1  = create(10, 20, 31, 41, 51, 61, 71, 81, 91 ,100);
        FastImmutableSet<Integer> s2  = create(10, 20, 30, 40, 50, 60, 70, 80, 90 ,100);
        s1.intersect(s2, buffer);
        assertDone(buffer, 10, 20, 100);
    }


    @Test public void testIntersectBeginAndEndAndMiddle() throws Exception {
        ArrayBuffer<Integer> buffer = new ArrayBuffer<Integer>(new Integer[10]);
        FastImmutableSet<Integer> s1  = create(10, 20, 31, 41, 50, 61, 71, 81, 91 ,100);
        FastImmutableSet<Integer> s2  = create(10, 20, 30, 40, 50, 60, 70, 80, 90 ,100);
        s1.intersect(s2, buffer);
        assertDone(buffer, 10, 20, 50, 100);
    }


    @Test public void testEmptyListIntersection() {
        ArrayBuffer<Integer> buffer = new ArrayBuffer<Integer>(new Integer[10]);
        FastImmutableSet<Integer> s1  = create(10, 20, 31, 41, 50, 61, 71, 81, 91 ,100);
        FastImmutableSet<Integer> s2  = create();
        s1.intersect(s2, buffer);
        assertDone(buffer);
    }


    @Test public void testOneDiginvsList() {
        ArrayBuffer<Integer> buffer = new ArrayBuffer<Integer>(new Integer[10]);
        FastImmutableSet<Integer> s1  = create(10, 20, 31, 41, 50, 61, 71, 81, 91 ,100);
        FastImmutableSet<Integer> s2  = create(55);
        s1.intersect(s2, buffer);
        assertDone(buffer);
    }

    public static FastImmutableSet<Integer> create(Integer... d) {
        return new FastImmutableSet<Integer>(d, new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
    }


    public static void assertDone(ArrayBuffer<Integer> result, Integer... excepted) {
        Set<Integer> resultSet = new HashSet<Integer>();
        for (Integer integer : result) {
            resultSet.add(integer);
        }

        assertEquals(new HashSet(Arrays.asList(excepted)), resultSet);
    }






    public static void main(String[] args) {
        int min = 1000;
        int max = 10000;
        int testTimes = 10000;
        int testPercent = Math.max(testTimes / 1000, 1);


        FastImmutableSet<Integer> s1 = create(fillDownSet(min, 100000));
        FastImmutableSet<Integer> s2 = create(fillDownSet(max, 100000));

        Integer[] buffer = new Integer[min];

        double average = 0;
        System.out.println("Starting");
        for (int i = 0 ; i < testTimes; i++) {
            long startTime = System.currentTimeMillis();
            s1.intersect(s2, buffer);
            long time = System.currentTimeMillis() - startTime;
            average = (average *i + time) / ( i + 1);

            if (i%testPercent == 0) {
                System.out.println("%:>" + (i*100.0/testTimes));
            }
        }

        System.out.println("Average > " + average);

    }


    private static Integer[] fillDownSet(int amount, int maxTagId) {
        Set<Integer> set  = new HashSet<Integer>();
        while (set.size() < amount) {
            set.add(r.nextInt(maxTagId));
        }
        Integer[] data = set.toArray(new Integer[amount]);
        Arrays.sort(data);
        return data;
    }

    private static final Random r = new Random();
}
