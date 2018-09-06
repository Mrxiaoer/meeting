package com.spider.tests;

import com.spider.modules.spider.dao.SpiderRuleDao;
import com.spider.modules.spider.entity.SpiderRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author: yaonuan
 * @Date: 2018/8/1 0001 下午 4:16
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class analogyTest {

    @Autowired
    SpiderRuleDao spiderRuleDao;

    @Test
    public void tt(){
        SpiderRule rule = new SpiderRule();
//        rule.setXpath("123");
//        rule.setId(null);
        spiderRuleDao.insertOne(rule);
        System.out.println(rule.getId());
    }
}
