package net.myscloud.open.apollo.domain.model;

import lombok.*;
import net.myscloud.open.apollo.common.framework.base.BaseModel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ConfigFile extends BaseModel {
    private Integer id;

    private String project;

    private String env;

    private String name;

    private String content;

    private Integer version;

    private Integer latest;

    private String desc;
}