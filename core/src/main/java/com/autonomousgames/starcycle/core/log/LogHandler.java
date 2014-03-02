package com.autonomousgames.starcycle.core.log;

import com.autonomousgames.starcycle.core.StarCycle;
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
	private FileHandle screenLog;
	private FileHandle gameLog;

	private static ArrayList<String> queuedScreens = new ArrayList<String>();
	private static ArrayList<String> queuedGames = new ArrayList<String>();

	public LogHandler () {
		StarCycle.json.setOutputType(JsonWriter.OutputType.json);
        String screenLogPath = "screen_progression.log";
        screenLog = Gdx.files.local(screenLogPath);
        String gameLogPath = "games.log";
        gameLog = Gdx.files.local(gameLogPath);
	}

	public void pushLog (String istring) throws Exception {
        Gdx.app.log("LogHandler", istring);
		if (!istring.isEmpty()) {
			HttpRequest httpRequest = new HttpRequest(HttpMethods.POST);
			String requestURL = baseURL;
			httpRequest.setUrl(requestURL);
			httpRequest.setHeader("Content-Type", "application/x-gzip");
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
        ArrayList<String[]> games = new ArrayList<String[]>();
        ArrayList<String[]> screen_changes = new ArrayList<String[]>();
		if (screenLog.exists()) {
			String[] screenSplit = screenLog.readString().split("\n");
			for (int i=0;i<screenSplit.length;i++) {
				screen_changes.add(StarCycle.json.fromJson(String[].class, screenSplit[i]));
			}
			screenLog.delete();
		}
		if (gameLog.exists()) {
			String[] gameSplit = gameLog.readString().split("\n");
			for (int i=0;i<gameSplit.length;i++) {
				games.add(StarCycle.json.fromJson(String[].class, gameSplit[i]));
			}
			gameLog.delete();
		}
        LogPackage logPackage = new LogPackage(StarCycle.uidHandler.getId(),
                StarCycle.startTime,
                Gdx.app.getType().toString(),
                games,
                screen_changes);

		try {
            Gdx.app.log("LogHandler", StarCycle.json.toJson(logPackage));
			pushLog(StarCycle.json.toJson(logPackage));
		}
        catch (Exception e) {
			Gdx.app.log("loghandler", e.getMessage());
		}
	}

	public void writeLogs() {
		writeLog(queuedScreens, screenLog);
		writeLog(queuedGames, gameLog);
	}
	
	public void writeLog(ArrayList<String> logData, FileHandle logFile) {
        LogWriter logWriter = new LogWriter(logData, logFile);
        logWriter.start();
	}
	
	public void logScreen(String istring){
		queuedScreens.add(istring);
	}
	
	public void logGame(String istring){
		queuedGames.add(istring);
	}
	
	public static ByteArrayOutputStream compressString(String str) throws IOException{
		ByteArrayOutputStream out = new ByteArrayOutputStream(str.length());
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		gzip.write(str.getBytes());
		gzip.close();
		out.close();
		return out;
	}
}