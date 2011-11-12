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

import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Include;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Struts;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsComponent;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsModel;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsVisitor;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.util.StrutsUtilities;
import org.netbeans.modules.xml.xam.AbstractReference;
import org.netbeans.modules.xml.xam.Reference;
import org.openide.filesystems.FileObject;
import org.w3c.dom.Element;

/**
 *
 * @author Rohan Ranade
 */
public class IncludeImpl extends StrutsComponentImpl implements Include {

    public IncludeImpl(StrutsModelImpl model, Element e) {
        super(model, e);
    }

    public IncludeImpl(StrutsModelImpl model) {
        this(model, createElementNS(model, StrutsQNames.INCLUDE));
    }

    public void accept(StrutsVisitor visitor) {
        visitor.visit(this);
    }

    public String getFile() {
        return super.getAttribute(StrutsAttributes.FILE);
    }

    public void setFile(String file) {
        super.setAttribute(FILE_PROPERTY, StrutsAttributes.FILE, file);
    }

    public Reference<Struts> getIncluded() {
        // First see if we have a test mock ref set, if yes, return it
        if(getTestIncluded() != null) return getTestIncluded();
        
        return new IncludedModelReference();
    }
    
    // ** Start - only for testing, not to be used by client code
    private Reference<Struts> testIncluded = null;
    
    public void setTestIncluded(Reference<Struts> includedModelRef) {
        this.testIncluded = includedModelRef;
    }
    
    private Reference<Struts> getTestIncluded() {
        return testIncluded;
    }
    // ** End
    
    private final class IncludedModelReference extends AbstractReference<Struts> {

        public IncludedModelReference() {
            super(Struts.class, IncludeImpl.this, IncludeImpl.this.getFile());
        }
        
        public Struts get() {
            if (getReferenced() == null) {
                FileObject targetFO = getAbsoluteLocation(refString);
                if(targetFO != null) {
                    StrutsModel model = StrutsUtilities.getStrutsModel(targetFO, false);
                    if(model != null) {
                        Struts root = model.getRootComponent();
                        setReferenced(root);
                    }
                }
            }
            
            return getReferenced();
        }
        
        protected StrutsComponent getReferencingComponent() {
            return (StrutsComponent) super.getParent();
        }
        
        private FileObject getAbsoluteLocation(String relLocation) {
            FileObject baseFO = 
                    IncludeImpl.this.getModel().getModelSource().getLookup().lookup(FileObject.class);
            if(baseFO == null) {
                return null;
            }
            
            FileObject targetFO = baseFO.getParent().getFileObject(relLocation);
            
            return targetFO;
        }
    }
}
