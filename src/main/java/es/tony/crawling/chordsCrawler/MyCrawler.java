package es.tony.crawling.chordsCrawler;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import es.tony.crawling.dao.ChordDao;

public class MyCrawler extends WebCrawler {

	// private final static Logger log = Logger.getLogger(MyCrawler.class);

	private final static Pattern FILTERS = Pattern
			.compile(".*(\\.(css|js|bmp|gif|jpe?g"
					+ "|png|tiff?|mid|mp2|mp3|mp4"
					+ "|wav|avi|mov|mpeg|ram|m4v|pdf"
					+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	private static File storageFolder;
	private static String[] crawlDomains;

	public static void configure(String[] domain, String storageFolderName) {
		MyCrawler.crawlDomains = domain;

		storageFolder = new File(storageFolderName);
		if (!storageFolder.exists()) {
			storageFolder.mkdirs();
		}
	}

	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();
		return !FILTERS.matcher(href).matches()
				&& href.startsWith("******");
	}

	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		System.out.println(url);
		JSONObject obj = null;

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String html = htmlParseData.getHtml();

			Pattern p = Pattern.compile(".*?<u>(.*?)</u>.*?", Pattern.DOTALL);
			Pattern p2 = Pattern
					.compile(
							".*?strKey = \"(.*?)\".*?artist = \"(.*?)\".*?title = \"(.*?)\".*?",
							Pattern.DOTALL);

			Matcher m = p.matcher(html);
			Matcher m2 = p2.matcher(html);

			obj = new JSONObject();
			JSONArray chords = new JSONArray();
			for (int i = 0; i < 1; i++) {
				if (m.matches()) {
					m = p.matcher(html);
					while (m.find()) {
						chords.add(m.group(1));
					}
				} else
					System.out.println("No match");
			}
			for (int i = 0; i < 1; i++) {
				if (m2.matches()) {
					m2 = p2.matcher(html);
					while (m2.find()) {
						obj.put("key", m2.group(1));
						obj.put("group", m2.group(2));
						obj.put("song", m2.group(3));
					}
				} else
					System.out.println("No matches 2");
			}
			obj.put("chords", chords);
			System.out.println(obj);
			ChordDao.insertar(obj.toString());

		}
	}
}
