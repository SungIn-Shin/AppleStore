package com.iheart.ssi.util;

public class Test {
	public static <T> void test(Class<T> clazz){
		System.out.println(clazz.getMethods()[0]);
		System.out.println(clazz.getMethods()[1]);
		System.out.println(clazz.getMethods()[2]);
		System.out.println(clazz.getMethods()[3]);
		System.out.println(clazz.getName());
		System.out.println(clazz.getCanonicalName());
		System.out.println(clazz.getSimpleName());
		System.out.println(clazz.getClass());
	}
	
	public static void test1(){
		int i = 5;
		if( i == 5){
			System.out.println("1. 틀렸다");
		} else if(i == 5){
			System.out.println("2. 틀림");
		} else if( i== 5){
			System.out.println("정답");
		}
	}
	
	public void test2(){
		
	}
	public static void main(String[] args) {
//		test(Test.class);
//		
//		Properties loader = new Properties();
//		try {
//			loader.load(new BufferedReader(new InputStreamReader(new FileInputStream("D:/log.conf"), "UTF-8")));
//			String header = loader.getProperty("dynamic_header");
//			System.out.println(header);
//			System.out.println(header.contains("CLASSNAME"));
//			System.out.println(header.contains("CLASSNAM"));
//			System.out.println(header.contains("IP"));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		test1();
	}
}