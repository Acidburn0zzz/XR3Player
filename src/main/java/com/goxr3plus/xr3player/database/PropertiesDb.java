/**
 * 
 */
package com.goxr3plus.xr3player.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.goxr3plus.xr3player.enums.FileType;
import com.goxr3plus.xr3player.utils.io.IOAction;

/**
 * This class is saving all the XR3Player Settings to a property file
 * 
 * @author GOXR3PLUS
 *
 */
public class PropertiesDb {

	private final Properties properties;

	/** This executor does the commit job. */
	private static final ExecutorService updateExecutorService = Executors.newSingleThreadExecutor();

	/**
	 * Using this variable when i want to prevent update of properties happen
	 */
	private boolean updatePropertiesLocked;

	/**
	 * The absolute path of the properties file
	 */
	private String fileAbsolutePath;

	/**
	 * Constructor
	 * 
	 * @param localDbManager
	 */
	public PropertiesDb(final String propertiesAbsolutePath, final boolean updatePropertiesLocked) {
		this.fileAbsolutePath = propertiesAbsolutePath;
		this.updatePropertiesLocked = updatePropertiesLocked;
		properties = new Properties();
	}

	/**
	 * Updates or Creates the given key , warning also updateProperty can be locked
	 * , if you want to unlock it or check if locked check the method is
	 * `isUpdatePropertyLocked()`
	 * 
	 * @param key
	 * @param value
	 */
	public void updateProperty(final String key, final String value) {
		if (updatePropertiesLocked)
			return;

		/// System.out.println("Updating Property!")

		// Submit it to the executors Service
		updateExecutorService.submit(() -> {
			// Check if exists [ Create if Not ]
			IOAction.createFileOrFolder(fileAbsolutePath, FileType.FILE);

			try (InputStream inStream = new FileInputStream(fileAbsolutePath);
					OutputStream outStream = new FileOutputStream(fileAbsolutePath)) {

				// load properties
				properties.load(inStream);

				// set the properties value
				properties.setProperty(key, value);

				// save properties
				properties.store(outStream, null);

			} catch (final IOException ex) {
				ex.printStackTrace();
			}
		});
	}

	/**
	 * Remove that property from the Properties file
	 * 
	 * @param key
	 */
	public void deleteProperty(final String key) {
		// Check if exists
		if (new File(fileAbsolutePath).exists())

			// Submit it to the executors Service
			updateExecutorService.submit(() -> {
				try (InputStream inStream = new FileInputStream(fileAbsolutePath);
						OutputStream outStream = new FileOutputStream(fileAbsolutePath)) {

					// load properties
					properties.load(inStream);

					// remove that property
					properties.remove(key);

					// save properties
					properties.store(outStream, null);

				} catch (final IOException ex) {
					ex.printStackTrace();
				}
			});

	}

	/**
	 * Loads the Properties
	 */
	public Properties loadProperties() {

		// Check if exists
		if (new File(fileAbsolutePath).exists())

			// Load the properties file
			try (InputStream inStream = new FileInputStream(fileAbsolutePath)) {

				// load properties
				// properties.clear()
				properties.load(inStream);

			} catch (final IOException ex) {
				ex.printStackTrace();
			}

		return properties;
	}

	/**
	 * Returns the property with that key
	 * 
	 * @param key The property key
	 * @return Returns the property with that key
	 */
	public String getProperty(final String key) {
		return properties.getProperty(key);
	}

	/**
	 * Get the properties instance of this class
	 * 
	 * @return the properties
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * Check if properties update is locked
	 * 
	 * @return the canUpdateProperty
	 */
	public boolean isUpdatePropertiesLocked() {
		return updatePropertiesLocked;
	}

	/**
	 * Lock or unlock the update of properties
	 * 
	 * @param canUpdateProperty the canUpdateProperty to set
	 */
	public void setUpdatePropertiesLocked(final boolean updatePropertiesLocked) {
		this.updatePropertiesLocked = updatePropertiesLocked;
	}

	/**
	 * @param fileAbsolutePath The new absolute path of the properties file
	 */
	public void setFileAbsolutePath(final String fileAbsolutePath) {
		this.fileAbsolutePath = fileAbsolutePath;
	}

	/**
	 * @return the propertiesAbsolutePath
	 */
	public String getFileAbsolutePath() {
		return fileAbsolutePath;
	}

}
