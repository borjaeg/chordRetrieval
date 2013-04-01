package es.tony.crawling.chordsCrawler;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.jongo.Jongo;

import com.mongodb.DB;
import com.mongodb.Mongo;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import es.tony.crawling.dao.ChordDao;

public class Controller {

	 private final static Logger log = Logger.getLogger(Controller.class);

	public static void main(String[] args) throws Exception {
		 BasicConfigurator.configure();
		DB db = new Mongo().getDB("chordsDB");
		Jongo jongo = new Jongo(db);
		ChordDao chordDao = new ChordDao(jongo);
		String crawlStorageFolder = "data";
		int numberOfCrawlers = 1;

		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setPolitenessDelay(2000);
		config.setMaxDepthOfCrawling(2);
		config.setMaxPagesToFetch(1000);

		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig,
				pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher,
				robotstxtServer);

		controller.addSeed("****");

		controller.start(MyCrawler.class, numberOfCrawlers);
	}
}
