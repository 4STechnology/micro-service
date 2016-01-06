/*
 * @(#) biz.fstechnology.micro.common.ServiceConnection
 * Copyright (c) 2015 4S Technology
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

package biz.fstechnology.micro.common.jms;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import org.springframework.jms.core.JmsTemplate;

import biz.fstechnology.micro.common.DefaultServiceConnection;
import biz.fstechnology.micro.common.Request;
import biz.fstechnology.micro.common.Result;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Service Connection for JMS Service.
 * <p>
 * This is singleton and <b>thread-unsafe</b> implementation.
 * </p>
 * 
 * @author Maruyama Takayuki
 * @since 2015/12/26
 */
public class JmsServiceConnection extends DefaultServiceConnection {

	private static final JmsServiceConnection INSTANCE = new JmsServiceConnection();

	protected JmsServiceConnection() {
		super(null);
	}

	@Getter(AccessLevel.PRIVATE)
	@Setter(AccessLevel.PRIVATE)
	private JmsTemplate jmsTemplate = null;

	@Getter
	@Setter(AccessLevel.PRIVATE)
	private String defaultDestination = null;

	public static JmsServiceConnection getInstance() {
		if (INSTANCE.getJmsTemplate() == null) {
			throw new IllegalStateException(); // TODO message
		}
		return INSTANCE;
	}

	public static JmsServiceConnection getInstance(String defaultDestination, ConnectionFactory connectionFactory) {
		if (INSTANCE.getJmsTemplate() != null) {
			// WARNING: the instance may be initialized. this cause
			// re-initialize and previous settings are lost.
		}
		INSTANCE.setJmsTemplate(new JmsTemplate(connectionFactory));
		INSTANCE.setDefaultDestination(defaultDestination);
		return INSTANCE;
	}

	@SuppressWarnings("unchecked")
	public <T, U> Result<U> call(String destination, Request<T> request) throws JMSException {
		return jmsTemplate.execute(new SyncSessionCallbackImpl<>(destination, Result.class));
	}

	/**
	 * @see biz.fstechnology.micro.common.DefaultServiceConnection#callAsync(biz.fstechnology.micro.common.Request,
	 *      java.util.function.Consumer)
	 */
	@Override
	public <T, U> void callAsync(Request<T> request, Consumer<Result<U>> callback) {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<Result<U>> rawResponse = executor.submit(() -> call(request));
		executor.submit(() -> {
			try {
				callback.accept(rawResponse.get());
			} catch (Exception e) {
				e.printStackTrace();
				Result<U> result = new Result<>(e);
				callback.accept(result);
			}
		});
		executor.shutdown();
	}

	/**
	 * @see biz.fstechnology.micro.common.DefaultServiceConnection#call(biz.fstechnology.micro.common.Request)
	 */
	@Override
	public <T, U> Result<U> call(Request<T> request) {
		try {
			return call(getDefaultDestination(), request);
		} catch (JMSException e) {
			e.printStackTrace();
			return new Result<>(e);
		}
	}
}
