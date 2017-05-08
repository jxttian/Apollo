package net.myscloud.open.apollo.console.search;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.myscloud.open.apollo.common.framework.PaginationParam;

/**
 * Created by genesis on 17-5-4.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SecuritySearch extends PaginationParam {
    private String project;
}
