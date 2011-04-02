/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package axirassa.ioc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author wiktor
 */
public class LoggingModule {
    public static Logger buildLogger() {
        return LoggerFactory.getLogger(LoggingModule.class);
    }
}
