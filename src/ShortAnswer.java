public class ShortAnswer extends Essay {

	private static final long serialVersionUID = 1L;
	protected int characterLimit; // stores the character limit requirement for the question

	public ShortAnswer(String prompt, int numberOfUserAnswersRequired, int characterLimit) {
		super(prompt, numberOfUserAnswersRequired);
		this.characterLimit = characterLimit; // set character limit for the question
	}

	@Override
	public void displayQuestion() {
		// display the question
		System.out.println(prompt);
		System.out.printf(
				"Please enter %d responses(s) and ensure that each response contains %d characters or less.\n",
				numberOfUserAnswersRequired, characterLimit);
	}

	// get the character limit
	public int getCharacterLimit() {
		return characterLimit;
	}

	// set the character limit
	public void setCharacterLimit(int characterLimit) {
		this.characterLimit = characterLimit;
	}

	@Override
	public boolean addUserAnswer(String answer) {

		// validate then add user answer

		// check if the answer longer than the character limit
		if (answer.length() > characterLimit) {

			// if so, print error message and return false
			System.out.printf("Answer is too long. Please write your answer in no more than %d characters\n",
					characterLimit);
			return false;
		}

		// otherwise, add the answer and return true
		userAnswers.addUserAnswer(answer);
		return true;
	}

	@Override
	public void modifyQuestion(InputParser inputParser) {

		// modify the question

		// prompt user to change the prompt of the question
		System.out.println("Prompt: " + getPrompt());

		// if the user answers "Yes" to modifying the prompt, then modify the prompt of
		// the question
		if (inputParser.getYesNoInput("Do you wish to modify the prompt? (Yes/No)")) {

			String newPrompt = inputParser.getStringInput("\nCurrent Prompt: " + getPrompt() + "\nEnter a new prompt:");
			setPrompt(newPrompt);
		}

		// prompt user to change the character limit for the question
		System.out.printf("Current character limit for this question: %d\n", characterLimit);
		if (inputParser.getYesNoInput("Do you want to change the character limit for this question? (Yes/No)")) {

			// if the user says yes to changing the character limit, then get new character
			// limit and set it
			setCharacterLimit(inputParser.getIntegerChoiceWithinRange(
					"Enter the max number of characters a response to this question can have:", 1, 500));

		}

		// prompt the user to change the number of responses for the question
		System.out.printf("Current number of responses this question requires: %d\n", numberOfUserAnswersRequired);
		if (inputParser
				.getYesNoInput("Do you want to change the number of responses the taker has to give? (Yes/No)")) {

			// if the user says yes, then prompt user to input new number and change the
			// number of required responses
			setNumberOfUserAnswersRequired(inputParser
					.getIntegerChoiceWithinRange("Enter the number of responses this question requires:", 1, 26));

		}
	}
}
