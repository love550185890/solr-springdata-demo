package com.study.solr.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SolrDataQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author
 * @Date
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-solr.xml")
public class SolrTestDemo {

    @Autowired
    SolrTemplate solrTemplate;
    /**
     * 增加索引
     */
    @Test
    public void testAdd(){
        TbItem tbItem = new TbItem();
        tbItem.setId(1l);
        tbItem.setTitle("华为Mate30手机");
        tbItem.setPrice(new BigDecimal(3000.00));
        tbItem.setCategory("手机");
        tbItem.setBrand("华为");
        tbItem.setSeller("华为旗舰店");
        solrTemplate.saveBean(tbItem);
        solrTemplate.commit();
    }

    /**
     * 修改
     */
    @Test
    public void testUpdateIndex(){
        TbItem tbItem = new TbItem();
        tbItem.setId(1l);
        tbItem.setTitle("华为Mate30手机2");
        tbItem.setPrice(new BigDecimal(3000.01));
        tbItem.setCategory("手机");
        tbItem.setBrand("华为");
        tbItem.setSeller("华为旗舰店2");
        solrTemplate.saveBean(tbItem);
        solrTemplate.commit();
    }

    /**
     * 添加集合
     */
    @Test
    public void testAddList(){
        List<TbItem> list = new ArrayList<TbItem>();
        for (int i = 0;i<100;i++){
            TbItem tbItem = new TbItem();
            tbItem.setId(1l+i);
            tbItem.setTitle("华为Mate30手机"+i);
            tbItem.setPrice(new BigDecimal(3000.01+i));
            tbItem.setCategory("手机");
            tbItem.setBrand("华为");
            tbItem.setSeller("华为旗舰店"+i);
            list.add(tbItem);
        }
        solrTemplate.saveBeans(list);
        solrTemplate.commit();
    }

    /**
     * 删除
     */
    @Test
    public void testDelete(){
        solrTemplate.deleteById(1l+"");
        solrTemplate.commit();
    }

    /**
     * 测试分页查询
     */
    @Test
    public void testPage(){
        Query query = new SimpleQuery("*:*");
        query.setOffset(10);
        query.setRows(10);
        ScoredPage<TbItem> itemsList = solrTemplate.queryForPage(query, TbItem.class);
        System.out.println("总记录数是："+itemsList.getTotalElements());
        for (TbItem tbItem:itemsList) {
            System.out.println("tbItem.getTitle() = " + tbItem.getTitle());
        }
    }

    /**
     * 测试条件查询
     */
    @Test
    public void testCriteria(){
        Query query = new SimpleQuery("*:*");
        Criteria criteria = new Criteria("item_title").contains("2");
        query.addCriteria(criteria);
        query.setOffset(0);
        query.setRows(100);
        ScoredPage<TbItem> items = solrTemplate.queryForPage(query, TbItem.class);
        System.out.println("总记录数："+items.getTotalElements());
        for (TbItem item: items){
            System.out.println("标题："+item.getTitle());
        }
    }

    @Test
    public void testDeleteBatch(){
        Query query = new SimpleQuery("*:*");
        solrTemplate.delete(query);
        solrTemplate.commit();
    }
}
