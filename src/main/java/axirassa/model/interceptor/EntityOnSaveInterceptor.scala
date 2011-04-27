package axirassa.model.interceptor

import org.hibernate.HibernateException;
import org.hibernate.event.SaveOrUpdateEvent;
import org.hibernate.event.SaveOrUpdateEventListener;

class EntityOnSaveInterceptor extends SaveOrUpdateEventListener {
    override def onSaveOrUpdate(event : SaveOrUpdateEvent) {
        event.getObject() match {
            case e : EntityPreSave => e.preSave
        }
    }
}