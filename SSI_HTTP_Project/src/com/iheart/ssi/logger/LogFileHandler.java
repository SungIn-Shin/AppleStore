package com.iheart.ssi.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.iheart.ssi.util.PropertyLoader;

public class LogFileHandler implements LogHandler {
	//
	private String filePath;
	private String fileName;
	private String fileNamePattern;
	private File logFileFolder, logFile;
	private PropertyLoader prop = PropertyLoader.getInstance();
	private Format format;
	
	public LogFileHandler() {
		//
		filePath = prop.getProperty("LOG_HOME");
		fileName = prop.getProperty("log_file_name");
		
		format = new Format();
		fileNamePattern = format.getLogFilePattern();
		// 파일 생성
		createFile();
	}
	
	/**
	 * 파일 디렉토리의 유무, 파일의 유무를 체크한 후 
	 * 디렉토리가 없다면 생성을 시키고, 파일이 없다면 생성을 시킨다.
	 */
	public void createFile(){
		// 로그 파일 폴더 경로
		// D:/HTTP/log_file/
		logFileFolder = new File(filePath);
		// 생성할 로그 파일
		// D:/HTTP/log_file/SSI_HTTP_2015-11-09.log
		logFile = new File(filePath + fileName + fileNamePattern + ".log");
		try {
			
			if(!logFileFolder.exists()){ // 폴더가 없다면
				//
				logFileFolder.mkdirs(); // 폴더를 생성시키고
			}
			
			if(!logFile.exists()){ // 로그파일이 없으면
				logFile.createNewFile(); // 로그파일을 생성시킨다.
			}
			
		} catch (IOException e) {
			//
			e.printStackTrace();
		}
	}
	
	
	/** (non-Javadoc)
	 * 생성된 Log파일에 로그 메세지를 추가한다.
	 */
	@Override
	public void append(String logMsg) {
		//
		BufferedWriter br = null;
		if(!logFile.exists()){ // 파일이 있을때 작성해야하겠죠.
			createFile();
		} 
		try {
			br = new BufferedWriter(new FileWriter(logFile, true));
			if(logFile.length() == 0){ // File의 크기가 0이면.. 즉 가장 처음 생성하는 시점에
				createHeader(br); // 헤더를 생성한다.
			}
			synchronized (br) {
				br.write(logMsg);
				br.newLine();
			}
			br.flush();
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
	private void createHeader(BufferedWriter br) throws IOException {
		//
		StringBuffer sb = new StringBuffer();
		sb.append("========================================================================\r\n");
		sb.append("안녕하세요 SSI Logger입니다. 이건 I-HEART 신성인 사원이 만든거에요~\r\n");
		sb.append("========================================================================");
		
		br.write(sb.toString());
		br.newLine();
		br.flush();
	}
	
}
