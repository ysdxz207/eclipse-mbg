package com.puyixiaowo.eclipsembg.test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.dom4j.DocumentException;
import org.yong.util.file.xml.XMLObject;
import org.yong.util.file.xml.XMLParser;

import com.puyixiaowo.eclipsembg.model.Plugin;

public class Test {
	List<Plugin> pluginList;

	public static void main(String[] args) {
		try {
			String fileName = "D:/workspace/eclipse-plugin/eclipse-mbg/bin/conf/generatorConfig.xml";
			
			File xml = new File(fileName);
			
			XMLParser parser = new XMLParser("generatorConfig.xml");
			XMLObject obj = parser.parse();
			
			obj.addChildTag(XMLParser.createNode("testa", "haha", null));
			
			parser.transferRoot(obj, xml, false);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
