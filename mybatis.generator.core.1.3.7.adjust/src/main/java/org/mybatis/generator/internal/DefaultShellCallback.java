/**
 *    Copyright 2006-2017 the original author or authors.
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

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.mybatis.generator.api.ShellCallback;
import org.mybatis.generator.exception.ShellException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

/**
 * The Class DefaultShellCallback.
 *
 * @author Jeff Butler
 */
public class DefaultShellCallback implements ShellCallback {

	/** The overwrite. */
	private boolean overwrite;

	/**
	 * Instantiates a new default shell callback.
	 *
	 * @param overwrite
	 *            the overwrite
	 */
	public DefaultShellCallback(boolean overwrite) {
		super();
		this.overwrite = overwrite;
	}

	/* (non-Javadoc)
	 * @see org.mybatis.generator.api.ShellCallback#getDirectory(java.lang.String, java.lang.String)
	 */
	@Override
	public File getDirectory(String targetProject, String targetPackage)
			throws ShellException {
		// targetProject is interpreted as a directory that must exist
		//
		// targetPackage is interpreted as a sub directory, but in package
		// format (with dots instead of slashes). The sub directory will be
		// created
		// if it does not already exist

		File project = new File(targetProject);
		if (!project.isDirectory()) {
			throw new ShellException(getString("Warning.9", //$NON-NLS-1$
					targetProject));
		}

		StringBuilder sb = new StringBuilder();
		StringTokenizer st = new StringTokenizer(targetPackage, "."); //$NON-NLS-1$
		while (st.hasMoreTokens()) {
			sb.append(st.nextToken());
			sb.append(File.separatorChar);
		}

		File directory = new File(project, sb.toString());
		if (!directory.isDirectory()) {
			boolean rc = directory.mkdirs();
			if (!rc) {
				throw new ShellException(getString("Warning.10", //$NON-NLS-1$
						directory.getAbsolutePath()));
			}
		}

		return directory;
	}

	/* (non-Javadoc)
	 * @see org.mybatis.generator.api.ShellCallback#refreshProject(java.lang.String)
	 */
	@Override
	public void refreshProject(String project) {
		// nothing to do in the default shell callback
	}

	/* (non-Javadoc)
	 * @see org.mybatis.generator.api.ShellCallback#isMergeSupported()
	 */
	@Override
	public boolean isMergeSupported() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.mybatis.generator.api.ShellCallback#isOverwriteEnabled()
	 */
	@Override
	public boolean isOverwriteEnabled() {
		return overwrite;
	}

	/* (non-Javadoc)
	 * @see org.mybatis.generator.api.ShellCallback#mergeJavaFile(java.lang.String, java.lang.String, java.lang.String[], java.lang.String)
	 */
	@Override
	public String mergeJavaFile(String newFileSource,
								File existingFile, String[] javadocTags, String fileEncoding)
			throws ShellException, FileNotFoundException {
//        throw new UnsupportedOperationException();

		// 本次生成的Java文件内容
		CompilationUnit newCompilationUnit = JavaParser.parse(newFileSource);
		// 上次生成的Java文件内容
		CompilationUnit existCompilationUnit = JavaParser.parse(existingFile);

		// 自定义Mapper.java中的import以原有文件为准
		newCompilationUnit.setImports(existCompilationUnit.getImports());

		List<String> names = Arrays.asList("insert", "insertSelective", "deleteByPrimaryKey", "updateByPrimaryKey",
				"updateByPrimaryKeySelective", "selectByPrimaryKey", "BaseResultMap", "Base_Column_List");
		NodeList<BodyDeclaration<?>> memberList = newCompilationUnit.getType(0).getMembers();
		List<MethodDeclaration> methodList = existCompilationUnit.getType(0).getMethods();
		for (MethodDeclaration methodDeclaration : methodList) {
			String name = methodDeclaration.getName().getIdentifier();
			if (!names.contains(name)) {
				memberList.add(methodDeclaration);
			}
		}

		return newCompilationUnit.toString().replace("    ", "\t").replace("*  ", "* ");
	}

}
