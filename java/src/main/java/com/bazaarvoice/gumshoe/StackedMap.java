package com.bazaarvoice.gumshoe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * A stack of Maps that can be flattened into a single Map.  Key
 * collisions are handled so that a value higher in the stack is
 * used over a value lower in the stack.  Fundamental data structure
 * needed for GumShoe's context data association behavior.
 *
 * @author lance.woodson
 *
 * @param <K>
 * @param <V>
 */
public class StackedMap<K, V> implements Map<K, V> {
    Stack<Map<K, V>> stack;

    /**
     * Create a StackedMap with an empty Map at the bottom of the
     * stack
     */
    public StackedMap() {
        this(new HashMap<K, V>());
    }

    /**
     * Create a StackedMap with the specified Map at the bottom
     * of the stack.
     *
     * @param map
     */
    public StackedMap(Map<K, V> map) {
        stack = new Stack<Map<K,V>>();
        stack.push(map);
    }

    /**
     * Push the passed Map onto the stack
     *
     * @param map
     */
    public void push(Map<K, V> map) {
        stack.push(map);
    }

    /**
     * Push an empty Map onto the stack
     */
    public void push() {
        stack.push(new HashMap<K, V>());
    }

    /**
     * Pop the last map off the stack, or null if the stack is
     * empty.
     *
     * @return
     */
    public Map<K, V> pop() {
        try {
            return stack.pop();
        } catch (EmptyStackException e) {
            return null;
        }
    }

    /**
     * Pop all maps off the stack, leaving it empty.  Returns
     * a List of the Maps in LIFO order.
     *
     * @return
     */
    public List<Map<K, V>> popAll() {
        List<Map<K, V>> stackContents = new ArrayList<Map<K,V>>();
        Map<K, V> map;
        while((map = pop()) != null) {
            stackContents.add(map);
        }
        return stackContents;
    }

    /**
     * Flattens the stack into a single Map.  If there are multiple
     * values for the same key at different levels within the stack,
     * the value from the Map higher in the stack is used.  i.e. newer
     * values override older values.
     *
     * @return
     */
    public Map<K, V> flatten() {
        Map<K, V> result = new HashMap<K, V>();
        Iterator<Map<K, V>> iterator = stack.iterator();

        // Java's stack is a subclass of Vector, so iteration order
        // is FIFO, not LIFO
        while(iterator.hasNext()) {
            Map<K, V> layer = iterator.next();
            for(K key : layer.keySet()) {
                result.put(key, layer.get(key));
            }
        }

        return result;
    }

    public void clear() {
        popAll();
        push();
    }

    public boolean containsKey(Object key) {
        return flatten().containsKey(key);
    }

    public boolean containsValue(Object value) {
        return flatten().containsValue(value);
    }

    public Set<Map.Entry<K, V>> entrySet() {
        return flatten().entrySet();
    }

    @Override
    public boolean equals(Object obj) {
        return flatten().equals(obj);
    }

    public V get(Object key) {
        return flatten().get(key);
    }

    @Override
    public int hashCode() {
        return flatten().hashCode();
    }

    public boolean isEmpty() {
        return flatten().isEmpty();
    }

    public Set<K> keySet() {
        return flatten().keySet();
    }

    /**
     * Associate a value with the key in the map at the top of the stack
     */
    public V put(K key, V value) {
        ensureMapInStack();
        stack.peek().put(key, value);
        return value;
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        ensureMapInStack();
        stack.peek().putAll(m);
    }

    /**
     * Remove the value associated with the key in the map at the top of the
     * stack
     */
    public V remove(Object key) {
        V result = null;

        Iterator<Map<K, V>> iterator = stack.iterator();
        while(iterator.hasNext()) {
            Map<K, V> map = iterator.next();
            if (map.containsKey(key)) {
                result = map.remove(key);
            }
        }

        return result;
    }

    public int size() {
        return flatten().size();
    }

    public Collection<V> values() {
        return flatten().values();
    }

    private void ensureMapInStack() {
        if (stack.isEmpty()) {
            stack.push(new HashMap<K, V>());
        }
    }
}
