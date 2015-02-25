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
	 * ����User��û�д��롣���裺�����˿յ�User��������һ��id����û�д���User�����ԣ��õ�user-token.xml�е����ݣ��������ݡ�
	 * @param worker
	 * @throws Exception
	 */
	protected void runTemplateTest(TemplateHandler worker) throws Exception {
		IDataSet setupDataSet = getReplacedDataSet(worker.getSetupDataSet(), worker.getId());
		DatabaseOperation.CLEAN_INSERT.execute(dbunitConnection, setupDataSet); // �����˿յ�User
		worker.doIt(); // ������һ��id����û�д���User������
		String comparisonDataSetName = worker.getAssertDataSet(); // �õ�user-token.xml�е�����
		if (comparisonDataSetName != null) {
			IDataSet expectedDataSet = getReplacedDataSet(comparisonDataSetName, worker.getId()); // ��������
			IDataSet actualDataSet = dbunitConnection.createDataSet();
			Assertion.assertEquals(expectedDataSet, actualDataSet);
		}
	}

	/**
	 * ��user-token.xml�е�User����̶�λ�á�
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
	 * doIt()��������User�Ƿ����
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
