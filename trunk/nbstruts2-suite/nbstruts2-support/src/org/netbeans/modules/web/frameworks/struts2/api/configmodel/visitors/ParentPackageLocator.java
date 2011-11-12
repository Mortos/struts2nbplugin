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

package org.netbeans.modules.web.frameworks.struts2.api.configmodel.visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Include;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Struts;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsComponent;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsPackage;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsVisitor;
import org.netbeans.modules.xml.xam.Model.State;

/**
 * Locate parent package of a specified Struts package
 * 
 * @author Rohan Ranade
 */
public class ParentPackageLocator extends StrutsVisitor.Deep {

    private StrutsPackage basePackage;
    private StrutsPackage parentPackage;
    
    private List<Struts> includesList = new ArrayList<Struts>();
    
    public ParentPackageLocator() {
    }

    public StrutsPackage findParentPackage(StrutsPackage basePackage) {
        if(basePackage.getModel().getState() == State.NOT_WELL_FORMED) {
            return null;
        }
        
        this.basePackage = basePackage;
        Struts root = basePackage.getModel().getRootComponent();
        root.accept(this);
        
        if(parentPackage != null) {
            return parentPackage;
        }
        
        Map<String, StrutsPackage> packs = new HashMap<String, StrutsPackage>();
        for(Struts s : includesList) {
            new PackagesScanner().scanPackages(s, packs);
        }
        
        parentPackage = packs.get(basePackage.getParentPackageName());
        
        return parentPackage;
    }
    
    @Override
    public void visit(Struts struts) {
        visitChild(struts);
    }

    @Override
    public void visit(StrutsPackage strutsPackage) {
        if(isVisible(strutsPackage) && strutsPackage.getName().equals(basePackage.getParentPackageName())) {
            this.parentPackage = strutsPackage;
        }
    }

    @Override
    public void visit(Include include) {
        if(isVisible(include)) {
            org.netbeans.modules.xml.xam.Reference<Struts> included = include.getIncluded();
            if(!included.isBroken()) {
                Struts incRef = included.get();
                includesList.add(incRef);
            }
        }
    }
    
    private boolean isVisible(StrutsComponent component) {
        return component.findPosition() < basePackage.findPosition();
    }
}
