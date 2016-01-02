/*
 * @(#) biz.fstechnology.micro.common.Request
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

package biz.fstechnology.micro.common;

import java.io.Serializable;

import lombok.Getter;

/**
 * TODO Type Description
 * 
 * @author Maruyama Takayuki
 * @since 2015/12/26
 * @param <T>
 */
@Getter
public class Request<T> implements Serializable {

	/** $Comment$ */
	private static final long serialVersionUID = -4969120509868352632L;
	private final T parameter;
	private final String serviceCode;
	private final String processCode;
	private final Version serviceVersion;

	public Request(String serviceCode, String processCode, Version serviceVersion, T parameter) {
		this.serviceCode = serviceCode;
		this.processCode = processCode;
		this.serviceVersion = serviceVersion;
		this.parameter = parameter;
	}

}
