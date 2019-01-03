
/******************************************************
* Copyrights @ 2016，Chanxa Technology Co., Ltd.
*		linayi
* All rights reserved.
* 
* Filename：
*		GoodsController.java
* Description：
* 		货品控制器
* Author:
*		chanxa
* Finished：
*		2017年11月20日
********************************************************/

package com.chanxa.linayi.controller.goods;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.chanxa.linayi.bo.goods.AttrValueBO;
import com.chanxa.linayi.bo.goods.AttributeBO;
import com.chanxa.linayi.bo.goods.BrandBO;
import com.chanxa.linayi.bo.goods.CategoryBO;
import com.chanxa.linayi.bo.goods.GoodsAttributeBO;
import com.chanxa.linayi.bo.goods.GoodsBO;
import com.chanxa.linayi.bo.goods.GoodsImageBO;
import com.chanxa.linayi.bo.goods.GoodsSkuBO;
import com.chanxa.linayi.bo.goods.ProductionAttributeBO;
import com.chanxa.linayi.bo.goods.ProductionBO;
import com.chanxa.linayi.bo.goods.SkuAttributeBO;
import com.chanxa.linayi.bo.goods.SortingCategoryBO;
import com.chanxa.linayi.common.bean.PageResult;
import com.chanxa.linayi.common.constant.Propert;
import com.chanxa.linayi.common.enums.GoodsStatusEnum;
import com.chanxa.linayi.common.enums.ProductionAttributeSalesEnum;
import com.chanxa.linayi.common.enums.RemovedEnum;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.common.util.CheckUtil;
import com.chanxa.linayi.common.util.Configuration;
import com.chanxa.linayi.common.util.FileUploadUtil;
import com.chanxa.linayi.common.util.ImageCompressUtil;
import com.chanxa.linayi.common.util.SimpleWorkBook;
import com.chanxa.linayi.controller.CommonControllerSupport;
import com.chanxa.linayi.populator.goods.AttributePopulator;
import com.chanxa.linayi.populator.goods.GoodsPopulator;
import com.chanxa.linayi.populator.goods.SkuAttributePopulator;
import com.chanxa.linayi.service.goods.AttrValueService;
import com.chanxa.linayi.service.goods.AttributeService;
import com.chanxa.linayi.service.goods.BrandService;
import com.chanxa.linayi.service.goods.CategoryService;
import com.chanxa.linayi.service.goods.GoodsAttributeService;
import com.chanxa.linayi.service.goods.GoodsImageService;
import com.chanxa.linayi.service.goods.GoodsService;
import com.chanxa.linayi.service.goods.GoodsSkuService;
import com.chanxa.linayi.service.goods.ProductionAttributeService;
import com.chanxa.linayi.service.goods.ProductionService;
import com.chanxa.linayi.service.goods.SkuAttributeService;
import com.chanxa.linayi.service.goods.SortingCategoryService;

@Controller
@RequestMapping("/goods/goods")
public class GoodsController extends CommonControllerSupport {
	@Resource
	private GoodsService goodsService;

	@Resource
	private GoodsImageService goodsImageService;

	@Resource
	private SortingCategoryService sortingCategoryService;

	@Resource
	private GoodsPopulator goodsPopulator;

	@Resource
	private GoodsAttributeService goodsAttributeService;

	@Resource
	private ProductionService productionService;

	@Resource
	private ProductionAttributeService productionAttributeService;

	@Resource
	private AttributeService attributeService;

	@Resource
	private AttributePopulator attributePopulator;

	@Resource
	private AttrValueService attrValueService;

	@Resource
	private GoodsSkuService goodsSkuService;

	@Resource
	private SkuAttributeService skuAttributeService;

	@Resource
	private SkuAttributePopulator skuAttributePopulator;
	
	@Resource
	private BrandService brandService;
	
	@Resource
	private CategoryService categoryService;
	

	@InitBinder
	public void initBind(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("goods.");
	}
	

