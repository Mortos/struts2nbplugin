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

package org.netbeans.modules.web.frameworks.struts2.completion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.impl.StrutsAttributes;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.impl.StrutsQNames;
import org.netbeans.modules.web.frameworks.struts2.completion.ui.StrutsXMLConfigCompletionItem;
import org.netbeans.modules.web.frameworks.struts2.editor.StrutsXMLUtils;
import org.netbeans.modules.web.frameworks.struts2.utils.PackageHierarchyTraverser;
import org.netbeans.modules.web.frameworks.struts2.utils.PackageProcessor;
import org.netbeans.modules.web.frameworks.struts2.utils.StringUtils;
import org.w3c.dom.Node;

/**
 *
 * @author Sujit Nair, Rohan Ranade
 */
public class DefaultInterceptorRefCompletor extends Completor {

    public DefaultInterceptorRefCompletor() {
    }
    
    @Override
    public List<StrutsXMLConfigCompletionItem> doCompletion(final CompletionContext context) {
        List<StrutsXMLConfigCompletionItem> results = new ArrayList<StrutsXMLConfigCompletionItem>();
        final Map<String, Node> name2Interceptor = new HashMap<String, Node>();
        
        PackageHierarchyTraverser hierarchyTraverser = new PackageHierarchyTraverser(context.getDocument(), context.getCaretOffset());
        hierarchyTraverser.traverse(new PackageProcessor() {
            public void process(Node packageNode) {
                collectInterceptorsInPackage(context.getTypedPrefix(), name2Interceptor, packageNode);
            }
        });
        
        int substitutionOffset = context.getCurrentToken().getOffset() + 1;
        
        for(String interceptorName : name2Interceptor.keySet()) {
            Node interceptor = name2Interceptor.get(interceptorName);
            String implClass = StrutsXMLUtils.getAttribute(interceptor, StrutsAttributes.CLASS.getName());
            StrutsXMLConfigCompletionItem item = StrutsXMLConfigCompletionItem.createDefaultInterceptorRefItem(substitutionOffset, interceptorName, implClass);
            results.add(item);
        }
        
        setAnchorOffset(substitutionOffset);
        return results;
    }

    private void collectInterceptorsInPackage(String namePrefix, Map<String, Node> name2Interceptor, Node packageNode) {
        List<Node> interceptorsTags = StrutsXMLUtils.getElementsByName(packageNode, StrutsQNames.INTERCEPTORS.getLocalName());
        for(Node interceptorsTag : interceptorsTags) {
            List<Node> interceptors = StrutsXMLUtils.getElementsByName(interceptorsTag, StrutsQNames.INTERCEPTOR.getLocalName());
            for(Node interceptor : interceptors) {
                String interceptorName = StrutsXMLUtils.getAttribute(interceptor, StrutsAttributes.NAME.getName());
                if(StringUtils.hasText(interceptorName) && name2Interceptor.get(interceptorName) == null && interceptorName.startsWith(namePrefix)) {
                    name2Interceptor.put(interceptorName, interceptor);
                }
            }
        }
    }
    

}
