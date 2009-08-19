/*
 *  Copyright 2009 mathieuancelin.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package cx.ath.mancel01.dependencyshot.injection.handlers;

import cx.ath.mancel01.dependencyshot.api.AnnotationHandler;

/**
 *
 * @author mathieuancelin
 */
public class SingletonHandler implements AnnotationHandler {

    /**
     * The unique instance of the class
     **/
    private static SingletonHandler INSTANCE = null;

    /**
     * The private constructor of the singleton
     **/
    private SingletonHandler() {

    }

    /**
     * The accessor for the unique instance of the singleton
     **/
    public static synchronized SingletonHandler getInstance() {
        if ( INSTANCE == null ) {
            INSTANCE = new SingletonHandler();
        }
        return INSTANCE;
    }
}