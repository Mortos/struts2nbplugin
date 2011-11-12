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

package org.netbeans.modules.web.frameworks.struts2.completion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.impl.StrutsAttributes;
import org.netbeans.modules.web.frameworks.struts2.editor.StrutsXMLUtils;
import org.netbeans.modules.web.frameworks.struts2.utils.Public;
import org.netbeans.modules.web.frameworks.struts2.utils.Static;
import org.w3c.dom.Node;

/**
 *
 * @author Rohan Ranade (Rohan.Ranade@Sun.COM)
 */
public class ActionMethodCompletor extends JavaMethodCompletor {

    public ActionMethodCompletor() {
    }

    @Override
    protected Public getPublicFlag(CompletionContext context) {
        return Public.DONT_CARE;
    }

    @Override
    protected Static getStaticFlag(CompletionContext context) {
        return Static.NO;
    }

    @Override
    protected int getArgCount(CompletionContext context) {
        return 0;
    }

    @Override
    protected String getTypeName(CompletionContext context) {
        Node actionTag = context.getTag();
        return StrutsXMLUtils.getAttribute(actionTag, StrutsAttributes.CLASS.getName());
    }

    @Override
    protected List<ExecutableElement> filter(List<ExecutableElement> methods) {
        List<ExecutableElement> retList = new ArrayList<ExecutableElement>();
        
        // return only methods which return a string
        for(ExecutableElement ee : methods) {
            if(ee.getReturnType().getKind() == TypeKind.DECLARED) {
                DeclaredType retType = (DeclaredType) ee.getReturnType();
                TypeElement te = (TypeElement) retType.asElement();
                if(te.getQualifiedName().toString().equals("java.lang.String")) { // NOI18N
                    retList.add(ee);
                }
            }
        }
        
        return Collections.unmodifiableList(retList);
    }
}
