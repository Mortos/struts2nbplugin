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

package org.netbeans.modules.web.frameworks.struts2.api.configmodel.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.netbeans.api.java.project.JavaProjectConstants;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.api.project.Sources;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Include;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Struts;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsModel;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsModelFactory;
import org.netbeans.modules.xml.retriever.catalog.Utilities;
import org.netbeans.modules.xml.xam.ModelSource;
import org.netbeans.modules.xml.xam.Nameable;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author Rohan Ranade
 */
public final class StrutsUtilities {

    private StrutsUtilities() {
    }

    static FileObject getStrutsConfig(Project project) {
        Sources sources = ProjectUtils.getSources(project);
        SourceGroup[] groups = sources.getSourceGroups(JavaProjectConstants.SOURCES_TYPE_JAVA);

        // Pick up struts.xml
        for (int i = 0; i < groups.length; i++) {
            String name = groups[i].getName();
            if (!name.equals("${src.dir}")) {
                // Is there no better way :( ?
                continue;
            }
            FileObject strutsConfig = groups[i].getRootFolder().getFileObject("struts", "xml");
            return strutsConfig;
        }

        return null;
    }

    /**
     * Get a model for the specified struts config file object
     * @param fo a struts config file object
     * @param editable true if the file is editable; false otherwise
     * @return a model for the specified file
     */
    public static StrutsModel getStrutsModel(FileObject fo, boolean editable) {
        ModelSource ms = Utilities.getModelSource(fo, editable);
        StrutsModel model = StrutsModelFactory.getInstance().getModel(ms);

        return model;
    }
    
    /**
     * Get a model for the specified struts config file path
     * @param fo a struts config file path
     * @param editable true if the file is editable; false otherwise
     * @return a model for the specified file
     */
    public static StrutsModel getStrutsModel(String filePath, boolean editable) {
        File f = new File(filePath);
        FileObject fo = FileUtil.toFileObject(f);
        
        return getStrutsModel(fo, editable);
    }

    private static List<String> getNamesList(List<? extends Nameable> objList) {
        List<String> namesList = new ArrayList<String>();
        for (Nameable<?> n : objList) {
            namesList.add(n.getName());
        }

        return namesList;
    }
    
    public static List<FileObject> getAllIncludes(Struts root, FileObject fo) {
        List<FileObject> foIncludedFiles = new ArrayList<FileObject>();
        String sep = System.getProperty("file.separator"); // NOI18N
        if (root != null && fo != null) {
            List<Include> includedFiles = root.getIncludes();
            for (Include includedFile : includedFiles) {
                String fileName = includedFile.getFile();
                String curPath = fo.getParent().getPath();
                
                File fi = new File(curPath + sep + fileName);
                FileObject fileObj = FileUtil.toFileObject(fi);
                if (fileObj != null) {
                    foIncludedFiles.add(fileObj);
                }
            }
        }
        return foIncludedFiles;
    }
}
