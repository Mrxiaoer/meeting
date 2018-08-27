package com.spider.modules.business.config;

import com.spider.common.utils.Constant;
import com.spider.modules.business.dao.PageInfoDao;
import com.spider.modules.business.dao.ResultInfoDao;
import com.spider.modules.business.entity.PageInfoEntity;
import com.spider.modules.business.entity.ResultInfoEntity;
import com.spider.modules.business.model.VcCodeImagePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 定时删除informationName空的数据
 * @Author : yaonuan
 * @Email : 806039077@qq.com
 * @Date : 2018-08-08
 */

@Component
public class PageAndResultJob {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    PageInfoDao pageInfoDao;

    @Autowired
    VcCodeImagePath vcCodeImagePath;

    @Autowired
    ResultInfoDao resultInfoDao;

    @Scheduled(cron = "0 0 0/12 * * *")
    public void pageAndResult(){
        List<PageInfoEntity> pages = pageInfoDao.queryAll();
        PageInfoEntity pa = new PageInfoEntity();
        for (PageInfoEntity page : pages){
            if(page.getInformationName() == null){
                pa.setPageId(page.getPageId());
                pa.setState(Constant.VALUE_ZERO);
                pageInfoDao.update(pa);
            }
        }
        List<ResultInfoEntity> resultInfos = resultInfoDao.queryAll();
        ResultInfoEntity rs = new ResultInfoEntity();
        for (ResultInfoEntity resultInfo : resultInfos){
            if(pageInfoDao.listByResultId(resultInfo.getId()).size() == Constant.VALUE_ZERO){
                rs.setId(resultInfo.getId());
                rs.setState(Constant.VALUE_ZERO);
                resultInfoDao.update(rs);
            }
        }
    }


    @Scheduled(cron = "0 0 0/12 * * *")
    public void deleteByPictures(){
        //定于时间删除2天前的文件夹
        DateFormat dateFormat = new SimpleDateFormat("y-M-d");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -2);
        String data =dateFormat.format(calendar.getTime());

        String filename = vcCodeImagePath.getNewpath() + data;
        //删除文件夹操作
        this.deleteDirectory(filename);
        String oldfilename = vcCodeImagePath.getOldPath() + data;
        this.deleteDirectory(oldfilename);
    }


    public boolean deleteDirectory(String filename){
        //如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!filename.endsWith(File.separator)){
            filename = filename + File.separator;
        }
        File dirfile = new File(filename);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if((!dirfile.exists())||(!dirfile.isDirectory())){
            logger.info("删除目录失败：" +filename + "不存在");
            return false;
        }
        boolean flag = true;
        File[] files =  dirfile.listFiles();
        for (int i=0;i<files.length;i++) {
            //删除子文件
            if (files[i].isFile()) {
                flag = this.deletefile(files[i].getAbsolutePath());
                if (!flag) break;
            }
            //删除子目录
            else if (files[i].isDirectory()) {
                flag = this.deleteDirectory(files[i].getAbsolutePath());
                if(!flag) break;
            }
        }
        if (!flag){
            logger.info("删除目录失败！");
            return false;
        }
        if (dirfile.delete()){
            logger.info("删除目录" + filename + "成功");
            return true;
        }else {
            return false;
        }
    }

    public  boolean deletefile(String fileName){
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()){
            if (file.delete()){
                logger.info("删除单个文件" + fileName + "成功!");
                return true;
            }else{
                logger.info("删除单个文件" + fileName + "失败!");
                return false;
            }
        }else {
            logger.info("删除单个文件失败:" + fileName + "不存在!");
            return false;
        }
    }

}
