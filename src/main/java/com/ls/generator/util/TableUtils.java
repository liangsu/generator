package com.ls.generator.util;

import com.ls.generator.db.Column;
import com.ls.generator.db.Table;
import com.ls.generator.support.DataTypeResolverComposite;

public class TableUtils {

	public static void validate(Table table){
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
	
	public static String getType(Column column){
		String type = DataTypeResolverComposite.INSTANCE.resolve(column);
		if(StringUtils.isBlank(type)){
			throw new IllegalArgumentException("暂不支持数据类型:"+column);
		}
		return type;
	}
	
	
	
}
