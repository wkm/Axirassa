
package axirassa.dao;

import java.util.List;

import axirassa.model.HttpStatisticsEntity;
import axirassa.model.PingerEntity;

public interface PingerDAO {

	public int SIX_HOURS = 360;


	public abstract PingerEntity findPingerById (long id);


	public abstract List<HttpStatisticsEntity> findStatistics (PingerEntity pinger);


	public abstract List<HttpStatisticsEntity> getDataPoints (PingerEntity pinger, int minutes);

}
