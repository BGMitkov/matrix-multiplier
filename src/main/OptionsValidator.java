package main;

import org.apache.commons.cli.*;

public class OptionsValidator {

	Options options;
	private String header = "Option 'i' and options 'm','n','k','r' are mutually exclusive. You shouldn't use them simultaneously.";

	public String getHeader() {
		return header;
	}

	public Options getOptions() {
		return options;
	}

	public OptionsValidator() {

		this.options = new Options();
		/*
		 * OptionGroup optionGroup1 = new OptionGroup(); OptionGroup
		 * optionGroup2 = new OptionGroup(); OptionGroup optionGroup3 = new
		 * OptionGroup(); OptionGroup optionGroup4 = new OptionGroup();
		 */

		options.addOption("m", true, "Number of rows for the left A matrix");
		options.addOption("n", true,
				"Number of columns for the left A matrix and rows for the right B");
		options.addOption("k", true, "Number of columns for the right B matrix");
		options.addOption("i", true,
				"Presents a file that contains matrices A and B");
		options.addOption("o", true,
				"Presents a file to which the result of the multiplication will be stored");
		options.addOption(Option.builder("t").longOpt("threads").hasArg()
				.desc("Number of threads in current run").required().build());
		options.addOption("q", false, "Displays only the time for calculating");
		options.addOption("r", true, "Sets the number range from 0 to r");
		options.addOption("e", true, "Exports the generated matrices");

		/*
		 * optionGroup1.addOption(options.getOption("i"));
		 * optionGroup1.addOption(options.getOption("m"));
		 * optionGroup1.setRequired(true);
		 * 
		 * optionGroup2.addOption(options.getOption("i"));
		 * optionGroup2.addOption(options.getOption("n"));
		 * optionGroup2.setRequired(true);
		 * 
		 * optionGroup3.addOption(options.getOption("i"));
		 * optionGroup3.addOption(options.getOption("k"));
		 * optionGroup3.setRequired(true);
		 * 
		 * optionGroup4.addOption(options.getOption("i"));
		 * optionGroup4.addOption(options.getOption("r"));
		 * optionGroup4.setRequired(true);
		 * 
		 * options.addOptionGroup(optionGroup1);
		 * options.addOptionGroup(optionGroup2);
		 * options.addOptionGroup(optionGroup3);
		 * options.addOptionGroup(optionGroup4);
		 */
	}

	public CommandLine parseOptions(String[] args) {

		CommandLine parsedOptions;
		CommandLineParser parser = new DefaultParser();

		try {

			parsedOptions = parser.parse(options, args);

		} catch (ParseException e) {

			new HelpFormatter().printHelp("java " + this.getClass().getName(),
					getHeader(), options, "\n" + e.getMessage(), true);

			return null;

		}

		return parsedOptions;
	}

}
