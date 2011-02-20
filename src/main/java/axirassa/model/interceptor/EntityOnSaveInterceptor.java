
package axirassa.model.interceptor;

import org.hibernate.HibernateException;
import org.hibernate.event.SaveOrUpdateEvent;
import org.hibernate.event.SaveOrUpdateEventListener;

public class EntityOnSaveInterceptor implements SaveOrUpdateEventListener {
	private static final long serialVersionUID = 6083976968826887130L;


	@Override
	public void onSaveOrUpdate(SaveOrUpdateEvent event) throws HibernateException {
		Object result = event.getObject();
		if (result instanceof EntityPreSave) {
			((EntityPreSave) result).preSave();
		}
	}
}
