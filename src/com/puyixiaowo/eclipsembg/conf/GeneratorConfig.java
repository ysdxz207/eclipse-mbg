package com.puyixiaowo.eclipsembg.conf;

import com.puyixiaowo.eclipsembg.model.ClassPathEntry;
import com.puyixiaowo.eclipsembg.model.Context;

public class GeneratorConfig {
	private String fileName;
	private ClassPathEntry classPathEntry;
	private Context context;

	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public ClassPathEntry getClassPathEntry() {
		return classPathEntry;
	}

	public void setClassPathEntry(ClassPathEntry classPathEntry) {
		this.classPathEntry = classPathEntry;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
	
	@Override
	public String toString() {
		
		return this.fileName;
	}
	///////////////////////////////
	
}
