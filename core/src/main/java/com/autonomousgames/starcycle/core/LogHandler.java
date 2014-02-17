package com.autonomousgames.starcycle.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.utils.JsonWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.GZIPOutputStream;

public class LogHandler extends Thread{
	String baseURL = "http://autonomousgam.es/api";
	//String baseURL = "http://127.0.0.1:5000/api";
	private FileHandle touchLog;
	private FileHandle screenLog;
	private FileHandle gameLog;

    private static ArrayList<String> queuedTouches = new ArrayList<String>();
	private static ArrayList<String> queuedScreens = new ArrayList<String>();
	private static ArrayList<String> queuedGames = new ArrayList<String>();

	public LogHandler () {
		StarCycle.json.setOutputType(JsonWriter.OutputType.json);
        String touchLogPath = "touch.log";
        touchLog = Gdx.files.local(touchLogPath);
        String screenLogPath = "screen.log";
        screenLog = Gdx.files.local(screenLogPath);
        String gameLogPath = "game.log";
        gameLog = Gdx.files.local(gameLogPath);
	}

	public void pushLog (String istring) throws Exception {
		if (!istring.isEmpty()) {
			//Gdx.app.log("loghandler", istring);
			HttpRequest httpRequest = new HttpRequest(HttpMethods.POST);
			String requestURL = baseURL;
			httpRequest.setUrl(requestURL);
			httpRequest.setHeader("Content-Type", "application/x-gzip");
			//httpRequest.setContent(istring);
			ByteArrayOutputStream s = compressString(istring);
			httpRequest.setContent(new ByteArrayInputStream(s.toByteArray()), s.size());
			Gdx.app.log("LogHandler","INITIALIZING LOG PUSH");
			Gdx.net.sendHttpRequest(httpRequest, new HttpResponseListener() { 
				public void handleHttpResponse(HttpResponse httpResponse) {
					HttpStatus status = httpResponse.getStatus();
					if (status.getStatusCode() >= 200 && status.getStatusCode() <= 300){
						Gdx.app.log("LogHandler", "Push successful!");
						Gdx.app.log("LogHandler", httpResponse.getResultAsString());
					}
		        }

				@Override
				public void failed(Throwable t) {
					Gdx.app.log("loghandler", "request failed");
				}
			});
		}
		else {
			Gdx.app.log("loghandler", "no logs found");
		}
	}
    @Override
	@SuppressWarnings("unchecked")
	public void run() {
		HashMap<String,Object> logMap = new HashMap<String,Object>();
		logMap.put("uid", UserSettingz.getLongSetting("uid"));
		logMap.put("start", StarCycle.startTime);
		logMap.put("platform", Gdx.app.getType().toString());
		logMap.put("touch", new ArrayList<HashMap<String,Object>>());
		logMap.put("screen", new ArrayList<HashMap<String,Object>>());
		logMap.put("game", new ArrayList<HashMap<String,Object>>());
		if (touchLog.exists()) {
			String[] touchSplit = touchLog.readString().split("\n");
			for (int i=0;i<touchSplit.length;i++){
				((ArrayList<HashMap<String,Object>>) logMap.get("touch")).add(StarCycle.json.fromJson(HashMap.class,touchSplit[i]));
			}
			touchLog.delete();
		}
		if (screenLog.exists()) {
			
			String[] screenSplit = screenLog.readString().split("\n");
			for (int i=0;i<screenSplit.length;i++){
				((ArrayList<HashMap<String,Object>>) logMap.get("screen")).add(StarCycle.json.fromJson(HashMap.class,screenSplit[i]));
			}
			
			screenLog.delete();
		}
		if (gameLog.exists()) {
			
			String[] gameSplit = gameLog.readString().split("\n");
			for (int i=0;i<gameSplit.length;i++){
				((ArrayList<HashMap<String,Object>>) logMap.get("game")).add(StarCycle.json.fromJson(HashMap.class,gameSplit[i]));
			}
			
			gameLog.delete();
		}
		try {
			pushLog(StarCycle.json.toJson(logMap));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Gdx.app.log("loghandler", e.getMessage());
		}
	}

	public void writeLogs() {
		writeLog(queuedTouches,touchLog);
		writeLog(queuedScreens,screenLog);
		writeLog(queuedGames,gameLog);
	}
	
	public void writeLog(ArrayList<String> logData, FileHandle logFile) {
        LogWriter logWriter = new LogWriter(logData, logFile);
        logWriter.start();
	}
	
	public void logTouch(String istring){
		queuedTouches.add(istring);
	}
	
	public void logScreen(String istring){
		queuedScreens.add(istring);
	}
	
	public void logGame(String istring){
		queuedGames.add(istring);
	}
	
	public static ByteArrayOutputStream compressString(String str) throws IOException{
		/*if (str == null || str.length() == 0) {
		    return new ByteArrayOutputStream(0);
		}*/
		ByteArrayOutputStream out = new ByteArrayOutputStream(str.length());
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		gzip.write(str.getBytes());
		gzip.close();
		out.close();

		return out;
	}
}