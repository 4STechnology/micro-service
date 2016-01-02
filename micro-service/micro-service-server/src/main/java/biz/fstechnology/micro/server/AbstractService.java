/*
 * @(#) biz.fstechnology.micro.server.AbstractService
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

package biz.fstechnology.micro.server;

import biz.fstechnology.micro.common.Request;
import biz.fstechnology.micro.common.Version;

/**
 * TODO Type Description
 * 
 * @author Maruyama Takayuki
 * @since 2016/01/01
 */
public abstract class AbstractService implements Service {

	/**
	 * @see biz.fstechnology.micro.server.Service#onPreProcessRequest(biz.fstechnology.micro.common.Request)
	 */
	@Override
	public <T> Request<T> onPreProcessRequest(Request<T> request) {
		if (!isValidVersion(request)) {
			// TODO message or original exception
			throw new IllegalArgumentException();
		}
		return request;
	}

	/**
	 * @see biz.fstechnology.micro.server.Service#isValidVersion(biz.fstechnology.micro.common.Request)
	 */
	@Override
	public boolean isValidVersion(Request<?> request) {
		Version clientVersion = request.getServiceVersion();
		Version serverVersion = this.getVersion();
		return serverVersion.equals(clientVersion);
	}

}
