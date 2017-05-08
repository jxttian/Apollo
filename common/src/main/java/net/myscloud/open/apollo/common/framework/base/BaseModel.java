package net.myscloud.open.apollo.common.framework.base;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseModel implements Serializable {
    /**
     * 是否可用
     */
    private Integer enable;

    /**
     * 创建时间
     */
    private Date creationTime;

    /**
     * 创建人
     */
    private Integer creator;

    /**
     * 修改时间
     */
    private Date modificationTime;

    /**
     * 修改
     */
    private Integer modifier;

    /**
     * 备注
     */
    private String memo;

    /**
     * 是否可用枚举
     */
    @AllArgsConstructor
    public enum Enable {
        Yes(1), No(0);

        @Getter
        private int code;
    }
}
