package linayi.webwar;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.chanxa.linayi.bo.user.UserLocationBO;
import com.chanxa.linayi.service.community.CommunityService;
import com.chanxa.linayi.service.community.CommunitySupermarketService;
import com.chanxa.linayi.service.goods.CorrectService;
import com.chanxa.linayi.service.system.AreaService;
import com.chanxa.linayi.service.task.ProcurementTaskService;
import com.chanxa.linayi.service.user.UserLocationService;

@RunWith(SpringJUnit4ClassRunner.class) // 使用junit4进行测试
@ContextConfiguration(locations = { "classpath:conf/linayi-context.xml" })
public class AwakeTest {
	@Resource
	private ProcurementTaskService procurementTaskService;
	@Resource
	private CommunityService communityService;
	
	@Resource
	private CommunitySupermarketService communitySupermarketService;
	
	@Resource
	private UserLocationService userLocationService;
	
	@Resource
	private AreaService areaService;
	
//	@Test
//	public void updateGoodsSkuCorrect(){
////		correctService.acceptCorrectPriceAtferTwoHours();
//		UserLocationBO location = new UserLocationBO();
//		location.setAreaName("12巷3号楼");
//		location.setAreaCode("101903070000020008");
//		location.getUserId();
//		location.setNickname("李先生");
//		location.setMobile("15889513101");
//		userLocationService.add(location);
//	}
//	@Test
//	public void updateGoodsSkuCorrect1(){
//		correctService.updateSku(98);
//		 
//	}
//	@Test
//	public void updateCorrectIntoSkuSupermarket() {
//		
//	}
//	
//	
	@Test
	public void publishProcurementTask() {
		ArrayList<Integer> communityList = new ArrayList<Integer>();
		communityList.add(109);
		communityList.add(110);
		procurementTaskService.publishProcurementTask(communityList, 1, "MANUAL", 1);
	}
	
	
}
