package net.myscloud.open.apollo.domain.model;

import java.util.Date;

import lombok.*;
import net.myscloud.open.apollo.common.base.BaseModel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Security extends BaseModel {
    private Integer id;

    private String project;

    private String env;

    private String certification;

    private String whitelists;

    private Date creationTime;

    private Integer creator;

    private Date modificationTime;

    private Integer modifier;
}