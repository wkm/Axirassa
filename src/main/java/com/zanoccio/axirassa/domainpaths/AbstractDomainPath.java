
package com.zanoccio.axirassa.domainpaths;

import org.hibernate.Session;

import com.zanoccio.axirassa.util.HibernateUtil;

public abstract class AbstractDomainPath implements DomainPath {

	/**
	 * @deprecated prefer using {@link #execute(Session)} for better session
	 *             management and vastly performance
	 */
	@Deprecated
	@Override
	public void execute() throws Exception {
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		execute(session);
		session.getTransaction().commit();
	}


	@Override
	abstract public void execute(Session session) throws Exception;

}
