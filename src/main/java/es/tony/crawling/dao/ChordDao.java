package es.tony.crawling.dao;

import org.jongo.Jongo;
import org.jongo.MongoCollection;

public class ChordDao {
	
	static Jongo jongo;
	
	public ChordDao(Jongo jongo){
		this.jongo = jongo;
	}
	
	public static void insertar(String document){
		MongoCollection chords = jongo.getCollection("chords");;
		chords.insert(document);
	}

}
