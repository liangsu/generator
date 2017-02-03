package com.ls.generator.support;

public class NumberResolver extends DataTypeResolverSupport{
	
	public NumberResolver() {
		super("NUMBER");
	}

	@Override
	public String resolveInternal(DataType dataType) {
		String type = null;
		if(dataType.getDataScale() == null){
			type = "NUMBER";
		}else if(dataType.getDataScale() == 0){
			if(dataType.getDataPrecision() == null){
				type = "int";
			}else{
				type = "NUMBER("+dataType.getDataPrecision()+")";
			}
		}else{
			type = "NUMBER("+dataType.getDataPrecision()+","+dataType.getDataScale()+")";
		}
		return type;
	}

}
