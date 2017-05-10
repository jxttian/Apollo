package net.myscloud.open.apollo.common;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import lombok.*;
import net.myscloud.open.apollo.common.kits.StringKits;

import java.io.Serializable;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Response<T> implements Serializable {

    /**
     * 返回码
     */
    private Status status;

    /**
     * 返回码
     */
    private int code;

    /**
     * 返回信息
     */
    private String msg;

    @Getter
    private String desc;

    /**
     * 数据总量
     */
    @Getter
    private Long count;

    /**
     * 返回数据
     */
    @Getter
    private T data;

    public static <T> Response<T> success() {
        return Response.<T>builder().status(Status.SUCCESS).build();
    }

    public static <T> Response<T> success(T data) {
        return Response.<T>builder().status(Status.SUCCESS).data(data).build();
    }

    public static <T> Response<T> success(long count, T data) {
        return Response.<T>builder().status(Status.SUCCESS).count(count).data(data).build();
    }

    public static <T> Response<T> error() {
        return Response.<T>builder().status(Status.SYSTEM_ERROR).build();
    }

    public static <T> Response<T> error(String msg) {
        return Response.<T>builder().status(Status.SYSTEM_ERROR).msg(msg).build();
    }

    public static <T> Response<T> error(String msg, String desc) {
        return Response.<T>builder().status(Status.SYSTEM_ERROR).msg(msg).desc(desc).build();
    }

    public static <T> Response<T> result(Status status) {
        return Response.<T>builder().status(status).build();
    }

    public static <T> Response<T> result(Status status, String msg) {
        return Response.<T>builder().status(status).msg(msg).build();
    }

    public static <T> Response<T> result(Status status, String msg, String desc) {
        return Response.<T>builder().status(status).msg(msg).desc(desc).build();
    }

    public int getCode() {
        return status.getCode();
    }

    public String getMsg() {
        if (StringKits.isNotEmpty(msg))
            return msg;
        else {
            return status.getMsg();
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public enum Status {
        SYSTEM_ERROR(500, "系统错误"),
        NOT_FOUND_HANDLER(501, "请求未找到相应执行者"),
        MYSQL_DUPLICATE_KEY_ERROR(502, "数据已存在，请勿重复插入"),

        BUSINESS_ERROR(300, "业务错误"),

        ILLEGAL_ARGUMENT(400, "参数不正确"),
        ILLEGAL_SIGN(401, "签名不正确"),

        SUCCESS(200, "请求成功"),;
        private static Map<Integer, Status> map;

        static {
            map = Maps.newHashMap();
            for (Status status : Status.values()) {
                if (!map.containsKey(status.code)) {
                    map.put(status.code, status);
                }
            }
        }

        @Getter
        private int code;
        @Getter
        private String msg;

        public static Status getStatus(int code) {
            Preconditions.checkArgument(Status.map.containsKey(code), "不存在相应Code的状态");
            return Status.map.get(code);
        }

        public static String getStatusMsg(int code) {
            Preconditions.checkArgument(Status.map.containsKey(code), "不存在相应Code的状态");
            return Status.map.get(code).getMsg();
        }
    }
}
