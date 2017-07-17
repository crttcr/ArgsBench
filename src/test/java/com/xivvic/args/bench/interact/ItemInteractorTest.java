package com.xivvic.args.bench.interact;

import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import xivvic.console.interact.Stdin;

public class ItemInteractorTest
{
	private ItemInteractor subject;

	@Before
	public void before()
	{
		Stdin stdin = new Stdin(System.in, System.out);
		subject = new ItemInteractor(stdin);
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
