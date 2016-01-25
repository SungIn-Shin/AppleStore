package com.iheart.mybatis;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class UserDAO {
	private SqlSessionFactory sqlSessionFactory;
	int count = 0;

	public UserDAO(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	public List<User> selectAll() {
		List<User> list = null;
		SqlSession session = sqlSessionFactory.openSession();
		list = session.selectList("User.selectAll");
		for (User e : list) {
			System.out.println(e.getUserHobby1());
		}
		session.close();
		return list;
	}

	// AutoCommit 사용하지 않는 예제
	public int insertUser(User user) {
		//
		SqlSession session = sqlSessionFactory.openSession(false);
		int result = 0;
		try {
			// 오토커밋 사용하지 않음
			result = session.insert("User.insertUser", user);
			
			if(count >= 20){
				throw new SQLException();
			} else if( count <= 19){
				System.out.println("19이하 Commit");
				session.rollback();
//				session.commit();
			}
		} catch(SQLException e){
			System.out.println("Exception 인데 커밋시킬거야!!!");
			session.commit();
		} finally {
			session.close();
		}
		return result;
	}

	public void upCount() {
		count++;
	}

}
