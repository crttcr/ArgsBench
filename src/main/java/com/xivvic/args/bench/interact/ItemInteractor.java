package com.xivvic.args.bench.interact;

import java.util.HashMap;
import java.util.Map;

import com.xivvic.args.schema.OptionType;
import com.xivvic.args.schema.item.Item;

import xivvic.util.io.Stdio;

public class ItemInteractor
{
	private final Stdio stdio;

	public ItemInteractor(Stdio stdio)
	{
		this.stdio = stdio;
	}

	/**
	 * Asks the user for all the information required to construct an Item.
	 * @return map of values used for the definition
	 */
	public Map<String, String> gatherItemDefinition()
	{
		Map<String, String> rv = new HashMap<>();

		rv.put(Item.NAME,        gatherName());
		rv.put(Item.TYPE,        gatherType());
		rv.put(Item.DESCRIPTION, gatherDescription());
		rv.put(Item.DEFAULT,     gatherDefault());
		rv.put(Item.REQUIRED,    gatherRequired());
		rv.put(Item.ENV_VAR,     gatherEnvironmentVariableName());

		return rv;
	}

	private String gatherName()
	{
		String prompt = "What is the name for this item? ";
		String   name = stdio.getString(prompt);

		return name;
	}

	private String gatherType()
	{
		String   prompt = "What type of value does this option provide?";
		OptionType type = stdio.getEnumWithDefault(prompt, OptionType.STRING);

		return type.toString();
	}

	private String gatherDescription()
	{
		String prompt = "What is the description for this item? ";
		String   desc = stdio.getString(prompt);

		return desc;
	}

	private String gatherDefault()
	{
		String msg = "The ability to define a default value has not been developed";
		stdio.output(msg);
		// FIXME: Default
		return null;
	}

	private String gatherRequired()
	{
		String prompt = "Is the item required? [no] ";
		Boolean  reqd = stdio.confirm(prompt, false);

		return reqd.toString();
	}

	private String gatherEnvironmentVariableName()
	{
		String prompt = "If there is an environment variable for this item, what is it? [none] ";
		String    env = stdio.getString(prompt);

		return env;
	}

}
