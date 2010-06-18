package cx.ath.mancel01.dependencyshot.test.basic;

import cx.ath.mancel01.dependencyshot.DependencyShot;
import cx.ath.mancel01.dependencyshot.api.DSInjector;
import org.junit.Test;
import static junit.framework.Assert.assertTrue;

/**
 * Tests of dependency-shot. 
 * 
 * @author Mathieu ANCELIN
 */
public class BasicInjectTest {
    /**
     * Mock test.
     */
    @Test
    public void testMockedClient() {
        BasicMockService mock = new BasicMockService();
        BasicClient client = new BasicClient(mock);
        client.setService2(mock);
        client.setService3(mock);
        client.go();
        assertTrue(client.getService().isGone());
        assertTrue(client.getService2().isGone());
        assertTrue(client.getService3().isGone());
    }

    public void testFluentInjectedClient() {
        DSInjector injector = DependencyShot.getInjector(new BasicFluentBinder());
        BasicClient client = injector.getInstance(BasicClient.class);
        client.go();
        assertTrue(!client.getService().isGone());
        assertTrue(!client.getService3().isGone());
        assertTrue(!client.getService2().isGone());

    }
}