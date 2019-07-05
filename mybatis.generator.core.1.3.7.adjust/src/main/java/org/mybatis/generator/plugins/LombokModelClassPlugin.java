package org.mybatis.generator.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

/**
 * @author mrathena on 2019/7/4 9:09
 */
public class LombokModelClassPlugin extends PluginAdapter {

	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

		topLevelClass.addImportedType(new FullyQualifiedJavaType("lombok.Getter"));
		topLevelClass.addImportedType(new FullyQualifiedJavaType("lombok.Setter"));
		topLevelClass.addImportedType(new FullyQualifiedJavaType("lombok.ToString"));
		topLevelClass.addImportedType(new FullyQualifiedJavaType("lombok.experimental.Accessors"));

		topLevelClass.addAnnotation("@Getter");
		topLevelClass.addAnnotation("@Setter");
		topLevelClass.addAnnotation("@Accessors(chain = true)");
		topLevelClass.addAnnotation("@ToString(callSuper = true)");

		topLevelClass.getMethods().clear();

		// 干掉ID字段
		List<Field> fields = topLevelClass.getFields();
		fields.removeIf(item -> "id".equals(item.getName()));

		return true;
	}

}
