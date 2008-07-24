package org.csstudio.nams.service.configurationaccess.localstore.declaration;

import org.csstudio.nams.service.configurationaccess.localstore.declaration.exceptions.InconsistentConfigurationException;
import org.csstudio.nams.service.configurationaccess.localstore.declaration.exceptions.StorageError;
import org.csstudio.nams.service.configurationaccess.localstore.declaration.exceptions.StorageException;
import org.csstudio.nams.service.configurationaccess.localstore.declaration.exceptions.UnknownConfigurationElementError;
import org.csstudio.nams.service.configurationaccess.localstore.internalDTOs.FilterConditionDTO;
import org.csstudio.nams.service.configurationaccess.localstore.internalDTOs.RubrikDTO;
import org.csstudio.nams.service.configurationaccess.localstore.internalDTOs.filterConditionSpecifics.HasJoinedElements;

/**
 * Ein Service zum Zugirff auf die NAMS-Konfiguration.
 * 
 * TODO Rename to ConfigurationStoreService TODO Alle konkreten Operationen auf
 * die allgemeinen DTO-Operationen umbauen; siehe auch Anmerkungen in
 * {@link HasJoinedElements}.
 */
public interface LocalStoreConfigurationService {

	/**
	 * Retrieves the current syncronize-state. This method is only used to
	 * enable a syncronisation support between the new decision-department-
	 * (earlier: FilterManager) and the deprecated but still in use
	 * Distributor-application; it is not recommend to use this flag for other
	 * purposes.
	 * 
	 * @return The current state flag.
	 * @throws StorageError
	 *             If a configuration error is present and this database is not
	 *             accessible.
	 * @throws StorageException
	 *             If a database exception occurred or database is unexpected
	 *             unreachable.
	 * @throws InconsistentConfiguration
	 *             If the stored configuration is inconsistent.
	 */
	public ReplicationStateDTO getCurrentReplicationState()
			throws StorageError, StorageException,
			InconsistentConfigurationException;

	/**
	 * Loads the entire configuration from the Database. ConfigurationDTO will
	 * be an object that contain a consitent set of all configuration elements
	 * loaded by exactly one transaction.
	 * 
	 * @return The entire configuration.
	 * @throws StorageError
	 *             If a configuration error is present and this database is not
	 *             accessible.
	 * @throws StorageException
	 *             If a database exception occurred or database is unexpected
	 *             unreachable.
	 * @throws InconsistentConfiguration
	 *             If the stored configuration is inconsistent.
	 */
	public Configuration getEntireConfiguration() throws StorageError,
			StorageException, InconsistentConfigurationException;

	/**
	 * Saves a new current syncronize-state. This method is only used to enable
	 * a syncronisation support between the new decision-department- (earlier:
	 * FilterManager) and the deprecated but still in use
	 * Distributor-application; it is not recommend to use this flag for other
	 * purposes.
	 * 
	 * @param currentState
	 *            The new state to save.
	 * @throws StorageError
	 *             If a configuration error is present and this database is not
	 *             accessible.
	 * @throws StorageException
	 *             If a database exception occurred or database is unexpected
	 *             unreachable.
	 * @throws UnknownConfigurationElementError
	 *             If the flag-object to be stored is not known by this
	 *             configuration database..
	 */
	public void saveCurrentReplicationState(ReplicationStateDTO currentState)
			throws StorageError, StorageException,
			UnknownConfigurationElementError;

	/**
	 * Saves a HistoryDTO to the local Database, shall not be called on the
	 * Configuration Database.
	 * 
	 * @param historyDTO
	 */
	public void saveHistoryDTO(HistoryDTO historyDTO) throws StorageError,
			StorageException, InconsistentConfigurationException;

	/**
	 * 
	 * IMPORTANT: Auf {@link HasJoinedElements} achten!!!
	 * 
	 * @param dto
	 * @throws StorageError
	 * @throws StorageException
	 * @throws InconsistentConfigurationException
	 */
	public void saveDTO(NewAMSConfigurationElementDTO dto) throws StorageError,
			StorageException, InconsistentConfigurationException;

	/**
	 * 
	 * IMPORTANT: Auf {@link HasJoinedElements} achten!!!
	 * 
	 * @param dto
	 * @throws StorageError
	 * @throws StorageException
	 * @throws InconsistentConfigurationException
	 */
	public void deleteDTO(NewAMSConfigurationElementDTO dto)
			throws StorageError, StorageException,
			InconsistentConfigurationException;

	public AlarmbearbeiterDTO saveAlarmbearbeiterDTO(
			AlarmbearbeiterDTO alarmBearbeiterDTO) throws StorageError,
			StorageException, InconsistentConfigurationException;

	public AlarmbearbeiterGruppenDTO saveAlarmbearbeiterGruppenDTO(
			AlarmbearbeiterGruppenDTO alarmBearbeiterGruppenDTO)
			throws InconsistentConfigurationException;

	public TopicDTO saveTopicDTO(TopicDTO topicDTO) throws StorageError,
			StorageException, InconsistentConfigurationException;

	public FilterDTO saveFilterDTO(FilterDTO dto) throws StorageError,
			StorageException, InconsistentConfigurationException;

	public RubrikDTO saveRubrikDTO(RubrikDTO dto) throws StorageError,
			StorageException, InconsistentConfigurationException;

	public void deleteAlarmbearbeiterDTO(AlarmbearbeiterDTO dto)
			throws StorageError, StorageException,
			InconsistentConfigurationException;

	public void deleteAlarmbearbeiterGruppenDTO(AlarmbearbeiterGruppenDTO dto)
			throws StorageError, StorageException,
			InconsistentConfigurationException;

	public void deleteAlarmtopicDTO(TopicDTO dto) throws StorageError,
			StorageException, InconsistentConfigurationException;

	public void deleteFilterDTO(FilterDTO dto)
			throws InconsistentConfigurationException, StorageError,
			StorageException;

	public void deleteFilterConditionDTO(FilterConditionDTO dto)
			throws StorageError, StorageException,
			InconsistentConfigurationException;

	/**
	 * Bereitet die Synchronisation mit dem Hintergrudsystem vor. Hierzu werden
	 * die SYN-Tabellen geschrieben.
	 * 
	 * @throws StorageError
	 *             If a configuration error is present and this database is not
	 *             accessible.
	 * @throws StorageException
	 *             If a database exception occurred or database is unexpected
	 *             unreachable.
	 * @throws UnknownConfigurationElementError
	 *             If the flag-object to be stored is not known by this
	 *             configuration database..
	 */
	public void prepareSynchonization() throws StorageError, StorageException,
			InconsistentConfigurationException;

}
