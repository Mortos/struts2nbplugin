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
 * The Original Software is XWork Support. The Initial Developer of the Original
 * Software is 
 * Aleh Maksimovich <aleh.maksimovich@gmail.com>, 
 *                  <aleh.maksimovich@hiqo-solutions.com>.
 * Portions Copyright 2011 Aleh Maksimovich. All Rights Reserved.
 */
package org.netbeans.modules.framework.xwork.sp.edtor.completion;

import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;

/**
 * Base class for completion queries providing both express and full completion
 * results.
 *
 * @author Aleh Maksimovich (aleh.maksimovich@gmail.com)
 * @since 0.5.2
 */
public abstract class AbstractAsyncCompletionQuery extends AsyncCompletionQuery {

    private boolean fullQuery;

    /**
     * Constructor with query mode. Specify
     * <code>true</code> for full completion mode and
     * <code>false</code> for express mode.
     *
     * @param fullQuery full query flag.
     */
    public AbstractAsyncCompletionQuery(boolean fullQuery) {
        this.fullQuery = fullQuery;
    }

    /**
     * Returns
     * <code>true</code> if object provides full mode completion, or
     * <code>false</code> for express one.
     *
     * @return full query flag.
     */
    public boolean isFullQuery() {
        return fullQuery;
    }
}
