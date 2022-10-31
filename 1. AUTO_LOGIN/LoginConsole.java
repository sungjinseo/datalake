package LoginConsole;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.GregorianCalendar;
import java.util.Calendar;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

public class LoginConsole {

	static int successResponse = 200;
	static String urlEncoding = "UTF-8";
	static String mixiLogin = "http://intranet.webcash.co.kr/main/login_p.jsp"; 
	static String mixiTopPage = "http://intranet.webcash.co.kr/comm/comm01_03_03p_ajax.jsp?";
	static String[] id = { "SungjinSeo","bongminkim" };
	static String[] pw = { "********","********" };

	public static void main(String[] args) {
		LoginConsole ssb = new LoginConsole();
		
		String loginTimeWeek = "";
		String logoutTimeWeek = "";
		String loginTimeSun = "";
		String logoutTimeSun = "";
		int todayLoginFlag = 0;
		int todayLogoutFlag = 0;
		String filePath = System.getProperty("user.dir") 	+ "\\login.log";
		
		SimpleDateFormat gFormatter = new SimpleDateFormat("yyyy.MM.dd", Locale.ENGLISH);
		Date gCurrentTime = new Date();
		String compareDay = gFormatter.format(gCurrentTime);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss EEE", Locale.ENGLISH);
		Date currentTime = new Date();
		String dTime = formatter.format(currentTime);

		int minute = (int) (Math.random() * (50 - 40) + 1) + 40;
		int hour = (int) (Math.random() * (23 - 22)) + 22;
		loginTimeWeek = " 08:" + Integer.toString(minute);
		minute = (int) (Math.random() * (50 - 0)) + 10;
		logoutTimeWeek = " " + Integer.toString(hour) + ":"	+ Integer.toString(minute);

		hour = (int) (Math.random() * 3) + 10;
		minute = (int) (Math.random() * (50 - 0)) + 10;
		loginTimeSun = " " + Integer.toString(hour) + ":" + Integer.toString(minute);
		hour = (int) (Math.random() * 3) + 18;
		minute = (int) (Math.random() * (50 - 0)) + 10;
		logoutTimeSun = " " + Integer.toString(hour) + ":" + Integer.toString(minute);
		
		if (!fileIsLive(filePath)) {
			fileMake(filePath);
		}
		
		while (true) {
			try {
				Thread.sleep(60000); 
				currentTime = new Date();
				dTime = formatter.format(currentTime);
				
				if (dTime.indexOf(" 23:58")!=-1){
					gCurrentTime.setTime(gCurrentTime.getTime()	+ (1000 * 60 * 60 * 24) * 1);
					compareDay = gFormatter.format(gCurrentTime);
					todayLoginFlag = 0;
					todayLogoutFlag = 0;
				}
				
				if (dTime.indexOf(compareDay) != -1	&& (todayLoginFlag != 0 || todayLogoutFlag != 0)) {
					gCurrentTime.setTime(gCurrentTime.getTime()	+ (1000 * 60 * 60 * 24) * 1);
					compareDay = gFormatter.format(gCurrentTime);
					todayLoginFlag = 0;
					todayLogoutFlag = 0;
				} 
				
				if ((dTime.indexOf(loginTimeWeek) != -1 || dTime.indexOf(logoutTimeWeek) != -1)	&& (dTime.indexOf(" Sun") == -1 || dTime.indexOf(" Sat") == -1)) {
					if ((dTime.indexOf(loginTimeWeek) != -1)) {
						int successLoginWeek=0;
						for(int i=0;i<id.length;i++){
							if (ssb.callBatchLogin(id[i], pw[i], filePath) == successResponse) {
								successLoginWeek += 1;
							} else {
								
							}
						}
						if(successLoginWeek==id.length){
							FileOutputStream fos = new FileOutputStream(filePath, true);
							PrintStream ps = new PrintStream(fos);
							ps.println("NowLoginDay : " + compareDay);
							ps.println("NowLoginTime : " + loginTimeWeek);
							minute = (int) (Math.random() * (50 - 40) + 1) + 40;
							loginTimeWeek = " 08:" + Integer.toString(minute);
							ps.println("NextLoginDay : " + gFormatter.format(gCurrentTime.getTime()	+ (1000 * 60 * 60 * 24) * 1));
							ps.println("NextLoginTime : " + loginTimeWeek);
							ps.close();
							fos.close();
							todayLoginFlag += 1;
						}else{
							FileOutputStream fos = new FileOutputStream(filePath, true);
							PrintStream ps = new PrintStream(fos);
							ps.println("NowLogoutTime : " + loginTimeWeek);
							ps.println("There is a problem to log in. This program will try to connect again");
							ps.println("NextLogoutTime is in 5 miutes");
							ps.close();
							fos.close();
							currentTime.setTime(currentTime.getTime() + (1000 * 60 * 5));
							dTime = formatter.format(currentTime);
							loginTimeWeek = dTime.substring(10, 16);
						}
						
					} else {
						int successLogoutWeek=0;
						for(int i=0;i<id.length;i++){
							if (ssb.callBatchLogout(id[i], pw[i], filePath) == successResponse) {
								successLogoutWeek += 1;
							} else {
								
							}
							if(successLogoutWeek==id.length){
								FileOutputStream fos = new FileOutputStream(filePath, true);
								PrintStream ps = new PrintStream(fos);
								ps.println("NowLogoutTime : " + logoutTimeWeek);
								minute = (int) (Math.random() * (40 - 0)) + 10;
								hour = (int) (Math.random() * (23 - 22)) + 22;
								logoutTimeWeek = " " + Integer.toString(hour) + ":" + Integer.toString(minute);
								ps.println("NextLogoutTime : " + logoutTimeWeek);
								ps.close();
								fos.close();
								todayLogoutFlag += 1;
							}else{
								FileOutputStream fos = new FileOutputStream(filePath, true);
								PrintStream ps = new PrintStream(fos);
								ps.println("NowLogoutTime : " + logoutTimeWeek);
								ps.println("There is a problem to log in. This program will try to connect again");
								ps.println("NextLogoutTime is in 5 miutes");
								ps.close();
								fos.close();
								currentTime.setTime(currentTime.getTime() + (1000 * 60 * 5));
								dTime = formatter.format(currentTime);
								if (dTime.indexOf(compareDay) != -1) {
									logoutTimeWeek = dTime.substring(10, 16);
								} else {
									todayLoginFlag = 0;
								}
							}
						}
					}
				}else if ((dTime.indexOf(loginTimeSun) != -1 || dTime.indexOf(logoutTimeSun) != -1) && (dTime.indexOf(" Sun") != -1 || dTime.indexOf(" Sat") != -1)) {
					if ((dTime.indexOf(loginTimeSun) != -1)) {
						int successLoginWeekend=0;
						for(int i=0;i<id.length;i++){
							if (ssb.callBatchLogin(id[i], pw[i], filePath) == successResponse) {
								successLoginWeekend +=1;
							} else {
								
							}
						}
						if(successLoginWeekend==id.length){
							FileOutputStream fos = new FileOutputStream(filePath, true);
							PrintStream ps = new PrintStream(fos);
							ps.println("NowLoginDay : " + compareDay);
							ps.println("NowLoginDay : " + loginTimeSun);
							ps.println("NowLoginTime : " + loginTimeSun);
							hour = (int) (Math.random() * 3) + 10;
							minute = (int) (Math.random() * (50 - 0)) + 10;
							loginTimeSun = " " + Integer.toString(hour)	+ ":" + Integer.toString(minute);
							ps.println("NextLoginDay : " + gFormatter.format(gCurrentTime.getTime()	+ (1000 * 60 * 60 * 24) * 1));
							ps.println("NextLoginTime : " + loginTimeSun);
							ps.close();
							fos.close();
							todayLoginFlag += 1;
						}else{
							FileOutputStream fos = new FileOutputStream(filePath, true);
							PrintStream ps = new PrintStream(fos);
							ps.println("NowLogoutTime : " + loginTimeSun);
							ps.println("There is a problem to log in. This program will try to connect again");
							ps.println("NextLogoutTime is in 5 miutes");
							ps.close();
							fos.close();
							currentTime.setTime(currentTime.getTime() + (1000 * 60 * 5));
							dTime = formatter.format(currentTime);
							loginTimeSun = dTime.substring(10, 16);
						}

					} else {
						int successLogoutWeekend=0;
						for(int i=0;i<id.length;i++){
							if (ssb.callBatchLogout(id[i], pw[i], filePath) == successResponse) {
								successLogoutWeekend+=1;
							} else {
								
							}
						}
						if(successLogoutWeekend==id.length){
							FileOutputStream fos = new FileOutputStream(filePath, true);
							PrintStream ps = new PrintStream(fos);
							ps.println("NowLogoutTime : " + logoutTimeSun);
							hour = (int) (Math.random() * 3) + 18;
							minute = (int) (Math.random() * (50 - 0)) + 10;
							logoutTimeSun = " " + Integer.toString(hour) + ":" + Integer.toString(minute);
							ps.println("NextLogoutTime : " + logoutTimeSun);
							ps.close();
							fos.close();
							todayLogoutFlag += 1;
						}else{
							FileOutputStream fos = new FileOutputStream(filePath, true);
							PrintStream ps = new PrintStream(fos);
							ps.println("NowLogoutTime : " + logoutTimeSun);
							ps.println("There is a problem to log in. This program will try to connect again");
							ps.println("NextLogoutTime is in 5 miutes");
							ps.close();
							fos.close();
							currentTime.setTime(currentTime.getTime() + (1000 * 60 * 5));
							dTime = formatter.format(currentTime);
							if (dTime.indexOf(compareDay) != -1) {
								logoutTimeSun = dTime.substring(10, 16);
							} else {
								todayLoginFlag = 0;
							}
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public int callBatchLogin(String userId, String userPw, String logPath) {
		HttpClient client = new HttpClient();

		GregorianCalendar cal = new GregorianCalendar();

		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DATE);
		int hol = Calendar.DAY_OF_WEEK - 1;
		int statusCode = 0;
		int getStatusCode = 0;

		String today = null;

		today = Integer.toString(year);
		if (month < 10) {
			today += "0" + Integer.toString(month);
		} else {
			today += Integer.toString(month);
		}
		if (day < 10) {
			today += "0" + Integer.toString(day);
		} else {
			today += Integer.toString(day);
		}

		String data = "searchDate=" + today + "&holiday="+ Integer.toString(hol) + "&userId=" + userId;

		PostMethod postMethod = new PostMethod(mixiLogin);
		postMethod.addParameter("userid", userId);
		postMethod.addParameter("password", userPw);
		postMethod.addParameter("siteId", "allin");
		postMethod.addParameter("systemName", "");
		postMethod.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);

		try {
			statusCode = client.executeMethod(postMethod);
			postMethod.releaseConnection();
			if (statusCode == 200) {
				GetMethod getMethod = new GetMethod(mixiTopPage + data);
				getStatusCode = client.executeMethod(getMethod);
				if (getStatusCode == 200) {
					FileOutputStream fos = new FileOutputStream(logPath, true);
					PrintStream ps = new PrintStream(fos);
					BufferedReader br = new BufferedReader(new InputStreamReader(getMethod.getResponseBodyAsStream(), urlEncoding));
					String line;
					int savedIndex=0;
					
					while ((line = br.readLine()) != null) {
						if(savedIndex==32 || savedIndex==33 || savedIndex==34){
							line = line.trim();
							line = line.replaceAll("opener.document.getElementById\\(\"", "");
							line = line.replaceAll("\"\\).innerHTML", "");
							line = line.replaceAll(" 	", "");
							line = line.replaceAll("	", "");
							ps.println(userId+" : ");
							ps.println(line);
						}
						if(savedIndex==34) break;
						savedIndex++;
					}
					ps.close();
					fos.close();
				}
				getMethod.releaseConnection();

			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return statusCode;
	}

	public int callBatchLogout(String userId, String userPw, String logPath) {
		HttpClient client = new HttpClient();

		GregorianCalendar cal = new GregorianCalendar();

		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DATE);
		int hol = Calendar.DAY_OF_WEEK - 1;
		int statusCode = 0;
		int getStatusCode = 0;

		String today = null;

		today = Integer.toString(year);
		if (month < 10) {
			today += "0" + Integer.toString(month);
		} else {
			today += Integer.toString(month);
		}
		if (day < 10) {
			today += "0" + Integer.toString(day);
		} else {
			today += Integer.toString(day);
		}

		String data = "searchDate=" + today + "&holiday=" + Integer.toString(hol) + "&userId=" + userId;

		PostMethod postMethod = new PostMethod(mixiLogin);
		postMethod.addParameter("userid", userId);
		postMethod.addParameter("password", userPw);
		postMethod.addParameter("siteId", "allin");
		postMethod.addParameter("systemName", "");
		postMethod.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);

		try {
			statusCode = client.executeMethod(postMethod);
			postMethod.releaseConnection();
			if (statusCode == 200) {
				GetMethod getMethod = new GetMethod(mixiTopPage + data);
				getStatusCode = client.executeMethod(getMethod);
				if (getStatusCode == 200) {
					FileOutputStream fos = new FileOutputStream(logPath, true);
					PrintStream ps = new PrintStream(fos);
					BufferedReader br = new BufferedReader(new InputStreamReader(getMethod.getResponseBodyAsStream(), urlEncoding));
					String line;
					int savedIndex=0;
					
					while ((line = br.readLine()) != null) {
						if(savedIndex==32 || savedIndex==33 || savedIndex==34){
							line = line.trim();
							line = line.replaceAll("opener.document.getElementById\\(\"", "");
							line = line.replaceAll("\"\\).innerHTML", "");
							line = line.replaceAll(" 	", "");
							line = line.replaceAll("	", "");
							ps.println(userId+" : ");
							ps.println(line);
						}
						if(savedIndex==34) break;
						savedIndex++;
					}
					ps.close();
					fos.close();
				}
				getMethod.releaseConnection();

			}

		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return statusCode;
	}

	public static Boolean fileIsLive(String isLivefile) {
		File f1 = new File(isLivefile);

		if (f1.exists()) {
			return true;
		} else {
			return false;
		}
	}

	public static void fileMake(String makeFileName) {
		File f1 = new File(makeFileName);
		try {
			f1.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}