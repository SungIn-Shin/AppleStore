package com.iheart.ssi.logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class LogFileHandler implements LogHandler {
	//
	private String logFilePath;
	private String logFileName;
	private String fileNamePattern;
	private File logFileFolder, logFile;
	private Format format;
	private Properties logProperties;
	
	public LogFileHandler(){
		
	}
	
	public LogFileHandler(String log_conf_path) {
		//
		logProperties = new Properties();
		try {
			logProperties.load(new BufferedReader(new InputStreamReader(new FileInputStream(log_conf_path), "UTF-8")));
			this.logFilePath = logProperties.getProperty("log_file_path");
			this.logFileName = logProperties.getProperty("log_file_name");
			format = new Format();
			fileNamePattern = format.getLogFilePattern(logProperties.getProperty("file_name_pattern"));
			isExistFile();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	//LogFileName 설정 - Default = SSI_LOG_
	public void setFileName(String fileName){
		this.logFileName = fileName;
	}
	
	/** (non-Javadoc)
	 * 생성된 Log파일에 로그 메세지를 추가한다.
	 */
	@Override
	public void append(String logMsg) {
		//
		isExistFile();
		
		
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(logFile, true));
			 // 파일이 있어야 작성을 하니.. 없으면 생성을 시키고, 헤더를 ㅁ나들고 작성 시작.
			if(logFile.length() < 1){
				createHeader(bw);
			} else {
				synchronized (bw) {
					bw.write(logMsg);
					bw.newLine();
					bw.flush();
				}
			}
		} catch (IOException e) {
			//
			e.printStackTrace();
		}
	}

	
	/**
	 * 파일 디렉토리의 유무, 파일의 유무를 체크한 후 
	 * 디렉토리가 없다면 생성을 시키고, 파일이 없다면 생성을 시킨다.
	 */
	private void isExistFile(){
		// 로그 파일 폴더 경로
		// D:/HTTP/log_file/
		logFileFolder = new File(logFilePath);
		// 생성할 로그 파일
		// D:/HTTP/log_file/SSI_HTTP_2015-11-09.log
		logFile = new File(logFilePath + logFileName + fileNamePattern + ".log");
		BufferedWriter bw = null;
		try {
			
			if(!logFileFolder.exists()){ // 폴더가 없다면
				logFileFolder.mkdirs(); // 폴더를 생성시키고
			}
			if(!logFile.exists()){ // 로그파일이 없거나 아무것도 없으면
				logFile.createNewFile(); // 로그파일을 생성시킨다.
			}
			
			bw = new BufferedWriter(new FileWriter(logFile, true));
			if(logFile.length() < 1){
				createHeader(bw);
			}
		} catch (IOException e) {
			//
			e.printStackTrace();
		}
		
	}
	
	

	/**
	 * @param br
	 * @throws IOException
	 * 로그파일 첫번째 헤드라인
	 */
	private void createHeader(BufferedWriter bw) throws IOException {
		//
		StringBuffer sb = new StringBuffer();
		sb.append("========================================================================\r\n");
		sb.append("개발자 : 신성인 \r\n");
		sb.append("소속 : I-Heart 개발팀 \r\n");
		sb.append("연락처 : 010-5099-5149 \r\n");
		sb.append("이메일 : tlstjddls123@naver.com \r\n");
		sb.append("========================================================================");
		
		bw.write(sb.toString());
		bw.newLine();
		bw.flush();
		bw.close();
	}
	
}
