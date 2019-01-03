
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi.webwar
 * All rights reserved.
 * 
 * Filename：
 *		CorrectTimer.java
 * Description：
 * 		
 * Author:
 *		jobd
 * Finished：
 *		2017年12月12日上午9:57:36 
 ********************************************************/
package com.chanxa.linayi.timer;

import javax.annotation.Resource;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.chanxa.linayi.common.util.ApplicationContextUtils;
import com.chanxa.linayi.service.order.OrderSkuService;

/**
 * <p>Project:  <p>
 * <p>Module:			<p>
 * <p>Description:		<p>
 *	商品金额结算定时器
 * @author jobd huang
 * @date 2017年12月12日 上午9:57:36
 */
public class GoodsClosingTimer extends QuartzJobBean{
	
	@Resource
	private OrderSkuService orderSkuService;
	
	@Override
	protected void executeInternal( JobExecutionContext context ) throws JobExecutionException {
		if( orderSkuService == null ){
			orderSkuService = ApplicationContextUtils.getApplicationContext().getBean( OrderSkuService.class );
		}
		orderSkuService.closingGoodsFee();
		
	}
}
