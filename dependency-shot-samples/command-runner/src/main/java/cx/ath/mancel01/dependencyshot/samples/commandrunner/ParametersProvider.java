/*
 *  Copyright 2010 mathieuancelin.
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

package cx.ath.mancel01.dependencyshot.samples.commandrunner;

import cx.ath.mancel01.dependencyshot.api.InjectionPoint;
import cx.ath.mancel01.dependencyshot.injection.util.EnhancedProvider;
import cx.ath.mancel01.dependencyshot.samples.commandrunner.api.CommandContext;

/**
 *
 * @author Mathieu ANCELIN
 */
public class ParametersProvider implements EnhancedProvider {

    private CommandContext context;

    public ParametersProvider(CommandContext context) {
        this.context = context;
    }

    @Override
    public Object enhancedGet(InjectionPoint p) {
        return null;
    }

    @Override
    public Object get() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
