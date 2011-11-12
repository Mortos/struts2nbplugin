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
public class DefaultActionRefCompletor extends Completor {

    public DefaultActionRefCompletor() {
    }

    @Override
    public List<StrutsXMLConfigCompletionItem> doCompletion(final CompletionContext context) {
        List<StrutsXMLConfigCompletionItem> results = new ArrayList<StrutsXMLConfigCompletionItem>();
        final Map<String, Node> name2Action = new HashMap<String, Node>();

        PackageHierarchyTraverser hierarchyTraverser = new PackageHierarchyTraverser(context.getDocument(), context.getCaretOffset());
        hierarchyTraverser.traverse(new PackageProcessor() {

            public void process(Node packageNode) {
                collectActionsInPackage(context.getTypedPrefix(), name2Action, packageNode);
            }
        });

        int substitutionOffset = context.getCurrentToken().getOffset() + 1;

        for (String actionName : name2Action.keySet()) {
            Node action = name2Action.get(actionName);
            String implClass = StrutsXMLUtils.getAttribute(action, StrutsAttributes.CLASS.getName());
            StrutsXMLConfigCompletionItem item = StrutsXMLConfigCompletionItem.createDefaultActionRefItem(substitutionOffset, actionName, implClass);
            results.add(item);
        }

        setAnchorOffset(substitutionOffset);
        return results;
    }

    private void collectActionsInPackage(String namePrefix, Map<String, Node> name2Action, Node packageNode) {
        List<Node> actions = StrutsXMLUtils.getElementsByName(packageNode, StrutsQNames.ACTION.getLocalName());
        for (Node action : actions) {
            String actionName = StrutsXMLUtils.getAttribute(action, StrutsAttributes.NAME.getName());
            if (StringUtils.hasText(actionName) && name2Action.get(actionName) == null && actionName.startsWith(namePrefix)) {
                name2Action.put(actionName, action);
            }
        }
    }
}
