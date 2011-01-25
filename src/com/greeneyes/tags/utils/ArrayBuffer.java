package com.greeneyes.tags.utils;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Author alex at 19.01.11 15:52
 */
public class ArrayBuffer<T> implements Buffer<T>{
    private final T[] buffer;
    private final int from;
    private final int to;

    private int cursor;

    public ArrayBuffer(T[] bufferStub, int from, int to) {
        if (from < 0 || to > bufferStub.length || to < from) {
            throw new IllegalArgumentException("Can't create buffer. Check arguments");
        }
        this.from = from;
        this.to = to;

        for (int i = from; i < to; i++) {
            bufferStub[i] = null;
        }
        buffer = bufferStub;
        cursor = from - 1;
    }

    public ArrayBuffer(T[] buffer) {
        this(buffer, 0, buffer.length);
    }




    public int emptySize() {
        return to - cursor;
    }

    public int filledSize() {
        return cursor - from + 1;
    }

    public int size() {
        return to - from;
    }

    public ArrayBuffer<T> add(T d) {
        cursor++;
        buffer[cursor] = d;
        return this;
    }


    public Iterator<T> iterator() {
        return new ABIterator<T>(this, cursor);
    }


    private static class ABIterator<T> implements Iterator<T> {
        private final ArrayBuffer<T> buffer;
        private final int cursor;

        private int iterablePos;

        private ABIterator(ArrayBuffer<T> buffer, int cursor) {
            this.buffer = buffer;
            this.cursor = cursor;
            iterablePos = buffer.from - 1;
        }

        public boolean hasNext() {
            return (iterablePos+1) >= buffer.from && iterablePos < cursor;
        }

        public T next() {
            iterablePos++;
            return buffer.buffer[iterablePos];
        }

        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArrayBuffer that = (ArrayBuffer) o;

        if (cursor != that.cursor) return false;
        if (from != that.from) return false;
        if (to != that.to) return false;
        if (!Arrays.equals(buffer, that.buffer)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = buffer != null ? Arrays.hashCode(buffer) : 0;
        result = 31 * result + from;
        result = 31 * result + to;
        result = 31 * result + cursor;
        return result;
    }
}
