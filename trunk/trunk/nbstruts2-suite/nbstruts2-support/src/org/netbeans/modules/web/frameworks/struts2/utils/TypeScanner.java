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

package org.netbeans.modules.web.frameworks.struts2.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementScanner6;

/**
 *
 * @author Rohan Ranade (Rohan.Ranade@Sun.COM)
 */
public class TypeScanner extends ElementScanner6<List<TypeElement>, Void> {

    private Set<ElementKind> acceptableKinds;

    public TypeScanner(Set<ElementKind> acceptableKinds) {
        super(new ArrayList<TypeElement>());
        this.acceptableKinds = acceptableKinds;
    }

    @Override
    public List<TypeElement> visitType(TypeElement typeElement, Void arg) {
        if (acceptableKinds.contains(typeElement.getKind()) && JavaUtils.isAccessibleClass(typeElement)) {
            DEFAULT_VALUE.add(typeElement);
        }

        return super.visitType(typeElement, arg);
    }

    @Override
    public List<TypeElement> visitExecutable(ExecutableElement arg0, Void arg1) {
        return DEFAULT_VALUE;
    }
}
