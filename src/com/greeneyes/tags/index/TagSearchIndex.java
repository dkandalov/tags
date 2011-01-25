package com.greeneyes.tags.index;

import com.greeneyes.tags.Item;
import com.greeneyes.tags.Tag;
import com.greeneyes.tags.utils.ArrayBuffer;
import com.greeneyes.tags.utils.Buffer;
import com.greeneyes.tags.utils.FastImmutableSet;

import java.util.*;

/**
 * Author alex at 20.01.11 9:04
 */
public class TagSearchIndex implements TagIndex {

    private final Map<Tag, FastImmutableSet<Item>> index;

    public TagSearchIndex(List<Item> itemsInIndex) {
        index = new HashMap<Tag, FastImmutableSet<Item>>();
        Map<Tag, List<Item>> tmpTagList = new HashMap<Tag, List<Item>>();
        for (Item item : itemsInIndex) {
            for (Tag tag : item.getTags()) {
                List<Item> p = tmpTagList.get(tag);
                if (p == null) {
                    p = new ArrayList<Item>();
                    tmpTagList.put(tag, p);
                }
                p.add(item);
            }
        }

        for (Map.Entry<Tag, List<Item>> entry : tmpTagList.entrySet()) {
            Tag tag = entry.getKey();
            List<Item> items = entry.getValue();
            Collections.sort(items, comparator);
            index.put(tag, new FastImmutableSet<Item>(items.toArray(new Item[items.size()]), comparator));
        }
    }

    public List<Item> searchForItems(List<Tag> tags) {
        List<FastImmutableSet<Item>> setsToSearch = new ArrayList<FastImmutableSet<Item>>(tags.size());
        for (Tag searchTag : tags) {
            FastImmutableSet<Item> set = index.get(searchTag);
            if (set == null) {
                //no results: this tag doesn't exist
                return Collections.emptyList();
            }
            setsToSearch.add(set);
        }

        Collections.sort(setsToSearch, new Comparator<FastImmutableSet<Item>>() {
            public int compare(FastImmutableSet<Item> o1, FastImmutableSet<Item> o2) {
                Integer sizeO1 = o1.size();
                return sizeO1.compareTo(o2.size());
            }
        });

        List<FastImmutableSet<Item>> last = search(new LinkedList<FastImmutableSet<Item>>(setsToSearch));

        if (last.size() == 0 || last.get(0).size() == 0) {
            return Collections.emptyList();
        }

        List<Item> result = new ArrayList<Item>(last.get(0).size());

        result.addAll(Arrays.asList(last.get(0).data()));

        return result;
    }



    private static List<FastImmutableSet<Item>> search(List<FastImmutableSet<Item>> items) {
        if (items.size() <= 1) {
            return items;
        }

        FastImmutableSet<Item> first = items.remove(0);
        FastImmutableSet<Item> last = items.remove(items.size() - 1);
        Item[] buffer = new Item[first.size()];
        Buffer<Item> o = new ArrayBuffer<Item>(buffer);

        first.intersect(last, o);

        Item[] tmp = Arrays.copyOf(buffer, o.filledSize());
        Arrays.sort(tmp, comparator);
        items.add(0, new FastImmutableSet<Item>(tmp, comparator));
        return search(items);
    }


    public Map<Tag, Integer> searchForTags(List<Tag> tags) {
        List<Item> items = searchForItems(tags);
        Map<Tag, Integer> result = new HashMap<Tag, Integer>();
        for (Item item : items) {
            for (Tag tag : item.getTags()) {
                Integer prev = result.get(tag);
                result.put(tag, prev == null?1: prev+1);
            }
        }
        return result;
    }

    private static final Comparator<Item> comparator = new Comparator<Item>() {
        public int compare(Item o1, Item o2) {
            return (o1.getId()<o2.getId() ? -1 : (o1.getId()==o2.getId() ? 0 : 1));
        }
    };
}
