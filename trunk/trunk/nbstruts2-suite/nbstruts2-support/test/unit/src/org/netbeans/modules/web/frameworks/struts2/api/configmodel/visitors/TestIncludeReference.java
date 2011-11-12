/*
 * TestIncludeReference.java
 * 
 * Created on Sep 10, 2007, 3:08:56 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.netbeans.modules.web.frameworks.struts2.api.configmodel.visitors;

import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Struts;
import org.netbeans.modules.xml.xam.Reference;

/**
 * Mock reference for testing only
 * @author Rohan Ranade
 */
public class TestIncludeReference implements Reference<Struts> {

    private Struts ref;
    private String refStr;

    public TestIncludeReference(Struts ref, String refStr) {
        this.ref = ref;
        this.refStr = refStr;
    }
    
    public Struts get() {
        return this.ref;
    }

    public Class<Struts> getType() {
        return Struts.class;
    }

    public boolean isBroken() {
        return false;
    }

    public boolean references(Struts ref) {
        if(ref.equals(this.ref)) {
            return true;
        }
        
        return false;
    }

    public String getRefString() {
        return this.refStr;
    }
}
