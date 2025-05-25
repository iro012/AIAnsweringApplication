package com.lhq.aianswer.mapper;

import com.lhq.aianswer.model.dto.statistic.AppAnswerCountDTO;
import com.lhq.aianswer.model.dto.statistic.AppAnswerResultCountDTO;
import com.lhq.aianswer.model.entity.UserAnswer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author lhq
* @description 针对表【user_answer(用户答题记录)】的数据库操作Mapper
* @createDate 2025-04-24 22:49:16
* @Entity com.lhq.aianswer.model.entity.UserAnswer
*/
public interface UserAnswerMapper extends BaseMapper<UserAnswer> {
    
    @Select("select appId, count(userId) as count from user_answer group by appId order by count desc limit 10")
    List<AppAnswerCountDTO> doAnswerCount();
    
    @Select("SELECT resultName, COUNT(resultName) AS count FROM user_answer WHERE appId = #{appId} GROUP BY resultName ORDER BY count DESC")
    List<AppAnswerResultCountDTO> doAnswerResultCount(@Param("appId") Long appId);
}




