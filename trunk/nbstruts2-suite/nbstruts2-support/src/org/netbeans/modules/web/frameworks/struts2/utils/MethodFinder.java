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
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2008 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.modules.web.frameworks.struts2.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.netbeans.api.java.source.ElementUtilities;
import org.netbeans.api.java.source.ElementUtilities.ElementAcceptor;

/**
 *
 * @author Rohan Ranade
 */
public final class MethodFinder {
    private ElementUtilities eu;
    private TypeMirror type;
    private Public publicFlag;
    private Static staticFlag;
    private int argCount;
    private String namePrefix;

    public MethodFinder(ElementUtilities eu, TypeMirror type, String namePrefix, Public publicFlag, Static staticFlag, int argCount) {
        this.eu = eu;
        this.type = type;
        this.namePrefix = namePrefix;
        this.publicFlag = publicFlag;
        this.staticFlag = staticFlag;
        this.argCount = argCount;
    }

    public List<ExecutableElement> findMethods() {
        List<ExecutableElement> retList = new ArrayList<ExecutableElement>();
        Iterable<? extends Element> acceptedElements = eu.getMembers(type, new ElementAcceptor() {
            public boolean accept(Element e, TypeMirror typeMirror) {
                // XXX : Display methods of java.lang.Object? 
                // XXX : Displaying them adds unnecessary clutter in the completion window
                if (e.getKind() == ElementKind.METHOD) {
                    TypeElement te = (TypeElement) e.getEnclosingElement();
                    if (te.getQualifiedName().contentEquals("java.lang.Object")) { // NOI18N
                        return false;
                    }

                    // match name
                    if (!e.getSimpleName().toString().startsWith(namePrefix)) {
                        return false;
                    }

                    ExecutableElement method = (ExecutableElement) e;
                    // match argument count
                    if (argCount != -1 && method.getParameters().size() != argCount) {
                        return false;
                    }

                    // match static
                    if (staticFlag != Static.DONT_CARE) {
                        boolean isStatic = method.getModifiers().contains(Modifier.STATIC);
                        if ((isStatic && staticFlag == Static.NO) || (!isStatic && staticFlag == Static.YES)) {
                            return false;
                        }
                    }

                    // match public
                    if (publicFlag != Public.DONT_CARE) {
                        boolean isPublic = method.getModifiers().contains(Modifier.PUBLIC);
                        if ((isPublic && publicFlag == Public.NO) || (!isPublic && publicFlag == Public.YES)) {
                            return false;
                        }
                    }

                    return true;
                }

                return false;
            }
        });
        
        for(Element e : acceptedElements) {
            retList.add((ExecutableElement) e);
        }
        
        return Collections.unmodifiableList(retList);
    }
}
