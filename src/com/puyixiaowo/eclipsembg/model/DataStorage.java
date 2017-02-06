package com.puyixiaowo.eclipsembg.model;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.osgi.service.prefs.Preferences;

import com.puyixiaowo.eclipsembg.constants.Constants;
import com.puyixiaowo.eclipsembg.util.Base64;

public class DataStorage {
	// We access the Configuration Scope
	static Preferences preferences = ConfigurationScope.INSTANCE.getNode(Constants.PROJECT_NAME);
	static Preferences defaultSub = preferences.node(Constants.PROJECT_NAME + ".data");
	//we should add the workspace name
	static String workspacePath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString();
	public static String getEncodeSignature(String workspacePath, String projectName) {
		return Base64.encodeString(workspacePath) + projectName;
	}
	public static void put(String projectName, String key, String value) {
		try {
			String path = getEncodeSignature(workspacePath, projectName);
			//System.out.println("put path = " + path);
			Preferences sub1 = projectName == null ? defaultSub : preferences.node(path);
			sub1.put(key, value);
			sub1.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static String get(String projectName, String key) {
		String path = getEncodeSignature(workspacePath, projectName);
		//System.out.println("get path = " + path);
		Preferences sub1 = projectName == null ? defaultSub : preferences.node(path);
		return sub1 == null ? null : sub1.get(key,"");
	}
}
