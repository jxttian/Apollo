package net.myscloud.open.apollo.core;

/**
 * Created by genesis on 17-5-9.
 */
public interface Register {

    void initialize();

    void register(String key, String data) throws Exception;

    void update(String key, String data) throws Exception;

    void remove(String key) throws Exception;

    String get(String key) throws Exception;

    void destroy();

}
