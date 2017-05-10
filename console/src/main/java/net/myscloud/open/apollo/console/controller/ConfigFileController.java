package net.myscloud.open.apollo.console.controller;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.myscloud.open.apollo.console.framework.Pagination;
import net.myscloud.open.apollo.common.Response;
import net.myscloud.open.apollo.common.base.BaseModel;
import net.myscloud.open.apollo.console.search.ConfigFileSearch;
import net.myscloud.open.apollo.console.service.ConfigFileService;
import net.myscloud.open.apollo.console.service.EnvironmentService;
import net.myscloud.open.apollo.console.service.ProjectService;
import net.myscloud.open.apollo.console.service.SecurityService;
import net.myscloud.open.apollo.domain.model.ConfigFile;
import net.myscloud.open.apollo.domain.model.Security;
import net.myscloud.open.yuna.common.kits.StringKits;
import net.myscloud.open.yuna.consts.YunaConsts;
import net.myscloud.open.yuna.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by genesis on 17-5-4.
 */
@Slf4j
@Controller
@RequestMapping("config/file")
public class ConfigFileController {

    @Autowired
    private ConfigFileService service;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private EnvironmentService environmentService;

    @Autowired
    private SecurityService securityService;

    @RequestMapping("index.html")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("config/file");
        modelAndView.addObject("projects", projectService.all()
                .orElse(Lists.newArrayList())
                .stream()
                .filter(BaseModel::enabled)
                .collect(Collectors.toList()));
        modelAndView.addObject("environments", environmentService.all()
                .orElse(Lists.newArrayList())
                .stream()
                .filter(BaseModel::enabled)
                .collect(Collectors.toList()));
        return modelAndView;
    }

    @RequestMapping("list.json")
    @ResponseBody
    public Pagination list(ConfigFileSearch search) {
        long count = service.count(search);
        if (count > 0) {
            return Pagination.build(count, service.page(search).orElse(Lists.newArrayList()));
        }
        return Pagination.empty();
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public Response save(HttpSession session, @RequestBody ConfigFile model) {
        User currentUser = (User) session.getAttribute(YunaConsts.SESSION_USER);
        // TODO: 17-5-8 下沉到service & +事务
        return service.save(currentUser, model);
    }

    /**
     * 获取远程配置文件
     *
     * @param request 请求 需认证及白名单校验
     * @param project 项目编码
     * @param env     环境编码
     * @param name    文件名称
     * @param version 文件版本
     * @return 文件文本
     */
    @RequestMapping(value = "fetch/{project}/{env}/{name}", method = RequestMethod.GET)
    @ResponseBody
    public Response fetch(HttpServletRequest request, @PathVariable String project, @PathVariable String env, @PathVariable String name, @RequestParam(required = false) Integer version) {
        Optional<Security> security = securityService.get(project, env);
        if (security.isPresent()) {
            if (StringKits.isNotEmpty(security.get().getCertification())
                    && !security.get().getCertification().equals(request.getHeader("certification"))) {
                log.warn("请求来源认证信息不正确 Certification:[{}]", request.getHeader("certification"));
                return Response.error("请求信息有误");
            }
            if (StringKits.isNotEmpty(security.get().getWhitelists())
                    && !security.get().getWhitelists().contains("0.0.0.0")
                    && !security.get().getWhitelists().contains(request.getRemoteHost())) {
                log.warn("请求来源未包含在白名单中 Remote Host:[{}]", request.getRemoteHost());
                return Response.error("请求信息有误");
            }
        }
        Optional<ConfigFile> configFile = service.getFirst(ConfigFileSearch.builder().project(project).env(env).name(name).version(version).latest(version != null ? null : 1).build());
        return configFile.<Response>map(configFile1 -> Response.success(configFile1.getContent())).orElseGet(() -> Response.error("不存在该配置文件"));
    }
}
