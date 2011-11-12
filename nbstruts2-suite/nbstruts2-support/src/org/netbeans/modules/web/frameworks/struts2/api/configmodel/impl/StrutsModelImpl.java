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

import java.util.Set;
import javax.xml.namespace.QName;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Struts;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsComponent;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsComponentFactory;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsModel;
import org.netbeans.modules.xml.xam.ComponentUpdater;
import org.netbeans.modules.xml.xam.ModelSource;
import org.netbeans.modules.xml.xam.dom.AbstractDocumentModel;
import org.w3c.dom.Element;

/**
 *
 * @author Rohan Ranade
 */
public class StrutsModelImpl extends AbstractDocumentModel<StrutsComponent> 
        implements StrutsModel {

    private StrutsComponentFactory factory;
    private StrutsComponent struts;
    
    public StrutsModelImpl(ModelSource source) {
        super(source);
        factory = new StrutsComponentFactoryImpl(this);
    }

    public StrutsComponent createRootComponent(Element root) {
        StrutsComponent newStruts = getFactory().create(root, null);
        if(newStruts != null) {
            struts = newStruts;
        }
        return struts;
    }

    protected ComponentUpdater<StrutsComponent> getComponentUpdater() {
        return new SyncUpdateVisitor();
    }

    public Struts getRootComponent() {
        return (Struts) struts;
    }

    public StrutsComponent createComponent(StrutsComponent parent, Element element) {
        return getFactory().create(element, parent);
    }

    public StrutsComponentFactory getFactory() {
        return factory;
    }

    @Override
    public Set<QName> getQNames() {
        return StrutsQNames.getMappedQNames();
    }
}
