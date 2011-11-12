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
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.modules.web.frameworks.struts2;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.project.libraries.Library;
import org.netbeans.spi.java.classpath.support.ClassPathSupport;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileStateInvalidException;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.JarFileSystem;
import org.openide.filesystems.URLMapper;
import org.openide.util.Exceptions;
import org.openide.util.Parameters;

/**
 *
 * @author Petr Pisl, Rohan Ranade
 */
public final class ConfigUtilities {

    public static final String DEFAULT_STRUTS_VERSION = "2.0.11";
    public static final String STRUTS_2_0_PUBLIC_ID = "-//Apache Software Foundation//DTD Struts Configuration 2.0//EN"; // NOI18N
    public static final String STRUTS_2_1_PUBLIC_ID = "-//Apache Software Foundation//DTD Struts Configuration 2.1//EN"; // NOI18N
    public static final String STRUTS_2_0_SYS_ID = "http://struts.apache.org/dtds/struts-2.0.dtd"; // NOI18N
    public static final String STRUTS_2_1_SYS_ID = "http://struts.apache.org/dtds/struts-2.1.dtd"; // NOI18N
    public static final String STRUTS_2_0_DISPATCHER_CLASS = "org.apache.struts2.dispatcher.FilterDispatcher";  //NOI18N
    public static final String STRUTS_2_1_P_AND_E_FILTER_CLASS = "org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter"; // NOI18N

    private static final String CLASSPATH_CONTENT = "classpath";
    private static final String STRUTS_CORE_POM_PROPERTIES_PATH = "META-INF/maven/org.apache.struts/struts2-core/pom.properties";
    private static final String VERSION_PREFIX = "version=";

    private ConfigUtilities() {
    }

    public static String readResource(InputStream is, String encoding) throws IOException {
        // read the config from resource first
        StringBuffer sb = new StringBuffer();
        String lineSep = System.getProperty("line.separator");//NOI18N
        BufferedReader br = new BufferedReader(new InputStreamReader(is, encoding));
        String line = br.readLine();
        while (line != null) {
            sb.append(line);
            sb.append(lineSep);
            line = br.readLine();
        }
        br.close();
        return sb.toString();
    }
    
    public static void createFile(FileObject target, String content, String encoding) throws IOException {
        FileLock lock = target.lock();
        try {
            OutputStreamWriter bw = new OutputStreamWriter(target.getOutputStream(lock), encoding);
            bw.write(content);
            bw.flush();
            bw.close();
            
        } finally {
            lock.releaseLock();
        }
    }
    
    /**
     * Returns true if the specified classpath contains a class of the given name,
     * false otherwise.
     * 
     * @param classpath consists of jar urls and folder urls containing classes
     * @param className the name of the class
     * 
     * @return true if the specified classpath contains a class of the given name,
     *         false otherwise.
     * 
     * @throws IOException if an I/O error has occurred
     * 
     * @since 1.15
     */
    public static boolean containsClass(List<URL> classPath, String className) throws IOException {
        Parameters.notNull(CLASSPATH_CONTENT, classPath); // NOI18N
        Parameters.notNull("className", className); // NOI18N
        
        List<File> diskFiles = new ArrayList<File>();
        for (URL url : classPath) {
            URL archiveURL = FileUtil.getArchiveFile(url);
            
            if (archiveURL != null) {
                url = archiveURL;
            }
            
            if ("nbinst".equals(url.getProtocol())) { // NOI18N
                // try to get a file: URL for the nbinst: URL
                FileObject fo = URLMapper.findFileObject(url);
                if (fo != null) {
                    URL localURL = URLMapper.findURL(fo, URLMapper.EXTERNAL);
                    if (localURL != null) {
                        url = localURL;
                    }
                }
            }
            
            FileObject fo = URLMapper.findFileObject(url);
            if (fo != null) {
                File diskFile = FileUtil.toFile(fo);
                if (diskFile != null) {
                    diskFiles.add(diskFile);
                }
            }
        }
        
        return containsClass(diskFiles, className);
    }
    
