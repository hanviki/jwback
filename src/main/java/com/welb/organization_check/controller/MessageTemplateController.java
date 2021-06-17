package com.welb.organization_check.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welb.organization_check.entity.MessageTemplate;
import com.welb.organization_check.service.ITemplateMessageService;
import com.welb.util.LogUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author luoyaozu
 * @title: MessageTemplateController
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/8/1411:47
 */
@RestController
@RequestMapping("template")
public class MessageTemplateController {
    private final Logger log = LogManager.getLogger(this.getClass());

    @Resource
    ITemplateMessageService templateService;
    /**
     * 组织部查找所有的短信模板
     *
     * @param template
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/list")
    public Object selectAllTemplate(HttpServletRequest req, MessageTemplate template, int pageNum, int pageSize) {

        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        if (usercode == null) {
            map.put("msg", "用户登录超时,请重新登录");
            map.put("code", 810);
        } else {
            PageHelper.startPage(pageNum, pageSize);
            try {

                List<MessageTemplate> messageTemplates = templateService.selectAllTemplate(template);
                PageInfo<MessageTemplate> pageInfo = new PageInfo<>(messageTemplates);
                messageTemplates = pageInfo.getList();
                map.put("totalPages", pageInfo.getTotal());
                map.put("data", messageTemplates);
                map.put("msg", "查询成功");
                map.put("code", 0);

            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("msg", "查询失败");
                map.put("code", 1);
            }
        }
        return map;
    }


    /**
     * 添加短信模板
     *
     * @param template
     * @return
     */
    @RequestMapping("/add")
    public Object addTemplate(MessageTemplate template) {
        ModelMap map = new ModelMap();
        int count = templateService.insertSelective(template);
        if (count > 0) {
            map.put("msg", "添加成功");
            map.put("code", 0);
        } else {
            map.put("msg", "添加失败 ");
            map.put("code", 1);
        }

        return map;
    }


    /**
     * 修改短信模板
     *
     * @param template
     * @return
     */
    @RequestMapping("/update")
    public Object updateTemplate(MessageTemplate template) {
        ModelMap map = new ModelMap();
        int count = templateService.updateByPrimaryKeySelective(template);
        if (count > 0) {
            map.put("msg", "修改成功");
            map.put("code", 0);
        } else {
            map.put("msg", "修改失败 ");
            map.put("code", 1);
        }

        return map;
    }

    /**
     * 删除短信模板
     *
     * @param templatecode
     * @return
     */
    @RequestMapping("/delete")
    public Object updateTemplate(String templatecode) {
        ModelMap map = new ModelMap();
        int count = templateService.deleteByPrimaryKey(templatecode);
        if (count > 0) {
            map.put("msg", "删除成功");
            map.put("code", 0);
        } else {
            map.put("msg", "删除失败 ");
            map.put("code", 1);
        }
        return map;
    }


    /**
     * 组织部查找所有的短信模板
     *
     * @return
     */
    @RequestMapping("/selectTemplateList")
    public Object selectTemplateList(String flag) {
        ModelMap map = new ModelMap();
        try {

            List<MessageTemplate> messageTemplates = templateService.selectTemplateList(flag);
            map.put("data", messageTemplates);
            map.put("msg", "查询成功");
            map.put("code", 0);

        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "查询失败");
            map.put("code", 1);
        }
        return map;
    }
}
