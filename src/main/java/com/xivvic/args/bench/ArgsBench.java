package com.xivvic.args.bench;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xivvic.args.Args;
import com.xivvic.args.bench.init.Initializer;
import com.xivvic.args.schema.Schema;
import com.xivvic.args.schema.item.Item;
import com.xivvic.args.util.FileUtil;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import xivvic.console.Application;

/**
 * Example application to run the Args command line processor.
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(fluent = true)
public final class ArgsBench extends Application
{
	private final String[] EMPTY_STRING_ARRAY = {};
	private final static String[] example_args = {"-h", "--path", "/tmp", "--file" , "out.txt", "--server", "localhost", "--port", "8080"};
	//	private final static String example_schema = getOptionDefinitions();

	private String[] arguments = {"-v", "foo", "bar"};
	private String   schemaText = null;
	private Args arg;
	private Schema schema;

	private final Initializer init;
	public ArgsBench(Initializer init)
	{
		super(init.mm(), init.am());
		this.init = init;
	}

	public static void main(String[] args)
	{
		Initializer init = new Initializer();
		ArgsBench  bench = new ArgsBench(init);

		bench.doLifecycle();
	}

	@Override
	protected void setup()
	{
		init.setup(this);    // Create and register actions, build menus
		super.setup();       // Invoke INITIALIZATION actions
		Map<String, Item<?>> map = new HashMap<>();
		schema = new Schema(map);
	}

	// Reads resource file containing option definitions
	//
	public String getOptionDefinitions()
	{
		String path = "src/main/resources/argsdefs";
		String file = "Option.definitions.example.argsdef";
		String defs = FileUtil.readFromResourceFile(path, file);

		return defs;
	}

	public void clearArgs()
	{
		arguments = EMPTY_STRING_ARRAY;
	}

	public void clearSchema()
	{
		Map<String, Item<?>> map = new HashMap<>();
		schema = new Schema(map);
	}

	public void addArg(String arg)
	{
		String[] tmp = new String[arguments.length + 1];
		System.arraycopy(arguments, 0, tmp, 0, arguments.length);
		arguments = tmp;
		arguments[arguments.length - 1] = arg;
	}

	public String[] exampleArguments()
	{
		return ArgsBench.example_args;
	}

	public boolean removeArgument(String arg)
	{
		if (arg == null || arg.length() == 0)
		{
			return false;
		}

		List<String> list = Arrays.asList(arguments);
		list = new ArrayList<>(list);

		boolean ok = list.remove(arg);
		if (! ok)
		{
			return false;
		}

		arguments = list.toArray(new String[0]);
		return true;
	}

}
