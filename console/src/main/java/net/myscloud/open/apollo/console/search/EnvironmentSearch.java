package net.myscloud.open.apollo.console.search;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.myscloud.open.apollo.console.framework.PaginationParam;

/**
 * Created by genesis on 17-5-4.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EnvironmentSearch extends PaginationParam {

    private String code;

    private String name;

    private Integer enable;
}
