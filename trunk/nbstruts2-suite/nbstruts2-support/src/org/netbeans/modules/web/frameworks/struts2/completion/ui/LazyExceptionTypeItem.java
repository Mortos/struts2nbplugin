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

package org.netbeans.modules.web.frameworks.struts2.completion.ui;

import javax.lang.model.element.TypeElement;
import org.netbeans.api.java.source.CompilationController;
import org.netbeans.api.java.source.ElementHandle;
import org.netbeans.api.java.source.JavaSource;
import org.netbeans.modules.web.frameworks.struts2.utils.JavaUtils;

/**
 * XXX : This approach is more of a hack than a solution. Need to find a way to create
 * XXX : lazy completion items based on a filter criteria
 * 
 * @author Rohan Ranade
 */
public class LazyExceptionTypeItem extends LazyTypeCompletionItem {

    public LazyExceptionTypeItem(int substitutionOffset, ElementHandle<TypeElement> eh, 
            JavaSource javaSource) {
        super(substitutionOffset, eh, javaSource);  
    }

    @Override
    protected boolean isAcceptable(CompilationController cc, TypeElement te) {
        return JavaUtils.isExceptionType(te);
    }
}
