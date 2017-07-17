package com.xivvic.args.bench.init;

import java.util.Arrays;

import com.xivvic.args.bench.ArgsBench;

import lombok.Data;
import lombok.experimental.Accessors;
import xivvic.console.action.Action;
import xivvic.console.action.ActionBase;
import xivvic.console.action.ActionManager;
import xivvic.console.interact.Stdin;
import xivvic.console.menu.Menu;
import xivvic.console.menu.MenuItem;
import xivvic.console.menu.MenuManager;

/**
 * This class creates the Args menu and associated actions
 *
 * @author reid
 */
@Data
@Accessors(fluent = true)
public class InitializerArgumentMenu
{
	private final ActionManager am;
	private final MenuManager mm;
	private final Stdin stdin;

	public InitializerArgumentMenu(ActionManager am, MenuManager mm, Stdin stdin)
	{
		this.am    = am;
		this.mm    = mm;
		this.stdin = stdin;
	}

	public Menu createMenu(ArgsBench bench)
	{
		final String name = "Args";
		Menu menu = new Menu(name, "am", mm);

		menu.addItem(showCurrentArguments(bench));
		menu.addItem(showExampleArguments(bench));
		menu.addItem(replaceExistingArgumentsWithExample(bench));
		menu.addItem(addNewArgument(bench));
		menu.addItem(removeArgument(bench));
		menu.addItem(clearArguments(bench));

		return menu;
	}

	private MenuItem showCurrentArguments(ArgsBench bench)
	{
		String label = "Show current args";
		String   def = "Displays the currently defined arguments used to test schema definition";
		Action     a = new ActionBase(label, def, true) {

			@Override
			protected void internal_invoke(Object param)
			{
				String[] args = bench.arguments();
				String    out = Arrays.toString(args);
				System.out.println(out);
			}
		};
		return new MenuItem(label, "sc", a);
	}

	private MenuItem showExampleArguments(ArgsBench bench)
	{
		String label = "Show example args";
		String   def = "Displays arguments from an example command line";
		Action     a = new ActionBase(label, def, true) {

			@Override
			protected void internal_invoke(Object param)
			{
				String[] example = bench.exampleArguments();
				String    out = Arrays.toString(example);
				System.out.println(out);
			}
		};
		return new MenuItem(label, "sx", a);
	}

	private MenuItem replaceExistingArgumentsWithExample(ArgsBench bench)
	{
		String label = "Replace with example";
		String   def = "Replaces the current arguments with the example args";
		Action     a = new ActionBase(label, def, true)
		{
			@Override
			protected void internal_invoke(Object param)
			{
				String[] example = bench.exampleArguments();
				bench.clearArgs();
				bench.arguments(example);
				String[] args = bench.arguments();
				String    out = Arrays.toString(args);
				System.out.println(out);
			}
		};
		return new MenuItem(label, "rx", a);
	}

	private MenuItem addNewArgument(ArgsBench bench)
	{
		String label = "Add a new argument";
		String   def = "Adds an argument to the existing command line";
		Action     a = new ActionBase(label, def, true)
		{
			@Override
			protected void internal_invoke(Object param)
			{
				String prompt = "New Argument to add: ";
				String arg = stdin.promptString(prompt, "-h");
				bench.addArg(arg);
				String[] args = bench.arguments();
				String    out = Arrays.toString(args);
				System.out.println(out);
			}
		};
		return new MenuItem(label, "a", a);
	}

	private MenuItem removeArgument(ArgsBench bench)
	{
		String label = "Remove argument";
		String   def = "Removes an argument from the existing command line";
		Action     a = new ActionBase(label, def, true)
		{
			@Override
			protected void internal_invoke(Object param)
			{
				String arg = null;

				if (param == null)
				{
					String prompt = "Argument to remove: ";
					arg = stdin.promptString(prompt, null);
				}
				else
				{
					arg = (String) param;
				}

				boolean ok = bench.removeArgument(arg);
				if (ok)
				{
					String msg = String.format("Removed argument [%s]", arg);
					System.out.println(msg);
				}
				else
				{
					String msg = String.format("Failed to remove argument [%s]", arg);
					System.out.println(msg);
				}

				String[] args = bench.arguments();
				String    out = Arrays.toString(args);
				System.out.println(out);
			}
		};
		return new MenuItem(label, "rm", a);
	}

	private MenuItem clearArguments(ArgsBench bench)
	{
		String label = "Clear arguments";
		String   def = "Resets the command line to be empty";
		Action     a = new ActionBase(label, def, true)
		{
			@Override
			protected void internal_invoke(Object param)
			{
				boolean ok = stdin.confirm(null, false);
				if (ok)
				{
					bench.clearArgs();
				}
				else
				{
					System.out.println("Aborting clear args");
				}
			}
		};
		return new MenuItem(label, "clr", a);
	}
}
