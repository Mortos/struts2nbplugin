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
package org.netbeans.modules.framework.xwork.completion.configuration.resource;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.openide.util.Exceptions;

/**
 *
 * @author Aleh
 */
public enum XWorkConfigurationCompletionItemIcon {

    CLASS_ICON("class.png"),
    PACKAGE_ICON("package.png");
    private static final String ICONS_PACKAGE = "org/netbeans/modules/framework/xwork/completion/configuration/resource";
    private ImageIcon icon;

    private XWorkConfigurationCompletionItemIcon(String iconName) {
        try {
            String resourcePath = String.format("%s/%s", ICONS_PACKAGE, iconName);
            InputStream imageStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
            Image image = ImageIO.read(imageStream);
            icon = new ImageIcon(image);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
            icon = null;
        } catch (IllegalArgumentException ex) {
            Exceptions.printStackTrace(ex);
            icon = null;
        }
    }

    public ImageIcon getImageIcon() {
        return icon;
    }
}
