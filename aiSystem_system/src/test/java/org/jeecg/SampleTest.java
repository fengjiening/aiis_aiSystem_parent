package org.jeecg;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ThreadFactory;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.utils.UuidUtils;
import org.jeecg.common.constant.SystemConstant;
import org.jeecg.common.es.JeecgElasticsearchTemplate;
import org.jeecg.common.util.CommonMethod;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.dynamic.db.DynamicDBUtil;
import org.jeecg.modules.ability.service.AbilityService;
import org.jeecg.modules.demo.mock.MockController;
import org.jeecg.modules.demo.test.entity.JeecgDemo;
import org.jeecg.modules.demo.test.mapper.JeecgDemoMapper;
import org.jeecg.modules.demo.test.service.IJeecgDemoService;
import org.jeecg.modules.record.entity.AeRecord;
import org.jeecg.modules.record.service.IAeRecordService;
import org.jeecg.modules.staff.entity.RegisteredInfo;
import org.jeecg.modules.staff.entity.faceUser;
import org.jeecg.modules.staff.service.IRegisteredInfoService;
import org.jeecg.modules.system.service.ISysDataLogService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SampleTest {

	@Resource
	private JeecgDemoMapper jeecgDemoMapper;
	@Resource
	private IJeecgDemoService jeecgDemoService;
	@Resource
	private ISysDataLogService sysDataLogService;
	@Resource
	private MockController mock;
	@Resource
	private AbilityService abilityService;
	@Resource
	private JeecgElasticsearchTemplate jeecgElasticsearchTemplate;
	@Autowired
	private IAeRecordService aeRecordService;
	@Test
	public void testIAeRecordService() {
		AeRecord ac =new AeRecord();
		ac.setIp("123456");
		ac.setEtype(3);
		ac.setToken(UuidUtils.getUUID());
		ac.setLoginid("123123");
		aeRecordService.save(ac);
	}
	@Test
	public void afr() {
		System.out.println(("----- selectAll method test ------"));

		String savePath = "c://face.png";
		String path = CommonMethod.getPath()+"/" + savePath;
		log.error("******************** afrImg path:"+path);
		try{
			File file = new File(savePath);
			InputStream inputFile = new FileInputStream(file);

			String s = CommonMethod.encodeBase64File(savePath);

			System.err.println(s);
			//System.err.println(abilityService.afrDetect(inputFile).toString());
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void testSelect() {
		System.out.println(("----- selectAll method test ------"));
		List<JeecgDemo> userList = jeecgDemoMapper.selectList(null);
		Assert.assertEquals(5, userList.size());
		userList.forEach(System.out::println);
	}

	@Test
	public void testXmlSql() {
		System.out.println(("----- selectAll method test ------"));
		List<JeecgDemo> userList = jeecgDemoMapper.getDemoByName("Sandy12");
		userList.forEach(System.out::println);
	}

	@Test
	public void testES() {
		System.out.println(("----- testES method test ------"));
		//boolean test1 = jeecgElasticsearchTemplate.createIndex("test1");

	}

	/**
	 * 测试事务
	 */
	@Test
	public void testTran() {
		jeecgDemoService.testTran();
	}
	
	//author:lvdandan-----date：20190315---for:添加数据日志测试----
	/**
	 * 测试数据日志添加
	 */
	@Test
	public void testDataLogSave() throws Exception {
		String id="20180917-6";
		String type="afr";
		String savePath = "c:/home/jietong/kaoqi/data";

		File file = new File(savePath);
		InputStream inputFile = new FileInputStream(file);
		String s = CommonMethod.encodeBase64File(savePath);
		InputStream faceStreamc =  CommonMethod.decoderBase64ToStream(s);

		String path = SystemConstant.getDataPathPre()+"/";
		String newDay =  DateUtils.formatDate();
		System.out.println(newDay);

		File dir1 = new File(path+newDay+"/"+id);
		if (!dir1.isDirectory()) {
			System.out.println("时间索引文件不存在 to 创建");
			dir1.mkdirs();
		}
		String newFileName="afr".equals(type)?id+"_"+type+".png":id+"_"+type+".mp3";
		//保存音頻
		String filePath =  path+newDay+"/"+id+"/"+newFileName;
		CommonMethod.writeToLocal(filePath,inputFile);
	}
	@Autowired
	private IRegisteredInfoService registeredInfoService;
	@Test
	public synchronized void testDB() {
		log.debug("人员同步 开始...................");
		QueryWrapper<RegisteredInfo> queryWrapper = new QueryWrapper<>();

		List<faceUser> root_mysql = DynamicDBUtil.findListEntities("root_mysql", "select * from registered_info", faceUser.class, new Object[]{});
		RegisteredInfo user =null;
		List<RegisteredInfo> userList =new ArrayList<>();
		Date date = new Date();
		for(faceUser registUser : root_mysql){
			user =new RegisteredInfo();
			BeanUtils.copyProperties(registUser,user);
			user.setLoginid(registUser.getStaffNum());
			user.setSex("男".equals(registUser.getSex())?"1":"0");
			user.setSysOrgCode("A01");
			user.setCreateBy("admin");
			user.setCreateTime(date);
			user.setKeepDays(0);
			user.setIdcard(registUser.getCardNumber());
			user.setId(registUser.getStaffNum());
			//查询天数
//			QueryWrapper<RegisteredInfo> queryWrapperDay = new QueryWrapper<>();
//			queryWrapperDay.eq("keep_days","");
//			loginid  //registeredInfoService.getOne(queryWrapperDay);
			RegisteredInfo byId = registeredInfoService.getById(registUser.getStaffNum());
			if (byId!=null){
				user.setKeepDays(byId.getKeepDays());
			}
			//获取人脸数据
			Map faceUser =(Map)DynamicDBUtil.findOne("root_mysql", "select * from afr_info where ID=?", new Object[]{registUser.getAfrInfo()});
			if (faceUser!=null){
				user.setUserFace(faceUser.get("AFR_PATH_ONE").toString());
			}
			userList.add(user);
			user=null;

		}
		registeredInfoService.remove(queryWrapper);
		registeredInfoService.saveBatch(userList);
		log.debug("人员同步 结束...................");
	}
	//author:lvdandan-----date：20190315---for:添加数据日志测试----
}
