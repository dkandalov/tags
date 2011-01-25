package com.greeneyes.tags.index;

import com.greeneyes.tags.Item;
import com.greeneyes.tags.Tag;
import com.greeneyes.tags.utils.Buffer;

import java.util.List;
import java.util.Map;

/**
 * Author alex at 20.01.11 9:12
 */
public interface TagIndex {
    public List<Item> searchForItems(List<Tag> tags);
    public Map<Tag, Integer> searchForTags(List<Tag> tags);
}
