
package axirassa.webapp.pages.monitor;

import java.io.IOException;
import java.util.List;

import org.apache.tapestry5.ioc.annotations.Inject;

import axirassa.dao.PingerDAO;
import axirassa.model.HttpStatisticsEntity;
import axirassa.model.PingerEntity;
import axirassa.util.CSVResponse;
import axirassa.util.SimpleCSVRowWriter;
import axirassa.webapp.services.AxirassaSecurityService;
import axirassa.webapp.services.exceptions.AxirassaSecurityException;

public class DataMonitor {
	@Inject
	private PingerDAO pingerDAO;

	@Inject
	private AxirassaSecurityService security;


	public CSVResponse onCSV(long pingerId) throws AxirassaSecurityException, IOException {
		PingerEntity pinger = pingerDAO.findPingerById(pingerId);
		security.verifyOwnership(pinger);

		List<HttpStatisticsEntity> dataPoints = pingerDAO.getDataPoints(pinger, PingerDAO.SIX_HOURS);
		CSVResponse response = new CSVResponse();

		response.setResponseData(dataPoints, new SimpleCSVRowWriter<HttpStatisticsEntity>() {
			@Override
			public void writeRow(HttpStatisticsEntity row, StringBuilder sb) {
				writeCell(sb, row.getTimestampInMillis());
				writeCell(sb, row.getLatency());
				writeCell(sb, row.getResponseTime());
			}
		});

		return response;
	}
}
