package com.ls.generator;

import java.util.ArrayList;
import java.util.List;

import com.ls.generator.db.Column;
import com.ls.generator.db.Constraint;
import com.ls.generator.db.Table;
import com.ls.generator.support.DataTypeResolverComposite;
import com.ls.generator.util.StringUtils;

public class TableSQLBuilder {

	private Table table;

	private String tableSQL = "";
	
	private List<String> columnSQLs = new ArrayList<String>();
	
	private List<String> commentsSQLs = new ArrayList<String>();
	
	private String constraintSQL = "";
	
	/** 缩进 */
	private static final String PRESTR = "    ";
	
	public TableSQLBuilder(Table table) {
		this.table = table;
	}
	
	private void validate(){
		if(table == null){
			throw new IllegalArgumentException("构建对象为空");
		}
		
		if(StringUtils.isBlank(table.getTableName())){
			throw new IllegalArgumentException("构建表的名称为空,"+table);
		}
		
		if(table.getColumns() == null || table.getColumns().size() <= 0){
			throw new IllegalArgumentException("构建表"+table.getTableName()+"的列不存在");
		}
		
		//校验复合联合主键
		if(table.getConstraints() != null && table.getConstraints().size() > 0){
			String constrainName = table.getConstraints().get(0).getConstraintName();
			if(StringUtils.isBlank(constrainName)){
				constrainName = "PK_" + table.getTableName();
			}
			
			for(int i = 1; i < table.getConstraints().size(); i++){
				if(StringUtils.isNotBlank(table.getConstraints().get(i).getConstraintName()) 
						&& !constrainName.equals(table.getConstraints().get(i).getConstraintName())){
					throw new IllegalArgumentException("暂不支持同表中constraintName不一致的情况："+table);
				}
			}
		}
	}
	
	private void buildTable(){
		if(StringUtils.isNotBlank(table.getComments())){
			int len = table.getComments().length()+table.getTableName().length();

			tableSQL += "/";
			for(int i = 0; i < len + 10; i++){
				tableSQL += "*";
			}
			tableSQL += "\n";
			
			for(int i = 0; i < 3; i++){
				tableSQL += " ";
			}
			tableSQL += table.getTableName()+"("+table.getComments()+")\n";
			
			for(int i = 0; i < len + 10; i++){
				tableSQL += "*";
			}
			tableSQL += "/\n";
		}
		
		tableSQL += "create table "+encodeTableName(table.getTableName()) + "(\n";
		
		if(StringUtils.isNotBlank(table.getComments())){
			commentsSQLs.add("COMMENTS ON TABLE "+encodeTableName(table.getTableName())+" IS '"+table.getComments()+"';\n");
		}
	}
	
	private String encodeTableName(String tableName){
		String regex = "^[0-9a-zA-Z\\_-][0-9a-zA-Z\\_-]*[0-9a-zA-Z\\_-]$";
		if(!StringUtils.match(regex, tableName)){
			return "\""+tableName+"\"";
		}else{
			return tableName;
		}
	}
	
	/**
	 * 构建constraint语句，如：CONSTRAINT PK_USERS PRIMARY KEY (ID, AGE)
	 */
	private void buildConstraint(){
		if(table.getConstraints() != null && table.getConstraints().size() > 0){
			constraintSQL = "CONSTRAINT "+table.getConstraints().get(0).getConstraintName()+" PRIMARY KEY (";	
			
			List<Constraint> cons = table.getConstraints();
			for(int i = 0; i < cons.size(); i++){
				constraintSQL += cons.get(i).getColumnName();
				if(i != cons.size() - 1){
					constraintSQL += ",";
				}
			}
			constraintSQL += ")\n";
		}
	}
	
	private void buildColumns(){
		for(Column col : table.getColumns()){
			buildColumn(col);
			buildColumnComments(col);
		}
	}
	
	private void buildColumn(Column column){
		String type = getType(column);
		
		String def = getDefault(column);
		
		String nullable = getNullable(column);
		
		String columnSql = column.getColumnName()+" "+type;
		if(StringUtils.isNotBlank(def)){
			columnSql += " "+def;
		}
		if(StringUtils.isNotBlank(nullable)){
			columnSql += " "+nullable;
		}
		columnSQLs.add(columnSql);
	}
	
	private void buildColumnComments(Column column){
		if(StringUtils.isNotBlank(column.getComments())){
			commentsSQLs.add("COMMENT ON COLUMN "+table.getTableName()+"."+column.getColumnName()+" IS '"+column.getComments()+"';\n");
		}
	}
	
	private String getType(Column column){
		String type = DataTypeResolverComposite.INSTANCE.resolve(column);
		if(StringUtils.isBlank(type)){
			throw new IllegalArgumentException("暂不支持数据类型:"+column);
		}
		return type;
	}
	
	private String getDefault(Column column){
		String def = null;
		if(column.getDataDefault() != null){
			if("NUMBER".equals(column.getDataType())){
				def = "default " + column.getDataDefault().trim();
			}else if("CHAR".equals(column.getDataType())){
				def = "default " + column.getDataDefault().trim();
			}else if("VARCHAR2".equals(column.getDataType())){
				def = "default " + column.getDataDefault().trim();
			}else if("TIMESTAMP(6)".equals(column.getDataType())){
				def = "default " + column.getDataDefault().trim();
			}else if("DATE".equals(column.getDataType())){
				def = "default " + column.getDataDefault().trim();
			}else if("NVARCHAR2".equals(column.getDataType())){
				def = "default " + "'"+column.getDataDefault().trim()+"'";
			}else{
				throw new IllegalArgumentException("暂不支持数据类型默认值:"+column);
			}
		}
		return def;
	}
	
	private String getNullable(Column column){
		String nullable = null;
		if(!column.isNullable()){
			nullable = "not null";
		}else{
			//nullable = "null";
		}
		return nullable;
	}
	
	public String getResult(){
		validate();
		
		buildTable();

		buildConstraint();
		
		buildColumns();
		
		//拼接表头
		String sql = tableSQL;
		
		//拼接列
		for(int i = 0; i < columnSQLs.size(); i++){
			sql += PRESTR + columnSQLs.get(i);
			if(StringUtils.isNotBlank(constraintSQL) || i != columnSQLs.size() - 1){
				sql += ",";
			}
			sql += "\n";
		}
		
		//拼接主键约束
		if(StringUtils.isNotBlank(constraintSQL)){
			sql += PRESTR + constraintSQL;
		}
		
		sql += ");\n";
		
		//拼接注释
		for(int i = 0; i < commentsSQLs.size(); i++){
			sql += commentsSQLs.get(i);
		}
		sql+= "\n\n";
		
		return sql;
	}
	
}