    /**
     * Returns true if the specified classpath contains a class of the given name,
     * false otherwise.
     * 
     * @param classpath consists of jar files and folders containing classes
     * @param className the name of the class
     * 
     * @return true if the specified classpath contains a class of the given name,
     *         false otherwise.
     * 
     * @throws IOException if an I/O error has occurred
     * 
     * @since 1.15
     */
    public static boolean containsClass(Collection<File> classpath, String className) throws IOException {
        Parameters.notNull(CLASSPATH_CONTENT, classpath); // NOI18N
        Parameters.notNull("driverClassName", className); // NOI18N
        String classFilePath = className.replace('.', '/') + ".class"; // NOI18N
        for (File file : classpath) {
            if (file.isFile()) {
                JarFile jf = new JarFile(file);
                try {
                    Enumeration entries = jf.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = (JarEntry) entries.nextElement();
                        if (classFilePath.equals(entry.getName())) {
                            return true;
                        }
                    }
                } finally {
                    jf.close();
                }
            } else {
                if (new File(file, classFilePath).exists()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Filter and return only the Struts2 libraries from the list of specified
     * libraries
     *
     * @param libraries list of libraries to filter from; never null
     * @return struts2 libraries contained within the specified libraries
     * 
     * @throws IOException if an error occurs during filter process
     */
    public static Library[] filterStruts2Libraries(Library[] libraries) throws IOException {
        Parameters.notNull("libraries", libraries); // NOI18N
        List<Library> sLibs = new ArrayList<Library>();
        for (Library l : libraries) {
            if (isStruts2Library(l)) {
                sLibs.add(l);
            }
        }
        return sLibs.toArray(new Library[sLibs.size()]);
    }

    /**
     * Returns whether the specified library is a struts2 library.
     *
     * @param library the library to be checked; never null
     * @return true if the specified library is a struts2 library; false otherwise
     *
     * @throws IOException if an error occurs during the operation
     */
    public static boolean isStruts2Library(Library library) throws IOException {
        Parameters.notNull("library", library); // NOI18N
        List<URL> content = library.getContent(CLASSPATH_CONTENT);
        return containsClass(content, STRUTS_2_0_DISPATCHER_CLASS)
                    || containsClass(content, STRUTS_2_1_P_AND_E_FILTER_CLASS);
    }

    /**
     * Retrieves the struts2 library version
     * 
     * @param library a struts2 library; never null
     * @return version of the struts2 library
     * @throws IOException
     */
    public static String getStrutsLibraryVersion(Library library) throws IOException {
        Parameters.notNull("library", library); // NOI18N
        List<URL> urls = library.getContent(CLASSPATH_CONTENT);
        ClassPath cp = createClassPath(urls);
        try {
            FileObject resource = cp.findResource(STRUTS_2_0_DISPATCHER_CLASS.replace('.', '/') + ".class"); // NOI18N
            FileObject resource2 = cp.findResource(STRUTS_2_1_P_AND_E_FILTER_CLASS.replace('.', '/') + ".class"); // NOI18N
            if (resource == null && resource2 == null) {
                return null;
            }
            if (resource == null) {
                resource = resource2;
            }
            FileObject ownerRoot = cp.findOwnerRoot(resource);
            if (ownerRoot != null) {
                if (ownerRoot.getFileSystem() instanceof JarFileSystem) {
                    JarFileSystem jarFileSystem = (JarFileSystem) ownerRoot.getFileSystem();
                    return getLibraryVersionFromMavenArtifacts(jarFileSystem);
                }
            }
        } catch (FileStateInvalidException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    public static String getStrutsLibraryVersionOrDefault(Library library) throws IOException {
        String ver = getStrutsLibraryVersion(library);
        if (ver == null) {
            ver = DEFAULT_STRUTS_VERSION;
        }
        return ver;
    }

    private static String getLibraryVersionFromMavenArtifacts(JarFileSystem jarFileSystem)
            throws IOException {
        FileObject root = jarFileSystem.getRoot();
        FileObject pomProps = root.getFileObject(STRUTS_CORE_POM_PROPERTIES_PATH);
        if (!pomProps.isValid()) {
            return null;
        }
        for (String line : pomProps.asLines()) {
            if (line.startsWith(VERSION_PREFIX)) {
                return line.substring(VERSION_PREFIX.length());
            }
        }
        return null;
    }

    private static ClassPath createClassPath(List<URL> roots) {
        List<URL> jarRootUrls = new ArrayList<URL>();
        for (URL url : roots) {
            // workaround for #126307: ClassPath roots should be jar root URL not file URLs.
            if (FileUtil.getArchiveFile(url) == null) {
                // not an archive root url
                url = FileUtil.getArchiveRoot(url);
            }
            jarRootUrls.add(url);
        }
        return ClassPathSupport.createClassPath(jarRootUrls.toArray(new URL[jarRootUrls.size()]));
    }
}
