/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at http://www.netbeans.org/cddl.html
 * or http://www.netbeans.org/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.netbeans.org/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is 
 * Aleh Maksimovich <aleh.maksimovich@gmail.com>, 
 *                  <aleh.maksimovich@hiqo-solutions.com>.
 * Portions Copyright 2011 Aleh Maksimovich. All Rights Reserved.
 */
package org.netbeans.modules.framework.xwork.completion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementScanner6;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.java.source.ClassIndex;
import org.netbeans.api.java.source.ClassIndex.SearchScope;
import org.netbeans.api.java.source.ClasspathInfo;
import org.netbeans.api.java.source.CompilationController;
import org.netbeans.api.java.source.JavaSource;
import org.netbeans.api.java.source.Task;
import org.netbeans.modules.framework.xwork.completion.configuration.XWorkConfigurationJavaClassAttributeCompletionItem;
import org.netbeans.modules.framework.xwork.completion.configuration.XWorkConfigurationJavaPackageAttributeCompletionItem;
import org.netbeans.spi.editor.completion.CompletionItem;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Aleh
 */
public class XWorkJavaClassCompletor implements XWorkCompletor {

    private static final Set<SearchScope> SCOPE_ALL = EnumSet.allOf(SearchScope.class);
    private Set<CompletionItem> choises = new HashSet<CompletionItem>();

    public XWorkJavaClassCompletor(XWorkCompletionContext context) {
        ClassPath sourceClassPath = ClassPath.getClassPath(context.file(), ClassPath.SOURCE);
        FileObject[] sourceRoots = sourceClassPath.getRoots();
        ArrayList<JavaSource> javaSourceList = new ArrayList<JavaSource>(sourceRoots.length);
        for (FileObject sourceRoot : sourceRoots) {
            ClasspathInfo sourceRootInfo = ClasspathInfo.create(sourceRoot);
            javaSourceList.add(JavaSource.create(sourceRootInfo));
        }

        String typedText = context.typedText().toString();
        for (JavaSource javaSource : javaSourceList) {
            try {
                javaSource.runUserActionTask(new XWorkClassCompletionTask(typedText, SCOPE_ALL, choises), true);
            } catch (IOException ex) {
            }
        }
    }

    @Override
    public Collection<? extends CompletionItem> items() {
        return choises;
    }
}

class XWorkClassCompletionTask implements Task<CompilationController> {

    private static final String PACKAGE_NAME_SEPARATOR = ".";
    private String typedText;
    private Set<SearchScope> scope;
    private Set<CompletionItem> target;

    public XWorkClassCompletionTask(String typedText, Set<SearchScope> scope, Set<CompletionItem> target) {
        this.typedText = typedText;
        this.scope = scope;
        this.target = target;
    }

    @Override
    public void run(CompilationController controller) throws Exception {
        controller.toPhase(JavaSource.Phase.ELEMENTS_RESOLVED);
        ClassIndex classIndex = controller.getClasspathInfo().getClassIndex();

        Set<String> packageNames = classIndex.getPackageNames(typedText, true, scope);
        for (String packageName : packageNames) {
            target.add(new XWorkConfigurationJavaPackageAttributeCompletionItem(packageName));
        }

        String currentPackageName = packageName(typedText);
        String currentClassPrefix = classNamePrefix(typedText);
        PackageElement currentPackage = controller.getElements().getPackageElement(currentPackageName);
        if (currentPackage != null) {
            List<TypeElement> classes = new ClassElementScanner().scan(currentPackage);
            for (TypeElement typeElement : classes) {
                try {
                    CharSequence nameStart = typeElement.getSimpleName().subSequence(0, currentClassPrefix.length());
                    if (nameStart.toString().equalsIgnoreCase(currentClassPrefix)) {
                        target.add(new XWorkConfigurationJavaClassAttributeCompletionItem(typeElement.getQualifiedName().toString()));
                    }
                } catch (IndexOutOfBoundsException ex) {
                }
            }
        }
    }

    private String packageName(String typedText) {
        int lastSeparatorIndex = typedText.lastIndexOf(PACKAGE_NAME_SEPARATOR);
        if (lastSeparatorIndex == -1) {
            return "";
        } else {
            return typedText.substring(0, lastSeparatorIndex);
        }
    }

    private String classNamePrefix(String typedText) {
        int lastSeparatorIndex = typedText.lastIndexOf(PACKAGE_NAME_SEPARATOR);
        if (lastSeparatorIndex == -1) {
            return typedText;
        } else {
            return typedText.substring(lastSeparatorIndex + 1);
        }
    }
}

class ClassElementScanner extends ElementScanner6<List<TypeElement>, Void> {

    public ClassElementScanner() {
        super(new LinkedList<TypeElement>());
    }

    @Override
    public List<TypeElement> visitType(TypeElement typeElement, Void arg) {
        if (typeElement.getKind() == ElementKind.CLASS && isAccessibleClass(typeElement)) {
            DEFAULT_VALUE.add(typeElement);
        }
        return super.visitType(typeElement, arg);
    }

    private boolean isAccessibleClass(TypeElement te) {
        NestingKind nestingKind = te.getNestingKind();
        return (nestingKind == NestingKind.TOP_LEVEL) || (nestingKind == NestingKind.MEMBER && te.getModifiers().contains(Modifier.STATIC));
    }
}