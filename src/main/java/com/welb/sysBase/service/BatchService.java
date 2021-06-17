package com.welb.sysBase.service;

import com.welb.util.PageData;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("batchService")
public class BatchService {
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public int doBatch(List<PageData> list, String sqlMapperPath) {
        // 新获取一个模式为BATCH，自动提交为false的session
        // 如果自动提交设置为true,将无法控制提交的条数，改为最后统一提交，可能导致内存溢出
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        int result = 1;
        try {
            if (null != list || list.size() > 0) {
                int lsize = list.size();
                for (int i = 0, n = list.size(); i < n; i++) {
                    session.insert(sqlMapperPath, list.get(i));
                    if ((i > 0 && i % 500 == 0) || i == lsize - 1) {
                        // 手动每500个一提交，提交后无法回滚
                        session.commit();
                        // 清理缓存，防止溢出
                        session.clearCache();
                    }
                }
            }
        } catch (Exception e) {
            // 没有提交的数据可以回滚
            e.printStackTrace();
            result = 0;
            throw new RuntimeException("批量插入失败");
        } finally {
            session.close();
        }
        return result;
    }

}
