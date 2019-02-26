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

package io.pivotal.cfenv.spring.boot;

import io.pivotal.cfenv.core.CfEnv;
import io.pivotal.cfenv.test.CfEnvTestUtils;
import org.junit.Test;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author David Turanski
 **/

public class CfEnvIntegrationTests {

	@Test
	public void cfEnvConfiguredByBootApplication() {
		CfEnvTestUtils.mockVcapServicesFromResource(new ClassPathResource("vcap-services.json"));
		assertThat(System.getenv(CfEnv.VCAP_SERVICES)).isNotEmpty();
		new SpringApplicationBuilder(TestApp.class)
			.web(WebApplicationType.NONE).build();
		CfEnv cfEnv = CfEnvSingleton.getCfEnvInstance();
		assertThat(cfEnv.findServicesByLabel("p-redis","p-mysql")).hasSize(2);
	}

	@SpringBootApplication
	static class TestApp {
	}
}
