
import echoclient.EchoClient;
import echoserver.EchoServer;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Lars Mortensen
 */
public class TestClient implements Observer
{

    private String input = null;

    public TestClient()
    {
    }

    @BeforeClass
    public static void setUpClass()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                EchoServer.main(null);
            }
        }).start();
    }

    @AfterClass
    public static void tearDownClass()
    {
        EchoServer.stopServer();
    }

    @Before
    public void setUp()
    {
    }

    @Test
    public void send() throws IOException
    {
        EchoClient client = new EchoClient();
        client.addObserver(this);

        new Thread(client).start();
        while (input == null)
        {
            client.send("bob");

        }
        assertEquals("BOB", input);
    }

    @Override
    public void update(Observable o, Object arg)
    {
        input = (String) arg;
        System.out.println(input);
    }

}
