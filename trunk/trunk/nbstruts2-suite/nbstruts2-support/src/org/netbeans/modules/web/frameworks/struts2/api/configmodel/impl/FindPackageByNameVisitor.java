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

package org.netbeans.modules.web.frameworks.struts2.api.configmodel.impl;

import org.netbeans.modules.web.frameworks.struts2.api.configmodel.ReferenceableStrutsComponent;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsComponent;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsPackage;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsVisitor;

/**
 *
 * @author Rohan Ranade
 */
public class FindPackageByNameVisitor<T extends ReferenceableStrutsComponent> extends StrutsVisitor.Deep {

    private String name;
    private Class<T> type;
    private T found;
    
    public FindPackageByNameVisitor() {
        
    }
    
    public T find(StrutsComponent root, String name, Class<T> type) {
        if(name == null || type == null) {
            return null;
        }
        
        this.name = name;
        this.type = type;
        root.accept(this);
        
        return found;
    }

    @Override
    public void visit(StrutsPackage component) {
        if(name.equals(component.getName())) {
            found = type.cast(component);
            return;
        }
    }

    
}
