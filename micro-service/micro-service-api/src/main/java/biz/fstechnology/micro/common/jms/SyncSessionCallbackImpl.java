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

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;

import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.core.SessionCallback;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * TODO Type Description
 * 
 * @author Maruyama Takayuki
 * @since 2016/01/01
 */
public class SyncSessionCallbackImpl<T> implements SessionCallback<T> {

	@Getter
	private final String topicName;

	@Getter
	private final Class<T> messageObjectCls;

	public SyncSessionCallbackImpl(String topicName, Class<T> messageObjectCls) {
		this.topicName = topicName;
		this.messageObjectCls = messageObjectCls;
	}

	@Getter
	@Setter
	private MessageCreator messageCreator;

	@Getter
	@Setter(AccessLevel.PROTECTED)
	private long timeout;

	/**
	 * @see org.springframework.jms.core.SessionCallback#doInJms(javax.jms.Session)
	 */
	@Override
	public T doInJms(Session session) throws JMSException {
		Topic destTopic = session.createTopic(topicName);
		try (MessageConsumer consumer = session.createConsumer(destTopic);
				MessageProducer producer = session.createProducer(destTopic)) {
			Message requestMessage = getMessageCreator().createMessage(session);
			producer.send(destTopic, requestMessage);
			return null;
		}
	}

	protected T waitResponse(MessageConsumer consumer, Message requestMessage) throws JMSException {
		return waitResponse(consumer, requestMessage, getTimeout());
	}

	protected final T waitResponse(MessageConsumer consumer, Message requestMessage, long timeout) throws JMSException {
		while (true) {
			Message response = consumer.receive(timeout);
			if (response == null) {
				// TIMEOUT
				break;
			} else {
				final String correlationId = response.getJMSCorrelationID();
				final String messageId = requestMessage.getJMSMessageID();
				if (correlationId.equals(messageId)) {
					// RESPONSE RECEIVED
					return response.getBody(messageObjectCls);
				}
			}
		}
		return null;
	}

}
