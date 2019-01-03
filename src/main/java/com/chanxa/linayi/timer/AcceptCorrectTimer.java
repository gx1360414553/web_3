/**   
* @Title: AcceptCorrectTimer.java  
* @Description: TODO
* @author Tom   
* @date 2018年4月25日 下午12:02:12   
*/
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
 * @ClassName AcceptCorrectTimer  
 * @Description  认可纠错定时器，用户认可纠错后两小时后生效
 * @author Tom  
 */
public class AcceptCorrectTimer extends QuartzJobBean {

	@Resource
	private CorrectService correctService;

	
	@Override
	protected void executeInternal( JobExecutionContext context ) throws JobExecutionException {
		if( correctService == null ){
			correctService = ApplicationContextUtils.getApplicationContext().getBean( CorrectService.class );
		}
		correctService.acceptCorrectPriceAtferTwoHours();
	}
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("conf/linayi-context.xml");
		CorrectService correctService = (CorrectService)context.getBean(CorrectService.class);
		correctService.acceptCorrectPriceAtferTwoHours();
		System.exit(0);
		
//		System.out.println(4%2);
	}
	
}
