package com.example.demo.common.extension;

import java.util.ArrayList;
import java.util.List;

public class ListExtension {
    public static <T> List<List<T>> chunk(List<T> self, int size) {
        List<List<T>> result = new ArrayList<>();

        for (int x = 0; x < self.size(); x += size) {
            result.add(self.subList(x, Math.min(x + size, self.size())));
        }

        return result;
    }
}
