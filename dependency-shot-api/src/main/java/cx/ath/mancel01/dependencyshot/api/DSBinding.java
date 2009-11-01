/*
 *  Copyright 2009 Mathieu ANCELIN.
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

package cx.ath.mancel01.dependencyshot.api;

/**
 * Binding between an interface and it's implementation.
 * 
 * @author Mathieu ANCELIN
 */
public interface DSBinding {
    /**
     * @return get generic interface.
     */
    Class getGeneric();
    /**
     * @return get specific class.
     */
    Class getSpecific();
    /**
     * @return get specific instance.
     */
    Object getSpecificInstance();

}