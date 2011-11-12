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

package org.netbeans.modules.web.frameworks.struts2;

import java.awt.Image;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.netbeans.modules.xml.catalog.spi.CatalogDescriptor;
import org.netbeans.modules.xml.catalog.spi.CatalogListener;
import org.netbeans.modules.xml.catalog.spi.CatalogReader;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Catalog for Struts2 DTDs
 * 
 * @author Rohan Ranade
 */
public class Struts2Catalog implements CatalogReader, CatalogDescriptor, EntityResolver {

    private static final String STRUTS_ID_2_0 = "-//Apache Software Foundation//DTD Struts Configuration 2.0//EN";
    private static final String STRUTS_ID_2_1 = "-//Apache Software Foundation//DTD Struts Configuration 2.1//EN";
    
    private static final String URL_STRUTS_2_0 = "nbres:/org/netbeans/modules/web/frameworks/struts2/resources/struts-2.0.dtd";
    private static final String URL_STRUTS_2_1 = "nbres:/org/netbeans/modules/web/frameworks/struts2/resources/struts-2.1.dtd";
    
    public Struts2Catalog() {
    }

    public Iterator getPublicIDs() {
        List<String> list = new ArrayList<String>();
        list.add(STRUTS_ID_2_0);
        list.add(STRUTS_ID_2_1);
        
        return list.listIterator();
    }

    public void refresh() {
    }

    public String getSystemID(String publicId) {
        if (STRUTS_ID_2_0.equals(publicId)) {
            return URL_STRUTS_2_0;
        } else if (STRUTS_ID_2_1.equals(publicId)) {
            return URL_STRUTS_2_1;
        }
        
        return null;
    }

    public String resolveURI(String arg0) {
        return null;
    }

    public String resolvePublic(String arg0) {
        return null;
    }

    public void addCatalogListener(CatalogListener arg0) {
    }

    public void removeCatalogListener(CatalogListener arg0) {
    }

    public Image getIcon(int type) {
        return ImageUtilities.loadImage("org/netbeans/modules/web/frameworks/struts2/resources/StrutsCatalog.png");
    }

    public String getDisplayName() {
        return NbBundle.getMessage(Struts2Catalog.class, "LBL_Struts2Catalog");
    }

    public String getShortDescription() {
        return NbBundle.getMessage(Struts2Catalog.class, "DESC_Struts2Catalog");
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
    }

    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        if (STRUTS_ID_2_0.equals(publicId)) {
            return new InputSource(URL_STRUTS_2_0);
        } else if (STRUTS_ID_2_1.equals(publicId)) {
            return new InputSource(URL_STRUTS_2_1);
        }
        
        return null;
    }

}
