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
import org.netbeans.modules.web.frameworks.struts2.completion.ui.StrutsXMLConfigCompletionItem;
import org.netbeans.modules.web.frameworks.struts2.editor.StrutsXMLUtils;
import org.netbeans.modules.web.frameworks.struts2.utils.StringUtils;
import javax.swing.text.Document;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Struts;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsModel;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsModelFactory;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsPackage;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.util.StrutsUtilities;
import org.netbeans.modules.xml.retriever.catalog.Utilities;
import org.netbeans.modules.xml.xam.Model.State;
import org.netbeans.modules.xml.xam.ModelSource;
import org.openide.filesystems.FileObject;
import org.w3c.dom.Node;

/**
 *
 * @author Sujit Nair (Sujit.Nair@Sun.COM)
 */
public class PackageExtendsCompletor extends Completor {

    public PackageExtendsCompletor() {
    }

    @Override
    public List<StrutsXMLConfigCompletionItem> doCompletion(CompletionContext context) {
        List<StrutsXMLConfigCompletionItem> results = new ArrayList<StrutsXMLConfigCompletionItem>();
        Map<String, FileObject> pkgName2File = new HashMap<String, FileObject>();

        Document document = context.getDocument();
        int offset = context.getCaretOffset();
        
        collectPackagesInFiles(context.getTypedPrefix(), pkgName2File, document, offset);
        
        int substitutionOffset = context.getCurrentToken().getOffset() + 1;

        for (String packageName : pkgName2File.keySet()) {
            StrutsXMLConfigCompletionItem item = StrutsXMLConfigCompletionItem.createStrutsPackageItem(substitutionOffset, 
                    packageName, pkgName2File.get(packageName));
            results.add(item);
        }

        setAnchorOffset(substitutionOffset);
        return results;
    }

    private void collectPackagesInFiles(String namePrefix, Map<String, FileObject> pkgName2File, Document document, int offset) {
        List<Node> localPackages = StrutsXMLUtils.getVisiblePackages(document, offset);
        for (Node pack : localPackages) {
            String packageName = StrutsXMLUtils.getAttribute(pack, StrutsAttributes.NAME.getName());
            if (StringUtils.hasText(packageName) && pkgName2File.get(packageName) == null && packageName.startsWith(namePrefix)) {
                pkgName2File.put(packageName, NbEditorUtilities.getFileObject(document));
            }
        }

        List<FileObject> includedFiles = StrutsXMLUtils.getVisibleIncludes(document, offset);
        findPackagesInExternalFiles(includedFiles, namePrefix, pkgName2File);
    }

    // XXX: look into unifying this code with PackageScanner
    private void findPackagesInExternalFiles(List<FileObject> includedFiles, 
            String namePrefix, Map<String, FileObject> packageNames) {
        if (includedFiles == null || includedFiles.isEmpty()) {
            return;
        } 
        
        for (FileObject fo : includedFiles) {
            ModelSource ms = Utilities.getModelSource(fo, false);
            StrutsModel model = StrutsModelFactory.getInstance().getModel(ms);
            if (model != null && model.getState() == State.VALID) {
                Struts root = model.getRootComponent();
                List<StrutsPackage> packages = root.getPackages();
                for (StrutsPackage pack : packages) {
                    String packageName = pack.getName();
                    if (StringUtils.hasText(packageName) && packageNames.get(packageName) == null && packageName.startsWith(namePrefix)) {
                        packageNames.put(packageName, getPackageFile(pack));
                    }
                }
                findPackagesInExternalFiles(StrutsUtilities.getAllIncludes(root, fo), namePrefix, packageNames);
            }
        }
    }
    
    private FileObject getPackageFile(StrutsPackage pack) {
        return pack.getModel().getModelSource().getLookup().lookup(FileObject.class);
    }
}
