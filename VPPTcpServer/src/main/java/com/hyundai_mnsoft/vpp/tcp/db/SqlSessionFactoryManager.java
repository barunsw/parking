package com.hyundai_mnsoft.vpp.tcp.db;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

import java.io.Reader;


public class SqlSessionFactoryManager {
	private static Logger LOGGER = Logger.getLogger(SqlSessionFactoryManager.class);
	private static final SqlSessionFactory sqlMapper;

	static {
		Reader reader = null;
		try {
			reader = Resources.getResourceAsReader("mybatis-config.xml");
		}
		catch (Exception ex) {
			LOGGER.error(ex.getMessage(), ex);
		}
		
		sqlMapper = new SqlSessionFactoryBuilder().build(reader, "development", System.getProperties());
	}
	
	public static SqlSessionFactory getSqlSessionFactory() {
		return sqlMapper;
	}
}
