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
 * Software is 
 * Aleh Maksimovich <aleh.maksimovich@gmail.com>, 
 *                  <aleh.maksimovich@hiqo-solutions.com>.
 * Portions Copyright 2011 Aleh Maksimovich. All Rights Reserved.
 */
package org.netbeans.modules.framework.xwork.completion.validator;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import javax.xml.xpath.*;
import org.openide.filesystems.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author Aleh
 */
public class XWorkValidatorConfigurationScaner implements FileChangeListener {

    private static final String XWORK_VALIDATOR_NAME_XPATH = "/validators/validator/@name";
    private static final XPathExpression selector;
    private FileObject file;
    private boolean actual;
    private Set<String> choises = new HashSet<String>();

    static {
        XPathExpression expression = null;
        try {
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xPath = xPathFactory.newXPath();
            expression = xPath.compile(XWORK_VALIDATOR_NAME_XPATH);
        } catch (XPathExpressionException ex) {
        }
        selector = expression;
    }

    public XWorkValidatorConfigurationScaner(FileObject file) {
        this.file = file;
        this.actual = false;
    }

    public Set<String> getChoises() {
        if (!actual) {
            refreshChoises();
        }
        return choises;
    }

    @Override
    public void fileFolderCreated(FileEvent fe) {
    }

    @Override
    public void fileDataCreated(FileEvent fe) {
    }

    @Override
    public void fileChanged(FileEvent fe) {
        actual = false;
    }

    @Override
    public void fileDeleted(FileEvent fe) {
    }

    @Override
    public void fileRenamed(FileRenameEvent fre) {
    }

    @Override
    public void fileAttributeChanged(FileAttributeEvent fae) {
    }

    private void refreshChoises() {
        try {
            InputStream fileStream = file.getInputStream();
            InputSource fileSource = new InputSource(fileStream);

            NodeList validatorNames = (NodeList) selector.evaluate(fileSource, XPathConstants.NODESET);

            choises.clear();
            int validatorNamesCount = validatorNames.getLength();
            for (int i = 0; i < validatorNamesCount; i++) {
                Node nameAttribute = validatorNames.item(i);
                choises.add(nameAttribute.getNodeValue());
            }
            actual = true;
        } catch (IOException ex) {
        } catch (XPathExpressionException ex) {
        }
    }
}
