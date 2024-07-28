import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ValidDate extends Question {

	private static final long serialVersionUID = 1L;
	protected String dateFormat = "yyyy-MM-dd"; // the appropriate date format for all answers

	public ValidDate(String prompt, int numberOfUserAnswersRequired) {
		super(prompt, numberOfUserAnswersRequired);
	}

	@Override
	public void displayQuestion() {
		// display the prompt and then some notes to help the user know how to format
		// the answer
		System.out.println(prompt);
		System.out.printf("A date should be entered in the following format: %s\n", dateFormat.toUpperCase());
		System.out.printf("Please provide %d date(s).\n", numberOfUserAnswersRequired);
	}

	public boolean validateDate(String date) {

		// validate the date string

		// create a pattern to check that the answer meets the appropriate date format
		DateTimeFormatter pattern = DateTimeFormatter.ofPattern(dateFormat);

		// check user's answer formatting
		try {

			// if the formatting is correct (no
			// exception occurs) then return true
			LocalDate.parse(date, pattern);

		} catch (DateTimeParseException e) {
			System.out.printf("Invalid Date. Please enter date in the format: %s\n", dateFormat.toUpperCase());
			return false;
		}

		return true;
	}

	@Override
	public boolean addUserAnswer(String answer) {
		// add the user's answer

		// validate the date
		if (validateDate(answer)) {

			// if date is valid then add the answer to the user answers and return true
			userAnswers.addUserAnswer(answer);
			return true;
		}

		// return false if the user's answer is not formatted correctly
		return false;
	}

	@Override
	public void modifyQuestion(InputParser inputParser) {

		// modify the question

		// ask user to modify the prompt
		super.modifyQuestion(inputParser);

		// ask user to modify the number of dates the question requires
		System.out.printf("Current number of dates this question requires: %d\n", numberOfUserAnswersRequired);

		// if the user says yes
		if (inputParser.getYesNoInput("Do you want to change the number of dates the taker has to give? (Yes/No)")) {

			// then modify the number of required answers based on user input
			setNumberOfUserAnswersRequired(inputParser
					.getIntegerChoiceWithinRange("Enter the number of dates this question requires:", 1, 26));

		}
	}
}
