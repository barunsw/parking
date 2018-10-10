package com.hyundai_mnsoft.vpp.tcp.dao;

import org.apache.ibatis.session.SqlSession;
import com.hyundai_mnsoft.vpp.tcp.db.SqlSessionFactoryManager;
import com.hyundai_mnsoft.vpp.vo.MsgHeaderVo;
import org.apache.log4j.Logger;

import java.util.List;

public class MsgDao {
    private static Logger LOGGER = Logger.getLogger(MsgDao.class);

    public static List<MsgHeaderVo> getMsgHeaderInfo() {
        SqlSession session = SqlSessionFactoryManager.getSqlSessionFactory().openSession();

        List<MsgHeaderVo> resultList = null;
        try{
            resultList = session.selectList("com.hyundai_mnsoft.vpp.tcp.dao.MsgDao.getMsgHeaderInfo");
        }
        finally{
            session.close();
        }

        return resultList;
    }


}
