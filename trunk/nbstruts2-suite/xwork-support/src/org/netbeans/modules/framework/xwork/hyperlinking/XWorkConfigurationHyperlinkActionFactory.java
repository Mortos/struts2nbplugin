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
package org.netbeans.modules.framework.xwork.hyperlinking;

import java.io.IOException;
import java.util.ArrayList;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.java.source.ClasspathInfo;
import org.netbeans.api.java.source.CompilationController;
import org.netbeans.api.java.source.JavaSource;
import org.netbeans.api.java.source.Task;
import org.netbeans.api.java.source.ui.ElementOpen;
import org.netbeans.modules.framework.xwork.completion.XWorkXMLCompletionContext;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Aleh
 */
public class XWorkConfigurationHyperlinkActionFactory {

    private static final String VALIDATOR_TAG = "validator";
    private static final String CLASS_ATTRIBITE = "class";

    public static boolean isRegisteredHyperlinkPoint(XWorkXMLCompletionContext context) {
        return context.atAttribute(CLASS_ATTRIBITE, VALIDATOR_TAG);
    }

    public static void hyperlinkAction(XWorkXMLCompletionContext context) {
        if (context.atAttribute(CLASS_ATTRIBITE, VALIDATOR_TAG)) {
            navigateToJavaClass(context);
        }
    }

    private static void navigateToJavaClass(XWorkXMLCompletionContext context) {
        ClassPath sourceClassPath = ClassPath.getClassPath(context.file(), ClassPath.SOURCE);
        FileObject[] sourceRoots = sourceClassPath.getRoots();
        ArrayList<JavaSource> javaSourceList = new ArrayList<JavaSource>(sourceRoots.length);
        for (FileObject sourceRoot : sourceRoots) {
            ClasspathInfo sourceRootInfo = ClasspathInfo.create(sourceRoot);
            javaSourceList.add(JavaSource.create(sourceRootInfo));
        }

        for (JavaSource javaSource : javaSourceList) {
            try {
                javaSource.runUserActionTask(new XWorkClassNavigationTask(context.text()), true);
            } catch (IOException ex) {
            }
        }
    }
}

class XWorkClassNavigationTask implements Task<CompilationController> {

    private CharSequence className;

    public XWorkClassNavigationTask(CharSequence className) {
        this.className = className;
    }

    @Override
    public void run(CompilationController controller) throws Exception {
        Elements elements = controller.getElements();
        TypeElement element = elements.getTypeElement(className);
        if (element != null) {
            ElementOpen.open(controller.getClasspathInfo(), element);
        }
    }
}