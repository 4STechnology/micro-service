/*
 * @(#) biz.fstechnology.micro.server.jms.AbstractJmsService
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

package biz.fstechnology.micro.server.jms;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;

import biz.fstechnology.micro.common.Request;
import biz.fstechnology.micro.common.Result;
import biz.fstechnology.micro.server.AbstractService;

/**
 * TODO Type Description
 * 
 * @author Maruyama Takayuki
 * @since 2016/01/01
 */
public abstract class AbstractJmsService extends AbstractService implements MessageListener {

	protected abstract String getBrokerUrl();

	protected abstract String getClientIdPrefix();

	protected abstract ConnectionFactory getConnectionFactory();

	protected abstract String getListenTopic();

	private MessageProducer replyProducer;

	private Session session;

	protected final JmsTemplate createJmsTemplate() {
		JmsTemplate jmsTemplate = new JmsTemplate(getConnectionFactory());
		jmsTemplate.setPubSubDomain(true);
		return jmsTemplate;
	}

	/**
	 * @see biz.fstechnology.micro.server.Service#init()
	 */
	@Override
	public void init() throws Exception {
		Connection connection = createJmsTemplate().getConnectionFactory().createConnection();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageConsumer consumer = session.createConsumer(session.createTopic(getListenTopic()));
		consumer.setMessageListener(this);

		replyProducer = session.createProducer(null);
		replyProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

		connection.start();
	}

	/**
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	@Override
	public void onMessage(Message message) {
		try {
			ExecutorService executor = Executors.newSingleThreadExecutor();

			if (((ObjectMessage) message).getObject() instanceof Result) {
				// no op
				return;
			}
			Request<?> request = (Request<?>) ((ObjectMessage) message).getObject(); // cast hell...
			Future<Request<?>> preProcessFuture = executor.submit(() -> onPreProcessRequest(request));

			Future<Result<?>> resultFuture = executor.submit(() -> processRequest(preProcessFuture.get()));

			Future<Result<?>> postProcessFuture = executor
					.submit(() -> onPostProcessRequest(request, resultFuture.get()));
			executor.shutdown();

			Result<?> result = postProcessFuture.get();

			ResponseMessageCreator messageCreator = new ResponseMessageCreator();
			messageCreator.setContents(result);
			messageCreator.setRequestId(message.getJMSCorrelationID());

			replyProducer.send(message.getJMSReplyTo(), messageCreator.createMessage(session));

		} catch (JMSException | InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Result<Object> result = new Result<>(e);
			try {
				ResponseMessageCreator messageCreator = new ResponseMessageCreator();
				messageCreator.setContents(result);
				messageCreator.setRequestId(message.getJMSCorrelationID());

				replyProducer.send(message.getJMSReplyTo(), messageCreator.createMessage(session));
			} catch (JmsException | JMSException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * @see biz.fstechnology.micro.server.Service#onPostProcessRequest(biz.fstechnology.micro.common.Request,
	 * biz.fstechnology.micro.common.Result)
	 */
	@Override
	public <T, U> Result<U> onPostProcessRequest(Request<T> request, Result<U> result) {
		return result;
	}

}
