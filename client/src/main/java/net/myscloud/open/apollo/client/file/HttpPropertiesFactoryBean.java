package net.myscloud.open.apollo.client.file;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.myscloud.open.apollo.common.kits.CollectionKits;
import net.myscloud.open.apollo.common.kits.StringKits;
import net.myscloud.open.apollo.common.Response;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

/**
 * 允许通过HTTP从远程加载配置，并将加载的配置保持到ClassPath下
 * 当远程服务器出现问题时，允许从本地ClassPath中加载配置文件
 *
 * @since 1.0
 */
@Getter
@Setter
@Slf4j
public class HttpPropertiesFactoryBean extends PropertiesFactoryBean {

    private String name;

    private List<String> names;

    @Override
    protected void loadProperties(Properties props) throws IOException {
        Properties apolloProp = PropertiesLoaderUtils.loadAllProperties("apollo.properties");
        if (apolloProp.size() == 0) {
            log.warn("请检查ClassPath下是否存在配置文件[apollo.properties]");
        }
        if (!apolloProp.containsKey("apollo.config.file.api")) {
            log.warn("找不到配置项[apollo.config.file.api]");
        }
        if (!apolloProp.containsKey("apollo.project")) {
            log.warn("找不到配置项[apollo.project]");
        }
        if (!apolloProp.containsKey("apollo.env")) {
            log.warn("找不到配置项[apollo.env]");
        }

        OkHttpClient client = new OkHttpClient();
        List<Resource> resources = Lists.newArrayList();
        if (StringKits.isNotEmpty(name)) {
            resources.add(loadRemoteProperties(client, name, apolloProp));
        }
        if (CollectionKits.isNotEmpty(names)) {
            names.stream()
                    .filter(StringKits::isNotEmpty)
                    .forEach(item -> resources.add(loadRemoteProperties(client, item, apolloProp)));
        }
        Resource[] tmp = new Resource[resources.size()];
        resources.toArray(tmp);
        super.setLocations(tmp);
        super.loadProperties(props);
    }

    private Resource loadRemoteProperties(OkHttpClient client, String name, Properties apolloProp) {
        String mode = apolloProp.getProperty("apollo.mode");
        if (StringKits.isEmpty(mode) || !"local".equals(mode)) {
            //非本地模式运行
            String url = Joiner.on("/").skipNulls()
                    .join(apolloProp.getProperty("apollo.config.file.api")
                            , apolloProp.getProperty("apollo.project")
                            , apolloProp.getProperty("apollo.env")
                            , name);
            log.info("加载远程配置文件... Url:[{}]", url);
            Request request = new Request.Builder()
                    .header("certification", apolloProp.getProperty("apollo.certification", ""))
                    .url(url).build();
            URL pathUrl = Thread.currentThread().getContextClassLoader().getResource("/");
            if (pathUrl == null) {
                log.warn("获取ClassPath失败");
                return new ClassPathResource(name);
            }
            //Windows环境获取ClassPath后需特殊处理
            String classPath = System.getProperty("os.name").toLowerCase().startsWith("win")
                    ? pathUrl.getPath().substring(1) : pathUrl.getPath();
            log.info("获取ClassPath[{}]成功", classPath);
            try (okhttp3.Response response = client.newCall(request).execute()) {
                String content = response.body().string();
                JSONObject result = JSON.parseObject(content);
                if (result.getInteger("code") == Response.Status.SUCCESS.getCode()) {
                    String configContent = result.getString("data");
                    log.info("加载远程配置文件成功 Content:[{}]", configContent);
                    //将配置文件保存到ClassPath
                    Files.write(Paths.get(classPath + name)
                            , configContent.getBytes(Charset.defaultCharset()));

                } else {
                    log.warn("加载远程配置文件失败，Error:{}", result.getString("msg"));
                }
            } catch (IOException e) {
                log.warn(e.getMessage(), e);
            }
        }
        return new ClassPathResource(name);
    }
}
