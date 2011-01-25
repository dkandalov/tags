package com.greeneyes.tags.utils;

/**
 * Author alex at 20.01.11 9:13
 */
public interface Buffer<T> extends Iterable<T>{
    public Buffer<T> add(T element);
    public int size();
    public int filledSize();
    public int emptySize();
}
