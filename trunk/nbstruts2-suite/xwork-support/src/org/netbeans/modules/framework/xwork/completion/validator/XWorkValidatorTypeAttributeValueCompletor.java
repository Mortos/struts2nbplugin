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
 * The Original Software is XWork Support. The Initial Developer of the Original
 * Software is 
 * Aleh Maksimovich <aleh.maksimovich@gmail.com>, 
 *                  <aleh.maksimovich@hiqo-solutions.com>.
 * Portions Copyright 2011 Aleh Maksimovich. All Rights Reserved.
 */
package org.netbeans.modules.framework.xwork.completion.validator;

import java.util.*;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.modules.framework.xwork.editor.EditorSupport;
import org.netbeans.modules.framework.xwork.completion.XWorkCompletionItem;
import org.netbeans.modules.framework.xwork.completion.XWorkCompletor;
import org.netbeans.modules.framework.xwork.completion.XWorkDocuments;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Aleh
 */
public class XWorkValidatorTypeAttributeValueCompletor implements XWorkCompletor {

    private static final String DEFAULT_XWORK_VALIDATORS_CONFIGURATION = "com/opensymphony/xwork2/validator/validators/default.xml";
    private static final String CUSTOM_XWORK_VALIDATORS_CONFIGURATION = "validators.xml";
    private static final WeakHashMap<FileObject, XWorkValidatorConfigurationScaner> scaners = new WeakHashMap<FileObject, XWorkValidatorConfigurationScaner>();
    private EditorSupport context;
    private Set<String> choises = new HashSet<String>();
    private Map<String, FileObject> choiseOrigins = new HashMap<String, FileObject>();
    private String typedText;

    public XWorkValidatorTypeAttributeValueCompletor(EditorSupport context) {
        this.context = context;
        typedText = context.getLeftContent().toString();

        ClassPath compileClassPath = ClassPath.getClassPath(context.getFileObject(), ClassPath.COMPILE);
        FileObject xworkLibraryConfig = compileClassPath.findResource(DEFAULT_XWORK_VALIDATORS_CONFIGURATION);
        extractValidatorNames(xworkLibraryConfig);

        ClassPath sourceClassPath = ClassPath.getClassPath(context.getFileObject(), ClassPath.SOURCE);
        FileObject xworkSourceConfig = sourceClassPath.findResource(CUSTOM_XWORK_VALIDATORS_CONFIGURATION);
        extractValidatorNames(xworkSourceConfig);
    }

    @Override
    public Collection<XWorkCompletionItem> items() {
        Collection<XWorkCompletionItem> result = new LinkedList<XWorkCompletionItem>();
        for (String choise : choises) {
            if (choise.startsWith(typedText)) {
                result.add(new XWorkValidatorAttributeCompletionItem(context, choise));
            }
        }
        return result;
    }

    private void extractValidatorNames(FileObject file) {
        if (!isValidConfig(file)) {
            return;
        }

        XWorkValidatorConfigurationScaner scaner;
        if (scaners.containsKey(file)) {
            scaner = scaners.get(file);
        } else {
            scaner = new XWorkValidatorConfigurationScaner(file);
            file.addFileChangeListener(scaner);
            scaners.put(file, scaner);
        }
        Set<String> fileChoices = scaner.getChoises();
        choises.addAll(fileChoices);
        for (String choice : fileChoices) {
            choiseOrigins.put(choice, file);
        }
    }

    private boolean isValidConfig(FileObject file) {
        return (file != null)
                && file.isValid()
                && XWorkDocuments.CONFIGURATION.isMimeType(file.getMIMEType());
    }

    public FileObject getChoiceOrigin(String choice) {
        return choiseOrigins.get(choice);
    }
}
