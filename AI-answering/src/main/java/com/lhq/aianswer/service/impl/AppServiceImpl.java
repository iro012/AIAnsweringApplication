package com.lhq.aianswer.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lhq.aianswer.common.ErrorCode;
import com.lhq.aianswer.constant.CommonConstant;
import com.lhq.aianswer.exception.ThrowUtils;
import com.lhq.aianswer.mapper.AppMapper;
import com.lhq.aianswer.model.dto.app.AppQueryRequest;
import com.lhq.aianswer.model.entity.App;
import com.lhq.aianswer.model.entity.User;
import com.lhq.aianswer.model.enums.AppTypeEnum;
import com.lhq.aianswer.model.enums.ReviewStatusEnum;
import com.lhq.aianswer.model.enums.ScoringStategyEnum;
import com.lhq.aianswer.model.vo.AppVO;
import com.lhq.aianswer.model.vo.UserVO;
import com.lhq.aianswer.service.AppService;
import com.lhq.aianswer.service.UserService;
import com.lhq.aianswer.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 应用服务接口
 *
 * @author  lhq
 * @date  25-04-2025 00:10:47
 * @version 1.0
*/
@Service
@Slf4j
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements AppService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param app
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validApp(App app, boolean add) {
        ThrowUtils.throwIf(app == null, ErrorCode.PARAMS_ERROR);
        // 从对象中取值
        String appName = app.getAppName();
        String appDesc = app.getAppDesc();
        Integer appType = app.getAppType();
        Integer scoringStrategy = app.getScoringStrategy();
        Integer reviewStatus = app.getReviewStatus();
        // 创建数据时，参数不能为空
        if (add) {
            //补充校验规则
            ThrowUtils.throwIf(StringUtils.isEmpty(appName), ErrorCode.PARAMS_ERROR, "应用名称不能为空");
            ThrowUtils.throwIf(StringUtils.isEmpty(appDesc), ErrorCode.PARAMS_ERROR, "应用描述不能为空");
            AppTypeEnum appTypeEnum = AppTypeEnum.getEnumByValue(appType);
            ThrowUtils.throwIf(appTypeEnum == null, ErrorCode.PARAMS_ERROR, "应用类型错误");
            ScoringStategyEnum scoringStategyEnum = ScoringStategyEnum.getEnumByValue(scoringStrategy);
            ThrowUtils.throwIf(scoringStategyEnum == null, ErrorCode.PARAMS_ERROR, "评分策略错误");
        }
        // 修改数据时，有参数则校验
        if (StringUtils.isNotBlank(appName)) {
            ThrowUtils.throwIf(appName.length() > 50, ErrorCode.PARAMS_ERROR, "应用名称过长");
        }
        if (StringUtils.isNotBlank(appDesc)) {
            ThrowUtils.throwIf(appDesc.length() > 200, ErrorCode.PARAMS_ERROR, "应用描述过长");
        }
        
        if (ObjectUtils.isNotEmpty(reviewStatus)) {
            ReviewStatusEnum reviewStatusEnum = ReviewStatusEnum.getEnumByValue(reviewStatus);
            ThrowUtils.throwIf(reviewStatusEnum == null, ErrorCode.PARAMS_ERROR, "审核状态错误");
        }
    }

    /**
     * 获取查询条件
     *
     * @param appQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<App> getQueryWrapper(AppQueryRequest appQueryRequest) {
        QueryWrapper<App> queryWrapper = new QueryWrapper<>();
        if (appQueryRequest == null) {
            return queryWrapper;
        }
        // 从对象中取值
        Long id = appQueryRequest.getId();
        String appName = appQueryRequest.getAppName();
        String appDesc = appQueryRequest.getAppDesc();
        Integer appType = appQueryRequest.getAppType();
        Integer scoringStrategy = appQueryRequest.getScoringStrategy();
        Integer reviewStatus = appQueryRequest.getReviewStatus();
        Long userId = appQueryRequest.getUserId();
        String searchText = appQueryRequest.getSearchText();
        String sortField = appQueryRequest.getSortField();
        String sortOrder = appQueryRequest.getSortOrder();
        
        // 补充需要的查询条件
        // 从多字段中搜索
        if (StringUtils.isNotBlank(searchText)) {
            // 需要拼接查询条件
            queryWrapper.and(qw -> qw.like("appName", searchText).or().like("appDesc", searchText));
        }
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(appName), "appName", appName);
        queryWrapper.like(StringUtils.isNotBlank(appDesc), "appDesc", appDesc);
       
       
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(appType), "appType", appType);
        queryWrapper.eq(ObjectUtils.isNotEmpty(scoringStrategy), "scoringStrategy", scoringStrategy);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        // 排序规则
            queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取应用封装
     *
     * @param app
     * @param request
     * @return
     */
    @Override
    public AppVO getAppVO(App app, HttpServletRequest request) {
        // 对象转封装类
        AppVO appVO = AppVO.objToVo(app);
        // 获取用户信息
        UserVO loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);
        appVO.setUser(loginUser);
        return appVO;
    }

    /**
     * 分页获取应用封装
     *
     * @param appPage
     * @param request
     * @return
     */
    @Override
    public Page<AppVO> getAppVOPage(Page<App> appPage, HttpServletRequest request) {
        List<App> appList = appPage.getRecords();
        Page<AppVO> appVOPage = new Page<>(appPage.getCurrent(), appPage.getSize(), appPage.getTotal());
        if (CollUtil.isEmpty(appList)) {
            return appVOPage;
        }
        // 对象列表 => 封装对象列表
        List<AppVO> appVOList = appList.stream().map(app -> {
            return AppVO.objToVo(app);
        }).collect(Collectors.toList());
        
        // 填充用户信息
        appVOList.forEach(appVO -> {
            Long userId = appVO.getUserId();
            User user = null;
            if (userId != null && userId > 0) {
                 user = userService.getById(userId);
            }
            appVO.setUser(UserVO.objToVo(user));
        });
        
        appVOPage.setRecords(appVOList);
        return appVOPage;
    }
    
    @Override
    public void setDefaultReview(App app, UserVO userVO) {
        ThrowUtils.throwIf(userVO == null, ErrorCode.NOT_LOGIN_ERROR);
        if(userService.isAdmin(userVO)) {
            app.setReviewStatus(ReviewStatusEnum.PASS.getValue());
            app.setReviewMessage("管理员申请，默认审核通过");
        } else {
            app.setReviewStatus(ReviewStatusEnum.REVIEWING.getValue());
            app.setReviewMessage("用户申请，默认审核中");
        }
        app.setReviewerId(userVO.getId());
        app.setReviewTime(new Date());
    }
    
}
