/*
 * @(#) biz.fstechnology.micro.sample.echo.EchoService
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

import biz.fstechnology.micro.common.ProcessResult;
import biz.fstechnology.micro.common.Request;
import biz.fstechnology.micro.common.Result;
import biz.fstechnology.micro.common.Version;
import biz.fstechnology.micro.server.jms.AbstractJmsService;

/**
 * TODO Type Description
 * 
 * @author Maruyama Takayuki
 * @since 2016/01/01
 */
public class EchoService extends AbstractJmsService {

	public static void main(String[] args) throws Exception {
		System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES", "biz.fstechnology.micro.common");
		new EchoService().init();
	}

	/**
	 * @see biz.fstechnology.micro.server.AbstractService#onPreProcessRequest(biz.fstechnology.micro.common.Request)
	 */
	@Override
	public <T> Request<T> onPreProcessRequest(Request<T> request) {
		// this service accepts the "echo" process only.
		if (!EchoServiceDefinition.ECHO_PROCESS.equals(request.getProcessCode())) {
			throw new IllegalArgumentException();
		}
		return super.onPreProcessRequest(request);
	}

	/**
	 * @see biz.fstechnology.micro.server.Service#processRequest(biz.fstechnology.micro.common.Request)
	 */
	@Override
	public <T, U> Result<U> processRequest(Request<T> request) {
		switch (request.getProcessCode()) {
		case EchoServiceDefinition.ECHO_PROCESS:
			@SuppressWarnings("unchecked")
			U resultObj = (U) request.getParameter();
			Result<U> result = new Result<U>(ProcessResult.Success, resultObj);
			return result;
		}
		Result<U> emptyResult = new Result<>(ProcessResult.Empty, null);
		return emptyResult;
	}

	/**
	 * @see biz.fstechnology.micro.server.Service#getVersion()
	 */
	@Override
	public Version getVersion() {
		return EchoServiceDefinition.VERSION;
	}

	/**
	 * @see biz.fstechnology.micro.server.jms.AbstractJmsService#getBrokerUrl()
	 */
	@Override
	protected String getBrokerUrl() {
		return EchoServiceDefinition.BROKER_URL;
	}

	/**
	 * @see biz.fstechnology.micro.server.jms.AbstractJmsService#getClientIdPrefix()
	 */
	@Override
	protected String getClientIdPrefix() {
		return EchoServiceDefinition.CLIENT_ID_PREFIX;
	}

	/**
	 * @see biz.fstechnology.micro.server.jms.AbstractJmsService#getConnectionFactory()
	 */
	@Override
	protected ConnectionFactory getConnectionFactory() {
		return new ActiveMQConnectionFactory("vm:(broker:(" + getBrokerUrl() + "))");
	}

	/**
	 * @see biz.fstechnology.micro.server.jms.AbstractJmsService#getListenTopic()
	 */
	@Override
	protected String getListenTopic() {
		return EchoServiceDefinition.DESTINATION_TOPIC;
	}

}
