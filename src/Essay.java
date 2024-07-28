public class Essay extends Question {

	private static final long serialVersionUID = 1L;

	public Essay(String prompt, int numberOfUserAnswersRequired) {
		super(prompt, numberOfUserAnswersRequired);
	}

	@Override
	public void displayQuestion() {
		// display the question
		System.out.println(prompt);
		System.out.printf("Please enter %d paragraph(s).\n", numberOfUserAnswersRequired);
	}

	@Override
	public void modifyQuestion(InputParser inputParser) {

		// modify the question

		// ask user to modify the prompt of thq question
		super.modifyQuestion(inputParser);

		// ask user if they want to change the number of paragraphs required for this
		// question
		System.out.printf("Current number of paragraphs this question requires: %d\n", numberOfUserAnswersRequired);
		if (inputParser
				.getYesNoInput("Do you want to change the number of paragraphs the taker has to give? (Yes/No)")) {

			// if user wants to change the number of paragraphs required, then change the
			// parameter based on their input
			setNumberOfUserAnswersRequired(inputParser
					.getIntegerChoiceWithinRange("Enter the number of paragraphs this question requires:", 1, 26));

		}
	}
}