	@RequestMapping("/downloadErrorData.do")
	public void downloadErrorData(String fileName, HttpServletResponse response) throws IOException {
		fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");

		response.reset();
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName + "");
		response.setContentType("application/octet-stream;charset=UTF-8");
		File errorDataFile = new File(Configuration.getConfig().getValue(Propert.IMAGE_PATH) + "/" + fileName);
		try (FileInputStream inputStream = new FileInputStream(errorDataFile);) {
			byte[] buff = new byte[inputStream.available()];
			inputStream.read(buff);
			response.getOutputStream().write(buff);
		}
	}

	/**
	 * 导入
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/importGoods.do")
	@ResponseBody
	public Object importGoods(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		try {
			String prefix = Configuration.getConfig().getValue(Propert.IMAGE_PATH);
			String filePath = FileUploadUtil.uploadFile(file, prefix);
			List<String[]> data = new ArrayList<String[]>();
			File errorDataFile = new File(
					Configuration.getConfig().getValue(Propert.IMAGE_PATH) + "/" + UUID.randomUUID() + ".xls");
			if (CheckUtil.isNotNullEmpty(filePath)) {
				Map<String, Object> result = goodsService.readExcel(prefix + "/" + filePath,request,getUserId(request));
				// 导入数据库
				goodsService.importGoods((List<GoodsBO>) result.get("resultList"), getUserId(request));
				// 生成错误信息表
				SimpleWorkBook book = new SimpleWorkBook("商品导入错误记录表");
				book.addFiled(new String[] { "商品编号", "商品标题", "商品副标题","商品品牌","产品名称","销售属性","规格","一级类别","二级类别","三级类别", "错误信息" }, 0, 0);
				List<Row> rowList = (List<Row>) result.get("errorResultList");
				int totalRows = rowList.size();
				while (--totalRows >= 0) {
					Row row = rowList.get(totalRows);
					String[] rowData = new String[11];
					data.add(rowData);
					for (int i = 0; i < 11; i++) {
						rowData[i] = goodsService.getCellStringValue(row.getCell(i));
					}
				}
				book.addData(data, 0, 1);
				try (OutputStream out = new FileOutputStream(errorDataFile);) {
					if (!errorDataFile.getParentFile().exists()) {
						errorDataFile.getParentFile().mkdirs();
					}
					out.write(book.getBuff());
				}
			}
			Map<String, Object> result = new HashMap<String, Object>();
			if (data.size() > 0) {
				result.put("file", errorDataFile.getName());
			} else {
				result.put("file", "");
			}
			result.put("data", data);
			return AjaxReturnMessage.createMessage("操作成功！", true, result);

		} catch (Exception e) {
			return AjaxReturnMessage.createMessage(false);
		}
	}

	/**
	 * 上架/下架
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/changeStatus.do")
	@ResponseBody
	public Object changeStatus(GoodsBO goods) {
		goodsService.update(goods);
		return AjaxReturnMessage.createMessage(true);
	}

	/**
	 * 查询列表,分页
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object goodsList(GoodsBO goods, HttpServletRequest request) {
		goods.setRemoved(RemovedEnum.FALSE.getValue());
		PageResult<GoodsBO> page = this.goodsService.getPage(goods);
		List<GoodsBO> goodsList = page.getRows();
		// 设置类别名称和品牌名称
		goodsPopulator.setBrandNameAndCategoryName(goodsList);
		// 设置状态名称
		goodsPopulator.setStatusName(goodsList);
		// 设置操作员
		goodsPopulator.setAdminName(goodsList);
		// 设置图片
		goodsPopulator.setImagePath(goodsList);
		String iamgeServer = Configuration.getConfig().getValue(Propert.IMAGE_SERVER);
		goodsList.parallelStream().forEach(gItem -> {
			gItem.setImagePath(iamgeServer + "/" + gItem.getImagePath());
		});
		
		return page;
	}

	/**
	 * 新增/更新goods
	 * 
	 * @param request
	 * @param goods
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	public Object save(@RequestParam("goods") String goodsStr, @RequestParam("imageList") MultipartFile[] imageList,
			@RequestParam("firstImages") MultipartFile firstImages, HttpServletRequest request) throws Exception {
		GoodsBO goods = JSONObject.parseObject(goodsStr, GoodsBO.class);
		// 新增的时候
		goods.setAdminId(getUserId(request));
		// 商品查重
		if (goods.getGoodsId() == null) {
			GoodsBO goodsBO = new GoodsBO();
			goodsBO.setSortingCategoryId(goods.getSortingCategoryId());
			goodsBO.setGoodsName(goods.getGoodsName());
			goodsBO.setRemoved(RemovedEnum.FALSE.getValue());
			List<GoodsBO> goodsList = goodsService.getList(goodsBO);
			if (goodsList.size() > 0) {
				for (GoodsBO goodsBO2 : goodsList) {
					Integer goodsId = goodsBO2.getGoodsId();
					GoodsSkuBO goodsSku = new GoodsSkuBO();
					goodsSku.setGoodsId(goodsId);
					List<GoodsSkuBO> list = goodsSkuService.getList(goodsSku);
					for (GoodsSkuBO goodsSkuBO : list) {
						//由于goods_sku是根据商品名+各种规格属性名拼接而成,查重只需要修改名即可
						StringBuilder goodsName = new StringBuilder();
						goodsName = goodsName.append(goods.getGoodsName());
						 List<GoodsSkuBO> goodsSkuList = goods.getGoodsSkuList();
						 for (GoodsSkuBO goodsSkuBO2 : goodsSkuList) {
							 List<SkuAttributeBO> skuAttributeList = goodsSkuBO2.getSkuAttributeList();
							 for (SkuAttributeBO skuAttributeBO : skuAttributeList) {
								 goodsName.append(" "+ skuAttributeBO.getValueName());
							}
						}
						 String name = goodsName.toString();
						if(name.equals(goodsSkuBO.getGoodsName())){
							return AjaxReturnMessage.createMessage("请勿添加重复商品", false);
						}
					}
					
				}
			}
			
			
			
			
			
			
			
			
			
			
			
			
//			if (goodsList.size() > 0) {
//				for (GoodsBO goodsBO2 : goodsList) {
//					GoodsAttributeBO goodsAttribute = new GoodsAttributeBO();
//					goodsAttribute.setGoodsId(goodsBO2.getGoodsId());
//					List<GoodsAttributeBO> goodsAttributeList = goodsAttributeService.getList(goodsAttribute);
//					if (goodsAttributeList.size() > 0) {
//						// 比对商品规格
//						for (GoodsAttributeBO goodsAttributeBO : goodsAttributeList) {
//							AttributeBO attribute = attributeService.get(goodsAttributeBO.getAttributeId());
//							List<AttributeBO> attributeList2 = goods.getAttributeList();
//							for (AttributeBO attributeBO : attributeList2) {
//								if (attributeBO.getName().equals(attribute.getName())) {
//									AttrValueBO attrValue = new AttrValueBO();
//									attrValue.setAttributeId(attribute.getAttributeId());
//									attrValue.setRemoved(RemovedEnum.FALSE.getValue());
//									List<AttrValueBO> attrValueList = attrValueService.getList(attrValue);
//									List<AttrValueBO> attrValueList2 = attributeBO.getAttrValueList();
//									boolean againResult = false;
//									if (attrValueList2.size() == attrValueList.size())
//										for (int i = 0; i < attrValueList.size(); i++) {
// 											if (!attrValueList2.get(i).getName()
//													.equals(attrValueList.get(i).getName())) {
//												againResult = false;
//											} else {
//												againResult = true;
//											}
//										}
//									if (againResult) {
//										return AjaxReturnMessage.createMessage("请勿添加重复商品", false);
//									}
//								}
//							}
//
//							// 获取属性值列表
//							// Map<String, List<AttributeBO>> collectMap =
//							// attributeList.stream().collect(Collectors.groupingBy(AttributeBO::getName));
//
//						}
//					}
//				}
//
//			}
		}
		// if(goodsService.getList(goodsBO).size()>0){
		// }

		// 上传图片并去除图片域名路径
		List<GoodsImageBO> goodsImageList = goods.getGoodsImageList();
		GoodsImageBO goodsImage = null;
		String prefix = Configuration.getConfig().getValue(Propert.IMAGE_PATH);
		String imageServer = Configuration.getConfig().getValue(Propert.IMAGE_SERVER) + "/";
		// 列表图片操作
		for (int i = 0; i < imageList.length; i++) {
			goodsImage = goodsImageList.get(i);
			goodsImage.setFirstImage(2 + "");
			MultipartFile image = imageList[i];
			if (image.getSize() > 0) {
				// 图片大于1M就压缩
				if (image.getSize() > 1024000) {
					File f = File.createTempFile("jpg", null);
					image.transferTo(f);
					// 写文件到服务器
					String prePath = ImageCompressUtil.saveMinPhoto(f.getAbsolutePath(), 1300, 0.9);
					String wrongSuffix = ImageCompressUtil.uploadFile(new File(prePath), prefix);
					String imagePath = wrongSuffix.replace("tmp", "jpg");
					boolean renameTo = new File(prefix + "/" + wrongSuffix)
							.renameTo(new File(prefix + "/" + imagePath));

					goodsImage.setImagePath(imagePath);
					goodsImageList.set(i, goodsImage);
				} else {
					String imagePath = FileUploadUtil.uploadFile(image, prefix);
					goodsImage.setImagePath(imagePath);
					goodsImageList.set(i, goodsImage);
				}
			} else {
				if (CheckUtil.isNotNullEmpty(goodsImage.getImagePath())) {
					goodsImage.setImagePath(goodsImage.getImagePath().replace(imageServer, ""));
				}
			}

		}

		// 主图片操作
		GoodsImageBO goodImageFirst = goods.getGoodImageFirst();
		goodImageFirst.setFirstImage(1 + "");
		if (firstImages.getSize() > 0) {
			if (firstImages.getSize() > 1024000) {
				File f = File.createTempFile("jpg", null);
				firstImages.transferTo(f);
				// 写文件到服务器
				String prePath = ImageCompressUtil.saveMinPhoto(f.getAbsolutePath(), 1300, 0.9);
				String wrongSuffix = ImageCompressUtil.uploadFile(new File(prePath), prefix);
				String imagePath = wrongSuffix.replace("tmp", "jpg");
				boolean renameTo = new File(prefix + "/" + wrongSuffix).renameTo(new File(prefix + "/" + imagePath));
				goodImageFirst.setImagePath(imagePath);
			} else {
				String imagePath = FileUploadUtil.uploadFile(firstImages, prefix);
				goodImageFirst.setImagePath(imagePath);
			}
		} else {
			if (CheckUtil.isNotNullEmpty(goodImageFirst.getImagePath())) {
				goodImageFirst.setImagePath(goodImageFirst.getImagePath().replace(imageServer, ""));
			}
		}

		goods.setGoodImageFirst(goodImageFirst);
		goods.setGoodsImageList(goodsImageList);
		List<GoodsSkuBO> goodsSkuList = goods.getGoodsSkuList();
		List<String> list = new ArrayList<String>();
		for (GoodsSkuBO goodsSku : goodsSkuList) {
			if (goodsSku.getBarcode() != null) {				
				String barcode = goodsSku.getBarcode().replace(" ", "");				
				for (String li : list) {
					if (barcode.equals(li)) {
						return AjaxReturnMessage.createMessage("商品条码重复", false);
					}
				}
				GoodsSkuBO bo = goodsSkuService.getGoodsSkuByBarCode(barcode);
				if (bo != null && !bo.getGoodsSkuId().equals(goodsSku.getGoodsSkuId())) {
					return AjaxReturnMessage.createMessage("商品条码已经存在", false);
				}
				list.add(barcode);				
				if ((barcode != null && barcode.length() != 13) && (!"".equals(barcode))) {
					return AjaxReturnMessage.createMessage("商品条码长度必须是13位", false);
				}
			}
		}
		goodsService.saveInWeb(goods);

		return AjaxReturnMessage.createMessage("操作成功!", true);
	}

	/**
	 * 通过主键删除
	 * 
	 * @param goodsId
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Object delete(Integer goodsId) {
		this.goodsService.delete(goodsId);
		return AjaxReturnMessage.createMessage("操作成功!", true);
	}

	/**
	 * 通过主键获取对象
	 * 
	 * @param goodsId
	 * @return
	 */
	@RequestMapping("/get.do")
	@ResponseBody
	public Object get(Integer goodsId) {
		return this.goodsService.get(goodsId);
	}

	/**
	 * 编辑页面
	 * 
	 * @param goodsId
	 * @return
	 */
	@RequestMapping("/edit.do")
	@ResponseBody
	public Object edit(GoodsBO goods) {
		/* 如果新增商品重复即返回 */
		// 图片服务器路径
		String imageServer = Configuration.getConfig().getValue(Propert.IMAGE_SERVER);
		Integer goodsId = goods.getGoodsId();
		Map<String, Object> result = new HashMap<>();
		GoodsImageBO goodImageFirst = new GoodsImageBO();
		goodImageFirst.setGoodsId(goodsId);
		if (goodsId != null) {
			goods = this.goodsService.get(goodsId);
			// 主图
			List<GoodsImageBO> ImageFirstlist = goodsImageService.getImageFirst(goodImageFirst);
			if (ImageFirstlist.size() > 0) {
				goodImageFirst = ImageFirstlist.get(0);
				goodImageFirst.setImagePath(imageServer + "/" + goodImageFirst.getImagePath());
			}
			// 获取商品图片
			GoodsImageBO goodsImage = new GoodsImageBO();
			goodsImage.setGoodsId(goodsId);
			List<GoodsImageBO> goodsImageList = goodsImageService.getList(goodsImage);
			goods.setGoodsImageList(goodsImageList);
			// 获取推荐商品
			GoodsBO recommendGoodsParam = new GoodsBO();
			recommendGoodsParam.setGoodsId(goods.getGoodsId());
			recommendGoodsParam.setRemoved(RemovedEnum.FALSE.getValue());
			goodsPopulator.setRecommendGoodsList(recommendGoodsParam);
			goods.setRecommendGoodsList(recommendGoodsParam.getRecommendGoodsList());
			// 设置图片路径
			goodsImageList.parallelStream().forEach(item -> {
				if (CheckUtil.isNotNullEmpty(item.getImagePath())) {
					item.setImagePath(imageServer + "/" + item.getImagePath());
				}
			});
			goods.getRecommendGoodsList().forEach(item -> {
				if (CheckUtil.isNotNullEmpty(item.getImagePath())) {
					item.setImagePath(imageServer + "/" + item.getImagePath());
				}
			});
			// 获取商品规格,商品销售属性
			GoodsAttributeBO goodsAttribute = new GoodsAttributeBO();
			goodsAttribute.setGoodsId(goodsId);
			// 以是否销售属性分组，规格一组，销售属性一组
			Map<Integer, List<GoodsAttributeBO>> goodsAttributeGroups = goodsAttributeService.getList(goodsAttribute)
					.parallelStream()//
					.collect(Collectors.groupingBy(GoodsAttributeBO::getSales));
			// 设置规格和销售属性
			for (Entry<Integer, List<GoodsAttributeBO>> entry : goodsAttributeGroups.entrySet()) {
				Integer key = entry.getKey();
				List<GoodsAttributeBO> value = entry.getValue();
				// 以规格ID分组
				Map<Integer, List<GoodsAttributeBO>> attrGroups = value.parallelStream()
						.collect(Collectors.groupingBy(GoodsAttributeBO::getAttributeId));
				AttributeBO attribute = new AttributeBO();
				attribute.setAttributeIdList(value.parallelStream()//
						.map(GoodsAttributeBO::getAttributeId)//
						.collect(Collectors.toList()) //
				);
				if (attribute.getAttributeIdList().isEmpty()) {
					continue;
				}
				List<AttributeBO> attributeList = attributeService.getList(attribute);
				attributePopulator.setAttrValueList(attributeList, true);
				// 如果是规格则执行
				if (ProductionAttributeSalesEnum.ATTR.eq(key)) {
					goods.setAttributeList(attributeList);
					// 设置已选中的规格值
					for (AttributeBO aItem : attributeList) {
						GoodsAttributeBO gaItem = attrGroups
								.getOrDefault(aItem.getAttributeId(), Collections.emptyList()).parallelStream()
								.findFirst().orElse(null);
						if (gaItem != null) {
							AttrValueBO attrValue = aItem.getAttrValueList().parallelStream().filter(item -> {
								return item.getAttrValueId().equals(gaItem.getAttrValueId());
							}).findFirst().orElse(new AttrValueBO());
							aItem.setAttrValue(attrValue);
						}

					}
					// 如果是销售属性则执行
				} else {
					goods.setSalesAttributeList(attributeList);
					// 设置规格值是否选中
					GoodsBO goodsParam = new GoodsBO();
					goodsParam.setGoodsId(goodsId);
					goodsParam.setRemoved(RemovedEnum.FALSE.getValue());
					Map<Integer, List<AttrValueBO>> attrValueGroups = attrValueService
							.listGoodsSaleAttrValueList(goodsParam).parallelStream()
							.collect(Collectors.groupingBy(AttrValueBO::getAttrValueId));
					for (AttributeBO attr : attributeList) {
						List<AttrValueBO> valueList = attr.getAttrValueList();
						for (AttrValueBO val : valueList) {
							// 用户标记复选框是否被选中
							val.setRemoved(attrValueGroups.getOrDefault(val.getAttrValueId(), null) == null ? 1 : 2);
						}
					}
					// 获取销售属性组合，规格组合的配送费和采买费
					GoodsSkuBO goodsSku = new GoodsSkuBO();
					goodsSku.setGoodsId(goodsId);
					List<GoodsSkuBO> goodsSkuList = goodsSkuService.getList(goodsSku);
					for (GoodsSkuBO gsItem : goodsSkuList) {
						SkuAttributeBO skuAttribute = new SkuAttributeBO();
						skuAttribute.setGoodsSkuId(gsItem.getGoodsSkuId());
						List<SkuAttributeBO> skuAttributeList = skuAttributeService.getList(skuAttribute);
						gsItem.setSkuAttributeList(skuAttributeList);
						// 设置属性值
						skuAttributePopulator.setValueName(skuAttributeList);
					}
					goods.setGoodsSkuList(goodsSkuList);
				}
			}
			;
		} else {
			ProductionBO production = productionService.get(goods.getProductionId());
			if (production == null) {
				return AjaxReturnMessage.createMessage("产品不存在", false);
			}
			goods.setGoodsName(production.getTitle());
			goods.setCategoryId(production.getCategoryId());
			goods.setBrandId(production.getBrandId());
			// 获取产品
			ProductionAttributeBO productionAttribute = new ProductionAttributeBO();
			productionAttribute.setProductionId(goods.getProductionId());
			// 获取产品属性，并以是否销售属性分组，规格一组，销售属性一组
			Map<Integer, List<ProductionAttributeBO>> groups = productionAttributeService.getList(productionAttribute)//
					.parallelStream().collect(Collectors.groupingBy(ProductionAttributeBO::getSales));
			for (Entry<Integer, List<ProductionAttributeBO>> entry : groups.entrySet()) {
				Integer key = entry.getKey();
				List<ProductionAttributeBO> value = entry.getValue();
				List<Integer> attributeIdList = value.parallelStream()//
						.map(ProductionAttributeBO::getAttributeId)//
						.collect(Collectors.toList());
				if (attributeIdList.isEmpty()) {
					continue;
				}
				// 设置属性值列表
				AttributeBO attribute = new AttributeBO();
				attribute.setAttributeIdList(attributeIdList);
				attribute.setRemoved(RemovedEnum.FALSE.getValue());
				List<AttributeBO> attributeList = attributeService.getList(attribute);
				attributePopulator.setAttrValueList(attributeList, false);
				// 如果是规格属性则执行
				if (ProductionAttributeSalesEnum.ATTR.eq(key)) {
					goods.setAttributeList(attributeList);
					// 如果是销售属性则执行
				} else {
					goods.setSalesAttributeList(attributeList);
				}
				// 设置销售属性不选中
				attributeList.forEach(aItem -> {
					aItem.getAttrValueList().parallelStream().forEach(vItem -> {
						vItem.setRemoved(RemovedEnum.TRUE.getValue());
					});
				});
			}
		}
		while (goods.getGoodsImageList().size() < 5) {
			goods.getGoodsImageList().add(new GoodsImageBO());
		}
		goods.setGoodImageFirst(goodImageFirst);
		result.put("goods", goods);
		// 获取分拣类别
		List<SortingCategoryBO> sortingCategoryList = sortingCategoryService.getList(new SortingCategoryBO());
		result.put("sortingCategoryList", sortingCategoryList);
		return AjaxReturnMessage.createMessage(true, result);
	}

	/**
	 * 详细页面
	 * 
	 * @param goodsId
	 * @return
	 */
	@RequestMapping("/show.do")
	public ModelAndView show(Integer goodsId) {
		ModelAndView mv = new ModelAndView("goods/GoodsShow");
		if (goodsId != null) {
			GoodsBO goods = this.goodsService.get(goodsId);
			mv.addObject("goods", goods);
		}		
		return mv;
	}	
	
	
}
