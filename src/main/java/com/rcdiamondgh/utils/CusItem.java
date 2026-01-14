package com.rcdiamondgh.utils;

import java.util.*;

public class CusItem {

    private Map<String, String> data;

    public CusItem() {
        this.data = new HashMap<String, String>();
    }

    public Map<String, String> getData() {
        return this.data;
    }

    public void dataMaker(final String key, final String value) {
        this.data.put(key, value);
    }
}
