/*
 * @(#) biz.fstechnology.micro.common.DefaultServiceConnection
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

package biz.fstechnology.micro.common;

import java.util.function.Consumer;

/**
 * TODO Type Description
 * 
 * @author Maruyama Takayuki
 * @since 2016/01/01
 */
public class DefaultServiceConnection implements ServiceConnection {

	private final ServiceConnection delegater;

	public DefaultServiceConnection(ServiceConnection delegater) {
		this.delegater = delegater;
	}

	/**
	 * @see biz.fstechnology.micro.common.ServiceConnection#call(biz.fstechnology.micro.common.Request)
	 */
	@Override
	public <T, U> Result<U> call(Request<T> request) {
		return delegater.call(request);
	}

	/**
	 * @see biz.fstechnology.micro.common.ServiceConnection#callAsync(biz.fstechnology.micro.common.Request,
	 *      java.util.function.Consumer)
	 */
	@Override
	public <T, U> void callAsync(Request<T> request, Consumer<Result<U>> callback) {
		delegater.callAsync(request, callback);
	}

	public <T> Request<T> createRequestMessage(String serviceName, String processName, Version version, T object) {
		return new Request<>(serviceName, processName, version, object);
	}
}
