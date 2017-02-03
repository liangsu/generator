package com.ls.generator.support;

public abstract class DataTypeResolverSupport implements DataTypeResolver{

	/** 支持的数据类型名称,如：VARCHAR */
	protected String[] supports;
	
	protected DataTypeResolverSupport(String... supports) {
		this.supports = supports;
	}

	@Override
	public String resolve(DataType dataType) {
		if(!support(dataType)){
			return null;
		}

		return resolveInternal(dataType);
	}
	
	protected boolean support(DataType dataType){
		if(supports == null || supports.length == 0){
			return false;
		}
		
		for (String support : supports) {
			if(support.equalsIgnoreCase(dataType.getDataType())){
				return true;
			}
		}
		
		return false;
	}
	
	protected abstract String resolveInternal(DataType dataType);

}
