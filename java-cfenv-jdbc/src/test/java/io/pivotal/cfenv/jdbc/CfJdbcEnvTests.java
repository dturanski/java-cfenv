/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.pivotal.cfenv.jdbc;

import io.pivotal.cfenv.core.test.CfEnvMock;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Mark Pollack
 */
public class CfJdbcEnvTests {

	private static final String mysqlJdbcUrl = "jdbc:mysql://10.0.4.35:3306/cf_2e23d10a_8738_8c3c_66cf_13e44422698c?user=mysql_username&password=mysql_password";

	@Test
	public void testCfService() {
		CfEnvMock.configure().vcapServicesResource("vcap-services-jdbc.json").mock();
		CfJdbcEnv cfJdbcEnv = new CfJdbcEnv();
		assertThat(cfJdbcEnv.findJdbcService().getUrl()).isEqualTo(mysqlJdbcUrl);
		assertThat(cfJdbcEnv.findJdbcServiceByName("mysql").getUrl())
				.isEqualTo(mysqlJdbcUrl);

		assertThatThrownBy(() -> {
			cfJdbcEnv.findJdbcServiceByName("blah").getUrl();
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("No database service with name [blah] was found.");

		assertThatThrownBy(() -> {
			cfJdbcEnv.findJdbcServiceByName((String[]) null).getUrl();
		}).isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("No database service with name [null]");

		CfJdbcService cfJdbcService = cfJdbcEnv.findJdbcService();
		assertThat(cfJdbcService.getUrl()).isEqualTo(mysqlJdbcUrl);
	}
}
