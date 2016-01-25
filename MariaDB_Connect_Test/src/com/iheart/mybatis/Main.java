package com.iheart.mybatis;

public class Main {
	private UserService service;
	
	public Main(){
		service = new UserService();
	}
	public void autoCommitTest(){
		
		/**
		 **  TEST_USER Table에 100만건 데이터 넣을때 사용했음.
		 **/
		String hobby[] = {"GAME","LOL","SING A SONG","PROGRAMMING","JAVA",
				"C","C++","C#","WOW","PC","SHOPING","FISHING","DRIKING","EATING"};
		String address[] = {"서울", "대전", "대구", "부산", "안동", "영월", "강촌", "도산", "강북", "강남", "강서", "강동", "성남", "성북", "서초", "논현"};
		int hobbyLen = hobby.length;
		int addressLen = address.length;
		System.out.println("데이터 입력 중...");
		long start = System.currentTimeMillis();
		for(int i = 1; i <= 101;i++){
//			if((i%10000) == 0){
//				System.out.println(i + "개째 데이터  생성완료.");
//			}
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
			
			User user = new User();
			user.setUserName("TEST_NAME_"+random);
			user.setUserNick("TEST_NICK_"+random);
			user.setUserAge(ageRan+"");
			user.setUserTel("010-"+ firstTel+"-" +lastTel);
			user.setUserPhone("010-"+ firstPhone+"-" +lastPhone);
			user.setUserAddress(address[addRan]);
			user.setUserHobby1(hobby[hobbyRan1]);
			user.setUserHobby2(hobby[hobbyRan2]);
			user.setUserHobby3(hobby[hobbyRan3]);
			service.insertUser(user);
		}
		long end = System.currentTimeMillis();
		
		System.out.println("실행 시간 : " + (end - start) / 1000 + " 초 ");
	}
	
	public void unAutoCommitTest(){
		service.insertUser(new User());
	}
	public void selectAll(){
		service.selectUser();
	}
	public static void main(String[] args) {
		Main main  = new Main();
		//main.autoCommitTest();
		main.unAutoCommitTest();
		main.selectAll();
	}
}
