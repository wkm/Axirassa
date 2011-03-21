
package axirassa.dao;

import java.util.List;

import axirassa.model.HttpStatisticsEntity;
import axirassa.model.PingerEntity;

public interface PingerDAO {

	public abstract PingerEntity findPingerById(long id);


	public abstract List<HttpStatisticsEntity> findStatistics(PingerEntity pinger);

}
