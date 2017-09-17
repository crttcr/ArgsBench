package com.xivvic.args.bench.init;

import java.util.List;
import java.util.Map;

import com.xivvic.args.bench.ArgsBench;
import com.xivvic.args.bench.interact.ItemInteractor;
import com.xivvic.args.error.SchemaException;
import com.xivvic.args.schema.Schema;
import com.xivvic.args.schema.SchemaBuilder;
import com.xivvic.args.schema.Text2Schema;
import com.xivvic.args.schema.item.Item;
import com.xivvic.args.schema.item.Item.Builder;

import lombok.Data;
import lombok.experimental.Accessors;
import xivvic.console.action.Action;
import xivvic.console.action.ActionBase;
import xivvic.console.action.ActionManager;
import xivvic.console.menu.Menu;
import xivvic.console.menu.MenuItem;
import xivvic.console.menu.MenuManager;
import xivvic.util.io.Stdio;

/**
 * This class creates the Schema menu and associated actions
 *
 * @author reid
 */
@Data
@Accessors(fluent = true)
public class InitializerSchemaMenu
	implements MenuCreator
{
	private final ActionManager am;
	private final MenuManager mm;
	private final Stdio stdin;

	public InitializerSchemaMenu(ActionManager am, MenuManager mm, Stdio stdin)
	{
		this.am    = am;
		this.mm    = mm;
		this.stdin = stdin;
	}

	public Menu createMenu(ArgsBench bench)
	{
		final String name = "Schema";
		Menu menu = new Menu(name, "sm", mm);

		menu.addItem(showCurrentSchema(bench));
		menu.addItem(showExampleSchema(bench));
		menu.addItem(replaceExistingSchemaWithExample(bench));
		menu.addItem(addNewSchemaItem(bench));
		menu.addItem(removeSchemaItem(bench));
		menu.addItem(removeAllExceptOne(bench));

		return menu;
	}
	private MenuItem showCurrentSchema(ArgsBench bench)
	{
		String label = "Show current schema";
		String   def = "Displays the currently defined schema";
		Action     a = new ActionBase(label, def, true) {

			@Override
			protected void internal_invoke(Object param)
			{
				Schema schema = bench.schema();
				System.out.println(schema);
			}
		};
		return new MenuItem(label, "sc", a);
	}

	private MenuItem showExampleSchema(ArgsBench bench)
	{
		String label = "Show example schema";
		String   def = "Display the text file representation of a basic set of example schema definitions";
		Action     a = new ActionBase(label, def, true) {

			@Override
			protected void internal_invoke(Object param)
			{
				String defs = bench.getOptionDefinitions();
				System.out.println(defs);
			}
		};
		MenuItem item = new MenuItem(def, "ex", a);
		return item;
	}

	private MenuItem replaceExistingSchemaWithExample(ArgsBench bench)
	{
		String label = "Replace schema with example";
		String   def = "Replaces the current schema with the example schema";
		Action     a = new ActionBase(label, def, true) {

			@Override
			protected void internal_invoke(Object param)
			{
				String defs = bench.getOptionDefinitions();
				Text2Schema t2s = new Text2Schema();
				try
				{
					Schema schema = t2s.createSchema(defs);
					bench.schema(schema);
				}
				catch (SchemaException e)
				{
					System.out.println("Failed to create schema: " + e.errorMessage());
				}
				System.out.println(bench.schema());
			}
		};
		return new MenuItem(label, "rx", a);
	}

	private MenuItem addNewSchemaItem(ArgsBench bench)
	{
		String label = "Add new schema item";
		String   def = "Define a new schema item and add it to the current set";
		Action     a = new ActionBase(label, def, true) {

			@Override
			protected void internal_invoke(Object param)
			{
				Map<String, String> defs = null;
				if (param == null)
				{
					ItemInteractor ii = new ItemInteractor(stdin);
					defs = ii.gatherItemDefinition();
				}
				else
				{
					String s = (String) param;
					System.err.println("Unable to process parameter [" + s + "] need more programmer foo");
					// FIXME: defs = parseIt(s);
				}

				try
				{
					Builder<?> builder = Item.builder(defs);
					Item<?> addition = builder.build();
					Schema schema = bench.schema();
					Schema replacement = schema.with(addition);
					bench.schema(replacement);
					System.out.println(addition + " added to schema");
				}
				catch (SchemaException e)
				{
					System.err.println("Failed to add new schema item " + e.errorMessage());
				}

			}
		};
		return new MenuItem(label, "a", a);
	}

	private MenuItem removeSchemaItem(ArgsBench bench)
	{
		String label = "Remove schema item";
		String   def = "Removes schema item from the current schema definition";
		Action     a = new ActionBase(label, def, true) {

			@Override
			protected void internal_invoke(Object param)
			{
				String target = null;
				Schema schema = bench.schema();
				if (param == null)
				{
					List<String> options = schema.getOptions();
					target = stdin.getStringFromList(options, "Remove which option?");
				}
				else
				{
					target = (String) param;
				}
				Schema  empty = schema.without(target);
				bench.schema(empty);
				System.out.println(target + " removed frmo schema");
			}
		};
		return new MenuItem(label, "rm", a);
	}

	private MenuItem removeAllExceptOne(ArgsBench bench)
	{
		String label = "Reduce to single item";
		String   def = "Eliminate all but one item from the current schema";
		Action     a = new ActionBase(label, def, true) {

			@Override
			protected void internal_invoke(Object param)
			{
				Schema schema = bench.schema();
				List<String> items = schema.getOptions();

				if (items.size() == 1)
				{
					System.out.println("There is only one item in the schema. Nothing to remove");
					return;
				}

				String prompt = "Which option should remain? ";
				String survivor = stdin.getStringFromListWithDefault(items, prompt, 0);

				Item<?> item = schema.getItem(survivor);
				SchemaBuilder builder = new SchemaBuilder();
				builder.item(item);
				Schema replacement;
				try
				{
					replacement = builder.build();
					bench.schema(replacement);
				}
				catch (SchemaException e)
				{
					System.out.println("Failed to reduce schema to " + survivor + ".");
					System.out.println(e.errorMessage());
					return;
				}
				System.out.println("Schema	reduced to " + survivor + ".");
			}
		};
		MenuItem item = new MenuItem(def, "clr", a);
		return item;
	}
}
