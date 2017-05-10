package net.myscloud.open.apollo.core.consts;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by jxtti on 2017/05/10.
 */
@AllArgsConstructor
public enum ConfigItemType {
    String(1), Number(2), Boolean(3), List(4), Map(5);
    @Getter
    private int code;
}
