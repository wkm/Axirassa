package axirassa.webapp.components;

import java.util.List;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Session;

public class AxDebugFooter {

    @Inject
    @Property
    private Request request;

    @Property
    private List<String> headers;

    @Property
    private String header;

    @Property
    private Session session;

    public void setupRender () {
        session = request.getSession(false);
        headers = request.getHeaderNames();
    }


}
