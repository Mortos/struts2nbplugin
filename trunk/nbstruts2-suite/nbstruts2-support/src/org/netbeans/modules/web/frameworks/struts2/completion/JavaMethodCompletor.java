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
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2007 Sun
 * Microsystems, Inc. All Rights Reserved.
 */
package org.netbeans.modules.web.frameworks.struts2.completion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import org.netbeans.api.java.source.CompilationController;
import org.netbeans.api.java.source.ElementUtilities;
import org.netbeans.api.java.source.JavaSource;
import org.netbeans.api.java.source.JavaSource.Phase;
import org.netbeans.api.java.source.Task;
import org.netbeans.modules.web.frameworks.struts2.completion.ui.StrutsXMLConfigCompletionItem;
import org.netbeans.modules.web.frameworks.struts2.utils.JavaUtils;
import org.netbeans.modules.web.frameworks.struts2.utils.MethodFinder;
import org.netbeans.modules.web.frameworks.struts2.utils.Public;
import org.netbeans.modules.web.frameworks.struts2.utils.Static;
import org.openide.util.Exceptions;

/**
 * XXX: Instead of calling the getTypeName() and asking it to fork off another 
 * XXX: java model task, why not do it within the task forked off here and 
 * XXX: pass the controller and needed elements to the getTypeName()?
 * 
 * @author Rohan Ranade (Rohan.Ranade@Sun.COM)
 */
public abstract class JavaMethodCompletor extends Completor {

    @Override
    public List<StrutsXMLConfigCompletionItem> doCompletion(final CompletionContext context) {
        final List<StrutsXMLConfigCompletionItem> results = new ArrayList<StrutsXMLConfigCompletionItem>();
        try {
            final String classBinaryName = getTypeName(context);
            final Public publicFlag = getPublicFlag(context);
            final Static staticFlag = getStaticFlag(context);
            final int argCount = getArgCount(context);

            if (classBinaryName == null || classBinaryName.equals("")) { // NOI18N

                return Collections.<StrutsXMLConfigCompletionItem>emptyList();
            }

            final JavaSource javaSource = JavaUtils.getJavaSource(context.getFileObject());
            if (javaSource == null) {
                return Collections.emptyList();
            }

            javaSource.runUserActionTask(new Task<CompilationController>() {

                public void run(CompilationController controller) throws Exception {
                    controller.toPhase(Phase.ELEMENTS_RESOLVED);
                    TypeElement classElem = JavaUtils.findClassElementByBinaryName(classBinaryName, controller);
                    if (classElem == null) {
                        return;
                    }

                    ElementUtilities eu = controller.getElementUtilities();
                    MethodFinder methodFinder = new MethodFinder(eu, classElem.asType(), context.getTypedPrefix(),
                            publicFlag, staticFlag, argCount);
                    List<ExecutableElement> methods = methodFinder.findMethods();
                    int substitutionOffset = context.getCurrentToken().getOffset() + 1;

                    methods = filter(methods);

                    for (ExecutableElement ee : methods) {
                        StrutsXMLConfigCompletionItem item = StrutsXMLConfigCompletionItem.createMethodItem(
                                substitutionOffset, ee, ee.getEnclosingElement() != classElem,
                                controller.getElements().isDeprecated(ee));
                        results.add(item);
                    }

                    setAnchorOffset(substitutionOffset);
                }
            }, false);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        return results;
    }

    /**
     * Should the method be public
     */
    protected abstract Public getPublicFlag(CompletionContext context);

    /**
     * Should the method be static
     */
    protected abstract Static getStaticFlag(CompletionContext context);

    /**
     * Number of arguments of the method
     */
    protected abstract int getArgCount(CompletionContext context);

    /**
     * Binary name of the class which should be searched for methods
     */
    protected abstract String getTypeName(CompletionContext context);

    /**
     * Post process applicable methods, for eg. return only those
     * methods which return do not return void
     */
    protected List<ExecutableElement> filter(List<ExecutableElement> methods) {
        return methods;
    }
}
