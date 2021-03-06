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

import java.util.List;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.GlobalResults;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Result;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsVisitor;
import org.w3c.dom.Element;

/**
 *
 * @author Rohan Ranade
 */
public class GlobalResultsImpl extends StrutsComponentImpl implements GlobalResults {

    public GlobalResultsImpl(StrutsModelImpl model, Element e) {
        super(model, e);
    }

    public GlobalResultsImpl(StrutsModelImpl model) {
        this(model, createElementNS(model, StrutsQNames.GLOBAL_RESULTS));
    }
    
    public void accept(StrutsVisitor visitor) {
        visitor.visit(this);
    }

    public List<Result> getResults() {
        return super.getChildren(Result.class);
    }

    public void addResult(int index, Result result) {
        super.insertAtIndex(RESULT_PROPERTY, result, index, Result.class);
    }

    public void removeResult(Result result) {
        super.removeChild(RESULT_PROPERTY, result);
    }

}
