
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi.webwar
 * All rights reserved.
 * 
 * Filename：
 *		TaskTimerGenerator.java
 * Description：
 * 		
 * Author:
 *		jobd
 * Finished：
 *		2017年12月9日下午3:01:44 
 ********************************************************/
package com.chanxa.linayi.timer;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.chanxa.linayi.bo.community.RouteCommunityBO;
import com.chanxa.linayi.common.constant.Propert;
import com.chanxa.linayi.common.util.BaseSqlSession;
import com.chanxa.linayi.common.util.Configuration;
import com.chanxa.linayi.service.community.CommunityService;
import com.chanxa.linayi.service.community.RouteCommunityService;
import com.chanxa.linayi.service.community.SupermarketService;
import com.chanxa.linayi.service.order.TempOrderSkuService;
import com.chanxa.linayi.service.system.AreaService;
import com.chanxa.linayi.service.task.ProcurementOrderService;
import com.chanxa.linayi.service.task.ProcurementSkuService;

/**
 * <p>Project:  <p>
 * <p>Module:			<p>
 * <p>Description:		<p>
 *	创建发布任务的定时器
 * @author jobd huang
 * @date 2017年12月9日 下午3:01:44
 */
public class TaskTimerGenerator{

	@Resource
	private BaseSqlSession baseSqlSession;
	
	@Resource
	private TempOrderSkuService tempOrderSkuService;
	
	@Resource
	private SupermarketService supermarketService;
	
	@Resource
	private CommunityService communityService;
	
	@Resource
	private ProcurementSkuService procurementSkuService;
	
	@Resource
	private ProcurementOrderService procurementOrderService;
	
	@Resource
	private AreaService areaService;
	
	@Resource
	private RouteCommunityService routeCommunityService;
	
