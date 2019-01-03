
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
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.chanxa.linayi.common.util.ApplicationContextUtils;
import com.chanxa.linayi.service.goods.CorrectService;

/**
 * <p>Project:  <p>
 * <p>Module:			<p>
 * <p>Description:		<p>
 *	价格纠错自动审核定时器
 * @author jobd huang
 * @date 2017年12月12日 上午9:57:36
 */
public class CorrectTimer extends QuartzJobBean{
	
	@Resource
	private CorrectService correctService;

	
	@Override
	protected void executeInternal( JobExecutionContext context ) throws JobExecutionException {
		if( correctService == null ){
			correctService = ApplicationContextUtils.getApplicationContext().getBean( CorrectService.class );
		}
		correctService.updateGoodsSkuCorrect();
	}
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("conf/linayi-context.xml");
		CorrectService correctService = (CorrectService)context.getBean(CorrectService.class);
		correctService.updateGoodsSkuCorrect();
		System.exit(0);
		
//		System.out.println(4%2);
	}
}
