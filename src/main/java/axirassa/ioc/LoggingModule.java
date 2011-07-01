/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package axirassa.ioc;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;

/**
 *
 * @author wiktor
 */
@Slf4j
public class LoggingModule {
    public static Logger buildLogger() {
        return log;
    }
}
