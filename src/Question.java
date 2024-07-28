import java.io.Serializable;

public abstract class Question implements Serializable {

	private static final long serialVersionUID = 1L;
	protected String prompt; // stores the prompt of the question
	protected Answer userAnswers; // stores list of user answers
	protected int numberOfUserAnswersRequired; // stores number of required responses/answers

	public Question(String prompt, int numberOfUserAnswersRequired) {
		this.prompt = prompt;
		this.userAnswers = new Answer(); // initialize the answer
		this.numberOfUserAnswersRequired = numberOfUserAnswersRequired;
	}

	// check if the number of responses/answers is reached (true for yes and false
	// for no)
	public boolean isMaxNumberOfUserAnswersReached() {
		return userAnswers.getNumberOfUserAnswers() == numberOfUserAnswersRequired;
	}

	// get all user answers for this questions
	public Answer getUserAnswers() {
		return userAnswers;
	}

	// add the user's answer for the question and return true if successful
	public boolean addUserAnswer(String answer) {
		userAnswers.addUserAnswer(answer);
		return true;
	}

	// get the question's prompt
	public String getPrompt() {
		return prompt;
	}

	// set the question's prompt
	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	// abstract method for displaying the question
	public abstract void displayQuestion();

	// get the number of required responses/answers
	public int getNumberOfUserAnswersRequired() {
		return numberOfUserAnswersRequired;
	}

	// set the number of required responses/answers
	public void setNumberOfUserAnswersRequired(int numberOfUserAnswersRequired) {
		this.numberOfUserAnswersRequired = numberOfUserAnswersRequired;
	}

	// modify the question
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

	// clear all answers for the question
	public void clearAnswer() {
		userAnswers.clearAnswers();
	}
}
