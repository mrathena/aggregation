/**
 *    Copyright 2006-2016 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.internal;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.MergeConstants;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.util.StringUtility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;

/** The Class DefaultCommentGenerator.
 *
 * @author Jeff Butler */
public class DefaultCommentGenerator implements CommentGenerator {

	/** The properties. */
	private Properties properties;
	/** The suppress date. */
	private boolean suppressDate;
	/** The suppress all comments. */
	private boolean suppressAllComments;
	/** The addition of table remark's comments. If suppressAllComments is true, this option is ignored */
	private SimpleDateFormat dateFormat;

	/** Instantiates a new default comment generator. */
	public DefaultCommentGenerator() {
		super();
		properties = new Properties();
		suppressDate = false;
		suppressAllComments = false;
	}

	/* (non-Javadoc)
	 * 
	 * @see org.mybatis.generator.api.CommentGenerator#addJavaFileComment(org.mybatis.generator.api.dom.java.CompilationUnit) */
	public void addJavaFileComment(CompilationUnit compilationUnit) {
		// add no file level comments by default
	}

	/** Adds a suitable comment to warn users that the element was generated, and when it was generated.
	 *
	 * @param xmlElement the xml element */
	public void addComment(XmlElement xmlElement) {
		if (suppressAllComments) {
			return;
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see org.mybatis.generator.api.CommentGenerator#addRootComment(org.mybatis.generator.api.dom.xml.XmlElement) */
	public void addRootComment(XmlElement rootElement) {
		// add no document level comments by default
	}

	/* (non-Javadoc)
	 * 
	 * @see org.mybatis.generator.api.CommentGenerator#addConfigurationProperties(java.util.Properties) */
	public void addConfigurationProperties(Properties properties) {
		this.properties.putAll(properties);
		suppressDate = isTrue(properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_DATE));
		suppressAllComments = isTrue(properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_ALL_COMMENTS));
//		addRemarkComments = isTrue(properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_ADD_REMARK_COMMENTS));
		String dateFormatString = properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_DATE_FORMAT);
		if (StringUtility.stringHasValue(dateFormatString)) {
			dateFormat = new SimpleDateFormat(dateFormatString);
		}
	}

	/** This method adds the custom javadoc tag for. You may do nothing if you do not wish to include the Javadoc tag - however, if you do not include the Javadoc tag then the Java merge capability of the eclipse plugin will break.
	 *
	 * @param javaElement the java element
	 * @param markAsDoNotDelete the mark as do not delete */
	protected void addJavadocTag(JavaElement javaElement, boolean markAsDoNotDelete) {
		javaElement.addJavaDocLine(" *"); //$NON-NLS-1$
		StringBuilder sb = new StringBuilder();
		sb.append(" * "); //$NON-NLS-1$
		sb.append(MergeConstants.NEW_ELEMENT_TAG);
		if (markAsDoNotDelete) {
			sb.append(" do_not_delete_during_merge"); //$NON-NLS-1$
		}
		String s = getDateString();
		if (s != null) {
			sb.append(' ');
			sb.append(s);
		}
		javaElement.addJavaDocLine(sb.toString());
	}

	/** This method returns a formated date string to include in the Javadoc tag and XML comments. You may return null if you do not want the date in these documentation elements.
	 * 
	 * @return a string representing the current timestamp, or null */
	protected String getDateString() {
		if (suppressDate) {
			return null;
		} else if (dateFormat != null) {
			return dateFormat.format(new Date());
		} else {
			return new Date().toString();
		}
	}

	public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
		if (suppressAllComments) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("/** ").append(introspectedTable.getFullyQualifiedTable()).append(" */");
		innerClass.addJavaDocLine(sb.toString());
	}

	public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
		if (suppressAllComments) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("/** ").append(introspectedTable.getFullyQualifiedTable()).append(" */");
		innerClass.addJavaDocLine(sb.toString());
	}

	public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if (suppressAllComments) {
			return;
		}
		int remarksLineCount = 0;
		String remarks = introspectedTable.getRemarks();
		String[] remarkLines = null;
		if (remarks != null) {
			if (!remarks.isEmpty()) {
				remarkLines = remarks.split(System.getProperty("line.separator"));
				remarksLineCount = remarkLines.length;
			}
		}
		StringBuilder sb = new StringBuilder();
		sb.append("/** ").append(introspectedTable.getFullyQualifiedTable());
		switch (remarksLineCount) {
			case 0:
				sb.append(" */");
				topLevelClass.addJavaDocLine(sb.toString());
				break;
			case 1:
				sb.append(" ").append(remarks).append(" */");
				topLevelClass.addJavaDocLine(sb.toString());
				break;
			default:
				topLevelClass.addJavaDocLine(sb.toString());
				for (String line : remarkLines) {
					topLevelClass.addJavaDocLine(" *  ".concat(line));
				}
				topLevelClass.addJavaDocLine(" */");
				break;
		}
	}

	public void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable) {
		if (suppressAllComments) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("/** ").append(introspectedTable.getFullyQualifiedTable()).append(" */");
		innerEnum.addJavaDocLine(sb.toString());
	}

	public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
		if (suppressAllComments) {
			return;
		}
		int remarksLineCount = 0;
		String remarks = introspectedColumn.getRemarks();
		String[] remarkLines = null;
		if (remarks != null) {
			if (!remarks.isEmpty()) {
				remarkLines = remarks.split(System.getProperty("line.separator"));
				remarksLineCount = remarkLines.length;
			}
		}
		StringBuilder sb = new StringBuilder();
		sb.append("/** ").append(introspectedTable.getFullyQualifiedTable())
		.append(".").append(introspectedColumn.getActualColumnName());
		switch (remarksLineCount) {
			case 0:
				sb.append(" */");
				field.addJavaDocLine(sb.toString());
				break;
			case 1:
				sb.append(" ").append(remarks).append(" */");
				field.addJavaDocLine(sb.toString());
				break;
			default:
				field.addJavaDocLine(sb.toString());
				for (String line : remarkLines) {
					field.addJavaDocLine(" *  ".concat(line));
				}
				field.addJavaDocLine(" */");
				break;
		}
	}

	public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
		if (suppressAllComments) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("/** ").append(introspectedTable.getFullyQualifiedTable()).append(" */");
		field.addJavaDocLine(sb.toString());
	}

	public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {
		if (suppressAllComments) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("/** ").append(introspectedTable.getFullyQualifiedTable()).append(" */");
		method.addJavaDocLine(sb.toString());
	}

	public void addGetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
		if (suppressAllComments) {
			return;
		}
		int remarksLineCount = 0;
		String remarks = introspectedColumn.getRemarks();
		String[] remarkLines = null;
		if (remarks != null) {
			if (!remarks.isEmpty()) {
				remarkLines = remarks.split(System.getProperty("line.separator"));
				remarksLineCount = remarkLines.length;
			}
		}
		StringBuilder sb = new StringBuilder();
		sb.append("/** ").append(introspectedTable.getFullyQualifiedTable())
		.append(".").append(introspectedColumn.getActualColumnName());
		switch (remarksLineCount) {
			case 0:
				sb.append(" */");
				method.addJavaDocLine(sb.toString());
				break;
			case 1:
				sb.append(" ").append(remarks).append(" */");
				method.addJavaDocLine(sb.toString());
				break;
			default:
				method.addJavaDocLine(sb.toString());
				for (String line : remarkLines) {
					method.addJavaDocLine(" *  ".concat(line));
				}
				method.addJavaDocLine(" */");
				break;
		}
	}

	public void addSetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
		if (suppressAllComments) {
			return;
		}
		int remarksLineCount = 0;
		String remarks = introspectedColumn.getRemarks();
		String[] remarkLines = null;
		if (remarks != null) {
			if (!remarks.isEmpty()) {
				remarkLines = remarks.split(System.getProperty("line.separator"));
				remarksLineCount = remarkLines.length;
			}
		}
		StringBuilder sb = new StringBuilder();
		sb.append("/** ").append(introspectedTable.getFullyQualifiedTable())
		.append(".").append(introspectedColumn.getActualColumnName());
		switch (remarksLineCount) {
			case 0:
				sb.append(" */");
				method.addJavaDocLine(sb.toString());
				break;
			case 1:
				sb.append(" ").append(remarks).append(" */");
				method.addJavaDocLine(sb.toString());
				break;
			default:
				method.addJavaDocLine(sb.toString());
				for (String line : remarkLines) {
					method.addJavaDocLine(" *  ".concat(line));
				}
				method.addJavaDocLine(" */");
				break;
		}
	}
	
}
