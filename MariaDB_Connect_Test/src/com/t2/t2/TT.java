package com.t2.t2;

import java.util.Properties;

public class TT {
	public static void main(String[] args) {
		System.out.println("OS : " + System.getProperty("os.name"));
		System.out.println("사용자 : " + System.getProperty("user.name"));
		System.out.println("사용자 : " + System.getProperty("user.home"));
		System.out.println("사용자 : " + System.getProperty("user.dir"));
		Properties prop = new Properties();
		System.out.println("사용자 : " + System.getProperty("file.separator"));
	}
}
