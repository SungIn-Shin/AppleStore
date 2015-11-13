package com.iheart.ssi.logger;

public class LogLevel {
	private String name;
	private int value;
	private boolean visible;
	
	/*********************************************/
	/***************** LogLevel ******************/
	/*********************************************/
	public static final LogLevel EMERG = new LogLevel("EMERG", Integer.MAX_VALUE, false);
	
	public static final LogLevel ALERT = new LogLevel("ALERT", 600, true);
	
	public static final LogLevel CRIT = new LogLevel("CRIT", 500, true);
	
	public static final LogLevel ERROR = new LogLevel("ERROR", 400, true);
	
	public static final LogLevel WARN = new LogLevel("WARN", 300, true);
	
	public static final LogLevel NOTICE = new LogLevel("NOTICE", 200, true);
	
	public static final LogLevel INFO = new LogLevel("NOTICE", 100, true);
	
	public static final LogLevel DEBUG = new LogLevel("DEBUG", Integer.MIN_VALUE, true);
	
	private LogLevel(String name, int value, boolean visible) {
		//
		if(name == null){
			try {
				throw new NullPointerException();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
		this.name = name;
		this.value = value;
		this.visible = visible;
	}
	
	// getter, setter
	public String getName() {
		return name;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	

	public void setName(String name) {
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
