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
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2008 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.modules.web.frameworks.struts2.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.text.Document;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsModel;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsModelFactory;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsPackage;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.impl.StrutsAttributes;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.visitors.PackagesScanner;
import org.netbeans.modules.web.frameworks.struts2.editor.StrutsXMLUtils;
import org.netbeans.modules.xml.retriever.catalog.Utilities;
import org.netbeans.modules.xml.text.syntax.dom.Tag;
import org.netbeans.modules.xml.xam.ModelSource;
import org.netbeans.modules.xml.xam.Reference;
import org.openide.filesystems.FileObject;
import org.w3c.dom.Node;

/**
 *
 * @author Rohan Ranade (Rohan.Ranade@Sun.COM)
 */
public class PackageHierarchyTraverser {

    private Document document;
    private int offset;
    private Set<Node> traversed = new HashSet<Node>();
    private boolean traverseIncluded = true;
    
    public PackageHierarchyTraverser(Document document, int offset) {
        this.document = document;
        this.offset = offset;
    }

    public PackageHierarchyTraverser(Document document, int offset, boolean traverseIncluded) {
        this(document, offset);
        this.traverseIncluded = traverseIncluded;
    }
    
    public void traverse(PackageProcessor packageProcessor) {
        initState();
        boolean circular = false;
        Tag startTag = StrutsXMLUtils.getTag(document, offset);
        Node packageTag = StrutsXMLUtils.getPackageTag(startTag);
        List<Node> packages = StrutsXMLUtils.getVisiblePackages(document, offset);
        Node currTag = packageTag;
        String parentName = null;
        while(currTag != null) {
            packageProcessor.process(currTag);
            parentName = StrutsXMLUtils.getAttribute(currTag, StrutsAttributes.EXTENDS.getName());
            if(parentName == null || parentName.equals("")) {
                break; // XXX perhaps include the struts-default.xml items
            }
            
            currTag = getLocalPackageByName(packages, parentName); 
            if(currTag != null && hasBeenTraversed(currTag)) {
                circular = true;
                break; // Circular ref, breaking out
            }
        }
        
        if(!traverseIncluded || circular) {
            return;
        }
        
        StrutsPackage pack = null;
        // package not found locally, so traverse the model and find the package
        List<FileObject> incFiles = StrutsXMLUtils.getVisibleIncludes(document, offset);
        for(FileObject fo : incFiles) {
            ModelSource ms = Utilities.getModelSource(fo, false);
            StrutsModel model = StrutsModelFactory.getInstance().getModel(ms);
            if(model == null) {
                continue;
            }
            try {
                model.sync();
            } catch (IOException ex) {
                continue;
            }
            
            PackagesScanner scanner = new PackagesScanner();
            Map<String, StrutsPackage> packs = new HashMap<String, StrutsPackage>();
            scanner.scanPackages(model.getRootComponent(), packs);
            
            pack = packs.get(parentName);
            if(pack != null) {
                break;
            }
        }
        
        while(pack != null) {
            packageProcessor.process(pack.getPeer());
            Reference<StrutsPackage> parentRef = pack.getParentPackage();
            if(parentRef == null || parentRef.isBroken()) {
                break;
            }
            
            pack = parentRef.get();
            if(hasBeenTraversed(pack.getPeer())) {
                break;
            }
        }
    }
    
    private Node getLocalPackageByName(List<Node> localPackages, String name) {
        for(Node p : localPackages) {
            if(StrutsXMLUtils.hasAttribute(p, StrutsAttributes.NAME.getName())) {
                String pName = StrutsXMLUtils.getAttribute(p, StrutsAttributes.NAME.getName());
                if(pName.equals(name)) {
                    return p;
                }
            }
        }
                
        return null;
    }
    
    private boolean hasBeenTraversed(Node node) {
        if(traversed.contains(node)) {
            return true;
        }
        
        traversed.add(node);
        return false;
    }
    
    private void initState() {
        this.traversed.clear();
    }
}
