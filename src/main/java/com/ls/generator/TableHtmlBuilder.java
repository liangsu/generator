package com.ls.generator;

import java.util.List;

import com.ls.generator.db.Column;
import com.ls.generator.db.Table;
import com.ls.generator.util.StringUtils;
import com.ls.generator.util.TableUtils;

public class TableHtmlBuilder {

	private Table table;
	
	public TableHtmlBuilder(Table table) {
		this.table = table;
	}
	
	public String getResult(){
		TableUtils.validate(table);
		
		String colHtml = "";
		List<Column> cols = table.getColumns();
		for (Column col : cols) {
			colHtml += "<tr><td>"+col.getColumnName()+"</td><td>"+TableUtils.getType(col)+"</td><td>"+getDataDefault(col, "&nbsp;")+"</td><td>"+getColumnComments(col, "&nbsp;")+"</td></tr>";
		}
		
		String html = getPrefix(table.getTableName(), table.getComments()) + colHtml + getSuffix();
		
		return html;
	}
	
	public String getPrefix(String tableName, String comments){
		if(StringUtils.isBlank(comments)){
			comments = "";
		}else{
			comments = "（"+comments+"）";
		}
		String prefix = "<!DOCTYPE html>"
                      + "<html>"
                      + "<head>"
                      + "<meta charset=\"UTF-8\">"
                      + "<title>"+tableName+"</title>"
                      + "</head>"
                      + "<body>"
                      + "<table width=\"950px\" border=\"1px\" cellspacing=\"0px;\" cellpadding=\"0\">"
                      + "	<thead>"
                      + "		<th colspan=\"4\" align=\"center\">"+tableName+comments+"</th>"
                      + "	</thead>"
                      + "	<tbody>"
                      + "		<tr><td width=\"300px\">列名</td><td width=\"200px\">类型</td><td width=\"50px\">默认值</td><td width=\"400px\">描述</td></tr>";
		return prefix;
	}
	
	public String getSuffix(){
		String suffix = "</tbody></table></div></body></html>";
		return suffix;
	}
	
	private static String getDataDefault(Column column, String defVal){
		if(StringUtils.isBlank(column.getDataDefault())){
			return defVal;
		}
		return column.getDataDefault();
	}
	
	private static String getColumnComments(Column column, String defVal){
		if(StringUtils.isBlank(column.getComments())){
			return defVal;
		}
		return column.getComments();
	}
}
