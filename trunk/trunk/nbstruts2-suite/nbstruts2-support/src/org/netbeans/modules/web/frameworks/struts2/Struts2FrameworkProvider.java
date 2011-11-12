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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.java.project.JavaProjectConstants;
import org.netbeans.api.java.project.classpath.ProjectClassPathModifier;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.api.project.Sources;
import org.netbeans.api.project.libraries.Library;
import org.netbeans.modules.j2ee.dd.api.web.DDProvider;
import org.netbeans.modules.j2ee.dd.api.web.Filter;
import org.netbeans.modules.j2ee.dd.api.web.FilterMapping;
import org.netbeans.modules.j2ee.dd.api.web.Listener;
import org.netbeans.modules.j2ee.dd.api.web.WebApp;
import org.netbeans.modules.j2ee.dd.api.web.WelcomeFileList;
import org.netbeans.modules.web.api.webmodule.WebModule;
import org.netbeans.modules.web.frameworks.struts2.ui.FrameworkSetupPanel;
import org.netbeans.modules.web.spi.webmodule.FrameworkConfigurationPanel;
import org.netbeans.modules.web.spi.webmodule.WebFrameworkProvider;
import org.openide.ErrorManager;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;

/**
 *
 * @author Petr Pisl, Rohan Ranade
 */
public class Struts2FrameworkProvider extends WebFrameworkProvider {
    
    private static final String SPRING_CONTEXT_LOADER = "org.springframework.web.context.ContextLoaderListener";  //NOI18N
            
    FrameworkSetupPanel frameworkSetup;
    
    public Struts2FrameworkProvider(){
        super(NbBundle.getMessage(Struts2FrameworkProvider.class, "Struts2_Name"),               //NOI18N
                NbBundle.getMessage(Struts2FrameworkProvider.class, "Struts2_Description"));       //NOI18N
    }
    
    @Override
    public Set extend(WebModule webModule) {
        if (!isInWebModule(webModule)) {
            Library struts2Library = frameworkSetup.getSelectedLibrary();
            if (struts2Library != null) {
                try {
                    // find version of struts2 library which has been added.
                    // this determines what gets generated in web.xml as
                    // struts 2.1 advises StrutsPrepareAndExecuteFilter instead
                    // of the FilterDispatcher
                    String strutsLibraryVersion = ConfigUtilities.getStrutsLibraryVersionOrDefault(struts2Library);
                    FileObject[] javaSources = webModule.getJavaSources();
                    if (javaSources.length > 0) {
                        Library[] libraries = new Library[]{struts2Library};
                        // This is a way how to add libraries to the project classpath and
                        // packed them to the war file by default.
                        ProjectClassPathModifier.addLibraries(libraries, javaSources[0], ClassPath.COMPILE);
                    }
                    FileSystem fs = webModule.getWebInf().getFileSystem();
                    // Older version of Struts 2 needs to register Spring ContextLoaderListener
                    boolean addSpringContextLoader = ConfigUtilities.containsClass(struts2Library.getContent("classpath"), SPRING_CONTEXT_LOADER); //NOI18N
                    fs.runAtomicAction(new Modifier(webModule, addSpringContextLoader, strutsLibraryVersion, frameworkSetup.isExampleRequired()));
                } catch (FileNotFoundException exc) {
                    Exceptions.printStackTrace(exc);
                } catch (IOException exc) {
                    Exceptions.printStackTrace(exc);
                }
            }
        }
        Set<FileObject> result = null;
        if (frameworkSetup.isExampleRequired()) {
            // if the example file is created, open it in the editor. 
            result = new HashSet<FileObject>();
            FileObject toOpen = webModule.getDocumentBase().getFileObject("example/HelloWorld.jsp");
            if (toOpen != null) {
                result.add(toOpen);
            }
        }
        return result;
    }
    
    public boolean isInWebModule(WebModule webModule) {
        return (webModule != null) ? (getStrutsFiles(webModule).size() > 0) : false;
    }
    
    public File[] getConfigurationFiles(WebModule webModule) {
        List <FileObject> files = getStrutsFiles(webModule);
        FileObject file = webModule.getWebInf().getFileObject("applicationContext", "xml");  //NOI18N
        if ( file != null)
            files.add(file);
        
        File[] confFile = null;
        if (files.size() > 0){
            confFile = new File[files.size()];
            for (int index = 0; index < files.size(); index++) {
                FileObject elem = files.get(index);
                confFile[index] = FileUtil.toFile(elem);
            }
        }
        return confFile;
    }
    
