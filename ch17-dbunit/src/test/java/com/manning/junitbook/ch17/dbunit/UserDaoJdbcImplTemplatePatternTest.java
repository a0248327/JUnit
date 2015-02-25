/* 
 * ========================================================================
 * 
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * ========================================================================
 */
package com.manning.junitbook.ch17.dbunit;

import static org.junit.Assert.*;
import static com.manning.junitbook.ch17.dbunit.EntitiesHelper.*;

import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;

public class UserDaoJdbcImplTemplatePatternTest extends AbstractDbUnitTestCase {

	protected interface TemplateHandler {
		long getId();

		void doIt() throws Exception;

		String getSetupDataSet();

		String getAssertDataSet();
	}

	/**
	 * 测试User有没有存入。步骤：创建了空的User，创建了一个id但还没有存入User的属性，得到user-token.xml中的数据，存入数据。
	 * @param worker
	 * @throws Exception
	 */
	protected void runTemplateTest(TemplateHandler worker) throws Exception {
		IDataSet setupDataSet = getReplacedDataSet(worker.getSetupDataSet(), worker.getId());
		DatabaseOperation.CLEAN_INSERT.execute(dbunitConnection, setupDataSet); // 创建了空的User
		worker.doIt(); // 创建了一个id但还没有存入User的属性
		String comparisonDataSetName = worker.getAssertDataSet(); // 得到user-token.xml中的数据
		if (comparisonDataSetName != null) {
			IDataSet expectedDataSet = getReplacedDataSet(comparisonDataSetName, worker.getId()); // 存入数据
			IDataSet actualDataSet = dbunitConnection.createDataSet();
			Assertion.assertEquals(expectedDataSet, actualDataSet);
		}
	}

	/**
	 * 把user-token.xml中的User存入固定位置。
	 * @throws Exception
	 */
	@Test
	public void testGetUserById() throws Exception {
		final long id = 42; // value here does not matter
		TemplateHandler worker = new TemplateHandler() {
			public void doIt() throws Exception {
				User user = dao.getUserById(id);
				assertUser(user);
			}

			public String getSetupDataSet() {
				return "/user-token.xml";
			}

			public String getAssertDataSet() {
				return null;
			}

			public long getId() {
				return id;
			}
		};
		runTemplateTest(worker);
	}

	/**
	 * doIt()方法测试User是否存入
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAddUser() throws Exception {
		TemplateHandler worker = new TemplateHandler() {
			private long id = -1;

			public void doIt() throws Exception {
				User user = newUser();
				id = dao.addUser(user);
				assertTrue(id > 0);
			}

			public String getSetupDataSet() {
				return "/empty.xml";
			}

			public String getAssertDataSet() {
				return "/user-token.xml";
			}

			public long getId() {
				return id;
			}
		};
		runTemplateTest(worker);
	}
}
