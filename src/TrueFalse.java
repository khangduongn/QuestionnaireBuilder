public class TrueFalse extends MultipleChoice {

	private static final long serialVersionUID = 1L;

	public TrueFalse(String prompt) {
		super(prompt, 1);
		choices.add("True");
		choices.add("False");
	}

	@Override
	public boolean addUserAnswer(String answer) {

		// validate and add the user answer

		// check if the answer is True or False
		if (answer.equalsIgnoreCase(choices.get(0)) || answer.equalsIgnoreCase(choices.get(1))) {
			// if so, add the answer
			userAnswers.addUserAnswer(answer.substring(0, 1).toUpperCase() + answer.substring(1).toLowerCase());
			return true;
		}

		// otherwise, print error message
		System.out.printf("Invalid Answer. Please enter either %s or %s.\n", choices.get(0), choices.get(1));
		return false;
	}

	@Override
	public void displayQuestion() {
		// display the question
		// the prompt first
		System.out.println(prompt);

		// then display the True/False options
		System.out.printf("%s/%s\n", choices.get(0), choices.get(1));
	}

	@Override
	public void modifyQuestion(InputParser inputParser) {
		// display prompt first
		System.out.println("Prompt: " + getPrompt());

		// if the user answers "Yes" to modifying the prompt, then modify the prompt of
		// the question
		if (inputParser.getYesNoInput("Do you wish to modify the prompt? (Yes/No)")) {

			String newPrompt = inputParser.getStringInput("\nCurrent Prompt: " + getPrompt() + "\nEnter a new prompt:");
			setPrompt(newPrompt);
		}
	}
}
