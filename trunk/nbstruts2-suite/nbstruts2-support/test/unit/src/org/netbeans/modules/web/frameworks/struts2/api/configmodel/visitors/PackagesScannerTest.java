package org.netbeans.modules.web.frameworks.struts2.api.configmodel.visitors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Include;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Struts;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsModel;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsPackage;
import org.netbeans.modules.web.frameworks.struts2.TestUtil;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.impl.IncludeImpl;
import static org.junit.Assert.*;

/**
 *
 * @author Rohan Ranade
 */
public class PackagesScannerTest {
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
    public void simpleNonIncludedPackagesScanTest() throws Exception {
        StrutsModel model = TestUtil.loadStrutsModel("api/configmodel/resources/mailreader-support.xml");
        Struts struts = model.getRootComponent();
        
        Map<String, StrutsPackage> packageMap = new HashMap<String, StrutsPackage>();
        PackagesScanner scanner = new PackagesScanner();
        scanner.scanPackages(struts, packageMap);
        
        assertNotNull(packageMap.get("mailreader-support"));
        assertNotNull(packageMap.get("subscription"));
        assertNotNull(packageMap.get("wildcard"));
    }
    
    @Test
    public void simpleIncludedPackagesScanTest() throws Exception {
        StrutsModel model = TestUtil.loadStrutsModel("api/configmodel/resources/struts-mailreader.xml");
        Struts struts = model.getRootComponent();
        
        // Set up the mock references so that the visitor can visit them in the test case
        StrutsModel incModel1 = TestUtil.loadStrutsModel("api/configmodel/resources/mailreader-default.xml");
        StrutsModel incModel2 = TestUtil.loadStrutsModel("api/configmodel/resources/mailreader-support.xml");
        
        List<Include> includes = struts.getIncludes();
        IncludeImpl inc1 = (IncludeImpl) includes.get(0);
        IncludeImpl inc2 = (IncludeImpl) includes.get(1);
        
        inc1.setTestIncluded(new TestIncludeReference(incModel1.getRootComponent(), inc1.getFile()));
        inc2.setTestIncluded(new TestIncludeReference(incModel2.getRootComponent(), inc2.getFile()));
        
        Map<String, StrutsPackage> packageMap = new HashMap<String, StrutsPackage>();
        PackagesScanner scanner = new PackagesScanner();
        scanner.scanPackages(struts, packageMap);
        
        assertNotNull(packageMap.get("mailreader-default"));
        assertNotNull(packageMap.get("mailreader-support"));
        assertNotNull(packageMap.get("subscription"));
        assertNotNull(packageMap.get("wildcard"));
    }
}
