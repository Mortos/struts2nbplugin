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

package org.netbeans.modules.web.frameworks.struts2.api.configmodel;

import org.netbeans.modules.web.frameworks.struts2.api.configmodel.impl.StrutsModelImpl;
import org.netbeans.modules.xml.xam.AbstractModelFactory;
import org.netbeans.modules.xml.xam.ModelSource;

/**
 *
 * @author Rohan Ranade
 */
public class StrutsModelFactory extends AbstractModelFactory<StrutsModel> {

    private StrutsModelFactory() {
    }

    private static StrutsModelFactory instance;
    public static StrutsModelFactory getInstance() {
        if(instance == null) {
            instance = new StrutsModelFactory();
        }
        
        return instance;
    }
    
    protected StrutsModel createModel(ModelSource source) {
        return new StrutsModelImpl(source);
    }

    @Override
    public StrutsModel getModel(ModelSource source) {
        return super.getModel(source);
    }
}
