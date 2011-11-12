package org.netbeans.modules.web.frameworks.struts2.api.configmodel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import org.netbeans.modules.web.frameworks.struts2.TestUtil;
import org.netbeans.modules.xml.xam.Model;
import static org.junit.Assert.*;

/**
 *
 * @author Rohan Ranade
 */
public class SyncUpdateTest extends TestCase {
    
    public SyncUpdateTest(String testName) {
        super(testName);
    }
    
    @Override
    public void setUp() throws Exception {
    }

    
    @Override
    public void tearDown() throws Exception {
    }
    
    public void testSyncConstantElement() throws Exception {
        StrutsModel model = TestUtil.loadStrutsModel("api/configmodel/resources/struts-mailreader.xml");
        Constant constant = model.getRootComponent().getConstants().get(0);
        
        assertEquals("do", constant.getValue());
        TestUtil.setDocumentContentTo(model, "api/configmodel/resources/struts-mailreader-02.xml");
        assertEquals("foobar", constant.getValue());
    }
    
    private Map<String, PropertyChangeEvent> events = new HashMap<String, PropertyChangeEvent>();

    public void testSyncRemoveConstantElement() throws Exception {
        StrutsModel model = TestUtil.loadStrutsModel("api/configmodel/resources/struts-mailreader.xml");
        model.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
                events.put(event.getPropertyName(), event);
                System.out.format("Event (RemoveRule):  property name: %s, old value: %s, new value %s%n", 
                        event.getPropertyName(), 
                        event.getOldValue(), 
                        event.getNewValue());
            }
        });
        
        try {
            TestUtil.setDocumentContentTo(model, "api/configmodel/resources/struts-mailreader-empty.xml");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            assertTrue(false);
        }
        
        assertNotNull(events.get(Struts.CONSTANT_PROPERTY));
    }
    
    private boolean propertyChangeCalled = false;
    
    public void testSyncNotWellFormedElement() throws Exception {
        StrutsModel model = TestUtil.loadStrutsModel("api/configmodel/resources/struts-mailreader-wellformed.xml");
        List<Constant> constants = model.getRootComponent().getConstants();
        assertEquals("struts.action.extension", constants.get(0).getName());
        
        boolean exceptionThrown = false;
        
        try {
            TestUtil.setDocumentContentTo(model, "api/configmodel/resources/struts-mailreader-notwellformed.xml");
        } catch (IOException ioe) {
            exceptionThrown = true;
            System.out.println(ioe.getMessage());
        } finally {
            assertTrue(exceptionThrown);
        }
        
       assertTrue(model.getState() == Model.State.NOT_WELL_FORMED);
        
        model.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
                propertyChangeCalled = true;
            }
        });
        TestUtil.setDocumentContentTo(model, "api/configmodel/resources/struts-mailreader-wellformed.xml");
        
        assertTrue(propertyChangeCalled);
    }
}
