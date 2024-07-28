import java.util.ArrayList;
import java.util.List;

public class Matching extends Question {

	private static final long serialVersionUID = 1L;
	protected List<String> leftChoices; // store the clues (left column choices)
	protected List<String> rightChoices; // store the matches (right column choices)

	public Matching(String prompt, int numberOfUserAnswersRequired) {
		super(prompt, numberOfUserAnswersRequired);
		leftChoices = new ArrayList<>();
		rightChoices = new ArrayList<>();

	}

	// get all the clues (left column choices)
	public List<String> getLeftChoices() {
		return leftChoices;
	}

	// get all the matches (right column choices)
	public List<String> getRightChoices() {
		return rightChoices;
	}

	// add a clue (add a choice to left column choices)
	public void addToLeftChoices(String choice) {
		leftChoices.add(choice);
	}

	// add a match (add a choice to right column choices)
	public void addToRightChoices(String choice) {
		rightChoices.add(choice);
	}

	// set a clue by the index (set a choice to left column choices using the index)
	public void setLeftChoiceByIndex(String choice, int index) {
		leftChoices.set(index, choice);
	}

	// set a match by the index (set a choice to right column choices using the
	// index)
	public void setRightChoiceByIndex(String choice, int index) {
		rightChoices.set(index, choice);
	}

	// display the left and right column choices
	public void displayChoices() {

		// initialize the starting alphabet and number for the left and right columns,
		// respectively
		char alphabet = 'A';
		int choiceNumber = 1;

		// display the choices nicely in columns
		int maxNumberOfChoices = Math.max(leftChoices.size(), rightChoices.size());

		// display the choices
		for (int i = 0; i < maxNumberOfChoices; i++) {

			System.out.printf("%-50s %s\n", i < leftChoices.size() ? alphabet++ + ") " + leftChoices.get(i) : "",
					i < rightChoices.size() ? choiceNumber++ + ") " + rightChoices.get(i) : "");
		}
	}

	@Override
	public void displayQuestion() {

		// display the question

		// first display the prompt and then some notes about the question
		System.out.println(prompt);
		System.out.println(
				"Each option in the left column must have a corresponding match in the right column. Multiple options in the left column may have the same matches in the right column.");
		System.out.println(
				"Enter each clue and match pair in the format <clueAlphabet> <matchNumber>. Make sure to leave a space in between. One pair per line.");

		// then display the two column choices
		displayChoices();
	}

	@Override
	public boolean addUserAnswer(String answer) {

		// validate and then add user answer

		answer = answer.toUpperCase();

		try {
			// split the answer by the space
			String[] answerPortions = answer.split(" ");

			// get the left choice (alphabet)
			String leftChoice = answerPortions[0];

			// get the right choice (number)
			int rightChoice = Integer.parseInt(answerPortions[1]);

			// validate that the alphabet and number are valid and within the range
			if ((answerPortions.length == 2) && leftChoice.matches("[A-Z]")
					&& (leftChoice.charAt(0) - 'A' <= leftChoices.size() - 1)
					&& (rightChoice >= 1 && rightChoice <= rightChoices.size())) {

				// check that the clue and match pair doesn't contain a repeat clue since each
				// clue must have a match
				boolean foundPreviousClue = false;

				for (String previousAnswer : userAnswers.getUserAnswers()) {
					if (previousAnswer.contains(leftChoice)) {
						foundPreviousClue = true;
						break;
					}
				}

				// if a duplicate clue was found then print error and return false
				if (foundPreviousClue) {
					System.out.println(
							"This clue already has a match. Please choose a different clue that does not have a match yet.");
					return false;
				}

				// otherwise, add the answer and return true
				userAnswers.addUserAnswer(answer);
				return true;
			}

			// print error if the formatting is not correct
			System.out.println(
					"Invalid pair format. Please enter the pair in the form <clueAlphabet> <matchNumber> with a space in between. Make sure the choices you enter are within the available choices.");

		} catch (Exception e) {
			System.out.println(
					"Invalid pair format. Please enter the pair in the form <clueAlphabet> <matchNumber> with a space in between.");
		}

		return false;

	}

	@Override
	public void modifyQuestion(InputParser inputParser) {

		// modify the question

		// first modify the prompt of the question
		super.modifyQuestion(inputParser);

		// ask user if they want to modify the choices in the left column and keep
		// prompting user to modify left column until they are done
		boolean modifyingLeftChoices = true;

		while (modifyingLeftChoices) {

			// ask user if they want to modify the left choices
			if (inputParser.getYesNoInput("Do you wish to modify the clues (left column)? (Yes/No)")) {

				// if so, ask for the choice that they want to modify and then change its text
				System.out.println(
						"What clue do you want to modify? Enter the alphabetical letter that corresponds with the clue:");
				displayChoices();

				int choiceIndex = inputParser.getAlphabetChoiceToNumberIndex(leftChoices.size());

				setLeftChoiceByIndex(inputParser.getStringInput("Enter the new text for the clue:"), choiceIndex);
			} else {
				modifyingLeftChoices = false;
			}
		}

		// ask user if they want to modify the choices in the right column and keep
		// prompting user to modify right column until they are done
		boolean modifyingRightChoices = true;

		while (modifyingRightChoices) {

			// ask user if they want to modify the right choices
			if (inputParser.getYesNoInput("Do you wish to modify the matches (right column)? (Yes/No)")) {

				// if so, ask for the choice that they want to modify and then change its text
				System.out
						.println("What match do you want to modify? Enter the number that corresponds with the match:");
				displayChoices();

				int choiceIndex = inputParser.getIntegerChoiceWithinRange("", 1, rightChoices.size());

				setRightChoiceByIndex(inputParser.getStringInput("Enter the new text for the match:"), choiceIndex - 1);
			} else {
				modifyingRightChoices = false;
			}
		}

		// update the number of required answers to reflect the size of the clues (as
		// each clue must have a match)
		setNumberOfUserAnswersRequired(leftChoices.size());

	}
}
