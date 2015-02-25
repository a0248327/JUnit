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
 * 测试在Jdbc中是否能存入用户
 * @author han
 *
 */
public class UserDaoJdbcImplTest extends AbstractDbUnitTestCase {

	/**
	 * 首先在user.xml中得到用户，再把这个用户插入到数据库，调用这个用户，再测试它所有的属性。
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
	 * 把 user.xml 中的对象存入 dbunitConnection， user.xml 中的对象， 创建真实的对象。
	 * @throws Exception
	 */
	@Test
	public void testAddUseIgnoringId() throws Exception {
		IDataSet setupDataSet = getDataSet("/user.xml"); 
		DatabaseOperation.DELETE_ALL.execute(dbunitConnection, setupDataSet); // 把 user.xml 中的对象存入 dbunitConnection
		User user = newUser();
		long id = dao.addUser(user);
		assertTrue(id > 0);
		IDataSet expectedDataSet = getDataSet("/user.xml"); // user.xml 中的对象
		IDataSet actualDataSet = dbunitConnection.createDataSet(); // 创建真实的对象
		Assertion.assertEqualsIgnoreCols(expectedDataSet, actualDataSet, "users", new String[] { "id" });
	}

	/**
	 * 插入一个确定ID的用户， 首先插入一个确定ID的用户， 插入到数据库，在数据库中得到这个用户，查看这个用户是否存在。
	 * @throws Exception
	 */
	@Test
	public void testGetUserByIdReplacingIds() throws Exception {
		long id = 42;
		IDataSet setupDataset = getReplacedDataSet("/user-token.xml", id);  // 插入一个确定ID的用户
		DatabaseOperation.INSERT.execute(dbunitConnection, setupDataset); // 插入到数据库
		User user = dao.getUserById(id); // 在数据库中得到这个用户
		assertUser(user); // 查看这个用户是否存在
	}

	/**
	 * 把 user-token.xml 中的对象存入 dbunitConnection，user-token.xml 中的对象，创建真实的对象，测试 user-token.xml 中的对象 和 真实的对象是否相等。
	 * @throws Exception
	 */
	@Test
	public void testAddUserReplacingIds() throws Exception {
		IDataSet setupDataSet = getDataSet("/user-token.xml");
		DatabaseOperation.DELETE_ALL.execute(dbunitConnection, setupDataSet); // 把 user-token.xml 中的对象存入 dbunitConnection
		User user = newUser();
		long id = dao.addUser(user);
		assertTrue(id > 0);
		IDataSet expectedDataSet = getReplacedDataSet(setupDataSet, id); // user-token.xml 中的对象
		IDataSet actualDataSet = dbunitConnection.createDataSet(); // 创建真实的对象
		Assertion.assertEquals(expectedDataSet, actualDataSet); // 测试 user-token.xml 中的对象 和 真实的对象是否相等
	}

}
