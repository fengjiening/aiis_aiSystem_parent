package org.jeecg.modules.workday.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.workday.entity.AeWorkDay;
import org.jeecg.modules.workday.service.IAeWorkDayService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: ae_work_day
 * @Author: jeecg-boot
 * @Date:   2020-07-20
 * @Version: V1.0
 */
@Api(tags="ae_work_day")
@RestController
@RequestMapping("/workday/aeWorkDay")
@Slf4j
public class AeWorkDayController extends JeecgController<AeWorkDay, IAeWorkDayService> {
	@Autowired
	private IAeWorkDayService aeWorkDayService;
	
	/**
	 * 分页列表查询
	 *
	 * @param aeWorkDay
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "ae_work_day-分页列表查询")
	@ApiOperation(value="ae_work_day-分页列表查询", notes="ae_work_day-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(AeWorkDay aeWorkDay,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<AeWorkDay> queryWrapper = QueryGenerator.initQueryWrapper(aeWorkDay, req.getParameterMap());
		Page<AeWorkDay> page = new Page<AeWorkDay>(pageNo, pageSize);
		IPage<AeWorkDay> pageList = aeWorkDayService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param aeWorkDay
	 * @return
	 */
	@AutoLog(value = "ae_work_day-添加")
	@ApiOperation(value="ae_work_day-添加", notes="ae_work_day-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody AeWorkDay aeWorkDay) {
		aeWorkDayService.save(aeWorkDay);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param aeWorkDay
	 * @return
	 */
	@AutoLog(value = "ae_work_day-编辑")
	@ApiOperation(value="ae_work_day-编辑", notes="ae_work_day-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody AeWorkDay aeWorkDay) {
		aeWorkDayService.updateById(aeWorkDay);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "ae_work_day-通过id删除")
	@ApiOperation(value="ae_work_day-通过id删除", notes="ae_work_day-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		aeWorkDayService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "ae_work_day-批量删除")
	@ApiOperation(value="ae_work_day-批量删除", notes="ae_work_day-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.aeWorkDayService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "ae_work_day-通过id查询")
	@ApiOperation(value="ae_work_day-通过id查询", notes="ae_work_day-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		AeWorkDay aeWorkDay = aeWorkDayService.getById(id);
		if(aeWorkDay==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(aeWorkDay);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param aeWorkDay
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AeWorkDay aeWorkDay) {
        return super.exportXls(request, aeWorkDay, AeWorkDay.class, "ae_work_day");
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
        return super.importExcel(request, response, AeWorkDay.class);
    }

}
