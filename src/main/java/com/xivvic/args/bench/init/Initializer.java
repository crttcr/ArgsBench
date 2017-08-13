package com.xivvic.args.bench.init;

import com.xivvic.args.bench.ArgsBench;

import lombok.Data;
import lombok.experimental.Accessors;
import xivvic.console.action.ActionManager;
import xivvic.console.menu.Menu;
import xivvic.console.menu.MenuManager;
import xivvic.util.io.Stdio;

/**
 * This class is responsible for initialization actions for the main application class.
 * This includes
 *
 * Creating an ActionManager
 * Creating a MenuManager
 * Creating Actions
 * Populating Menus and Menu Items
 *
 * @author reid
 */
@Data
@Accessors(fluent = true)
public class Initializer
{
	private final ActionManager am;
	private final MenuManager mm;
	private final Stdio stdio;

	public Initializer()
	{
		this.am    = new ActionManager();
		this.mm    = new MenuManager(am);
		this.stdio = new Stdio(System.in, System.out);
	}

	//	// TODO Auto-generated method stub
	//	arguments = arguments == null ? example_args : arguments;
	//	schemaText = schemaText == null ? example_schema : schemaText;
	//	Text2Schema t2s = new Text2Schema();
	//	schema = t2s.createSchema(schemaText);
	//	arg = Args.processOrThrowException(schema, arguments);

	public void setup(ArgsBench bench)
	{
		Menu main = createMainMenu(bench);

		mm.addStartMenu(main);
		mm.addCoreActions();
	}

	private Menu createMainMenu(ArgsBench bench)
	{
		Menu main = new Menu("ArgsBench :: MainMenu", "code", mm);

		InitializerArgumentMenu iam = new InitializerArgumentMenu(am, mm, stdio);
		InitializerSchemaMenu   ism = new InitializerSchemaMenu(am, mm, stdio);
		InitializerUtilityMenu  ium = new InitializerUtilityMenu(am, mm, stdio);

		main.addItem(iam.createMenu(bench));
		main.addItem(ism.createMenu(bench));
		main.addItem(ium.createMenu(bench));

		return main;
	}

}
