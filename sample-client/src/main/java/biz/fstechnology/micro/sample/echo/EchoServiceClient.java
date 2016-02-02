/*
 * @(#) biz.fstechnology.micro.sample.echo.EchoServiceClient
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

import java.io.BufferedReader;
import java.io.InputStreamReader;

import biz.fstechnology.micro.common.Request;
import biz.fstechnology.micro.common.Result;
import biz.fstechnology.micro.common.ServiceConnection;
import biz.fstechnology.micro.common.jms.JmsServiceConnection;

/**
 * TODO Type Description
 * 
 * @author Maruyama Takayuki
 * @since 2016/01/01
 */
public class EchoServiceClient {

	public static void main(String... args) throws Exception {
		System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES", "biz.fstechnology.micro.common");
		ServiceConnection jmsConnection = JmsServiceConnection.getInstance(EchoServiceDefinition.DESTINATION_TOPIC,
				new EchoServiceDefinition().createConnectionFactory());
		BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			System.out.print("> ");
			String line = consoleIn.readLine();
			System.out.println(">> Request: " + line);
			Result<String> result = jmsConnection.call(new Request<>(EchoServiceDefinition.DESTINATION_TOPIC,
					EchoServiceDefinition.ECHO_PROCESS, EchoServiceDefinition.VERSION, line));
			System.out.println("<< Result: " + result.getResponse());
		}
	}

}
