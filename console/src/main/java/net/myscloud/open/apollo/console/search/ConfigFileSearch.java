package net.myscloud.open.apollo.console.search;

import lombok.*;
import net.myscloud.open.apollo.common.framework.PaginationParam;

/**
 * Created by genesis on 17-5-4.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ConfigFileSearch extends PaginationParam {
    private String project;

    private String env;

    private String name;

    private Integer latest;

    private Integer version;

    private Integer enable;
}
