package com.iheart.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;

public class UserService {
	private UserDAO dao;
	private SqlSessionFactory sqlSessionFactory;
	public UserService(){
		sqlSessionFactory = MybatisConnectionFactory.getSqlSessionFactory();
		System.out.println("Session 생성");
		dao = new UserDAO(sqlSessionFactory);
	}

	public int insertUser(User user) {
		// 오토커밋 사용안할때 처리. - 1000개씩 모아서 커밋.
		int result = 0;
		// try {
		// // SqlSession session = sqlSessionFactory.openSession(true); // 오토커밋
		// // 사용
		// session = sqlSessionFactory.openSession(true); // 오토커밋 사용하지 않음
		// result = session.insert("User.insertUser", user);
		// } finally {
		// session.close();
		// }
			String hobby[] = { "GAME", "LOL", "SING A SONG", "PROGRAMMING", "JAVA", "C", "C++", "C#", "WOW", "PC",
					"SHOPING", "FISHING", "DRIKING", "EATING" };
			String address[] = { "서울", "대전", "대구", "부산", "안동", "영월", "강촌", "도산", "강북", "강남", "강서", "강동", "성남", "성북",
					"서초", "논현" };
			int hobbyLen = hobby.length;
			int addressLen = address.length;
			System.out.println("데이터 입력 중...");
			long start = System.currentTimeMillis();
			
			for (int i = 1; i <= 23; i++) {
				// if((i%10000) == 0){
				// System.out.println(i + "개째 데이터 생성완료.");
				// }
				System.out.println(i + "번째 생성중.");
				int random = (int) (Math.random() * 9999) + 1;
				int ageRan = (int) (Math.random() * 130) + 1;
				int firstTel = (int) (Math.random() * 9999) + 1;
				int lastTel = (int) (Math.random() * 9999) + 1;
				int firstPhone = (int) (Math.random() * 9999) + 1;
				int lastPhone = (int) (Math.random() * 9999) + 1;
				int hobbyRan1 = (int) (Math.random() * hobbyLen - 1);
				int hobbyRan2 = (int) (Math.random() * hobbyLen - 1);
				int hobbyRan3 = (int) (Math.random() * hobbyLen - 1);
				int addRan = (int) (Math.random() * addressLen - 1);

				User userz = new User();
				userz.setUserName("TEST_NAME_" + random);
				userz.setUserNick("TEST_NICK_" + random);
				userz.setUserAge(ageRan + "");
				userz.setUserTel("010-" + firstTel + "-" + lastTel);
				userz.setUserPhone("010-" + firstPhone + "-" + lastPhone);
				userz.setUserAddress(address[addRan]);
				userz.setUserHobby1(hobby[hobbyRan1]);
				userz.setUserHobby2(hobby[hobbyRan2]);
				userz.setUserHobby3(hobby[hobbyRan3]);
				dao.insertUser(userz);
				dao.upCount();
			}
			long end = System.currentTimeMillis();
			System.out.println("실행 시간 : " + (end - start) / 1000 + " 초 ");
			

		return result;
	}
	
	public void selectUser(){
		dao.selectAll();
	}
}
