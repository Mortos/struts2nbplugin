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

package org.netbeans.modules.web.frameworks.struts2.ui;

import java.awt.Component;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.project.libraries.Library;
import org.netbeans.modules.web.spi.webmodule.FrameworkConfigurationPanel;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;

/**
 *
 * @author Petr Pisl
 */
public class FrameworkSetupPanel implements FrameworkConfigurationPanel, WizardDescriptor.FinishablePanel, WizardDescriptor.ValidatingPanel {
    
    private WizardDescriptor wizardDescriptor;
    private FrameworkSetupPanelVisual component;
    
    private String error_message;
    private final Set <ChangeListener> listeners = new HashSet<ChangeListener>(1);
    
    private boolean isExampleRequired;
    
    Library selectedLibrary;
    
    boolean customizer;
    
    /** Creates a new instance of FrameworkSetupPanel */
    public FrameworkSetupPanel(boolean customizer) {
        selectedLibrary = null;
        this.customizer = customizer;
        isExampleRequired = true;
    }

    public void enableComponents(boolean b) {
        
    }

    public Component getComponent() {
        if (component == null)
            component = new FrameworkSetupPanelVisual(this);//(this, customizer);
        
        return component;
    }

    public HelpCtx getHelp() {
        return new HelpCtx(FrameworkSetupPanel.class);
    }

    public void readSettings(Object settings) {
        wizardDescriptor = (WizardDescriptor) settings;
        //component.read(wizardDescriptor);
        
        // XXX hack, TemplateWizard in final setTemplateImpl() forces new wizard's title
        // this name is used in NewProjectWizard to modify the title
        Object substitute = ((JComponent) getComponent()).getClientProperty("NewProjectWizard_Title"); // NOI18N
        if (substitute != null)
            wizardDescriptor.putProperty("NewProjectWizard_Title", substitute); // NOI18N
    }

    public void storeSettings(Object settings) {
        WizardDescriptor d = (WizardDescriptor) settings;
        //component.store(d);
        ((WizardDescriptor) d).putProperty("NewProjectWizard_Title", null); // NOI18N
    }

    
    public Library getSelectedLibrary(){
        return selectedLibrary;
    }
    
    public void setSelectedLibrary(Library library){
        selectedLibrary = library;
    }
    
    public boolean isValid() {
        if (error_message != null && !"".equals(error_message)){
            wizardDescriptor.putProperty( "WizardPanel_errorMessage", error_message); // NOI18N
            return false;
        }
        getComponent();
        
        return true; //component.valid(wizardDescriptor);
    }

    public void addChangeListener(ChangeListener changeListener) {
        synchronized (listeners) {
            listeners.add(changeListener);
        }
    }

    public void removeChangeListener(ChangeListener changeListener) {
        synchronized (listeners) {
            listeners.remove(changeListener);
        }
    }

    public boolean isFinishPanel() {
        return true;
    }

    public void validate() throws WizardValidationException {
    }
    
    public boolean isExampleRequired() {
        return isExampleRequired;
    }
    
    public void setIsExampleRequired(boolean isRequired) {
        isExampleRequired = isRequired;
    }
}
