package com.spider.tests;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import com.spider.modules.spider.utils.MyStringUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.Set;

/**
 * 测试
 * ------------------------------
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-06-20
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class SpiderTest {

	@Value("${webmagic.selenuim_config}")
	private String selenuim_config;

	@Test
	public void tt() {
		CronUtil.setMatchSecond(true);
		CronUtil.schedule("1", "*/5 * * * *", new Task() {
			@Override
			public void execute() {
				System.out.println("----");
			}
		});
		CronUtil.schedule("2", "*/5 * * * *", new Task() {
			@Override
			@Async("asyncServiceExecutor")
			public void execute() {
				System.out.println("++++");
			}
		});
		CronUtil.start();
		try {
			Thread.sleep(10000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void xxx() {
		for (int i = 0; i < 9; i++) {
			ThreadUtil.execute(new job() {
				@Override
				public void run() {
					System.out.println(Thread.currentThread().getName() + " === run ---- job");
				}
			});
		}
	}

	@Test
	public void startSpider() {
		//		new SpiderLink().startSpider("https://github.com/code4craft",true);
		System.setProperty("selenuim_config", selenuim_config);
		System.out.println(System.getProperty("selenuim_config"));
		//new SpiderPage().startSpider("https://github.com/code4craft",true);
	}

	@Test
	public void one2two() {
		One one = new One();
		one.setId(1);
		one.setAge(1);
		one.setName("yi");
		one.setSex("man");
		Two1 two1 = new Two1();
		Two2 two2 = new Two2();
		BeanUtil.copyProperties(one, two1);
		BeanUtil.copyProperties(one, two2);
		System.out.println("two1 ==> " + two1.toString());
		System.out.println("two2 ==> " + two2.toString());
	}

	@Test
	public void toCookies() {
		String doc = "[{\"path\":\"/citysystemgz\",\"domain\":\"114.55.11.227\",\"name\":\"JSESSIONID\",\"isHttpOnly\":false,\"isSecure\":false,\"value\":\"8FA1E5BB280B72D2000CE210279CAD55\"},{\"path\":\"/citysystemgz\",\"domain\":\"114.55.11.227\",\"name\":\"rememberMe\",\"isHttpOnly\":false,\"isSecure\":false,\"expiry\":1563956740000,\"value\":\"PXqTAKaPZpYqjOUmKU0/iv4nZv+QLPeUhlJoUcW/7QUT6rlKDWRSAjM0C07a9wRee7x/q2/cZg4+t2R8K1a0zzhf6oh7UguQH0ZKAp0qOlt1guUPFd+7cvQvEFN0eeCdQTI8Dai5UgJfnE3zgmbB6Jo2m5r47Dtx3e8IOYFwssT4SFMtRjQ1mchVayD0ixXcRBSVSeI44tUwbx4i+6xc3YLuzo/p7eeIfgh74Snv+S9fbFSFR9L6IRZrJJoyJEff454WwkX6vdXrBp1oz2WTe9EmiN4gbpZV6Gk2dMqA6p5rZxWOy5bYzGUbDoYgmNXpupPAhYrsdCc3Q73ArT74C6CO8pFLR6tbLsMAuWANzE2hPVUTHWaHe3spKwYpI0DF+1SFHbgtlFI/5zgscjMtIjgSSlOEI+Jtu0VbZZVtuE/Zvb9KiKZgfR6vbS3IvrZU0BPPYN1RvIAcem/w/bqS5al/B7siwLVb4j1SlhAis4yHow1HsMygQmedY0fxs9IqD0tyL4mnxeASROgElGKgM3VNeeTD6qzreHQIS3taiCg=\"}]";
		Set<Cookie> cookies = MyStringUtil.json2cookie(doc);
		System.out.println(cookies);
	}

	private class job implements Runnable {

		@Override
		public void run() {

		}

	}

	class One {
		private int id;
		private String name;
		private String sex;
		private int age;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getSex() {
			return sex;
		}

		public void setSex(String sex) {
			this.sex = sex;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}
	}

	class Two1 {
		private int id;
		private String name;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return "Two1{" + "id=" + id + ", name='" + name + '\'' + '}';
		}
	}

	class Two2 {
		private int id;
		private String sex;
		private int age;

		public String getSex() {
			return sex;
		}

		public void setSex(String sex) {
			this.sex = sex;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

		@Override
		public String toString() {
			return "Two2{" + "id=" + id + ", sex='" + sex + '\'' + ", age=" + age + '}';
		}
	}

	@Test
	public void ttttt(){
		String htmlstr = "<html>\n" +
				" <head> \n" +
				"  <meta charset=\"utf-8\"> \n" +
				"  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"> \n" +
				"  <meta name=\"renderer\" content=\"webkit|ie-comp|ie-stand\"> \n" +
				"  <title>部门登记</title> \n" +
				"  <meta name=\"keywords\" content=\"\"> \n" +
				"  <meta name=\"description\" content=\"\"> \n" +
				"  <link rel=\"shortcut icon\" href=\"http://ioa.govmade.com/template/2/default/_files/img/favicon.ico\"> \n" +
				"  <link href=\"http://ioa.govmade.com/template/2/default/_files/css/bootstrap.css\" rel=\"stylesheet\"> \n" +
				"  <link href=\"http://ioa.govmade.com/template/2/default/_files/css/font-awesome.css\" rel=\"stylesheet\"> \n" +
				"  <link href=\"http://ioa.govmade.com/template/2/default/_files/css/plugins/dataTables/dataTables.bootstrap.css\" rel=\"stylesheet\"> \n" +
				"  <link href=\"http://ioa.govmade.com/template/2/default/_files/css/animate.css\" rel=\"stylesheet\"> \n" +
				"  <link href=\"http://ioa.govmade.com/template/2/default/_files/css/style.css?201808020207798\" rel=\"stylesheet\"> \n" +
				"  <link href=\"http://ioa.govmade.com/template/2/default/_files/css/iconfont.css?201808020207798\" rel=\"stylesheet\"> \n" +
				"  <link href=\"http://ioa.govmade.com/template/2/default/_files/css/plugins/iCheck/custom.css\" rel=\"stylesheet\"> \n" +
				"  <link href=\"http://ioa.govmade.com/template/2/default/_files/css/zoomify.min.css\" rel=\"stylesheet\"> \n" +
				"  <link href=\"http://ioa.govmade.com/template/2/default/_files/css/notes.css\" rel=\"stylesheet\"> \n" +
				"  <link href=\"http://ioa.govmade.com/template/2/default/_files/css/labels-detail.css\" rel=\"stylesheet\"> \n" +
				"  <link rel=\"stylesheet\" href=\"http://ioa.govmade.com/template/2/default/_files/js/plugins/layer/skin/layer.css\" id=\"layui_layer_skinlayercss\" style=\"\"> \n" +
				"  <link rel=\"stylesheet\" href=\"http://ioa.govmade.com/template/2/default/_files/js/plugins/layer/laydate/theme/default/laydate.css?v=5.0.9\" id=\"layuicss-laydate\"> \n" +
				"  <style>\n" +
				".divs{\n" +
				"display: none;\n" +
				"}\n" +
				"</style> \n" +
				" </head> \n" +
				" <body class=\"gray-bg\"> \n" +
				"  <div class=\"wrapper wrapper-content animated fadeInRight\"> \n" +
				"   <div class=\"row\"> \n" +
				"    <div class=\"col-sm-12\"> \n" +
				"     <div class=\"ibox no-margins\"> \n" +
				"      <div class=\"ibox-title\"> \n" +
				"       <h5>部门登记</h5> \n" +
				"       <span class=\"dataRemarks\">[共0项资源]</span> \n" +
				"       <a class=\"btn btn-primary pull-right\" href=\"http://ioa.govmade.com/inner/create?nodeId=427\">新增</a> \n" +
				"      </div> \n" +
				"      <div class=\"ibox-content\"> \n" +
				"       <div class=\"row\"> \n" +
				"        <div class=\"col-sm-12\"> \n" +
				"         <form id=\"file_form\" action=\"http://ioa.govmade.com/innerExcel/excelImport\" enctype=\"multipart/form-data\" method=\"post\"> \n" +
				"          <div class=\"form-inline search-form-box\"> \n" +
				"           <div id=\"import-box\" style=\"display:none;\"> \n" +
				"            <input type=\"hidden\" name=\"nodeId\" value=\"427\"> \n" +
				"            <div class=\"form-group pic-upload\"> \n" +
				"             <input type=\"text\" id=\"filename\" class=\"form-control\"> \n" +
				"             <a href=\"javascript:void(0);\" class=\"btn btn-gray input-file\" style=\"margin:0;\">选择文件<input type=\"file\" name=\"file\" id=\"file_input\"></a> \n" +
				"            </div> \n" +
				"            <button class=\"btn btn-sm btn-primary btn-search\" type=\"submit\" id=\"upFile-btn\">提交</button> \n" +
				"            <a class=\"btn btn-gray btn-search\" href=\"http://ioa.govmade.com/innerExcel/getExcelModel?modelId=403\" target=\"_blank\"><i class=\"fa fa-file-excel-o\"></i> 模板下载</a> \n" +
				"            <!-- <a class=\"btn btn-gray btn-search\" href=\"http://ioa.govmade.com/excl/deptment.xls\" target=\"_blank\"><i class=\"fa fa-file-excel-o\"></i> 模板下载</a> --> \n" +
				"           </div> \n" +
				"          </div> \n" +
				"         </form> \n" +
				"         <form role=\"form\" id=\"myform\" class=\"form-inline search-form-box\" action=\"http://ioa.govmade.com/inner/list/427\"> \n" +
				"          <input type=\"hidden\" name=\"isPrivate\" value=\"1\"> \n" +
				"          <div class=\"form-group\"> \n" +
				"           <label>部门名称:&nbsp;</label> \n" +
				"           <input type=\"text\" placeholder=\"请输入部门名称\" class=\"form-control\" name=\"search_CONTAIN_detail.title\" value=\"\"> \n" +
				"          </div> \n" +
				"          <div class=\"form-group\"> \n" +
				"           <label>所属大区:&nbsp;</label> \n" +
				"           <select class=\"form-control input-sm\" name=\"search_EQ_p\"> <option value=\"\">---所有---</option> <option value=\"6\">董事会</option> <option value=\"7\">先锋队</option> <option value=\"1\">北方区</option> <option value=\"2\">华东区</option> <option value=\"3\">华南区</option> <option value=\"4\">舟山大区</option> <option value=\"5\">国脉研究院</option> </select> \n" +
				"          </div> \n" +
				"          <div class=\"form-group\"> \n" +
				"          </div> \n" +
				"          <div class=\"form-group\"> \n" +
				"           <label>是否财务结算公司:&nbsp;</label> \n" +
				"           <select class=\"form-control input-sm\" name=\"search_EQ_p1\"> <option value=\"\">---所有---</option> <option value=\"0\">否</option> <option value=\"1\">是</option> </select> \n" +
				"          </div> \n" +
				"          <div class=\"form-group\"> \n" +
				"           <label>是否内网结算部门:&nbsp;</label> \n" +
				"           <select class=\"form-control input-sm\" name=\"search_EQ_p2\"> <option value=\"\">---所有---</option> <option value=\"0\">否</option> <option value=\"1\">是</option> </select> \n" +
				"          </div> \n" +
				"          <button class=\"btn btn-sm btn-primary btn-search\" type=\"button\" onclick=\"$('#page').val('0')[0].form.submit();\"><strong>搜索</strong></button> \n" +
				"          <a class=\"btn btn-sm btn-success btn-import\" id=\"btn-import\"><strong>数据导入</strong></a> \n" +
				"          <a class=\"btn btn-sm btn-success btn-import\" id=\"deleteall\"><strong>批量删除</strong></a> \n" +
				"         </form> \n" +
				"        </div> \n" +
				"       </div> \n" +
				"       <table class=\"table table-striped table-bordered table-hover\"> \n" +
				"        <thead> \n" +
				"         <tr> \n" +
				"          <th width=\"40px;\"> \n" +
				"           <div class=\"checkbox i-checks\" style=\"display: inline-block;\"> \n" +
				"            <label> \n" +
				"             <div class=\"icheckbox_square-green\" style=\"position: relative;\"> \n" +
				"              <input type=\"checkbox\" id=\"allOptionId\" style=\"position: absolute; opacity: 0;\"> \n" +
				"              <ins class=\"iCheck-helper\" style=\"position: absolute; top: 0%; left: 0%; display: block; width: 100%; height: 100%; margin: 0px; padding: 0px; background-color: rgb(255, 255, 255); border: 0px; opacity: 0; background-position: initial initial; background-repeat: initial initial;\"></ins> \n" +
				"             </div><i></i></label> \n" +
				"           </div> </th> \n" +
				"          <th>部门编号</th> \n" +
				"          <th width=\"25%\">部门名称</th> \n" +
				"          <th>所属大区</th> \n" +
				"          <th>负责人</th> \n" +
				"          <th>成立时间</th> \n" +
				"          <th>是否财务结算公司</th> \n" +
				"          <th>是否内网结算部门</th> \n" +
				"          <th width=\"15%\">操作</th> \n" +
				"         </tr> \n" +
				"        </thead> \n" +
				"        <tbody> \n" +
				"        </tbody> \n" +
				"       </table> \n" +
				"       <div class=\"row form-inline page-line\"> \n" +
				"        <div class=\"col-sm-5\"> \n" +
				"         <input type=\"hidden\" id=\"page\" name=\"page\" value=\"0\"> \n" +
				"         <div class=\"dataTables_length\"> \n" +
				"          <label>共 0 项，每页 </label> \n" +
				"          <select class=\"form-control input-sm pageSize\" id=\"pageSize\" name=\"pageSize\" onchange=\"this.form.submit();\"> <option value=\"10\" selected>10</option> <option value=\"25\">25</option> <option value=\"50\">50</option> <option value=\"100\">100</option> </select> \n" +
				"          <label> 条记录</label> \n" +
				"         </div> \n" +
				"        </div> \n" +
				"        <div class=\"col-sm-7\"> \n" +
				"         <div class=\"dataTables_paginate\"> \n" +
				"          <ul class=\"pagination\"> \n" +
				"           <li class=\"previous disabled\"><a href=\"javascript:;\">上一页</a></li> \n" +
				"           <li class=\"active\"> <a href=\"javascript:;\" onclick=\"$('#page').val('0')[0].form.submit();\">1</a> </li> \n" +
				"           <li class=\"previous disabled\"><a href=\"javascript:;\">下一页</a></li> \n" +
				"          </ul> \n" +
				"         </div> \n" +
				"        </div> \n" +
				"       </div> \n" +
				"      </div> \n" +
				"     </div> \n" +
				"    </div> \n" +
				"   </div> \n" +
				"  </div> \n" +
				"  <style type=\"text/css\">\n" +
				"#zzc{\n" +
				"display: none;\n" +
				"}\n" +
				"</style> \n" +
				"  <div class=\"layer-mask\" id=\"zzc\"> \n" +
				"   <div class=\"loadEffect\"> \n" +
				"    <span></span> \n" +
				"    <span></span> \n" +
				"    <span></span> \n" +
				"    <span></span> \n" +
				"    <span></span> \n" +
				"    <span></span> \n" +
				"    <span></span> \n" +
				"    <span></span> \n" +
				"   </div> \n" +
				"  </div>  \n" +
				" </body>\n" +
				"</html>";
		Html html = Html.create(htmlstr);
		Selectable select = html.xpath("/html/body[1]/div[1]/div[1]/div[1]/div[1]/div[2]/table[1]/thead[1]");
		Selectable select1 = html.xpath("/html[1]/body[1]/div[1]/div/div/div/div[2]/table/thead");
		System.out.println(select);
		System.out.println(select1);
	}

}
