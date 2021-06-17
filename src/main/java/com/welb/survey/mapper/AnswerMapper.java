package com.welb.survey.mapper;

import com.welb.survey.entity.Answer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AnswerMapper {

    int batchInsert(List<Answer>list);

    int getAnswerCount(Integer surveyinfoid);

    List<Answer>getPackList(Answer answer);

    List<Answer>selectListByInfoId(Integer surveyinfoid);

    int batchDelete(List<Integer>ids);

    int selectOptionCount(Integer optionid);

    List<Answer> selectAnswerByMoneyCardAndInfoId(@Param("surveyinfoid")Integer surveyinfoid,@Param("moneycard")String moneycard);

    int selectQuestionCount(@Param("surveyinfoid")Integer surveyinfoid,@Param("questionid")Integer questionid);

    List<Answer>getOptionPackList(Answer answer);


}
