/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.container.entrypoint;

import org.apache.flink.runtime.entrypoint.parser.ParserResultFactory;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import javax.annotation.Nonnull;

import java.util.Properties;

import static org.apache.flink.runtime.entrypoint.parser.CommandLineOptions.CONFIG_DIR_OPTION;
import static org.apache.flink.runtime.entrypoint.parser.CommandLineOptions.DYNAMIC_PROPERTY_OPTION;
import static org.apache.flink.runtime.entrypoint.parser.CommandLineOptions.REST_PORT_OPTION;

/**
 * Parser factory which generates a {@link StandaloneJobClusterConfiguration} from a given
 * list of command line arguments.
 */
public class StandaloneJobClusterConfigurationParserFactory implements ParserResultFactory<StandaloneJobClusterConfiguration> {

	private static final Option JOB_CLASS_NAME_OPTION = Option.builder("j")
		.longOpt("job-classname")
		.required(true)
		.hasArg(true)
		.argName("job class name")
		.desc("Class name of the job to run.")
		.build();

	@Override
	public Options getOptions() {
		final Options options = new Options();
		options.addOption(CONFIG_DIR_OPTION);
		options.addOption(REST_PORT_OPTION);
		options.addOption(JOB_CLASS_NAME_OPTION);
		options.addOption(DYNAMIC_PROPERTY_OPTION);

		return options;
	}

	@Override
	public StandaloneJobClusterConfiguration createResult(@Nonnull CommandLine commandLine) {
		final String configDir = commandLine.getOptionValue(CONFIG_DIR_OPTION.getOpt());
		final Properties dynamicProperties = commandLine.getOptionProperties(DYNAMIC_PROPERTY_OPTION.getOpt());
		final String restPortString = commandLine.getOptionValue(REST_PORT_OPTION.getOpt(), "-1");
		final int restPort = Integer.parseInt(restPortString);
		final String jobClassName = commandLine.getOptionValue(JOB_CLASS_NAME_OPTION.getOpt());

		return new StandaloneJobClusterConfiguration(
			configDir,
			dynamicProperties,
			commandLine.getArgs(),
			restPort,
			jobClassName);
	}
}