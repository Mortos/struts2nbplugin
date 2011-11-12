/*
 * ParentPackageLocatorTest.java
 * 
 * Created on Sep 8, 2007, 12:24:27 AM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.netbeans.modules.web.frameworks.struts2.api.configmodel.visitors;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Struts;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsModel;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsPackage;
import org.netbeans.modules.web.frameworks.struts2.TestUtil;
import static org.junit.Assert.*;

/**
 *
 * @author Rohan Ranade
 */
public class ParentPackageLocatorTest {
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
    @Test
    public void localParentPackageLocationTest() throws Exception {
        StrutsModel model = TestUtil.loadStrutsModel("api/configmodel/resources/mailreader-support.xml");
        Struts struts = model.getRootComponent();
        
        for(StrutsPackage sp : struts.getPackages()) {
            if(sp.getName().equals("wildcard")) {
                ParentPackageLocator locator = new ParentPackageLocator();
                StrutsPackage parent = locator.findParentPackage(sp);
                
                assertEquals("mailreader-support", parent.getName());
            }
        }
    }
}
