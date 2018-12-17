package com.hyundai_mnsoft.vpp.tcp.dao;

import com.hyundai_mnsoft.vpp.tcp.db.SqlSessionFactoryManager;
import com.hyundai_mnsoft.vpp.vo.MsgMetaSearchVo;
import com.hyundai_mnsoft.vpp.vo.MsgMetaVo;
import com.hyundai_mnsoft.vpp.vo.VehicleStatusInfoVo;
import com.hyundai_mnsoft.vpp.vo.VehicleTraceInfoVo;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import java.util.List;

// ### Mybatis 연동 Dao.
public class MsgDao {
    private static Logger LOGGER = Logger.getLogger(MsgDao.class);

    // 메시지 Meta 정보 열람.
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

    // 차량 위치 정보 insert/update.
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

    // 차량 상태 정보 insert/update.
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

    public static void updateRouteData(VehicleStatusInfoVo vehicleStatusInfoVo) {
        SqlSession session = SqlSessionFactoryManager.getSqlSessionFactory().openSession();

        int result = 0;
        try{
            result = session.update("com.hyundai_mnsoft.vpp.tcp.dao.MsgDao.updateRouteData", vehicleStatusInfoVo);
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
