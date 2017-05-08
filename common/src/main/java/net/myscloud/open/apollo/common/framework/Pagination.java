package net.myscloud.open.apollo.common.framework;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Pagination<R> implements Serializable {

    @Getter
    private long total;

    @Getter
    private List<R> rows;

    public static <R> Pagination<R> build(long total, List<R> rows) {
        return new Pagination<>(total, rows);
    }

    public static <R> Pagination<R> empty() {
        return new Pagination<>(0, Lists.newArrayList());
    }
}
