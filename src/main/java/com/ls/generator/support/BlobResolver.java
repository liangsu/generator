package com.ls.generator.support;

public class BlobResolver extends DataTypeResolverSupport{

	public BlobResolver() {
		super("BLOB");
	}

	@Override
	protected String resolveInternal(DataType dataType) {
		return "BLOB("+dataType.getDataLength()+")";
	}
}
