package com.greeneyes.tags.utils;

import java.util.*;

/**
 * Author alex at 19.01.11 15:36
 */
public class FastImmutableSet<T>  {
    private final T[] data;
    private final Comparator<T> comparator;


    /**
     * May use only sorted sets of not null data
     * @param data
     * @param comparator
     */
    public FastImmutableSet(T[] data, Comparator<T> comparator) {
        int global_sign = 0;
        T prev = null;
        for (T t : data) {
            if (t == null) {
                throw new IllegalArgumentException("One of element is null");
            }
            if (prev == null) {
                prev = t;
            } else {
                int local_sign = comparator.compare(prev, t);
                if (local_sign != 0) {
                    local_sign = local_sign > 0 ? 1 : -1;
                    if (global_sign != 0) {
                        if (local_sign * global_sign < 0) {
                            throw  new IllegalArgumentException("Data is not sorted");
                        }
                    } else {
                        global_sign = local_sign;
                    }
                }
            }
        }
        this.data = data;
        this.comparator = comparator;
    }


    public T[] data() {
        return data;
    }

    public int size() {
        return data.length;
    }

    public void intersect(FastImmutableSet<T> other, T[] base) {
        for (int i = 0; i < base.length; i++) {
            base[i] = null;
        }
        intersect(other, new ArrayBuffer<T>(base));
    }

    public void intersect(FastImmutableSet<T> other, Buffer<T> buffer) {
        T[] small;
        T[] large;
        if (this.size() > other.size()) {
            small = other.data;
            large = this.data;
        } else {
            small = this.data;
            large = other.data;
        }
        intersect(comparator, buffer, small, large, 0, small.length, 0, large.length);
    }


    private static <T> void intersect(Comparator<T> comparator, Buffer<T> buffer, T[] small, T[] large, int smallFrom, int sTo, int largeFrom, int lTo) {
        int sLength = sTo - smallFrom;
        int lLength = lTo - largeFrom;
        if (
                        small.length == 0    ||
                        large.length == 0    ||
                        sLength <= 0   ||
                        lLength <= 0   ||
                        (smallFrom < 0)          ||
                        (largeFrom < 0)          ||
                        (sTo > small.length) ||
                        (lTo > large.length)
                )
        {
            return;
        }


        //optimisation for small arrays 1x1, 1x2, 1x3
        if (sLength * lLength <= 3) {
            T sElement = small[smallFrom];
            for (int i = largeFrom; i < lTo; i++) {
                if (comparator.compare(sElement, large[i]) == 0) {
                    buffer.add(sElement);
                    return;
                }
            }
            return;
        }

        int positionInLarge = (largeFrom + lTo) /2;
        T elementInLarge = large[positionInLarge];


        int tmpPosition = Arrays.binarySearch(small, elementInLarge, comparator);

        int offset;
        int positionInSmall;

        if (tmpPosition > 0) {
            offset = 1;
            positionInSmall = tmpPosition;
            buffer.add(elementInLarge);
        } else {
            offset = 0;
            positionInSmall = Math.abs(tmpPosition + 1);
        }



        if (tmpPosition != -1) {
            T[] leftSmall;
            T[] leftLarge;

            int leftSmallFrom;
            int leftSmallTo;
            int leftLargeFrom;
            int leftLargeTo;


            if ((positionInSmall - smallFrom) < (positionInLarge - largeFrom)) {
                leftSmall = small;
                leftLarge = large;
                leftSmallFrom = smallFrom;
                leftSmallTo = positionInSmall;
                leftLargeFrom = largeFrom;
                leftLargeTo = positionInLarge;
            } else {
                leftSmall = large;
                leftLarge = small;
                leftSmallFrom = largeFrom;
                leftSmallTo = positionInLarge;
                leftLargeFrom = smallFrom;
                leftLargeTo = positionInSmall;
            }

            intersect(comparator, buffer, leftSmall, leftLarge, leftSmallFrom, leftSmallTo, leftLargeFrom, leftLargeTo);
        }



        if (tmpPosition != sTo) {
            T[] rightSmall;
            T[] rightLarge;

            int rightSmallFrom;
            int rightSmallTo;
            int rightLargeFrom;
            int rightLargeTo;

            if ((sTo - positionInSmall) < (largeFrom - positionInLarge)) {
                rightSmall = small;
                rightLarge = large;
                rightSmallFrom = positionInSmall + offset;
                rightSmallTo = sTo;
                rightLargeFrom = positionInLarge + 1;
                rightLargeTo = lTo;
            } else {
                rightSmall = large;
                rightLarge = small;
                rightSmallFrom = positionInLarge + 1;
                rightSmallTo = lTo;
                rightLargeFrom = positionInSmall + offset;
                rightLargeTo = sTo;
            }

            intersect(comparator, buffer, rightSmall, rightLarge, rightSmallFrom, rightSmallTo, rightLargeFrom, rightLargeTo);
        }
    }
}
