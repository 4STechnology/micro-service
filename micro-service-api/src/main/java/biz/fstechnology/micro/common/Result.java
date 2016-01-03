/*
 * @(#) biz.fstechnology.micro.common.Result
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
 */
@Getter
public class Result<T> implements Serializable {

	/** $Comment$ */
	private static final long serialVersionUID = -9184426445053911773L;

	private final T response;

	private final ProcessResult result;

	private final Exception exception;

	protected Result(ProcessResult result, T response, Exception exception) {
		this.response = response;
		this.result = result;
		this.exception = exception;
	}

	public Result(ProcessResult result, T response) {
		this(result, response, null);
	}

	public Result(Exception exception) {
		this(ProcessResult.UnExpectedException, null, exception);
	}

	@SuppressWarnings("unchecked")
	public <U> Result<U> cast() {
		return (Result<U>) this;
	}

	public <U> Result<U> cast(Class<U> clazz) {
		return cast();
	}

}
