package net.myscloud.open.apollo.domain.model;

import lombok.*;
import net.myscloud.open.apollo.common.framework.base.BaseModel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Project extends BaseModel {
    private Integer id;

    private String code;

    private String name;

    /**
     * 负责人
     */
    private String principal;

    private String phone;

    private String email;

    private String desc;
}