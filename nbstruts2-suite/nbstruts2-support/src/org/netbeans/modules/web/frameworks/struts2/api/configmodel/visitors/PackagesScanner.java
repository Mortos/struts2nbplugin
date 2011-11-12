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

import java.util.Map;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Include;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Struts;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsPackage;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsVisitor;

/**
 *
 * @author Rohan Ranade
 */
public class PackagesScanner extends StrutsVisitor.Deep {

    private Map<String, StrutsPackage> packs;

    public PackagesScanner() {
    }

    public void scanPackages(Struts root, Map<String, StrutsPackage> packs) {
        this.packs = packs;
        root.accept(this);
    }

    @Override
    public void visit(Struts struts) {
        visitChild(struts);
    }

    @Override
    public void visit(StrutsPackage strutsPackage) {
        packs.put(strutsPackage.getName(), strutsPackage);
    }

    @Override
    public void visit(Include include) {
        org.netbeans.modules.xml.xam.Reference<Struts> included = include.getIncluded();
        if (!included.isBroken()) {
            Struts incRef = included.get();
            new PackagesScanner().scanPackages(incRef, packs);
        }
    }
}