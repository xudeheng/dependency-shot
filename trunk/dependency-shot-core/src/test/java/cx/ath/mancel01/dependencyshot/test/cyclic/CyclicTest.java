/*
 *  Copyright 2009-2010 Mathieu ANCELIN.
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

package cx.ath.mancel01.dependencyshot.test.cyclic;

import cx.ath.mancel01.dependencyshot.DependencyShot;
import cx.ath.mancel01.dependencyshot.api.DSInjector;
import org.junit.Test;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;

/**
 * Tests of dependency-shot. 
 * 
 * @author Mathieu ANCELIN
 */
public class CyclicTest {

    @Test
    public void testCyclicDependency() {
        boolean isCyclic = false;
        Exception ex = null;
        try {
            DSInjector injector = DependencyShot.getInjector();
            BillingService service = injector.getInstance(BillingService.class);
            service.chargeAccountFor(123);
            assertTrue(service.getAccount().getMoney() == (100000 - 123));
        } catch (Exception e) {
            ex = e;
            isCyclic = true;
        }
        assertTrue(isCyclic);
        assertTrue(ex.getMessage().contains("DSCyclicDependencyDetectedException"));
    }

    @Test
    public void testCyclicDependencyAllowed() {
        boolean isCyclic = false;
        Exception ex = null;
        try {
            DSInjector injector = DependencyShot.getInjector();
            injector.allowCircularDependencies(true);
            BillingService service = injector.getInstance(BillingService.class);
            service.chargeAccountFor(123);
            assertTrue(service.getAccount().getMoney() == (100000 - 123));
        } catch (Exception e) {
            System.out.println(e);
            ex = e;
            isCyclic = true;
            System.out.println("boooom");
        }
        assertFalse(isCyclic);
        assertNull(ex);
    }

    @Test
    public void testNonCyclicDependency() {
        DSInjector injector = DependencyShot.getInjector(new NonCyclicBinder());
        BillingService service = injector.getInstance(BillingService.class);
        service.chargeAccountFor(123);
        assertTrue(service.getAccount().getMoney() == (100000 - 123));
    }
}