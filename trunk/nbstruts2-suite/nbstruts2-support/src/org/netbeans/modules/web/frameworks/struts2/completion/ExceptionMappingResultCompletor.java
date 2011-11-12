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
public class ExceptionMappingResultCompletor extends Completor {

    public ExceptionMappingResultCompletor() {
    }
    
    @Override
    public List<StrutsXMLConfigCompletionItem> doCompletion(final CompletionContext context) {
        List<StrutsXMLConfigCompletionItem> results = new ArrayList<StrutsXMLConfigCompletionItem>();
        final Map<String, Node> name2Result = new HashMap<String, Node>();
        
        PackageHierarchyTraverser hierarchyTraverser = new PackageHierarchyTraverser(context.getDocument(), context.getCaretOffset());
        hierarchyTraverser.traverse(new PackageProcessor() {
            public void process(Node packageNode) {
                collectResultsInPackage(context.getTypedPrefix(), name2Result, packageNode);
            }
        });
        
        int substitutionOffset = context.getCurrentToken().getOffset() + 1;
        
        for(String resultName : name2Result.keySet()) {
            Node result = name2Result.get(resultName);
            String implClass = StrutsXMLUtils.getAttribute(result, StrutsAttributes.TYPE.getName());
            StrutsXMLConfigCompletionItem item = StrutsXMLConfigCompletionItem.createResultItem(substitutionOffset, resultName, implClass);
            results.add(item);
        }
        
        setAnchorOffset(substitutionOffset);
        return results;
    }

    private void collectResultsInPackage(String namePrefix, Map<String, Node> name2Result, Node packageNode) {
        List<Node> globalResultsTags = StrutsXMLUtils.getElementsByName(packageNode, StrutsQNames.GLOBAL_RESULTS.getLocalName());
        for(Node globalResultTag : globalResultsTags) {
            List<Node> resultTags = StrutsXMLUtils.getElementsByName(globalResultTag, StrutsQNames.RESULT.getLocalName());
            for(Node result : resultTags) {
                String resultName = StrutsXMLUtils.getAttribute(result, StrutsAttributes.NAME.getName());
                if(StringUtils.hasText(resultName) && name2Result.get(resultName) == null && resultName.startsWith(namePrefix)) {
                    name2Result.put(resultName, result);
                }
            }
        }
    }
    

}
