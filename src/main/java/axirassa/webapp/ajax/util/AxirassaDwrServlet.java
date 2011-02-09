
package axirassa.webapp.ajax.util;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.directwebremoting.Container;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.event.ScriptSessionListener;
import org.directwebremoting.extend.ScriptSessionManager;
import org.directwebremoting.servlet.DwrServlet;

public class AxirassaDwrServlet extends DwrServlet {
	private static final long serialVersionUID = -159936788119263455L;


	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		System.out.println("Adding script session listeners");

		Container container = ServerContextFactory.get().getContainer();
		ScriptSessionManager manager = container.getBean(ScriptSessionManager.class);
		ScriptSessionListener listener = new StreamSessionListener();
		manager.addScriptSessionListener(listener);
	}

}
