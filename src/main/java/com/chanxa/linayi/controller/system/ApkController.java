package com.chanxa.linayi.controller.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chanxa.linayi.bo.system.ApkInfo;
import com.chanxa.linayi.bo.system.ApkInfo.Android;
import com.chanxa.linayi.common.constant.Propert;
import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.linayi.common.util.CheckUtil;
import com.chanxa.linayi.common.util.Configuration;
import com.chanxa.linayi.common.util.DateTimeUtils;

import net.dongliu.apk.parser.ApkParser;

/**
 * <p>Project:  <p>
 * <p>Module:			<p>
 * <p>Description:		<p>
 *
 * @author jobd huang
 * @date 2016年11月17日 上午9:33:18
 */
@RequestMapping("/system/apk/")
@Controller
public class ApkController {
	
	Logger logger = LoggerFactory.getLogger( ApkController.class );
	
	@RequestMapping("uploadApk")
	@ResponseBody
	public AjaxReturnMessage uploadApk( @RequestParam("file")CommonsMultipartFile file ) throws IOException{
		try{
			//获取配置
			String apkDiskPath = Configuration.getConfig().getValue( Propert.APK_DISK_PATH );
			String apkDownloadPath = Configuration.getConfig().getValue( Propert.APK_DOWNLOAD_PATH );
			String apkVersionFilePre = Configuration.getConfig().getValue( Propert.APK_VERSION_FILE_PRE );
			String apkNameFilePre = Configuration.getConfig().getValue( Propert.APK_NAME_FILE_PRE );
			String apkPackage = Configuration.getConfig().getValue( Propert.APK_PACKAGE );
			//上传文件
			File apkFile = new File( apkDiskPath +"\\"+DateTimeUtils.formatDate( "yyyy-MM-dd-HH-mm-ss", new Date() )+".apk" );
			if( !apkFile.exists() ){
				apkFile.getParentFile().mkdirs();
			}
			file.transferTo( apkFile );
			//解析apk
			ApkParser apkParser = new ApkParser( apkFile );
			String versionName = apkParser.getApkMeta().getVersionName();
			Long versionCode = apkParser.getApkMeta().getVersionCode();
			String packageName = apkParser.getApkMeta().getPackageName().toLowerCase();
			Long size = apkFile.length();
			apkParser.close();
			//获取后缀，兼容多端app
			String suffix = "";
			if( CheckUtil.isNotNullEmpty( apkNameFilePre ) && CheckUtil.isNotNullEmpty( apkPackage ) ){
				String[] packageList = apkPackage.split( "," );
				for( String packageInof : packageList ) {
					String[] packageParts = packageInof.split( ":" );
					if( packageParts[0].toLowerCase().equals( packageName ) ){
						suffix = "_"+packageParts[1];
						break;
					}
					
				}
			}
			//获取配置文件
			File versionFile = new File( apkDiskPath + "\\" + apkVersionFilePre+ suffix +".txt" );
			ApkInfo apkInfo = new ApkInfo();
			if( versionFile.exists() ){
				FileInputStream in = new FileInputStream( versionFile );
				byte[] versionBuffIn = new byte[in.available()];
				in.read( versionBuffIn );
				in.close();
				if( versionBuffIn.length > 0 ){
					String versionContent = new String( versionBuffIn,"utf-8" );
					apkInfo = JSONObject.parseObject( versionContent,ApkInfo.class );
				}
			}else{
				versionFile.createNewFile();
			}
			Android android = apkInfo.getAndroid();
			android.setVersionName( versionName );
			android.setVersionCode( versionCode );
			android.setSize( size );
			android.setDownloadUrl( apkDownloadPath + "/" + apkNameFilePre + suffix +".apk" );
			
			Map<String,Object> result = new HashMap<String,Object>();
			result.put( "versionPath", versionFile.getAbsoluteFile() );
			result.put( "sourcePath", apkFile.getAbsolutePath() );
			result.put( "targetPath", apkDiskPath + "\\" + apkNameFilePre + suffix +".apk" );
			result.put( "apkInfo", apkInfo );
			return AjaxReturnMessage.createMessage( "上传成功！",true, result );
		}catch( Exception e ){
			logger.error( "apk包解析出错->",e );
			return AjaxReturnMessage.createMessage( "apk无法解析", false );
		}
	}
	
	
	/**确认上传并修改版本文件信息
	 * @param download_url 
	 * @param version 
	 * @param logo_url 
	 * @param share_link 
	 * @param share_title 
	 * @throws IOException 
	 * @throws UnsupportedEncodingException */
	@RequestMapping("updateApkInfo")
	@ResponseBody
	public AjaxReturnMessage updateApkInfo( ApkInfo apkInfo, String sourcePath, String targetPath, String versionPath ) throws UnsupportedEncodingException, IOException{
		File sourceFile = new File( sourcePath );
		Integer index = sourceFile.getName().lastIndexOf( "." );
		File newFile = new File( sourceFile.getParentFile().getAbsolutePath() +"\\" + sourceFile.getName().substring( 0,index )+ "-"+apkInfo.getAndroid().getVersionName() +".apk" );
		sourceFile.renameTo( newFile );
		FileUtils.copyFile( newFile, new File( targetPath )  );
		//修改配置文件
		File versionFile = new File( versionPath );
		FileOutputStream out = new FileOutputStream( versionFile );
		out.write( JSONObject.toJSONString( apkInfo ).getBytes("utf-8") );
		out.close();
		return AjaxReturnMessage.createMessage( "操作成功!",true );
	}
}
