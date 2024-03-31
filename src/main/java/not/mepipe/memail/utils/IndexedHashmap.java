package not.mepipe.memail.utils;

import java.io.Serializable;
import java.util.ArrayList;

public class IndexedHashmap<K, V> implements Serializable {

    private final ArrayList<Integer> keys = new ArrayList<>();
    private final ArrayList<K> firsts = new ArrayList<>();
    private final ArrayList<V> seconds = new ArrayList<>();

    public void add(K first, V second) {
        keys.add(keys.size() + 1);
        firsts.add(first);
        seconds.add(second);
    }

    public K getFirst(int key) {
        return firsts.get(key);
    }

    public V getSecond(int key) {
        return seconds.get(key);
    }

    public void setSecond(int key, V value) {
        seconds.set(key, value);
    }

    public int size() {
        return keys.size();
    }

}
