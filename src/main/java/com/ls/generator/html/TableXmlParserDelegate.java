package com.ls.generator.html;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TableXmlParserDelegate {

	private final TableDirectoryRegister register;
	
	public TableXmlParserDelegate(TableDirectoryRegister register) {
		this.register = register;
	}
	
	public void load(InputStream is){
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
			
			parseDocument(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void parseDocument(Document doc){
		Element root = doc.getDocumentElement();
		String rootName = root.getNodeName();
		
		if("root".equals(rootName)){
			parseRootEle(root);
		}
	}
	
	private void parseRootEle(Element root){
		String baseDir = root.getAttribute("path");
		
		NodeList nl = root.getChildNodes();
		for(int i = 0; i < nl.getLength(); i++){
			Node node = nl.item(i);
			if(node instanceof Element){
				if("dir".equals(node.getNodeName())){
					parseDirEle((Element)node, baseDir);
				}
			}
		}
	}
	
	private void parseDirEle(Element dir, String baseDir){
		String dirName = baseDir + File.separator + dir.getAttribute("name") ;
		
		NodeList nl = dir.getChildNodes();
		for(int i = 0; i < nl.getLength(); i++){
			Node node = nl.item(i);
			if(node instanceof Element){
				if("table".equals(node.getNodeName())){
					parseTableEle((Element)node, dirName);
					
				}else if("dir".equals(node.getNodeName())){
					parseDirEle((Element)node, dirName);
				}
			}
		}
	}
	
	private void parseTableEle(Element table, String dirName){
		String tableName = table.getTextContent();
		
		register.registe(tableName, dirName);
	}
	
	
	public static void main(String[] args) throws Exception {
		TableDirectoryRegister register = new TableDirectoryRegister();
		FileInputStream is = new FileInputStream("E:\\Workspaces\\workspace_p6spy\\generator\\src\\main\\java\\table2Html.xml");
		TableXmlParserDelegate delegate = new TableXmlParserDelegate(register);
		delegate.load(is);
	}
}