    /** Finds all struts.xml files in the defaults packages.
     */
    private List <FileObject> getStrutsFiles(WebModule webModule){
        ArrayList <FileObject> files = new ArrayList<FileObject>();
        if (webModule != null) {
            FileObject [] javaRoots = webModule.getJavaSources();
            FileObject strutsFile;
            for (int i = 0; i < javaRoots.length; i++) {
                strutsFile = javaRoots[i].getFileObject("struts", "xml");   // NOI18N
                if (strutsFile != null)
                    files.add(strutsFile);
            }
        }
        return files;
    }
    
    @Override
    public FrameworkConfigurationPanel getConfigurationPanel(WebModule webModule) {
        frameworkSetup = new FrameworkSetupPanel(isInWebModule(webModule));
        return frameworkSetup;
    }
    
    private class Modifier implements FileSystem.AtomicAction{
        
        private final WebModule wm;
        private final boolean addSpringContextLoader;
        private final String strutsLibraryVersion;
        private final boolean isNewerStruts;
        private final boolean isExampleRequired;
        
        public Modifier(WebModule wm, boolean addSpringContextLoader, 
                String strutsLibraryVersion, boolean isExampleRequired){
            this.wm = wm;
            this.strutsLibraryVersion = strutsLibraryVersion;
            this.isNewerStruts = strutsLibraryVersion.startsWith("2.1"); // NOI18N
            this.addSpringContextLoader = isNewerStruts ? false : addSpringContextLoader;
            this.isExampleRequired = isExampleRequired;
        }
        
