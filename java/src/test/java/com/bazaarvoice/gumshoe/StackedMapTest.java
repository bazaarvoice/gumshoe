package com.bazaarvoice.gumshoe;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class StackedMapTest extends Assert {
    private Map<String, String> bottom;
    private Map<String, String> top;
    private StackedMap<String, String> stackedMap;

    @BeforeMethod
    public void setUp() {
        bottom = new HashMap<String, String>();
        top = new HashMap<String, String>();
        stackedMap = new StackedMap<String, String>(bottom);
    }

    @Test
    public void ensureCreationWithMapPlacesItAtBottomOfStack() {
        assertEquals(stackedMap.stack.peek(), bottom);
    }

    @Test
    public void ensurePushPushesMapsOntoStack() {
        stackedMap.push(top);

        assertTrue(stackedMap.stack.contains(bottom));
        assertTrue(stackedMap.stack.contains(top));
    }

    @Test
    public void ensureNoArgPushPushesEmptyMapOntoStackk() {
        bottom.put("test", "value");

        stackedMap.push();

        assertEquals(stackedMap.stack.size(), 2);
        assertNotEquals(stackedMap.stack.peek(), bottom);
    }

    @Test
    public void ensurePopRemovesMapsInLIFOOrder() {
        stackedMap.push(top);

        assertEquals(stackedMap.pop(), top);
        assertEquals(stackedMap.pop(), bottom);
    }

    @Test
    public void ensurePopOfEmptyStackReturnsNull() {
        stackedMap.pop();

        assertEquals(stackedMap.pop(), null);
    }

    @Test
    public void ensurePopAllRemovesAllMapsFromStack() {
        stackedMap.push(top);

        List<Map<String, String>> results = stackedMap.popAll();

        assertEquals(stackedMap.stack.size(), 0);
        assertEquals(results.size(), 2);
        assertEquals(results.get(0), top);
        assertEquals(results.get(1), bottom);
    }

    @Test
    public void ensureFlattenWithEmptyStackReturnsEmptyMap() {
        stackedMap.popAll();

        assertEquals(stackedMap.flatten(), new HashMap<String, String>());
    }

    @Test
    public void ensureFlattenWithStackOfOneReturnsSameMap() {
        bottom.put("test", "value");

        assertEquals(stackedMap.flatten(), bottom);
    }

    @Test
    public void ensureFlattenWithStackOfManyMapsReturnsMergeOfMaps() {
        loadStackWithTwoMapsWithKeysAandB("1", "2");

        Map<String, String> expected = new HashMap<String, String>();
        expected.put("a", "1");
        expected.put("b", "2");

        assertEquals(stackedMap.flatten(), expected);
    }

    @Test
    public void ensureFlattenUsesNewerValuesOnKeyCollisions() {
        bottom.put("a", "1");
        top.put("a", "2");
        stackedMap.push(top);

        assertEquals(stackedMap.flatten(), top);
    }

    @Test
    public void ensureClearEmptiesStack() {
        loadStackWithTwoMapsWithKeysAandB("1", "2");

        stackedMap.clear();

        assertEquals(stackedMap.stack.size(), 1);
        assertEquals(stackedMap.stack.peek(), new HashMap<String, String>());
    }

    @Test
    public void ensureContainsKeyReturnsTrueIfKeyFoundInMapInStack() {
        loadStackWithTwoMapsWithKeysAandB("1", "2");

        assertTrue(stackedMap.containsKey("a"));
        assertTrue(stackedMap.containsKey("b"));
    }

    @Test
    public void ensureContainsKeyReturnsFalseIfKeyNotFoundInMapInStack() {
        loadStackWithTwoMapsWithKeysAandB("1", "2");

        assertFalse(stackedMap.containsKey("c"));
    }

    @Test
    public void ensureContainsValueReturnsTrueIfValueFoundInMapInStack() {
        loadStackWithTwoMapsWithKeysAandB("1", "2");

        assertTrue(stackedMap.containsValue("1"));
        assertTrue(stackedMap.containsValue("2"));
    }

    @Test
    public void ensureContainsValueReturnsFalseIfValueNotFoundInMapInStack() {
        loadStackWithTwoMapsWithKeysAandB("1", "2");

        assertFalse(stackedMap.containsValue("3"));
    }

    @Test
    public void ensureEntrySetInlcudesEntriesFromAllMapsInStack() {
        loadStackWithTwoMapsWithKeysAandB("1", "2");
        Set<String> keys = new HashSet<String>();
        Set<String> values = new HashSet<String>();

        Iterator<Map.Entry<String, String>> iterator = stackedMap.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }

        assertEquals(keys.size(), 2);
        assertTrue(keys.contains("a"));
        assertTrue(keys.contains("b"));

        assertEquals(values.size(), 2);
        assertTrue(values.contains("1"));
        assertTrue(values.contains("2"));
    }

    @Test
    public void ensureEqualsComparesFlattenedMap() {
        loadStackWithTwoMapsWithKeysAandB("1", "2");
        Map<String, String> flatMap = stackedMap.flatten();
        assertTrue(stackedMap.equals(flatMap));
    }

    @Test
    public void ensureGetPullsValueFromAnywhereInTheStack() {
        loadStackWithTwoMapsWithKeysAandB("1", "2");

        assertEquals(stackedMap.get("a"), "1");
        assertEquals(stackedMap.get("b"), "2");
    }

    @Test
    public void ensureHashCodeReturnsHashCodeOfFlattenedMap() {
        loadStackWithTwoMapsWithKeysAandB("1", "2");
        Map<String, String> flatMap = stackedMap.flatten();

        assertEquals(stackedMap.hashCode(), flatMap.hashCode());
    }

    @Test
    public void ensureIsEmptyReturnsFalseIfAnyMapsInStackHaveValues() {
        loadStackWithTwoMapsWithKeysAandB("1", "2");
        top.clear();

        assertFalse(stackedMap.isEmpty());
    }

    @Test
    public void ensureIsEmptyReturnsTrueIfAllMapsInStackAreEmpty(){
        loadStackWithTwoMapsWithKeysAandB("1", "2");
        bottom.clear();
        top.clear();

        assertTrue(stackedMap.isEmpty());
    }

    @Test
    public void ensureKeySetIncludesKeysFromAllMapsInStack() {
        loadStackWithTwoMapsWithKeysAandB("1", "2");
        Set<String> expected = new HashSet<String>();
        expected.add("a");
        expected.add("b");

        assertEquals(stackedMap.keySet(), expected);
    }

    @Test
    public void ensurePutAddsItemsToMapAtTopOfStack() {
        loadStackWithTwoMapsWithKeysAandB("1", "2");

        stackedMap.put("c", "3");

        assertEquals(stackedMap.stack.peek().get("c"), "3");
    }

    @Test
    public void ensurePutWorksWithEmptyStack() {
        stackedMap.stack.clear();

        stackedMap.put("a", "1");

        assertEquals(stackedMap.get("a"), "1");
    }

    @Test
    public void ensurePutAllMergesIntoMapAtTopOfStack() {
        loadStackWithTwoMapsWithKeysAandB("1", "2");
        Map<String, String> otherMap = new HashMap<String, String>();
        otherMap.put("c", "3");

        stackedMap.putAll(otherMap);

        assertEquals(stackedMap.stack.peek().get("c"), "3");
    }

    @Test
    public void ensurePutAllWorksWithEmptyStack() {
        stackedMap.stack.clear();
        Map<String, String> otherMap = new HashMap<String, String>();
        otherMap.put("c", "3");

        stackedMap.putAll(otherMap);

        assertEquals(stackedMap.stack.peek().get("c"), "3");
    }

    @Test
    public void ensureRemoveRemovesFromAllMapsInTheStack() {
        bottom.put("a", "1");
        top.put("a", "2");
        stackedMap.push(top);

        assertEquals(stackedMap.remove("a"), "2");
        assertNull(stackedMap.get("a"));
    }

    @Test
    public void ensureSizeReturnsSizeOfFlattenedMap() {
        loadStackWithTwoMapsWithKeysAandB("1", "2");

        assertEquals(stackedMap.size(), 2);
    }

    @Test
    public void ensureValuesContainsAllValues() {
        loadStackWithTwoMapsWithKeysAandB("1", "2");

        assertEquals(stackedMap.values().size(), 2);
        assertTrue(stackedMap.values().contains("1"));
        assertTrue(stackedMap.values().contains("2"));
    }

    private void loadStackWithTwoMapsWithKeysAandB(String a, String b) {
        bottom.put("a", a);
        top.put("b", b);
        stackedMap.push(top);
    }
}
