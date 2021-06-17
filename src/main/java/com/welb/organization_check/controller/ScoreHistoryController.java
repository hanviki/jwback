package com.welb.organization_check.controller;

import com.welb.organization_check.dto.UserDto;
import com.welb.organization_check.entity.ScoreHistory;
import com.welb.organization_check.service.IScoreHistoryService;
import com.welb.organization_check.service.IUserDtoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author luoyaozu
 * @title: ScoreHistoryController
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2020/7/21
 */
@RestController
@RequestMapping("/scoreHistory")
public class ScoreHistoryController {
    private final Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    IScoreHistoryService historyService;
    @Autowired
    IUserDtoService dtoService;


  @RequestMapping("/demo")
  public Object demo(UserDto dto){
      ModelMap map=new ModelMap();
      try {

          List<UserDto> userDtos = dtoService.gradeList(dto);
          for(UserDto dto1:userDtos){
              ScoreHistory history = new ScoreHistory();
              history.setMonth(dto1.getMonth());
              history.setYear(dto1.getYear());
              history.setState(dto1.getState());
              history.setScorestatus(dto1.getScorestatus());
              history.setUsercode(dto1.getUsercode());
              ScoreHistory history1 = historyService.selectOneByHistory(history);
              if (history1!=null){
                  history.setId(history1.getId());
                  historyService.updateByPrimaryKeySelective(history);
              }else {
                  historyService.insertSelective(history);
              }
          }
          map.put("msg","成功");
      }catch (Exception e){
          map.put("msg","失败");
          map.put("error",e.getStackTrace());
      }
      return map;
  }

}
