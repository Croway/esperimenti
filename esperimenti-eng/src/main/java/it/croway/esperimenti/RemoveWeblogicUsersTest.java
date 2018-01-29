package it.croway.esperimenti;

import java.util.Hashtable;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;

public class RemoveWeblogicUsersTest {

	private static String codFis = "pippo";

	public static void main(String[] args) throws Exception {
		JMXConnector connector = null;
		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES, "weblogic.management.remote");
			env.put(Context.SECURITY_PRINCIPAL, "weblogic");
			env.put(Context.SECURITY_CREDENTIALS, "weblogic1");
			env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
			env.put(Context.PROVIDER_URL, "t3://localhost:7001");

			JMXServiceURL serviceUrl = new JMXServiceURL(
					"service:jmx:iiop://localhost:7001/jndi/weblogic.management.mbeanservers.domainruntime");

			connector = JMXConnectorFactory.connect(serviceUrl, env);
			MBeanServerConnection connection = connector.getMBeanServerConnection();

			Set<ObjectName> mbeans = connection.queryNames(null, null);
			for (ObjectName mbeanName : mbeans) {
				System.out.println(mbeanName);
			}

			ObjectName userEditor = null;
			ObjectName mBeanTypeService = new ObjectName(
					"com.bea:Name=MBeanTypeService,Type=weblogic.management.mbeanservers.MBeanTypeService");
			ObjectName rs = new ObjectName(
					"com.bea:Name=DomainRuntimeService,Type=weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean");
			ObjectName domainMBean = (ObjectName) connection.getAttribute(rs, "DomainConfiguration");
			ObjectName securityConfig = (ObjectName) connection.getAttribute(domainMBean, "SecurityConfiguration");
			ObjectName defaultRealm = (ObjectName) connection.getAttribute(securityConfig, "DefaultRealm");
			ObjectName[] authProviders = (ObjectName[]) connection.getAttribute(defaultRealm,
					"AuthenticationProviders");

			for (ObjectName providerName : authProviders) {
				System.out.println("Auth provider is: " + providerName);

				if (userEditor == null) {
					ModelMBeanInfo info = (ModelMBeanInfo) connection.getMBeanInfo(providerName);
					String className = (String) info.getMBeanDescriptor().getFieldValue("interfaceClassName");
					System.out.println("className is: " + className);

					if (className != null) {
						String[] mba = (String[]) connection.invoke(mBeanTypeService, "getSubtypes",
								new Object[] { "weblogic.management.security.authentication.UserEditorMBean" },
								new String[] { "java.lang.String" });
						for (String mb : mba) {
							System.out.println("Model Bean is: " + mb);
							if (className.equals(mb)) {
								System.out.println("Found a macth for the model bean and class name!");
								userEditor = providerName;
							}
						}
					}
				}
			}

			if (userEditor == null)
				throw new RuntimeException("Could not retrieve user editor");

			connection.invoke(userEditor, "removeUser", new Object[] { codFis }, new String[] { "java.lang.String" });

			System.out.println("User -> " + codFis + " removed successfully");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connector != null)
				connector.close();
		}

	}

}
