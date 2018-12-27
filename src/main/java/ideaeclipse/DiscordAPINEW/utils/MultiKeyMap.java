package ideaeclipse.DiscordAPINEW.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class is used to store data in a map with multiple keys
 *
 * @param <K1> Key value 1
 * @param <K2> Key value 2
 * @param <V>  value
 * @author Ideaeclipse
 */
public class MultiKeyMap<K1, K2, V> {
    private final Map<K1, V> k1VMap = new HashMap<>();
    private final Map<K2, V> k2VMap = new HashMap<>();

    /**
     * Inserts a row into both maps
     *
     * @param key1  key value 1
     * @param key2  key value 2
     * @param value value
     */
    public void put(final K1 key1, final K2 key2, final V value) {
        this.k1VMap.put(key1, value);
        this.k2VMap.put(key2, value);
    }

    /**
     * Returns data based on the first key value
     *
     * @param key key value
     * @return value
     */
    public V getByK1(final K1 key) {
        return this.k1VMap.get(key);
    }

    /**
     * Returns data based on second key value
     *
     * @param key key value
     * @return value
     */
    public V getByK2(final K2 key) {
        return this.k2VMap.get(key);
    }

    /**
     * Removes value from k1 map
     *
     * @param key value to remove
     */
    public void removeByK1(final K1 key) {
        V value = this.k1VMap.remove(key);
        List<Map.Entry<K2, V>> list = this.k2VMap.entrySet().stream().filter(o -> o.getValue().equals(value)).collect(Collectors.toList());
        if (!list.isEmpty()) {
            System.out.println("Removed from Map2: " + list.get(0).getKey());
            this.k2VMap.remove(list.get(0).getKey());
        }
        System.out.println(this.k1VMap);
        System.out.println(this.k2VMap);
    }

    /**
     * Removes value from k2 map
     *
     * @param key value to remove
     */
    public void removeByK2(final K2 key) {
        V value = this.k2VMap.remove(key);
        List<Map.Entry<K1, V>> list = this.k1VMap.entrySet().stream().filter(o -> o.getValue().equals(value)).collect(Collectors.toList());
        if (!list.isEmpty()) {
            System.out.println("Removed from Map1: " + list.get(0).getKey());
            this.k1VMap.remove(list.get(0).getKey());
        }
        System.out.println(this.k1VMap);
        System.out.println(this.k2VMap);
    }

    /**
     * @return map of key1 and value
     */
    public Map<K1, V> getK1VMap() {
        return this.k1VMap;
    }

    /**
     * @return map of key2 and value
     */
    public Map<K2, V> getK2VMap() {
        return this.k2VMap;
    }
}
