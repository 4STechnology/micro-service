/*
 * @(#) biz.fstechnology.micro.common.jms.RequestMessageCreator
 * Copyright (c) 2016 bBreak Systems Co, Ltd. All Rights Reserved.
 *
 * THE SOFTWARE IS PROVIDED BY bBreak Systems Co, Ltd.,
 * WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR
 * A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
 * CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package biz.fstechnology.micro.common.jms;

import java.io.Serializable;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.springframework.jms.core.MessageCreator;

import lombok.Getter;
import lombok.Setter;

/**
 * TODO Type Description
 * 
 * @author T.Maruyama
 * @since 2016/01/07
 */
public class RequestMessageCreator implements MessageCreator {

	@Getter
	@Setter
	private Serializable contents;

	@Getter
	@Setter
	private Destination replyTo;

	@Getter
	@Setter
	private String requestId;

	/**
	 * @see org.springframework.jms.core.MessageCreator#createMessage(javax.jms.Session)
	 */
	@Override
	public Message createMessage(Session session) throws JMSException {
		ObjectMessage message = session.createObjectMessage();
		message.setObject(getContents());
		message.setJMSReplyTo(getReplyTo());
		if (getRequestId() == null) {
			setRequestId(Double.toHexString(Math.random()));
		}
		message.setJMSCorrelationID(getRequestId());

		return message;
	}

}
