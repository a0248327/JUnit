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

import static com.manning.junitbook.ch17.dbunit.EntitiesHelper.*;
import static org.junit.Assert.*;

import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Ignore;
import org.junit.Test;

/**
 * ������Jdbc���Ƿ��ܴ����û�
 * @author han
 *
 */
public class UserDaoJdbcImplTest extends AbstractDbUnitTestCase {

	/**
	 * ������user.xml�еõ��û����ٰ�����û����뵽���ݿ⣬��������û����ٲ��������е����ԡ�
	 * @throws Exception
	 */
	@Test
	public void testGetUserById() throws Exception {
		IDataSet setupDataSet = getDataSet("/user.xml");
		DatabaseOperation.CLEAN_INSERT.execute(dbunitConnection, setupDataSet);
		User user = dao.getUserById(1);
		assertNotNull(user);
		assertEquals("Jeffrey", user.getFirstName());
		assertEquals("Lebowsky", user.getLastName());
		assertEquals("ElDuderino", user.getUsername());
	}

	@Test
	@Ignore("fails if run together with others")
	public void testAddUser() throws Exception {
		User user = newUser();
		long id = dao.addUser(user);
		assertTrue(id > 0);
		IDataSet expectedDataSet = getDataSet("/user.xml");
		IDataSet actualDataSet = dbunitConnection.createDataSet();
		Assertion.assertEquals(expectedDataSet, actualDataSet);
	}

	/**
	 * �� user.xml �еĶ������ dbunitConnection�� user.xml �еĶ��� ������ʵ�Ķ���
	 * @throws Exception
	 */
	@Test
	public void testAddUseIgnoringId() throws Exception {
		IDataSet setupDataSet = getDataSet("/user.xml"); 
		DatabaseOperation.DELETE_ALL.execute(dbunitConnection, setupDataSet); // �� user.xml �еĶ������ dbunitConnection
		User user = newUser();
		long id = dao.addUser(user);
		assertTrue(id > 0);
		IDataSet expectedDataSet = getDataSet("/user.xml"); // user.xml �еĶ���
		IDataSet actualDataSet = dbunitConnection.createDataSet(); // ������ʵ�Ķ���
		Assertion.assertEqualsIgnoreCols(expectedDataSet, actualDataSet, "users", new String[] { "id" });
	}

	/**
	 * ����һ��ȷ��ID���û��� ���Ȳ���һ��ȷ��ID���û��� ���뵽���ݿ⣬�����ݿ��еõ�����û����鿴����û��Ƿ���ڡ�
	 * @throws Exception
	 */
	@Test
	public void testGetUserByIdReplacingIds() throws Exception {
		long id = 42;
		IDataSet setupDataset = getReplacedDataSet("/user-token.xml", id);  // ����һ��ȷ��ID���û�
		DatabaseOperation.INSERT.execute(dbunitConnection, setupDataset); // ���뵽���ݿ�
		User user = dao.getUserById(id); // �����ݿ��еõ�����û�
		assertUser(user); // �鿴����û��Ƿ����
	}

	/**
	 * �� user-token.xml �еĶ������ dbunitConnection��user-token.xml �еĶ��󣬴�����ʵ�Ķ��󣬲��� user-token.xml �еĶ��� �� ��ʵ�Ķ����Ƿ���ȡ�
	 * @throws Exception
	 */
	@Test
	public void testAddUserReplacingIds() throws Exception {
		IDataSet setupDataSet = getDataSet("/user-token.xml");
		DatabaseOperation.DELETE_ALL.execute(dbunitConnection, setupDataSet); // �� user-token.xml �еĶ������ dbunitConnection
		User user = newUser();
		long id = dao.addUser(user);
		assertTrue(id > 0);
		IDataSet expectedDataSet = getReplacedDataSet(setupDataSet, id); // user-token.xml �еĶ���
		IDataSet actualDataSet = dbunitConnection.createDataSet(); // ������ʵ�Ķ���
		Assertion.assertEquals(expectedDataSet, actualDataSet); // ���� user-token.xml �еĶ��� �� ��ʵ�Ķ����Ƿ����
	}

}
