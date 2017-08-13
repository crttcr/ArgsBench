package com.xivvic.args.bench.interact;

import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import xivvic.util.io.Stdio;


public class ItemInteractorTest
{
	private ItemInteractor subject;

	@Before
	public void before()
	{
		Stdio stdio = new Stdio(System.in, System.out);
		subject = new ItemInteractor(stdio);
	}

	@Test
	public void onConstruct_withNullStdin_thenThrowException() throws Exception
	{
		new ItemInteractor(null);
	}

	@Ignore("At present this test requires a user to answer questions.")
	@Test
	public void onX_thenY()
	{
		Map<String, String> result = subject.gatherItemDefinition();

		System.out.println(result);
	}

}
