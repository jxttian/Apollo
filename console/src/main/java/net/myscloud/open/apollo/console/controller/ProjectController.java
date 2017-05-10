package net.myscloud.open.apollo.console.controller;

import com.google.common.collect.Lists;
import net.myscloud.open.apollo.console.framework.Pagination;
import net.myscloud.open.apollo.common.Response;
import net.myscloud.open.apollo.common.base.BaseModel;
import net.myscloud.open.apollo.common.kits.StringKits;
import net.myscloud.open.apollo.console.search.ProjectSearch;
import net.myscloud.open.apollo.console.search.SecuritySearch;
import net.myscloud.open.apollo.console.service.EnvironmentService;
import net.myscloud.open.apollo.console.service.ProjectService;
import net.myscloud.open.apollo.console.service.SecurityService;
import net.myscloud.open.apollo.domain.model.Project;
import net.myscloud.open.apollo.domain.model.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.stream.Collectors;

/**
 * Created by genesis on 17-5-4.
 */
@Controller
@RequestMapping("project")
public class ProjectController {

    @Autowired
    private ProjectService service;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private EnvironmentService environmentService;

    @RequestMapping("index.html")
    public ModelAndView index() {
        return new ModelAndView("project/index");
    }

    @RequestMapping("list.json")
    @ResponseBody
    public Pagination list(ProjectSearch search) {
        long count = service.count(search);
        if (count > 0) {
            return Pagination.build(count, service.page(search).orElse(Lists.newArrayList()));
        }
        return Pagination.empty();
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public Response save(@RequestBody Project model) {
        if (model.getId() == null) {
            //新增
            service.insert(model);
        } else {
            service.update(model);
        }
        return Response.success();
    }

    @RequestMapping("security.html")
    public ModelAndView security(@RequestParam String project) {
        ModelAndView modelAndView = new ModelAndView("project/security");
        modelAndView.addObject("project", project);
        modelAndView.addObject("environments", environmentService.all()
                .orElse(Lists.newArrayList())
                .stream()
                .filter(BaseModel::enabled)
                .collect(Collectors.toList()));
        return modelAndView;
    }

    @RequestMapping("security.json")
    @ResponseBody
    public Pagination listSecurities(SecuritySearch search) {
        long count = securityService.count(search);
        if (count > 0) {
            return Pagination.build(count, securityService.page(search).orElse(Lists.newArrayList()));
        }
        return Pagination.empty();
    }

    @RequestMapping("security/save")
    @ResponseBody
    public Response saveSecurity(@RequestBody Security model) {
        if (model.getId() == null) {
            //新增
            securityService.insert(model);
        } else {
            securityService.update(model);
        }
        return Response.success();
    }

    @RequestMapping("build/certification")
    @ResponseBody
    public Response buildCertification() {
        return Response.success(StringKits.buildShortUUID());
    }
}
