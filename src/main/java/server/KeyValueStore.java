package server;

import java.util.HashMap;
// For future use with multithreading
// we can import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple key-value store.
 */
public class KeyValueStore {
  private final HashMap<String, String> store;

  /**
   * Constructs a new KeyValueStore.
   */
  public KeyValueStore() {
    // For multithreading, consider using ConcurrentHashMap
    // store = new ConcurrentHashMap<>();
    store = new HashMap<>();
  }

  /**
   * Inserts or updates a key-value pair.
   *
   * @param key The key to insert.
   * @param value The value to associate with the key.
   * @return The previous value associated with the key, or null if none.
   */
  public synchronized String put(String key, String value) {
    return store.put(key, value);
  }

  /**
   * Retrieves the value for the given key.
   *
   * @param key The key to retrieve.
   * @return The value associated with the key, or null if not found.
   */
  public synchronized String get(String key) {
    return store.get(key);
  }

  /**
   * Removes the key-value pair for the given key.
   *
   * @param key The key to delete.
   * @return The removed value, or null if the key was not found.
   */
  public synchronized String delete(String key) {
    return store.remove(key);
  }
}
