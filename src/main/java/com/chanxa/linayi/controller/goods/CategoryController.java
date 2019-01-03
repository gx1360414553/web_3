
 /******************************************************
 * Copyrights @ 2016，Chanxa Technology Co., Ltd.
 *		linayi
 * All rights reserved.
 * 
 * Filename：
 *		CategoryController.java
 * Description：
 * 		类别，类别->品牌->产品控制器
 * Author:
 *		chanxa
 * Finished：
 *		2017年11月20日
 ********************************************************/

package com.chanxa.linayi.controller.goods;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.chanxa.linayi.controller.CommonControllerSupport;
import com.alibaba.fastjson.JSONObject;
import com.chanxa.linayi.bo.goods.CategoryBO;
import com.chanxa.linayi.bo.goods.GoodsBO;
import com.chanxa.linayi.common.constant.Propert;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.common.util.CheckUtil;
import com.chanxa.linayi.common.util.Configuration;
import com.chanxa.linayi.common.util.FileUploadUtil;
import com.chanxa.linayi.service.goods.CategoryService;
import com.chanxa.linayi.service.goods.GoodsService;

@Controller
@RequestMapping("/goods/category")
public class CategoryController extends CommonControllerSupport {
	@Resource
	private CategoryService categoryService;

	

	
	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("category.");
	}

	/**
	 * 查询列表,分页
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object categoryList(CategoryBO category,HttpServletRequest request) {
		List<CategoryBO> categoryList = this.categoryService.getList( category );
		String imageServer = Configuration.getConfig().getValue( Propert.IMAGE_SERVER );
		categoryList.parallelStream().forEach( item->{
			if( CheckUtil.isNotNullEmpty( item.getLogo() ) ){
				item.setLogo( imageServer +"/"+item.getLogo() );
			}
		} );
		List<CategoryBO> rootList = categoryList.parallelStream().filter( item->{
											return item.getParentId().equals( -1 );
										} ).collect( Collectors.toList() );
		buildTree( rootList, categoryList );
		return rootList;
	}

	public void buildTree( List<CategoryBO> rootList, List<CategoryBO> categoryList ){
		rootList.parallelStream().forEach( aItem->{
			aItem.setChildren( 
					categoryList.parallelStream().filter( item->{
						return aItem.getCategoryId().equals( item.getParentId() );
					} ).collect( Collectors.toList() )
			);
			buildTree( aItem.getChildren(), categoryList );
		} );
	}
	
	/**
	 * 新增/更新category
	 * @param category
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save( @RequestParam("category") String categoryStr, @RequestParam("logo")MultipartFile file ) {
		CategoryBO category = JSONObject.parseObject( categoryStr, CategoryBO.class );	
		if( file != null && file.getSize() > 0 ){
			String logo = FileUploadUtil.uploadFile( file, Configuration.getConfig().getValue( Propert.IMAGE_PATH ) );
			category.setLogo( logo );
		}
		if( CheckUtil.isNotNullEmpty( category.getLogo() ) ){
			CharSequence imageServer = Configuration.getConfig().getValue( Propert.IMAGE_SERVER );
			category.setLogo( category.getLogo().replace( imageServer+"/" ,"" ) );
		}
		if( category.getCategoryId() != null ){
			this.categoryService.update(category);
		}else{
			this.categoryService.add(category);
		}
		return AjaxReturnMessage.createMessage("操作成功",true);
	}

	/**
	 * 通过主键删除
	 * @param categoryId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(String categoryIdList) {
		return categoryService.deleteByCategoryId(categoryIdList);		
	}

	/**
	 * 通过主键获取对象
	 * @param categoryId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Integer categoryId) {
		return this.categoryService.get(categoryId);
	}

	/**
	 * 编辑页面
	 * @param categoryId
	 * @return
	 */
	@RequestMapping("/edit.do")
	public ModelAndView edit(Integer categoryId) {
		ModelAndView mv = new ModelAndView("goods/CategoryEdit");
		if (categoryId !=null) {
			CategoryBO category = this.categoryService.get(categoryId);
			mv.addObject("category", category);
		}
		return mv;
	}
	
	/**
	 * 详细页面
	 * @param categoryId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Integer categoryId) {
		ModelAndView mv = new ModelAndView("goods/CategoryShow");
		if (categoryId !=null) {
			CategoryBO category = this.categoryService.get(categoryId);
			mv.addObject("category", category);
		}
		return mv;
	}

}
