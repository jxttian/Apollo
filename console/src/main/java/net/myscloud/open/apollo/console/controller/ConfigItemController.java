package net.myscloud.open.apollo.console.controller;

import com.google.common.collect.Lists;
import net.myscloud.open.apollo.common.framework.Pagination;
import net.myscloud.open.apollo.common.framework.Response;
import net.myscloud.open.apollo.common.framework.base.BaseModel;
import net.myscloud.open.apollo.console.search.ConfigFileSearch;
import net.myscloud.open.apollo.console.search.ConfigItemSearch;
import net.myscloud.open.apollo.console.service.ConfigItemService;
import net.myscloud.open.apollo.console.service.EnvironmentService;
import net.myscloud.open.apollo.console.service.ProjectService;
import net.myscloud.open.apollo.domain.model.ConfigItem;
import net.myscloud.open.yuna.consts.YunaConsts;
import net.myscloud.open.yuna.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Created by genesis on 17-5-4.
 */
@Controller
@RequestMapping("config/item")
public class ConfigItemController {

    @Autowired
    private ConfigItemService service;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private EnvironmentService environmentService;

    @RequestMapping("index.html")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("config/item");
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
    public Pagination list(ConfigItemSearch search) {
        long count = service.count(search);
        if (count > 0) {
            return Pagination.build(count, service.page(search).orElse(Lists.newArrayList()));
        }
        return Pagination.empty();
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public Response save(HttpSession session, @RequestBody ConfigItem model) {
        User currentUser = (User) session.getAttribute(YunaConsts.SESSION_USER);
        // TODO: 17-5-8 下沉到service & +事务
        return service.save(currentUser, model);
    }
}
