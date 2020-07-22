package org.jeecg.modules.attendence.controller;

import java.util.Arrays;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.attendence.entity.AeDailySign;
import org.jeecg.modules.attendence.service.IAeDailySignService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 考勤数据
 * @Author: jeecg-boot
 * @Date:   2020-06-28
 * @Version: V1.0
 */
@Api(tags="考勤数据")
@RestController
@RequestMapping("/attendence/aeDailySign")
@Slf4j
public class AeDailySignController extends JeecgController<AeDailySign, IAeDailySignService> {
	@Autowired
	private IAeDailySignService aeDailySignService;
	
	/**
	 * 分页列表查询
	 *
	 * @param aeDailySign
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "考勤数据-分页列表查询")
	@ApiOperation(value="考勤数据-分页列表查询", notes="考勤数据-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(AeDailySign aeDailySign,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<AeDailySign> queryWrapper = QueryGenerator.initQueryWrapper(aeDailySign, req.getParameterMap());
		Page<AeDailySign> page = new Page<AeDailySign>(pageNo, pageSize);
		IPage<AeDailySign> pageList = aeDailySignService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param aeDailySign
	 * @return
	 */
	@AutoLog(value = "考勤数据-添加")
	@ApiOperation(value="考勤数据-添加", notes="考勤数据-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody AeDailySign aeDailySign) {
		aeDailySignService.save(aeDailySign);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param aeDailySign
	 * @return
	 */
	@AutoLog(value = "考勤数据-编辑")
	@ApiOperation(value="考勤数据-编辑", notes="考勤数据-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody AeDailySign aeDailySign) {
		aeDailySign.setHasModify(1);
		aeDailySign.setUpdateTime(new Date());
		aeDailySignService.updateById(aeDailySign);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "考勤数据-通过id删除")
	@ApiOperation(value="考勤数据-通过id删除", notes="考勤数据-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		aeDailySignService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "考勤数据-批量删除")
	@ApiOperation(value="考勤数据-批量删除", notes="考勤数据-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.aeDailySignService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "考勤数据-通过id查询")
	@ApiOperation(value="考勤数据-通过id查询", notes="考勤数据-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		AeDailySign aeDailySign = aeDailySignService.getById(id);
		if(aeDailySign==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(aeDailySign);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param aeDailySign
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AeDailySign aeDailySign) {
        return super.exportXls(request, aeDailySign, AeDailySign.class, "考勤数据");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, AeDailySign.class);
    }

}
