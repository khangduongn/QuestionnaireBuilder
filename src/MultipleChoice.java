import java.util.ArrayList;
import java.util.List;

public class MultipleChoice extends Question {

	private static final long serialVersionUID = 1L;
	protected List<String> choices; // store the choices for the question

	public MultipleChoice(String prompt, int numberOfChoicesRequired) {
		super(prompt, numberOfChoicesRequired);
		choices = new ArrayList<>();
	}

	// get the number of choices in the question
	public int getNumberOfChoices() {
		return choices.size();
	}

	// add a choice to the end of the list
	public void addChoice(String choice) {
		choices.add(choice);
	}

	// get all choices in the question
	public List<String> getChoices() {
		return choices;
	}

	// set the choices in the question
	public void setChoices(List<String> choices) {
		this.choices = choices;
	}

	// set the choice in the question by the index
	public void setChoicesByIndex(String choice, int index) {
		choices.set(index, choice);
	}

	@Override
	public boolean isMaxNumberOfUserAnswersReached() {

		// check if the required number of user answers is reached

		// get number of choices
		int numChoices = getNumberOfChoices();

		return userAnswers.getNumberOfUserAnswers() == Math.min(numChoices, numberOfUserAnswersRequired);
	}

	public void displayChoices() {

		// display the question

		// keep track of the choice's alphabet
		char alphabet = 'A';

		// for each choice
		for (String choice : choices) {

			// print the choice
			System.out.printf("%c) %s\t", alphabet++, choice);
		}
		System.out.println();
	}

	@Override
	public void displayQuestion() {

		// display the question

		// get number of choices
		int numChoices = getNumberOfChoices();

		// print prompt and then the note for the question
		System.out.println(prompt);
		System.out.printf("Please give %d choice(s).\n", Math.min(numChoices, numberOfUserAnswersRequired));

		// print the choices
		displayChoices();

	}

	@Override
	public boolean addUserAnswer(String answer) {

		// get number of choices
		int numChoices = getNumberOfChoices();

		answer = answer.toUpperCase();

		// check if the answer is an alphabet and is within the appropriate alphabet
		// range
		if (answer.matches("[A-Z]") && (answer.charAt(0) - 'A') <= numChoices - 1) {

			// check if choice is already chosen
			if (userAnswers.getUserAnswers().contains(answer)) {
				System.out.println("Invalid choice. Please select a choice that has not already been chosen.");
				return false;
			} else {
				// if choice is not already chosen, add the answer and return true
				userAnswers.addUserAnswer(answer);
				return true;
			}

		}

		// otherwise, print error message and return false
		System.out.println("Invalid choice. Please enter an alphabetical letter that corresponds to the choice.");

		return false;
	}

	@Override
	public void modifyQuestion(InputParser inputParser) {

		// modify the question

		// first prompt user to modify the prompt of the question
		super.modifyQuestion(inputParser);

		// keep prompting the user to modify the choices until they are done
		boolean modifyingChoices = true;

		while (modifyingChoices) {

			// ask user if they want to modify choices
			if (inputParser.getYesNoInput("Do you wish to modify choices? (Yes/No)")) {

				// if so, then ask what choice they want to modify and then modify it
				System.out.println("What choice do you want to modify?");
				displayChoices();

				int choiceIndex = inputParser.getAlphabetChoiceToNumberIndex(getNumberOfChoices());

				setChoicesByIndex(inputParser.getStringInput("Enter the new text for the choice:"), choiceIndex);
			} else {
				modifyingChoices = false;
			}
		}

		// prompt user to change the number of choices required by the question
		System.out.printf("Current number of choices this question requires: %d\n", numberOfUserAnswersRequired);
		if (inputParser.getYesNoInput("Do you want to change the number of choices the taker has to give? (Yes/No)")) {

			// if the user says yes to changing the number of choices, then change the
			// number of choices
			setNumberOfUserAnswersRequired(inputParser
					.getIntegerChoiceWithinRange("Enter the number of choices this question requires:", 1, 26));

		}
	}
}
