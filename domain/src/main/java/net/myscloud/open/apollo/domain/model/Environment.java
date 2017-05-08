package net.myscloud.open.apollo.domain.model;

import lombok.*;
import net.myscloud.open.apollo.common.framework.base.BaseModel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Environment extends BaseModel {
    private Integer id;

    private String code;

    private String name;

    private String desc;
}