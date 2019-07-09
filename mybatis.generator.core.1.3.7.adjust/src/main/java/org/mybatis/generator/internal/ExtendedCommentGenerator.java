package org.mybatis.generator.internal;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.util.StringUtility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * 默认注释生成器(DefaultCommentGenerator)的扩展
 *
 * @author mrathena on 2019/7/4 13:14
 */
public class ExtendedCommentGenerator extends DefaultCommentGenerator {

	private String author;

	private String datetime;

	public String getAuthor() {
		return author;
	}

	public String getDatetime() {
		return datetime;
	}

	private static final String DATE_TIME_PATTERN = "yyyy/MM/dd HH:mm";

	@Override
	public void addComment(XmlElement xmlElement) {}

	@Override
	public void addRootComment(XmlElement rootElement) {}

	@Override
	public void addConfigurationProperties(Properties properties) {
		super.addConfigurationProperties(properties);

		author = properties.getProperty("author");
		author = author == null ? "" : author;
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_PATTERN);
		datetime = sdf.format(new Date());
	}

	@Override
	public void addModelClassComment(TopLevelClass topLevelClass,
									 IntrospectedTable introspectedTable) {
		topLevelClass.addJavaDocLine("/**");
		topLevelClass.addJavaDocLine(" * " + introspectedTable.getFullyQualifiedTable());
		String remark = introspectedTable.getRemarks();
		if (StringUtility.stringHasValue(remark)) {
			topLevelClass.addJavaDocLine(" * ");
			topLevelClass.addJavaDocLine(" * " + remark);
		}
		topLevelClass.addJavaDocLine(" * ");
		topLevelClass.addJavaDocLine(" * " + "@author " + getAuthor() + " on " + getDatetime());
		topLevelClass.addJavaDocLine(" */");
	}

	@Override
	public void addFieldComment(Field field, IntrospectedTable
			introspectedTable, IntrospectedColumn introspectedColumn) {
		String remark = introspectedColumn.getRemarks();
		StringBuilder sb = new StringBuilder();
		sb.append("/** ");
		sb.append(introspectedColumn.getActualColumnName());
		if (StringUtility.stringHasValue(remark)) {
			sb.append(" ");
			sb.append(remark);
		}
		sb.append(" */");
		field.addJavaDocLine(sb.toString());
	}

	@Override
	public void addMapperInterfaceComment(Interface topLevelInterface,
										  IntrospectedTable introspectedTable) {
		topLevelInterface.addJavaDocLine("/**");
		topLevelInterface.addJavaDocLine(" * " + introspectedTable.getFullyQualifiedTable());
		String remark = introspectedTable.getRemarks();
		if (StringUtility.stringHasValue(remark)) {
			topLevelInterface.addJavaDocLine(" * ");
			topLevelInterface.addJavaDocLine(" * " + remark);
		}
		topLevelInterface.addJavaDocLine(" * ");
		topLevelInterface.addJavaDocLine(" * " + "@author " + getAuthor() + " on " + getDatetime());
		topLevelInterface.addJavaDocLine(" */");
	}

	@Override
	public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {
		method.addJavaDocLine("/** ");
		method.addJavaDocLine(" * " + method.getName());
		method.addJavaDocLine(" * ");
		List<Parameter> parameters = method.getParameters();
		if (null != parameters && parameters.size() > 0) {
			for (Parameter parameter : parameters) {
				method.addJavaDocLine(" * @param " + parameter.getName() + " .");
			}
		}
		FullyQualifiedJavaType returnType = method.getReturnType();
		if (null != returnType) {
			method.addJavaDocLine(" * @return .");
		}
		method.addJavaDocLine(" */");
	}

}
