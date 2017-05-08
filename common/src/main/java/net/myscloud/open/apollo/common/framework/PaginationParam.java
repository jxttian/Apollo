package net.myscloud.open.apollo.common.framework;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.myscloud.open.apollo.common.framework.base.BaseBean;
import net.myscloud.open.apollo.common.kits.StringKits;

@Data
@EqualsAndHashCode(callSuper = true)
public class PaginationParam extends BaseBean {
    private String order = "desc";
    private Integer limit = Integer.MAX_VALUE;
    private Long offset = 0L;
    private String sort = "id";

    public String getSort() {
        return StringKits.isNotEmpty(sort) ? StringKits.underscoreName(sort) : null;
    }
}
