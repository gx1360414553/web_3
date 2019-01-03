
package com.chanxa.linayi.controller.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chanxa.linayi.common.util.AjaxReturnMessage;
import com.chanxa.dict.util.DictionaryUtil;

/**
 * <p>Project:  <p>
 * <p>Module:			<p>
 * <p>Description:		<p>
 *
 * @author jobd huang
 * @date 2017年6月27日 上午9:16:51
 */
@Controller
@RequestMapping("/system/dict")
public class DictionaryController {

	@RequestMapping("/reload")
	@ResponseBody
	public AjaxReturnMessage reload(){
		DictionaryUtil.reload();
		return AjaxReturnMessage.createMessage( true );
	}
	
}
