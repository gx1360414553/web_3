
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi.webwar
 * All rights reserved.
 * 
 * Filename：
 *		ProcurementTask.java
 * Description：
 * 		
 * Author:
 *		jobd
 * Finished：
 *		2017年12月8日上午11:50:11 
 ********************************************************/
package com.chanxa.linayi.timer;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.chanxa.linayi.bo.community.RouteCommunityBO;
import com.chanxa.linayi.common.util.ApplicationContextUtils;
import com.chanxa.linayi.service.task.ProcurementTaskService;

/**
 * <p>Project:  <p>
 * <p>Module:			<p>
 * <p>Description:		<p>
 *	采买任务发布定时器
 * @author jobd huang
 * @date 2017年12月8日 上午11:50:11
 */
public class ProcurementTaskTimer extends QuartzJobBean {

	@Resource
	private ProcurementTaskService procurementTaskService;
	@Override
	protected void executeInternal( JobExecutionContext context ) throws JobExecutionException {
		
		if( procurementTaskService == null ){
			procurementTaskService = ApplicationContextUtils.getApplicationContext().getBean( ProcurementTaskService.class );
		}
	   
	   JobDataMap dataMap = context.getMergedJobDataMap();
	   
	   @SuppressWarnings( "unchecked" )
	   List<RouteCommunityBO> routeCommunityList = ( List<RouteCommunityBO> )dataMap.get( "routeCommunityList" );
	   //每个时间段有两个截单时间，该时间段最后一个截单时间所有商品必须发布任务，所以使用index取模标识是否是最后一个截单时间段
	   Integer timeNo = dataMap.getIntValue( "timeNo" ) % 60 == 0 ? 0 : 1;
	   Integer index = dataMap.getIntValue( "index" ) % 2 == 0 ? 0 : 1;
	   String taskNo = dataMap.getString( "taskNo" );
	   
	   
	   List<Integer> communityIdList = routeCommunityList.stream()//
			   .collect( //
					   Collectors.mapping( //
							   RouteCommunityBO::getCommunityId, Collectors.toList() //
						) //
				);
	   procurementTaskService.publishProcurementTask( communityIdList, index, taskNo ,timeNo);
	
	}
}
