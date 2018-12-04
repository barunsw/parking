package com.hyundai_mnsoft.vpp.tcp.dao;

import com.hyundai_mnsoft.vpp.tcp.db.SqlSessionFactoryManager;
import com.hyundai_mnsoft.vpp.vo.MsgMetaSearchVo;
import com.hyundai_mnsoft.vpp.vo.MsgMetaVo;
import com.hyundai_mnsoft.vpp.vo.VehicleStatusInfoVo;
import com.hyundai_mnsoft.vpp.vo.VehicleTraceInfoVo;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import java.util.List;

public class MsgDao {
    private static Logger LOGGER = Logger.getLogger(MsgDao.class);

    public static List<MsgMetaVo> getMsgMetaInfo(MsgMetaSearchVo msgMetaSearchVo) {
        SqlSession session = SqlSessionFactoryManager.getSqlSessionFactory().openSession();

        List<MsgMetaVo> resultList = null;
        try{
            resultList = session.selectList("com.hyundai_mnsoft.vpp.tcp.dao.MsgDao.getMsgMetaInfo", msgMetaSearchVo);
        }
        finally{
            session.close();
        }

        return resultList;
    }

    public static void insertVehicleTraceInfo(VehicleTraceInfoVo vehicleTraceInfoVo) {
        SqlSession session = SqlSessionFactoryManager.getSqlSessionFactory().openSession();

        int result = 0;
        try{
            result = session.insert("com.hyundai_mnsoft.vpp.tcp.dao.MsgDao.insertVehicleTraceInfo", vehicleTraceInfoVo);
            session.commit();
        }
        catch (Exception ex) {
            session.rollback();
            LOGGER.error(ex.getMessage(), ex);
        }
        finally{
            session.close();
        }
    }

    public static void insertVehicleStatusInfo(VehicleStatusInfoVo vehicleStatusInfoVo) {
        SqlSession session = SqlSessionFactoryManager.getSqlSessionFactory().openSession();

        int result = 0;
        try{
            result = session.insert("com.hyundai_mnsoft.vpp.tcp.dao.MsgDao.insertVehicleStatusInfo", vehicleStatusInfoVo);
            session.commit();
        }
        catch (Exception ex) {
            session.rollback();
            LOGGER.error(ex.getMessage(), ex);
        }
        finally{
            session.close();
        }
    }
}
