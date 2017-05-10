package net.myscloud.open.apollo.domain.model;

import lombok.*;
import net.myscloud.open.apollo.common.base.BaseModel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ConfigItem extends BaseModel {
    private Integer id;

    private String project;

    private String env;

    /**
     * 类型 1:String 2:Number 3:Boolean 4:List 5:Map
     */
    private Integer type;

    /**
     * 风险等级 0-9 数字越大 修改风险越高
     */
    private Integer riskLevel;

    private String key;

    private String name;

    private String value;

    private Integer version;

    private Integer latest;

    private String desc;
}