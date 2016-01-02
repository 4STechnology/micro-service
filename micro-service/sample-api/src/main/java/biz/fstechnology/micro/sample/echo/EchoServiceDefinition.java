/*
 * @(#) biz.fstechnology.micro.sample.echo.EchoServiceDefinition
 * Copyright (c) 2016 4S Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * 	Unless required by applicable law or agreed to in writing, software
 * 	distributed under the License is distributed on an "AS IS" BASIS,
 * 	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 	See the License for the specific language governing permissions and
 * 	limitations under the License.
 */

package biz.fstechnology.micro.sample.echo;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;

import biz.fstechnology.micro.common.Version;
import biz.fstechnology.micro.common.jms.ConnectionFactoryProvider;

/**
 * TODO Type Description
 * 
 * @author Maruyama Takayuki
 * @since 2016/01/01
 */
public final class EchoServiceDefinition implements ConnectionFactoryProvider {

	EchoServiceDefinition() {
		// NO OP
	}

	public static final String DESTINATION_TOPIC = "echoTopic";
	public static final String BROKER_URL = "tcp://localhost:61616?connectionTimeout=30000";
	public static final String CLIENT_ID_PREFIX = "ECHOCLIENT";
	public static final String ECHO_PROCESS = "echo";
	public static final Version VERSION = new Version("1", "0", "0");

	/**
	 * @see biz.fstechnology.micro.common.jms.ConnectionFactoryProvider#createConnectionFactory()
	 */
	@Override
	public ConnectionFactory createConnectionFactory() {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
		connectionFactory.setClientIDPrefix(CLIENT_ID_PREFIX);
		return connectionFactory;
	}

}
