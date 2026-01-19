package com.ssdsaad.rcdiamondgh.rcseffect.utils;

import java.util.*;

/**
 * 代理迭代器：
 * 遍历时：使用 Snapshot (ArrayList)，防止 CME
 * 删除时：操作原 Map (HashMap)，确保原版清理逻辑生效
 */
public class SafePotionIterator implements Iterator<Integer> {

    private final Iterator<Integer> snapshotIterator;
    private final Map<Integer, Object> realMap;
    private Integer lastKey;

    public SafePotionIterator(Set<Integer> keySet, Map<Integer, Object> realMap) {
        // 创建快照
        this.snapshotIterator = new ArrayList<Integer>(keySet).iterator();

        // 持有引用，用于remove
        this.realMap = realMap;
    }

    @Override
    public boolean hasNext() {
        return snapshotIterator.hasNext();
    }

    @Override
    public Integer next() {
        this.lastKey = snapshotIterator.next();
        return this.lastKey;
    }

    @Override
    public void remove() {
        // 转发删除操作到真实 Map
        if (this.lastKey != null) {
            this.realMap.remove(this.lastKey);
        }
    }
}
