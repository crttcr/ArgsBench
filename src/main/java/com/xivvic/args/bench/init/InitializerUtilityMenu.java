package com.xivvic.args.bench.init;

import java.util.Arrays;

import com.xivvic.args.bench.ArgsBench;
import com.xivvic.args.schema.Schema;

import lombok.Data;
import lombok.experimental.Accessors;
import xivvic.console.action.Action;
import xivvic.console.action.ActionBase;
import xivvic.console.action.ActionManager;
import xivvic.console.action.DummyAction;
import xivvic.console.menu.Menu;
import xivvic.console.menu.MenuItem;
import xivvic.console.menu.MenuManager;
import xivvic.util.io.Stdio;

/**
 * This class constructs the utility menu and associated actions.
 *
 * @author reid
 */
@Data
@Accessors(fluent = true)
public class InitializerUtilityMenu
{
	private final ActionManager am;
	private final MenuManager mm;
	private final Stdio stdio;

	public InitializerUtilityMenu(ActionManager am, MenuManager mm, Stdio stdio)
	{
		this.am    = am;
		this.mm    = mm;
		this.stdio = stdio;
	}

	public Menu createMenu(ArgsBench bench)
	{
		final String name = "Utility";
		Menu menu = new Menu(name, "um", mm);

		menu.addItem(runCurrentConfiguration(bench));
		menu.addItem(runSingleArgumentWithCurrentSchema(bench));
		menu.addItem(utilRunWithHelp(bench));
		menu.addItem(utilExportSchema(bench));
		menu.addItem(utilShowCommands(bench));
		menu.addItem(utilShowState(bench));

		return menu;
	}

	private MenuItem runCurrentConfiguration(ArgsBench bench)
	{
		String label = "Run current args and schema";
		String   def = "Applies the current arguments to the current schema";
		Action     a = new ActionBase(label, def, true) {

			@Override
			protected void internal_invoke(Object param)
			{
				boolean ok = bench.applyArgsToSchema();
				System.out.println(ok ? "SUCCESS" : "FAILURE");
			}
		};
		return new MenuItem(label, "r", a);
	}

	private MenuItem runSingleArgumentWithCurrentSchema(ArgsBench bench)
	{
		String label = "Run one argument with current schema";
		String   def = "Applies a single arguments to the current schema";
		Action a = new DummyAction(label, def, false);
		return new MenuItem(label, "rone", a);
	}

	private MenuItem utilRunWithHelp(ArgsBench bench)
	{
		String label = "Run with help";
		String   def = "Run command line with help argument";
		Action a = new DummyAction(label, def, false);
		return new MenuItem(label, "h", a);
	}

	private MenuItem utilExportSchema(ArgsBench bench)
	{
		String label = "Export current schema";
		String   def = "Exports the currently defined schema to a file to use in a program";
		Action a = new DummyAction(label, def, false);
		return new MenuItem(label, "ex", a);
	}

	private MenuItem utilShowCommands(ArgsBench bench)
	{
		String label = "Show program commands";
		String   def = "Shows the menu commands for the program";
		Action a = new ActionBase(label, def, true) {

			@Override
			protected void internal_invoke(Object param)
			{
				String[] cmds = bench.init().am().commands();
				System.out.println(Arrays.toString(cmds));
			}
		};
		return new MenuItem(label, "sc", a);
	}

	private MenuItem utilShowState(ArgsBench bench)
	{
		String label = "Show program state";
		String   def = "Shows the major program variable values";
		Action     a = new ActionBase(label, def, true)
		{
			@Override
			public void internal_invoke(Object param)
			{
				String fmt = "ArgsBench current state:";
				final 	Schema schema = bench.schema();
				final 	String[] args = bench.arguments();

				System.out.println(fmt);
				System.out.println(schema == null ? "No schema defined" : schema);
				System.out.println(args   == null ? "No args defined"   : "Args   " + Arrays.toString(args));
			}
		};
		return new MenuItem(label, "ss", a);
	}
}
