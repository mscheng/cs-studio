package org.csstudio.nams.configurator.service;

import org.csstudio.nams.common.fachwert.RubrikTypeEnum;
import org.csstudio.nams.configurator.beans.AlarmbearbeiterBean;
import org.csstudio.nams.configurator.beans.AlarmbearbeiterGruppenBean;
import org.csstudio.nams.configurator.beans.AlarmtopicBean;
import org.csstudio.nams.configurator.beans.FilterBean;
import org.csstudio.nams.configurator.beans.FilterbedingungBean;
import org.csstudio.nams.configurator.beans.IConfigurationBean;
import org.csstudio.nams.service.configurationaccess.localstore.declaration.exceptions.InconsistentConfigurationException;
import org.csstudio.nams.service.configurationaccess.localstore.declaration.exceptions.StorageError;
import org.csstudio.nams.service.configurationaccess.localstore.declaration.exceptions.StorageException;

public interface ConfigurationBeanService {
	
	/**
	 * XXX Ein sehr inperformanter Weg alle Daten neu zu laden,...
	 */
	public void refreshData();

	public abstract AlarmbearbeiterBean[] getAlarmBearbeiterBeans();

	public abstract AlarmbearbeiterGruppenBean[] getAlarmBearbeiterGruppenBeans();

	public abstract AlarmtopicBean[] getAlarmTopicBeans();

	public abstract FilterBean[] getFilterBeans();
	
	public abstract FilterbedingungBean[] getFilterConditionBeans();
	public FilterbedingungBean[] getFilterConditionsBeans();

	public abstract String[] getRubrikNamesForType(RubrikTypeEnum type);

	public <T extends IConfigurationBean> T save(T bean) throws InconsistentConfigurationException, StorageError, StorageException;
	
	public void delete(IConfigurationBean bean) throws StorageError, StorageException, InconsistentConfigurationException;

	public void addConfigurationBeanServiceListener(ConfigurationBeanServiceListener listener);
	public void removeConfigurationBeanServiceListener(ConfigurationBeanServiceListener listener);
}