	private static SchedulerFactoryBean schedulerFactory;
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void generatorTimers() throws Exception{
		JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();
				
		Date now = new Date();
		//计算不同社区任务发布时间点,并根据路线分组
		Map<Integer, List<RouteCommunityBO>> routeCommunityGroups = routeCommunityService.getList( new RouteCommunityBO() )
																	.stream()//
																			.sorted( ( a,b ) ->{//
																		return a.getRank() - b.getRank();//
																	} )//
																	.collect( //
																			Collectors.groupingBy( RouteCommunityBO::getRouteId ) //
																	);//
		//根据发布顺序分组
		
		Set<Integer> keySet = routeCommunityGroups.keySet();
		Iterator<Integer> it = keySet.iterator();
		Integer endKey = 0;
		while(it.hasNext()) {
			endKey = it.next();
		}
		int k =0;
		//对于路线中的重复社区,取发布时间早的,摈弃发布时间晚的
		//迭代到最后一条路线就不用再计算
		it = keySet.iterator();
		while(it.hasNext()){
			Integer keyi = it.next();
			if(keyi.equals(endKey)) {
				break;
			}
			k++;
			List<RouteCommunityBO> routeCommunityListi = routeCommunityGroups.get(keyi);
			for (int i = 0; i < routeCommunityListi.size(); i++) {
				RouteCommunityBO routeCommunityi = routeCommunityListi.get(i);
				Integer communityId = routeCommunityi.getCommunityId();
				Iterator<Integer> it2 = keySet.iterator();
				Integer keyj = null;
				for(int n= 0; n <= k; n++){
					if(it2.hasNext()){
					  keyj = it2.next();
					}
				}
				while(true){
					List<RouteCommunityBO> routeCommunityListNext = routeCommunityGroups.get(keyj);
					for (int j = 0; j < routeCommunityListNext.size(); j++) {
						RouteCommunityBO routeCommunityNext = routeCommunityListNext.get(j);
						if(routeCommunityNext.getCommunityId() == communityId){
							if(i > j){
								routeCommunityListi.remove(routeCommunityi);
								i--;
							}else{
								routeCommunityListNext.remove(routeCommunityNext);
								j--;
							}
						}
					}
					routeCommunityGroups.put(keyj, routeCommunityListNext);
					if(it2.hasNext()) {
						keyj = it2.next();
					}else {
						break;
					}
				}
				routeCommunityGroups.put(keyi, routeCommunityListi);
			}
			
		}
		
		Map<Integer,List<RouteCommunityBO>> groupsByRank = new TreeMap<>();
		for( Entry<Integer,List<RouteCommunityBO>> entry : routeCommunityGroups.entrySet() ) {
			List<RouteCommunityBO> routeCommunityList = entry.getValue();
			for( int i = 0 ; i < routeCommunityList.size(); i++ ) {
				List<RouteCommunityBO> group =  groupsByRank.getOrDefault( i, new ArrayList<>() );
				if( group.isEmpty() ){
					groupsByRank.put( i, group );
				}
				group.add( routeCommunityList.get( i ) );
			}
		}
		
		//获取截单时间
		String[] timeAndNos = Configuration.getConfig().getValue( Propert.CUT_OFF_TIME ).split( "," );
		//List<Map< taskNo| time，任务编号， 任务发布时间>> localTimeList = new ArrayList<>();
		List<Map<String,Object>> localTimeList = new ArrayList<>();
		for( String timeAndNo : timeAndNos ) {
			String[] time = timeAndNo.split( ":" );
			Map<String, Object> timeItem = new HashMap<>();
			timeItem.put( "time", LocalTime.of( Integer.valueOf( time[0] ), 00 ));
			timeItem.put( "taskNo", time[1] );
			localTimeList.add( timeItem );
		}
		Integer betweenTimes = Integer.valueOf( Configuration.getConfig().getValue( Propert.BETWEEN_TIMES ) );
		//以发布时间分组
		//Map<发布时间，Map<taskNo | routeCommunityList，任务编号 | 路线项列表  >>
		Map<LocalTime, Map<String, Object>> timeListGroups = new TreeMap<>();
		for (int i = 0; i < localTimeList.size(); i++) {
			int timeNo = 0;
			for (Entry<Integer, List<RouteCommunityBO>> entry : groupsByRank.entrySet()) {
				Integer rank = entry.getKey();
				List<RouteCommunityBO> routeCommunityList = entry.getValue();
				Map<String, Object> timeListItem = new HashMap<>();
				Map<String, Object> timeItem = localTimeList.get(i);
				timeListItem.put("taskNo", timeItem.get("taskNo"));
				// index用来标识任务发布的顺序，每个时间段有两个发布任务时间，如果index%2 == 0 代表该时间段最后一个发布时间
				timeListItem.put("index", i);
				timeListItem.put("timeNo", timeNo);
				timeListItem.put("routeCommunityList", routeCommunityList);
				LocalTime localTime = ((LocalTime) timeItem.get("time")).plusMinutes(-60-rank * -betweenTimes );
				Map<String, Object> item = timeListGroups.get(localTime);
//				如果两条路线的存在相同社区,取消后者的任务发布
				if (item == null) {
					timeListGroups.put(localTime, timeListItem);
				} else {
					((List<RouteCommunityBO>) item.get("routeCommunityList")).addAll(routeCommunityList);
				}
				timeNo += 10;
			}
		}
		//构建定时器规则
		//Map<表达式,Map<taskNo | routeCommunityList，任务编号 | 路线项列表>> cronExpressionList = new TreeMap<>();
		Map<String,Map<String,Object>> cronExpressionList = new TreeMap<>();
		for( Entry<LocalTime,Map<String,Object>> entry : timeListGroups.entrySet() ) {
			LocalTime localTime = entry.getKey();
			Map<String,Object> value = entry.getValue();
			StringJoiner joiner = new StringJoiner( " " );
			joiner.add( "0" );
			joiner.add( localTime.getMinute() +"" );
			joiner.add( localTime.getHour() +"" );
			joiner.add( "*" );
			joiner.add( "*" );
			joiner.add( "?" );
			joiner.add( "*" );
			cronExpressionList.put( joiner.toString(), value );
		}
		//发布定时任务
		JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
		jobDetailFactoryBean.setBeanName( JobDetailFactoryBean.class.getName() );
		jobDetailFactoryBean.setJobClass( ProcurementTaskTimer.class );
		jobDetailFactoryBean.afterPropertiesSet();
		//定时器执行任务
		JobDetail jobDetail = jobDetailFactoryBean.getObject();
		//定时器开关
		List<Trigger> triggerList = new ArrayList<>();
		for( Entry<String,Map<String,Object>> entry : cronExpressionList.entrySet() ) {
			String cronExpression = entry.getKey();
			//创建定时器开关
			CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
			factoryBean.setGroup( "procurement" );
			factoryBean.setJobDetail( jobDetail );
			factoryBean.setCronExpression( cronExpression );
			factoryBean.setName( UUID.randomUUID().toString() );
			factoryBean.setStartTime( now );
			//定时器数据
			JobDataMap jobDataMap = new JobDataMap();
			jobDataMap.putAll( entry.getValue() );
			factoryBean.setJobDataMap( jobDataMap   );
			factoryBean.afterPropertiesSet();
			triggerList.add( factoryBean.getObject() );
		}
		if( schedulerFactory != null ){
			schedulerFactory.destroy();
		}
		
		schedulerFactory = new SchedulerFactoryBean();//ApplicationContextUtils.getApplicationContext().getBean( SchedulerFactoryBean.class );
		//获取任务定时器
		Trigger[] triggerArr = new Trigger[ triggerList.size() ];
		while( triggerList.size() > 0 ){
			triggerArr[ triggerList.size() -1 ] = triggerList.remove( triggerList.size()-1 );
		}
		schedulerFactory.setTriggers( triggerArr  );
		schedulerFactory.setStartupDelay( 10000 );
		schedulerFactory.afterPropertiesSet();
		schedulerFactory.getScheduler().start();
	}
}
