package ee.pri.bcup.server.dao;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

/**
 * Base class for storing server model entities in memory.
 *
 * @author Raivo Laanemets
 */
public class AbstractMemoryDao<T extends Entity> {
	private static final Logger log = Logger.getLogger(AbstractMemoryDao.class);
	
	private AtomicLong key = new AtomicLong(0L);
	private Map<Long, T> entities = new ConcurrentHashMap<Long, T>();
	
	/**
	 * Return the entity with the given id.
	 */
	public T find(Long id) {
		if (!entities.containsKey(id)) {
			throw new IllegalArgumentException("No entity with id " + id);
		}
		
		return entities.get(id);
	}
	
	/**
	 * Stores the given entity.
	 */
	public void store(T e) {
		log.debug("storing entity " + e.getClass());
		e.setId(key.incrementAndGet());
		entities.put(e.getId(), e);
	}
	
	/**
	 * Deletes the given entity.
	 */
	public void delete(T e) {
		log.debug("deleting entity " + e.getClass() + " with id " + e.getId());
		entities.remove(e.getId());
	}
	
	/**
	 * Returns all entities.
	 */
	public Collection<T> all() {
		return entities.values();
	}

	@Override
	public String toString() {
		return entities.toString();
	}

}
