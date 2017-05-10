package net.myscloud.open.apollo.console.controller;

import com.google.common.collect.Lists;
import net.myscloud.open.apollo.console.framework.Pagination;
import net.myscloud.open.apollo.common.Response;
import net.myscloud.open.apollo.console.search.EnvironmentSearch;
import net.myscloud.open.apollo.console.service.EnvironmentService;
import net.myscloud.open.apollo.domain.model.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by genesis on 17-5-4.
 */
@Controller
@RequestMapping("environment")
public class EnvironmentController {

    @Autowired
    private EnvironmentService service;

    @RequestMapping("index.html")
    public ModelAndView index() {
        return new ModelAndView("environment/index");
    }

    @RequestMapping("list.json")
    @ResponseBody
    public Pagination list(EnvironmentSearch search) {
        long count = service.count(search);
        if (count > 0) {
            return Pagination.build(count, service.page(search).orElse(Lists.newArrayList()));
        }
        return Pagination.empty();
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public Response save(@RequestBody Environment model) {
        if (model.getId() == null) {
            //新增
            service.insert(model);
        } else {
            service.update(model);
        }
        return Response.success();
    }
}
