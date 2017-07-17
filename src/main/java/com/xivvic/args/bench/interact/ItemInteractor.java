package com.xivvic.args.bench.interact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.xivvic.args.schema.OptionType;
import com.xivvic.args.schema.item.Item;

import xivvic.console.interact.Stdin;

public class ItemInteractor
{
	private final Stdin stdin;

	public ItemInteractor(Stdin stdin)
	{
		this.stdin = stdin;
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
		String   name = stdin.promptString(prompt, null);

		return name;
	}

	private String gatherType()
	{
		String   prompt = "Select the type for this item\n";
		OptionType type = promptForEnumValueWithDefault(prompt, OptionType.STRING);

		return type.toString();
	}

	private String gatherDescription()
	{
		String prompt = "What is the description for this item? ";
		String   desc = stdin.promptString(prompt, null);

		return desc;
	}

	private String gatherDefault()
	{
		// FIXME: Default
		return null;
	}

	private String gatherRequired()
	{
		String prompt = "Is the item required? [no] ";
		Boolean  reqd = stdin.confirm(prompt, false);

		return reqd.toString();
	}

	private String gatherEnvironmentVariableName()
	{
		String prompt = "If there is an environment variable for this item, what is it? [none] ";
		String    env = stdin.promptString(prompt, null);

		return env;
	}

	// TODO: This needs to be moved to Stdin class when Ryan has moved it ...
	//
	private <E extends Enum<E>> E promptForEnumValueWithDefault(String prompt, E dv)
	{
		Objects.requireNonNull(dv);

		@SuppressWarnings("unchecked")
		E[] values = (E[]) dv.getClass().getEnumConstants();

		List<String> names = new ArrayList<>(values.length);

		int defaultPosition = 0;
		for (int i = 0; i < values.length; i++)
		{
			E item = values[i];
			names.add(item.name());
			if (dv == item)
			{
				defaultPosition = i;
			}
		}

		String answer = stdin.getStringFromListWithDefault(names, prompt, defaultPosition);

		for (int i = 0; i < values.length; i++)
		{
			E item = values[i];
			if (answer.equals(item.name()))
			{
				return item;
			}
		}

		// Should never exit the loop above without finding the chosen item.
		//
		return dv;
	}

}