        public void run() throws IOException {
            // alter deployment descriptor
            FileObject dd = wm.getDeploymentDescriptor();
            WebApp ddRoot = DDProvider.getDefault().getDDRoot(dd);
            if (ddRoot != null){
                try{
                    Filter filter = (Filter)ddRoot.createBean("Filter"); //NOI18N
                    String filterName = "struts2";                      //NOI18N
                    filter.setFilterName(filterName);
                    if (isNewerStruts) {
                        filter.setFilterClass(ConfigUtilities.STRUTS_2_1_P_AND_E_FILTER_CLASS);
                    } else {
                        filter.setFilterClass(ConfigUtilities.STRUTS_2_0_DISPATCHER_CLASS);
                    }
                    ddRoot.addFilter(filter);
                    
                    FilterMapping mapping = (FilterMapping)ddRoot.createBean("FilterMapping"); //NOI18N
                    mapping.setFilterName(filterName);//NOI18N
                    mapping.setUrlPattern("/*");//NOI18N
                    ddRoot.addFilterMapping(mapping);
                    
                    if (addSpringContextLoader) {
                        Listener listener = (Listener)ddRoot.createBean("Listener");  //NOI18N
                        listener.setListenerClass(SPRING_CONTEXT_LOADER);   //NOI18N
                        ddRoot.addListener(listener);
                    }
                    
                    if (isExampleRequired) {
                        // add welcome file
                        WelcomeFileList welcomeFiles = ddRoot.getSingleWelcomeFileList();
                        if (welcomeFiles == null) {
                            welcomeFiles = (WelcomeFileList) ddRoot.createBean("WelcomeFileList"); //NOI18N
                            ddRoot.setWelcomeFileList(welcomeFiles);
                        }
                        // add the welcome file only if there not any
                        if (welcomeFiles.sizeWelcomeFile() == 0) {
                            welcomeFiles.addWelcomeFile("example/HelloWorld.jsp");  //NOI18N
                        }
                    }
                    
                    ddRoot.write(dd);
                } catch (ClassNotFoundException cnfe){
                    ErrorManager.getDefault().notify(cnfe);
                }
            }
            
            /**
             * Maven projects have a separate source group for putting resources.
             */
            FileObject configFileBase = wm.getJavaSources()[0];
            Project proj = FileOwnerQuery.getOwner(wm.getDocumentBase());
            Sources sources = ProjectUtils.getSources(proj);
            SourceGroup[] rsrcSourceGroups = sources.getSourceGroups(JavaProjectConstants.SOURCES_TYPE_RESOURCES);
            if (rsrcSourceGroups.length > 0) {
                configFileBase = rsrcSourceGroups[0].getRootFolder();
            }
            
            final String baseFolder = "org/netbeans/modules/web/frameworks/struts2/resources/";
            final String templatesFolder = baseFolder + "templates/";
            
            InputStream is = Struts2FrameworkProvider.class.getClassLoader()
                .getResourceAsStream(templatesFolder + "Struts2Template.xml");
            String content = ConfigUtilities.readResource(is, "UTF-8");
            FileObject target = FileUtil.createData(configFileBase, "struts.xml");//NOI18N
            ConfigUtilities.createFile(target, content, "UTF-8");

            if (addSpringContextLoader) {
                is = Struts2FrameworkProvider.class.getClassLoader()
                    .getResourceAsStream(baseFolder + "applicationContext.xml");
                content = ConfigUtilities.readResource(is, "UTF-8");
                target = FileUtil.createData(wm.getWebInf(), "applicationContext.xml");//NOI18N
                ConfigUtilities.createFile(target, content, "UTF-8");
            }
            /*            FileObject indexjsp = wm.getDocumentBase().getFileObject("index.jsp"); //NOI18N
            if (indexjsp != null){
            changeIndexJSP(indexjsp);
            }*/
            /*        private void changeIndexJSP(FileObject indexjsp) throws IOException {
            String content = FaceletsUtils.readResource(indexjsp.getInputStream(), "UTF-8"); //NO18N
            // what find
            String find = "<h1>JSP Page</h1>"; // NOI18N
            String endLine = System.getProperty("line.separator"); //NOI18N
            if ( content.indexOf(find) > 0){
            StringBuffer replace = new StringBuffer();
            replace.append(find);
            replace.append(endLine);
            replace.append("    <br/>");                        //NOI18N
            replace.append(endLine);
            replace.append("    <a href=\"./template-client.jsf\">");                  //NOI18N
            replace.append(NbBundle.getMessage(FaceletsFrameworkProvider.class,"LBL_FaceletWelcomePage"));
            replace.append("</a>");                             //NOI18N
            content = content.replaceFirst(find, new String(replace.toString().getBytes("UTF8"), "UTF-8")); //NOI18N
            FaceletsUtils.createFile(indexjsp, content, "UTF-8"); //NOI18N
            } */

            if (isExampleRequired) {
                // HelloWorld.jsp
                is = Struts2FrameworkProvider.class.getClassLoader()
                    .getResourceAsStream(baseFolder + "example/HelloWorld.jsp");    //NOI18N
                content = ConfigUtilities.readResource(is, "UTF-8");        //NOI18N
                target = FileUtil.createData(wm.getDocumentBase(), "example/HelloWorld.jsp");//NOI18N
                ConfigUtilities.createFile(target, content, "UTF-8");       //NOI18N
                // struts.xml
                is = Struts2FrameworkProvider.class.getClassLoader()
                    .getResourceAsStream(baseFolder + "example/struts.xml"); //NOI18N
                content = ConfigUtilities.readResource(is, "UTF-8");        //NOI18N
                target = FileUtil.createData(configFileBase, "struts.xml"); //NOI18N
                ConfigUtilities.createFile(target, content, "UTF-8");       //NOI18N
                // example.xml
                is = Struts2FrameworkProvider.class.getClassLoader()
                    .getResourceAsStream(baseFolder + "example/example.xml"); //NOI18N
                content = ConfigUtilities.readResource(is, "UTF-8");        //NOI18N
                target = FileUtil.createData(configFileBase, "example.xml"); //NOI18N
                ConfigUtilities.createFile(target, content, "UTF-8");       //NOI18N
                // HelloWorld.java
                is = Struts2FrameworkProvider.class.getClassLoader()
                    .getResourceAsStream(baseFolder + "example/HelloWorld.template"); //NOI18N
                content = ConfigUtilities.readResource(is, "UTF-8");        //NOI18N
                target = FileUtil.createData(wm.getJavaSources()[0], "example/HelloWorld.java"); //NOI18N
                ConfigUtilities.createFile(target, content, "UTF-8");       //NOI18N
                // package.properties
                is = Struts2FrameworkProvider.class.getClassLoader()
                    .getResourceAsStream(baseFolder + "example/package.properties"); //NOI18N
                content = ConfigUtilities.readResource(is, "UTF-8");        //NOI18N
                target = FileUtil.createData(configFileBase, "example/package.properties"); //NOI18N
                ConfigUtilities.createFile(target, content, "UTF-8");       //NOI18N
                // package_es.properties
                is = Struts2FrameworkProvider.class.getClassLoader()
                    .getResourceAsStream(baseFolder + "example/package_es.properties"); //NOI18N
                content = ConfigUtilities.readResource(is, "UTF-8");        //NOI18N
                target = FileUtil.createData(configFileBase, "example/package_es.properties"); //NOI18N
                ConfigUtilities.createFile(target, content, "UTF-8");       //NOI18N
            }
            else {
                is = Struts2FrameworkProvider.class.getClassLoader()
                    .getResourceAsStream(baseFolder + "struts.xml");        //NOI18N
                content = ConfigUtilities.readResource(is, "UTF-8");        //NOI18N
                target = FileUtil.createData(configFileBase, "struts.xml");//NOI18N
                ConfigUtilities.createFile(target, content, "UTF-8");       //NOI18N
            }
        }
    }
}
