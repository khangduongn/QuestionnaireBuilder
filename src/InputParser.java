import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InputParser {

	private final Scanner input;

	public InputParser() {
		input = new Scanner(System.in);
	}

	// display the questionnaire type menu and get the user input/choice
	public int getQuestionnaireTypeChoice() {

		System.out.println();
		System.out.println("1) Survey");
		System.out.println("2) Test");
		System.out.println("3) Quit");

		return getIntegerChoiceWithinRange("", 1, 3);
	}

	// display the manage survey menu and get the user input/choice
	public int getSurveyMenuChoice() {

		System.out.println();
		System.out.println("1) Create a new Survey");
		System.out.println("2) Display an existing Survey");
		System.out.println("3) Load an existing Survey");
		System.out.println("4) Save the current Survey");
		System.out.println("5) Take the current Survey");
		System.out.println("6) Modify the current Survey");
		System.out.println("7) Tabulate a survey");
		System.out.println("8) Return to previous menu");

		return getIntegerChoiceWithinRange("", 1, 8);
	}

	// display the manage test menu and get the user input/choice
	public int getTestMenuChoice() {

		System.out.println();
		System.out.println("1) Create a new Test");
		System.out.println("2) Display an existing Test without correct answers");
		System.out.println("3) Display an existing Test with correct answers");
		System.out.println("4) Load an existing Test");
		System.out.println("5) Save the current Test");
		System.out.println("6) Take the current Test");
		System.out.println("7) Modify the current Test");
		System.out.println("8) Tabulate a Test");
		System.out.println("9) Grade a Test");
		System.out.println("10) Return to previous menu");

		return getIntegerChoiceWithinRange("", 1, 10);
	}

	// display the create questionnaire menu and get the user input/choice
	public int getCreateQuestionnaireChoice() {

		System.out.println();
		System.out.println("1) Add a new T/F question");
		System.out.println("2) Add a new multiple-choice question");
		System.out.println("3) Add a new short answer question");
		System.out.println("4) Add a new essay question");
		System.out.println("5) Add a new date question ");
		System.out.println("6) Add a new matching question");
		System.out.println("7) Return to previous menu");

		return getIntegerChoiceWithinRange("", 1, 7);
	}

	// prompts the user to choose a number between <low> and <high> inclusive
	public int getIntegerChoiceWithinRange(String prompt, int low, int high) {

		// if the prompt is not null or not empty
		if (prompt != null && !prompt.trim().isEmpty()) {

			// then print the prompt
			System.out.println(prompt);
		}

		// keep prompting user to enter a valid number between the range until they
		// enter a valid number
		boolean isValidInput = false;
		int choice = high;

		while (!isValidInput) {
			try {
				choice = input.nextInt();

				// ensure that the user's choice is in the range, otherwise show error message
				if (choice >= low && choice <= high) {
					isValidInput = true;
				} else {

					if (low == high) {
						System.out.printf("Invalid Choice. Please select %d.\n", low);
					} else {
						System.out.printf("Invalid Choice. Please enter a number between %d and %d (inclusive).\n", low,
								high);
					}

				}

			} catch (java.util.InputMismatchException e) {
				// display error message for when user enters a non number
				System.out.println("Invalid Choice. Please enter a number.");
				input.nextLine();
			}
		}
		input.nextLine();

		return choice;
	}

	// prompts the user to create a matching question with choices and gathers
	// all user inputs into one list of strings for processing
	public List<String> getCreateMatchingInput() {

		// initialize list
		List<String> createMatchingParameters = new ArrayList<>();

		// get the prompt for the question and add it to list
		createMatchingParameters.add(getStringInput("Enter the prompt for your matching question:"));

		System.out.println(
				"Note: One clue has exactly one match. Multiple clues can have the same match. You can have equal, more, or less number of matches than the number of clues.");

		// get the number of left and right options for the matching question from the
		// user and add them to the list
		int numberOfLeftOptions = getIntegerChoiceWithinRange("Enter the number of clues to have (left column):", 2,
				26);

		int numberOfRightOptions = getIntegerChoiceWithinRange("Enter the number of matches to have (right column):", 2,
				26);

		createMatchingParameters.add(Integer.toString(numberOfLeftOptions));
		createMatchingParameters.add(Integer.toString(numberOfRightOptions));

		char alphabet = 'A';

		// for each left option, get the text and add it to the list
		for (int i = 0; i < numberOfLeftOptions; i++) {

			createMatchingParameters
					.add(getStringInput(String.format("Enter clue (left option) #%d (%c)", i + 1, alphabet++)));
		}

		// for each right option, get the text and add it to the list
		for (int i = 0; i < numberOfRightOptions; i++) {

			createMatchingParameters.add(getStringInput(String.format("Enter match (right option) #%d", i + 1)));

		}

		return createMatchingParameters;
	}

	// prompts the user to create multiple choice question with choices and gathers
	// all user inputs into one list of strings for processing
	public List<String> getCreateMultipleChoiceInput() {

		// initialize list
		List<String> createMultipleChoiceParameters = new ArrayList<>();

		// add the prompt of the question
		createMultipleChoiceParameters.add(getStringInput("Enter the prompt for your multiple-choice question:"));

		// get the number of choices for the question from the user
		int numberOfChoices = getIntegerChoiceWithinRange(
				"Enter the number of choices for your multiple-choice question:", 2, 26);

		char alphabet = 'A';

		// iterate through each choice and gather the text for them
		for (int i = 0; i < numberOfChoices; i++) {

			// add to list
			createMultipleChoiceParameters
					.add(getStringInput(String.format("Enter choice #%d (%c)", i + 1, alphabet++)));

		}

		return createMultipleChoiceParameters;
	}

	// prompts the user to answer yes or no and returns a boolean
	// true for yes and false for no
	public boolean getYesNoInput(String prompt) {
		System.out.println(prompt);

		// keep prompting user to enter input until they enter yes or no
		while (true) {
			String userInput = input.nextLine().trim();

			if (userInput.equalsIgnoreCase("yes")) {
				return true;
			} else if (userInput.equalsIgnoreCase("no")) {
				return false;
			} else {
				System.out.println("Invalid Input. Please enter yes or no.");
			}
		}
	}

	// prompts the user to answer True or False, validates the input, and returns a
	// string with either True or False
	public String getTrueFalse(String prompt) {
		System.out.println(prompt);

		// keep prompting user to enter input until they enter True or False
		while (true) {
			String userInput = input.nextLine().trim();

			if (userInput.equalsIgnoreCase("true") || userInput.equalsIgnoreCase("false")) {

				return userInput.substring(0, 1).toUpperCase() + userInput.substring(1).toLowerCase();
			} else {
				System.out.println("Invalid Input. Please enter True or False.");
			}
		}
	}

	// prompts the user to enter a string input
	public String getStringInput(String prompt) {

		// print the prompt only if it is not empty and not null
		if (prompt != null && !prompt.trim().isEmpty()) {
			System.out.println(prompt);
		}

		// the user must enter a non empty string otherwise it will keep prompting the
		// user to enter again
		while (true) {
			String userInput = input.nextLine().trim();

			if (!userInput.isEmpty()) {
				return userInput;
			}

			System.out.println("Invalid input. The input provided was empty.");
		}
	}

	// prompts the user to enter an alphabet letter and converts that to an index
	// (A = 0, B = 1, ...)
	public int getAlphabetChoiceToNumberIndex(int numChoices) {
		boolean isValidInput = false;
		char alphabet = 'a';
		int index = 0;

		// keep prompting user to enter an alphabet letter until it is within the range
		// and is valid
		while (!isValidInput) {

			String userInput = input.nextLine().trim().toLowerCase();
			if (userInput.matches("[a-z]")) {

				// if the alphabet is outside the range, then show error message
				if ((userInput.charAt(0) - alphabet + 1) > numChoices) {
					System.out.println("Invalid choice. Please enter a choice that within the question.");
				} else {
					// otherwise, convert alphabet to index
					isValidInput = true;
					index = userInput.charAt(0) - alphabet;
				}
			} else {
				// show error message for when the user enters a non-alphabet character
				System.out
						.println("Invalid choice. Please enter an alphabetical letter that corresponds to the choice.");
			}
		}

		return index;
	}

	// prompts the user to enter an alphabet letter and checks to make sure that the
	// alphabet choice is within the correct range
	public String getAlphabetChoiceWithinRange(int numChoices) {

		char alphabet = 'A';

		// keep prompting user to enter an alphabet letter until it is within the range
		// and is valid
		while (true) {

			String userInput = input.nextLine().trim().toUpperCase();

			// check that the input is an alphabet letter
			if (userInput.matches("[A-Z]")) {

				// if the alphabet is outside the range, then show error message
				if ((userInput.charAt(0) - alphabet + 1) > numChoices) {
					System.out.println("Invalid choice. Please enter a choice that within the question.");
				} else {
					// otherwise return the input
					return userInput;
				}
			} else {
				// show error message for when the user enters a non-alphabet character
				System.out
						.println("Invalid choice. Please enter an alphabetical letter that corresponds to the choice.");
			}
		}
	}

	// prompts the user to enter their selection from the file menu and converts
	// their numeric selection to the corresponding filename in the list
	public String getFileSelection(String prompt, File[] files) {

		// keeps track of the file number
		int fileNumber = 1;

		// show menu of available files to select
		System.out.println(prompt);
		for (File file : files) {
			System.out.printf("%d) %s\n", fileNumber++, file.getName());
		}

		// prompt user to enter number that is in the menu and use that number as index
		// to get the filename and path
		return files[getIntegerChoiceWithinRange("", 1, files.length) - 1].getPath();
	}
}
