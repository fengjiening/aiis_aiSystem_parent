package org.jeecg.modules.staff.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.staff.entity.RegisteredInfo;
import org.jeecg.modules.staff.service.IRegisteredInfoService;

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
 * @Description: 考勤员工
 * @Author: jeecg-boot
 * @Date:   2020-07-02
 * @Version: V1.0
 */
@Api(tags="考勤员工")
@RestController
@RequestMapping("/staff/registeredInfo")
@Slf4j
public class RegisteredInfoController extends JeecgController<RegisteredInfo, IRegisteredInfoService> {
	@Autowired
	private IRegisteredInfoService registeredInfoService;
	
	/**
	 * 分页列表查询
	 *
	 * @param registeredInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "考勤员工-分页列表查询")
	@ApiOperation(value="考勤员工-分页列表查询", notes="考勤员工-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(RegisteredInfo registeredInfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<RegisteredInfo> queryWrapper = QueryGenerator.initQueryWrapper(registeredInfo, req.getParameterMap());
		Page<RegisteredInfo> page = new Page<RegisteredInfo>(pageNo, pageSize);
		IPage<RegisteredInfo> pageList = registeredInfoService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param registeredInfo
	 * @return
	 */
	@AutoLog(value = "考勤员工-添加")
	@ApiOperation(value="考勤员工-添加", notes="考勤员工-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RegisteredInfo registeredInfo) {
		registeredInfoService.save(registeredInfo);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param registeredInfo
	 * @return
	 */
	@AutoLog(value = "考勤员工-编辑")
	@ApiOperation(value="考勤员工-编辑", notes="考勤员工-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RegisteredInfo registeredInfo) {
		registeredInfoService.updateById(registeredInfo);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "考勤员工-通过id删除")
	@ApiOperation(value="考勤员工-通过id删除", notes="考勤员工-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		registeredInfoService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "考勤员工-批量删除")
	@ApiOperation(value="考勤员工-批量删除", notes="考勤员工-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.registeredInfoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "考勤员工-通过id查询")
	@ApiOperation(value="考勤员工-通过id查询", notes="考勤员工-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		RegisteredInfo registeredInfo = registeredInfoService.getById(id);
		if(registeredInfo==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(registeredInfo);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param registeredInfo
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RegisteredInfo registeredInfo) {
        return super.exportXls(request, registeredInfo, RegisteredInfo.class, "考勤员工");
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
        return super.importExcel(request, response, RegisteredInfo.class);
    }
	 /**
	  *   通过id启用
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "考勤员工-通过id启用")
	 @ApiOperation(value="考勤员工-通过id启用", notes="考勤员工-通过id启用")
	 @DeleteMapping(value = "/awake")
	 public Result<?> awake(@RequestParam(name="id",required=true) String id) {
         RegisteredInfo registeredInfo = registeredInfoService.getById(id);
         registeredInfo.setIsDel(0);
         registeredInfoService.updateById(registeredInfo);
         return Result.ok("启用成功!");
	 }
	 /**
	  *   通过id停用
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "考勤员工-通过id停用")
	 @ApiOperation(value="考勤员工-通过id停用", notes="考勤员工-通过id停用")
	 @DeleteMapping(value = "/stop")
	 public Result<?> stop(@RequestParam(name="id",required=true) String id) {
         RegisteredInfo registeredInfo = registeredInfoService.getById(id);
         registeredInfo.setIsDel(1);
         registeredInfoService.updateById(registeredInfo);
		 return Result.ok("停用成功!");
	 }

}
