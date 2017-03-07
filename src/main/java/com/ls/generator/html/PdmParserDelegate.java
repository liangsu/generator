package com.ls.generator.html;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ls.generator.util.Configuration;
import com.ls.generator.util.StringUtils;
import com.ls.generator.util.XmlUtils;

/**
 * pdm文件解析
 * @author warhorse
 *
 */
public class PdmParserDelegate {

	private TableDirectoryRegister register;
	private String rootPath;

	public PdmParserDelegate(TableDirectoryRegister register) {
		this.register = register;
		this.rootPath = Configuration.getValue("html.directory");
	}
	
	public int load(InputStream is){
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
			
			return loadPdm(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
	
	private int loadPdm(Document doc){
		Element model = doc.getDocumentElement(); //根元素是model
		
		Element rootObject = (Element) model.getElementsByTagName("o:RootObject").item(0);
		
		Element children = (Element) rootObject.getElementsByTagName("c:Children").item(0);
		
		Element oModel = (Element) children.getElementsByTagName("o:Model").item(0);
		
		Map<String, TableInfo> tabMap = new HashMap<String, TableInfo>(200);
		List<PhysicalDiagram> diagrams = new ArrayList<PhysicalDiagram>(15);
		
		NodeList nl = oModel.getChildNodes();
		if(nl != null && nl.getLength() > 0){
			for(int i = 0; i < nl.getLength(); i++){
				Node node = nl.item(i);
				if(node instanceof Element){
					Element ele = (Element) node;
					String nodeName = node.getNodeName();
					if("c:Tables".equals(nodeName)){
						parseTables(ele,tabMap);
					}else if("c:PhysicalDiagrams".equals(nodeName)){
						parsePhysicalDiagrams(ele, diagrams);
					}
				}
			}
		}
		
		System.out.println("从pdm中共解析出pdm数量："+tabMap.size());
		for (PhysicalDiagram diagram : diagrams) {
			System.out.println(diagram.getName()+":");
			for (String tableId : diagram.getTableIds()) {
				diagram.addTable(tabMap.get(tableId));
			}
			
			for(TableInfo tab : diagram.getTables()){
				System.out.println("    "+tab);
			}
			registerDiagram(diagram);
		}
		
		return tabMap.size();
	}

	private void parsePhysicalDiagrams(Element diagramsEle, List<PhysicalDiagram> diagrams){
		NodeList nl = diagramsEle.getChildNodes();
		for(int i = 0; i < nl.getLength(); i++){
			Node node = nl.item(i);
			if(node instanceof Element){
				Element ele = (Element) node;
				if("o:PhysicalDiagram".equals(node.getNodeName())){
					parsePhysicalDiagram(ele, diagrams);
				}
			}
		}
	}
	
	private void parsePhysicalDiagram(Element diagramEle, List<PhysicalDiagram> diagrams){
		String id = diagramEle.getAttribute("Id");
		
		PhysicalDiagram diagram = new PhysicalDiagram();
		diagram.setId(id);
		
		NodeList nl = diagramEle.getChildNodes();
		for(int i = 0; i < nl.getLength(); i++){
			Node node = nl.item(i);
			if(node instanceof Element){
				Element ele = (Element) node;
				
				if("a:Name".equals(node.getNodeName())){
					diagram.setName(ele.getTextContent());
					
				}else if("a:Code".equals(node.getNodeName())){
					diagram.setCode(ele.getTextContent());
					
				}else if("c:Symbols".equals(node.getNodeName())){
					parsePhysicalDiagramSymbols(ele, diagram);
				}
				
			}
		}
		
		diagrams.add(diagram);
	}
	
	private void parsePhysicalDiagramSymbols(Element symbolsEle, PhysicalDiagram diagram){
		
		NodeList tableSymbolEles = symbolsEle.getElementsByTagName("o:TableSymbol");
		
		for(int i = 0; i < tableSymbolEles.getLength(); i++){
			Element tableSymbolEle = (Element)tableSymbolEles.item(i);
			
			Element objectEle = (Element) tableSymbolEle.getElementsByTagName("c:Object").item(0);
			
			if(objectEle != null){
				Element tableEle = (Element) objectEle.getElementsByTagName("o:Table").item(0);
				diagram.addTableId(tableEle.getAttribute("Ref"));
			}
			
		}
	}
	
	private void parseTables(Element tablesEle, Map<String, TableInfo> tabMap){
		NodeList nl = tablesEle.getChildNodes();
		for(int i = 0; i < nl.getLength(); i++){
			Node node = nl.item(i);
			if(node instanceof Element){
				Element ele = (Element) node;
				if("o:Table".equals(node.getNodeName())){
					parseTable(ele,tabMap);
				}
			}
		}
	}
	
	private void parseTable(Element tableEle, Map<String, TableInfo> tabMap){
		String id = tableEle.getAttribute("Id");
		String name = "";
		String code = "";
		
		NodeList nl = tableEle.getChildNodes();
		for(int i = 0; i < nl.getLength(); i++){
			Node node = nl.item(i);
			if(node instanceof Element){
				Element ele = (Element) node;
				if("a:Name".equals(node.getNodeName())){
					name = ele.getTextContent();
				}else if("a:Code".equals(node.getNodeName())){
					code = ele.getTextContent();
				}
			}
		}
		
		//System.out.println(id + "-" + name + "-" + code);
		TableInfo tab = new TableInfo(name, code, id);
		tabMap.put(id, tab);
	}
	
	private void registerDiagram(PhysicalDiagram diagram){
		List<TableInfo> tabs = diagram.getTables();
		String diagramName = diagram.getName();
		diagramName = diagramName.replaceAll("、", "");
		
		for (TableInfo tab : tabs) {
			
			String tableCode = tab.getCode();
			
			if(StringUtils.isNotBlank(tableCode)){
				register.registe(tab.getCode().toUpperCase(), rootPath + diagramName);
			}
		}
	}
	
	class TableInfo{
		private String name;
		private String code;
		private String id;

		public TableInfo(String name, String code, String id) {
			this.name = name;
			this.code = code;
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		@Override
		public String toString() {
			return "TableInfo [name=" + name + ", code=" + code + ", id=" + id + "]";
		}
	}
	
	class PhysicalDiagram{
		private String id;
		private String name;
		private String code;
		private List<String> tableIds = new ArrayList<String>();
		private List<TableInfo> tables = new ArrayList<>();
		
		public PhysicalDiagram() {
		}

		public PhysicalDiagram(String name, String code, String id) {
			this.name = name;
			this.code = code;
			this.id = id;
		}
		
		public void addTableId(String tableId){
			tableIds.add(tableId);
		}
		
		public List<String> getTableIds() {
			return tableIds;
		}

		public void addTable(TableInfo tab){
			tables.add(tab);
		}
		
		public List<TableInfo> getTables() {
			return tables;
		}
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}
		
		@Override
		public String toString() {
			return "TableInfo [name=" + name + ", code=" + code + ", id=" + id + "]";
		}
	}
	
	public static void main(String[] args) {
		PdmParserDelegate delegate = new PdmParserDelegate(null);
		//delegate.loadPdm(XmlUtils.readDocument("C:\\Users\\warhorse\\Desktop\\测试.pdm"));
		delegate.loadPdm(XmlUtils.readDocument("C:\\Users\\warhorse\\Desktop\\采购辅助系统-数据模型汇总V2.0.PDM"));
	}
}
