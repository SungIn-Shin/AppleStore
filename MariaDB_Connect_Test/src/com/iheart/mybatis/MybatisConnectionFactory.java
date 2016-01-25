package com.iheart.mybatis;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MybatisConnectionFactory {
	private static SqlSessionFactory seqSessionFactory;
	
	
	static{
		try {
			String resource = "com/iheart/mybatis/config.xml";
			Reader reader = Resources.getResourceAsReader(resource);
			if(seqSessionFactory == null){
				seqSessionFactory = new SqlSessionFactoryBuilder().build(reader);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static SqlSessionFactory getSqlSessionFactory(){
		return seqSessionFactory;
	}
}
