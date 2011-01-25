package com.greeneyes.tags.index;

import com.greeneyes.tags.Item;
import com.greeneyes.tags.Tag;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static junit.framework.Assert.assertEquals;

/**
 * Author alex at 20.01.11 10:16
 */
public class TagSearchIndexTest {
    @Before
    public void clear() {
        id = 1;
        tagsMap.clear();
        itemMap.clear();
    }


    @Test
    public void testSearchForItems() throws Exception {
        TagSearchIndex ss = new TagSearchIndex(Arrays.asList(
                item("item1", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
                item("item2",    2, 3, 4),
                item("item3",       3, 4, 5),
                item("item4",          4, 5, 6),
                item("item6",             5, 6, 7),
                item("item7",                6, 7, 8),
                item("item8",                   7, 8, 9),
                item("item9",                      8, 9, 10)
        ));
        List<Item> items = ss.searchForItems(Arrays.asList(tag(4), tag(5)));
        assertDone(items, "item1", "item3", "item4");
        items = ss.searchForItems(Arrays.asList(tag(9)));
        assertDone(items, "item1", "item8", "item9");

    }


    public void assertDone(List<Item> items, String... shouldHave) {
        Set<Item> set1 = new HashSet<Item>(items);
        Set<Item> set2 = new HashSet<Item>();
        for (String s : shouldHave) {
            set2.add(item(s));
        }
        assertEquals(set2, set1);
    }


    @Test
    public void testSearchForTags() throws Exception {
        TagSearchIndex ss = new TagSearchIndex(Arrays.asList(
                item("item1", 1, 2, 3),
                item("item2",    2, 3, 4),
                item("item3",       3, 4, 5),
                item("item4",          4, 5, 6),
                item("item6",             5, 6, 7),
                item("item7",                6, 7, 8),
                item("item8",                   7, 8, 9),
                item("item9",                      8, 9, 10)
        ));
        Map<Tag, Integer> map = ss.searchForTags(Arrays.asList(tag(2), tag(3)));
        Map<Tag, Integer> r = new HashMap<Tag, Integer>();
        r.put(tag(1), 1);
        r.put(tag(2), 2);
        r.put(tag(3), 2);
        r.put(tag(4), 1);
        assertEquals(r, map);
    }

    private Map<Integer, Tag> tagsMap = new HashMap<Integer, Tag>();
    private Map<String, Item> itemMap = new HashMap<String, Item>();

    private int id = 1;


    public Item item(String name, int... tags) {
        Item item = itemMap.get(name);
        if (item == null) {
            List<Tag> ttt = new ArrayList<Tag>();
            for (int tag : tags) {
                ttt.add(tag(tag));
            }
            item = new Item(id++, name, ttt);
            itemMap.put(name, item);
        }
        return item;
    }

    public Tag tag(int id) {
        Tag tag = tagsMap.get(id);
        if (tag == null) {
            tag = new Tag(id, "tag-"+id);
            tagsMap.put(id, tag);
        }
        return tag;
    }
}
