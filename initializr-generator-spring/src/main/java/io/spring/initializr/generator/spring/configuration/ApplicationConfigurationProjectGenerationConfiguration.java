/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.initializr.generator.spring.configuration;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.io.template.MustacheTemplateRenderer;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.spring.choice.contributor.ChoiceDriverLoggerContributor;
import io.spring.initializr.generator.spring.choice.contributor.CommonContributor;
import io.spring.initializr.metadata.InitializrMetadata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

/**
 * Configuration for application-related contributions to a generated project.
 *
 * @author Stephane Nicoll
 */
@ProjectGenerationConfiguration
public class ApplicationConfigurationProjectGenerationConfiguration {

	private final InitializrMetadata metadata;

	private final ProjectDescription description;

	public ApplicationConfigurationProjectGenerationConfiguration(InitializrMetadata metadata, ProjectDescription description) {
		this.metadata = metadata;
		this.description = description;
	}

	@Bean
	public ApplicationPropertiesContributor applicationPropertiesContributor() {
		return new ApplicationPropertiesContributor();
	}

	@Bean
	public WebFoldersContributor webFoldersContributor(Build build, InitializrMetadata metadata) {
		return new WebFoldersContributor(build, metadata);
	}

	@Bean
	public CommonContributor commonContributor(MustacheTemplateRenderer templateRenderer) {
		return new CommonContributor(description, templateRenderer);
	}

	@Bean
	@ConditionalOnRequestedDependency("choice-driver-logger")
	public ChoiceDriverLoggerContributor choiceDriverLoggerContributor() {
		return new ChoiceDriverLoggerContributor();
	}

}
