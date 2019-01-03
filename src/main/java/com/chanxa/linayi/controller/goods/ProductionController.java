
/******************************************************
* Copyrights @ 2016，Chanxa Technology Co., Ltd.
*		linayi
* All rights reserved.
* 
* Filename：
*		ProductionController.java
* Description：
* 		产品，产品用于货品新增时带出货品的所有属性[例如荣耀5S中所有的属性]
类别->品牌->产品控制器
* Author:
*		chanxa
* Finished：
*		2017年11月20日
********************************************************/

package com.chanxa.linayi.controller.goods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.chanxa.dict.domain.DataDictionary;
import com.chanxa.linayi.bo.goods.ProductionAttributeBO;
import com.chanxa.linayi.bo.goods.ProductionBO;
import com.chanxa.linayi.common.bean.PageResult;
import com.chanxa.linayi.common.enums.ProductionAttributeSalesEnum;
import com.chanxa.linayi.common.enums.RemovedEnum;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.controller.CommonControllerSupport;
import com.chanxa.linayi.populator.goods.ProductionPopulator;
import com.chanxa.linayi.service.goods.BrandService;
import com.chanxa.linayi.service.goods.CategoryService;
import com.chanxa.linayi.service.goods.ProductionAttributeService;
import com.chanxa.linayi.service.goods.ProductionService;
import com.chanxa.linayi.service.system.OptionsService;

@Controller
@RequestMapping("/goods/production")
public class ProductionController extends CommonControllerSupport {
	@Resource
	private ProductionService productionService;

	@Resource
	private ProductionPopulator productionPopulator;

	@Resource
	private CategoryService categoryService;

	@Resource
	private BrandService brandService;

	@Resource
	private OptionsService optionsService;

	@Resource
	private ProductionAttributeService productionAttributeService;

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("production.");
	}

	/**
	 * 查询列表,分页
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object productionList(ProductionBO production, HttpServletRequest request) {
		production.setRemoved(RemovedEnum.FALSE.getValue());
		PageResult<ProductionBO> page = this.productionService.getPage(production);
		// 设置类别，和品牌
		productionPopulator.setCategoryAndBrand(page.getRows());
		return page;
	}

	/**
	 * 新增/更新production
	 * 
	 * @param production
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save(@RequestBody ProductionBO production, HttpServletRequest request) {
		production.setAdminId(getUserId(request));
		productionService.saveInWeb(production);
		return AjaxReturnMessage.createMessage("操作成功!", true);
	}

	/**
	 * 通过主键删除
	 * 
	 * @param productionId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Integer productionId) {
		ProductionBO production = new ProductionBO();
		production.setRemoved(RemovedEnum.TRUE.getValue());
		production.setProductionId(productionId);
		productionService.update(production);
		return AjaxReturnMessage.createMessage("操作成功!", true);
	}

	/**
	 * 通过主键获取对象
	 * 
	 * @param productionId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Integer productionId) {
		return this.productionService.get(productionId);
	}

	/**
	 * 编辑页面
	 * 
	 * @param productionId
	 * @return
	 */
	@RequestMapping("/edit.do")
	@ResponseBody
	public AjaxReturnMessage edit(Integer productionId) {
		Map<String, Object> result = new HashMap<String, Object>();
		ProductionBO production = null;
		if (productionId != null) {
			production = this.productionService.get(productionId);
			productionPopulator.setCategoryAndBrand(Arrays.asList(production));
			productionPopulator.setAttributeListAndSaleAttributeList(Arrays.asList(production));
		}

		Map<String, Object> params = new HashMap<>();
		params.put("level", 3);
		params.put("sqlId", "listCategory");
		List<DataDictionary> categoryList = (List<DataDictionary>) optionsService.listOptions(params);
		List<DataDictionary> brandList = new ArrayList<>();
		List<ProductionAttributeBO> attrList = new ArrayList<>();
		List<ProductionAttributeBO> salesAttrList = new ArrayList<>();
		if (production != null && production.getProductionId() != null) {

			params.clear();
			// params.put( "categoryId", production.getCategoryId() );
			// params.put( "sqlId", "listBrand" );
			// brandList = ( List<DataDictionary> )optionsService.listOptions(
			// params );
			// 获取规格属性和销售属性
			ProductionAttributeBO productionAttribute = new ProductionAttributeBO();
			productionAttribute.setProductionId(production.getProductionId());
			List<ProductionAttributeBO> productionAttributeList = productionAttributeService
					.getList(productionAttribute);
			// 分组
			Map<Integer, List<ProductionAttributeBO>> attrGroups = productionAttributeList.parallelStream()
					.collect(Collectors.groupingBy(ProductionAttributeBO::getSales));
			attrList = attrGroups.getOrDefault(ProductionAttributeSalesEnum.ATTR.getValue(), Collections.emptyList());
			salesAttrList = attrGroups.getOrDefault(ProductionAttributeSalesEnum.SALES.getValue(),
					Collections.emptyList());
		} else {
			production = new ProductionBO();
		}
		result.put("production", production);
		result.put("categoryList", categoryList);
		// result.put( "brandList", brandList );
		result.put("attrList",
				attrList.parallelStream().map(ProductionAttributeBO::getAttributeId).collect(Collectors.toList()));
		result.put("salesAttrList",
				salesAttrList.parallelStream().map(ProductionAttributeBO::getAttributeId).collect(Collectors.toList()));
		return AjaxReturnMessage.createMessage(result);
	}

	/**
	 * 详细页面
	 * 
	 * @param productionId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Integer productionId) {
		ModelAndView mv = new ModelAndView("goods/ProductionShow");
		if (productionId != null) {
			ProductionBO production = this.productionService.get(productionId);
			mv.addObject("production", production);
		}
		return mv;
	}

}
