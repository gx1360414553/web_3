package com.chanxa.linayi.timer;

import javax.annotation.Resource;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.chanxa.linayi.common.util.ApplicationContextUtils;
import com.chanxa.linayi.service.community.CommunitySupermarketService;

/**清理解绑超市标记定时器
 * @author Awake
 * @date 2018年7月11日 下午2:29:26
 */
public class CleanDeBindSupermarektStatusTimer extends QuartzJobBean{

	@Resource
	private CommunitySupermarketService communitySupermarketService;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		if( communitySupermarketService == null ){
			communitySupermarketService = ApplicationContextUtils.getApplicationContext().getBean( CommunitySupermarketService.class );
		}  
		communitySupermarketService.cleanDeBindSupermarketStatus();
	}

	
	
}
