/*
 * @(#) biz.fstechnology.micro.common.SyncSessionCallbackImpl
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

package biz.fstechnology.micro.common.jms;

import java.io.Serializable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import javax.jms.Topic;

import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.core.SessionCallback;
import org.springframework.jms.support.JmsUtils;

import biz.fstechnology.micro.common.AutoCloseableWrapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * TODO Type Description
 * 
 * @author Maruyama Takayuki
 * @since 2016/01/01
 */
public class SyncSessionCallbackImpl<T extends Serializable> implements SessionCallback<T>, MessageListener {

	@Getter
	private final String topicName;

	@Getter
	private Serializable messageObj;
	@Getter
	private final Class<T> messageObjectCls;

	public SyncSessionCallbackImpl(String topicName, Serializable messageObj, Class<T> messageObjectCls) {
		this.topicName = topicName;
		this.messageObj = messageObj;
		this.messageObjectCls = messageObjectCls;
	}

	@Getter
	@Setter
	private MessageCreator messageCreator;

	@Getter
	@Setter(AccessLevel.PROTECTED)
	private long timeout;

	protected MessageProducer createProducer(Session session, Topic destTopic) {
		try {
			return session.createProducer(destTopic);
		} catch (JMSException ex) {
			throw new RuntimeException(ex);
		}
	}

	protected MessageConsumer createConsumer(Session session, Destination responseDestination) {
		try {
			return session.createConsumer(responseDestination);
		} catch (JMSException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * @see org.springframework.jms.core.SessionCallback#doInJms(javax.jms.Session)
	 */
	@Override
	public T doInJms(Session session) throws JMSException {
		Topic destTopic = session.createTopic(topicName);
		TemporaryQueue responseQueue = session.createTemporaryQueue();

		// oh my god...
		// why MessageProducer & MessageConsumer not have AutoCloseable!!!
		try (AutoCloseableWrapper<MessageProducer> producerCont = new AutoCloseableWrapper<>(
				() -> createProducer(session, destTopic), JmsUtils::closeMessageProducer);
				AutoCloseableWrapper<MessageConsumer> consumerCont = new AutoCloseableWrapper<>(
						() -> createConsumer(session, responseQueue), JmsUtils::closeMessageConsumer)) {

			MessageProducer producer = producerCont.unwrap();
			MessageConsumer consumer = consumerCont.unwrap();
			consumer.setMessageListener(this);

			if (getMessageCreator() == null) {
				RequestMessageCreator messageCreator = new RequestMessageCreator();
				messageCreator.setContents(getMessageObj());
				setMessageCreator(messageCreator);
			}
			if (getMessageCreator() instanceof RequestMessageCreator) {
				((RequestMessageCreator) getMessageCreator()).setReplyTo(responseQueue);
			}
			Message requestMessage = getMessageCreator().createMessage(session);
			producer.send(destTopic, requestMessage);

			if (getMessageCreator() instanceof RequestMessageCreator) {
				return waitResponse(consumer, requestMessage);
			} else {
				return null;
			}
		}
	}

	protected T waitResponse(MessageConsumer consumer, Message requestMessage) throws JMSException {
		return waitResponse(consumer, requestMessage, getTimeout());
	}

	protected final T waitResponse(MessageConsumer consumer, Message requestMessage, long timeout) throws JMSException {
		if (timeout <= 0) {
			return waitResponse(consumer, requestMessage, 30000);
		}
		try {
			return responseQueue.poll(timeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO TIMEOUT
			return null;
		}
	}

	final LinkedBlockingQueue<T> responseQueue = new LinkedBlockingQueue<>();

	/**
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onMessage(Message message) {
		if (message instanceof ObjectMessage) {
			try {
				responseQueue.put((T) ((ObjectMessage) message).getObject());
			} catch (JMSException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
