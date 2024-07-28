import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Survey implements Serializable {
	private static final long serialVersionUID = 1L;
	protected final List<Question> questions; // store the list of questions for the questionnaire
	protected String name; // store the name of the questionnaire
	protected transient InputParser inputParser; // store the input parser

	public Survey(String name, InputParser inputParser) {
		this.name = name;
		questions = new ArrayList<>();
		this.inputParser = inputParser;
	}

	public void setInputParser(InputParser inputParser) {
		this.inputParser = inputParser;
	}

	public void createQuestions() {

		// create questions in the questionnaire

		// keep prompting user to create questions until they are done
		boolean isCreatingQuestions = true;

		while (isCreatingQuestions) {

			// display the create question menu and get user choice
			int createSurveyChoice = inputParser.getCreateQuestionnaireChoice();

			System.out.println();

			switch (createSurveyChoice) {
			case 1: // add new true-false question

				TrueFalse trueFalse = new TrueFalse(
						inputParser.getStringInput("Enter the prompt for your True/False question:"));
				addQuestion(trueFalse);
				break;
			case 2: // add new multiple-choice question

				List<String> createMultipleChoiceParameters = inputParser.getCreateMultipleChoiceInput();

				MultipleChoice multipleChoice = new MultipleChoice(createMultipleChoiceParameters.get(0),
						inputParser.getIntegerChoiceWithinRange(
								"Enter the number of choices the taker has to give for this question:", 1, 26));

				// add choices to the question
				for (int i = 1; i < createMultipleChoiceParameters.size(); i++) {
					multipleChoice.addChoice(createMultipleChoiceParameters.get(i));
				}

				addQuestion(multipleChoice);
				break;
			case 3: // add a new short answer question

				ShortAnswer shortAnswer = new ShortAnswer(
						inputParser.getStringInput("Enter the prompt for your short answer question:"),
						inputParser.getIntegerChoiceWithinRange("Enter the number of responses this question requires:",
								1, 26),
						inputParser.getIntegerChoiceWithinRange(
								"Enter the max number of characters a response to this question can have:", 1, 500));
				addQuestion(shortAnswer);
				break;
			case 4: // add a new essay question

				Essay essay = new Essay(inputParser.getStringInput("Enter the prompt for your essay question:"),
						inputParser.getIntegerChoiceWithinRange(
								"Enter the number of paragraphs this question requires:", 1, 26));
				addQuestion(essay);
				break;
			case 5: // add a new date question

				ValidDate dateQuestion = new ValidDate(
						inputParser.getStringInput("Enter the prompt for your date question:"),
						inputParser.getIntegerChoiceWithinRange("Enter the number of dates this question requires:", 1,
								26));
				addQuestion(dateQuestion);
				break;
			case 6: // add a new matching question

				// get all the parameters for creating a matching question from the user
				List<String> createMatchingParameters = inputParser.getCreateMatchingInput();

				// get the number of left and right options from the parameters
				int numLeftOptions = Integer.parseInt(createMatchingParameters.get(1));
				int numRightOptions = Integer.parseInt(createMatchingParameters.get(2));

				Matching matching = new Matching(createMatchingParameters.get(0), numLeftOptions);

				// for each left option
				for (int i = 3; i < 3 + numLeftOptions; i++) {

					// add it to the list
					matching.addToLeftChoices(createMatchingParameters.get(i));
				}

				// for each right option
				for (int i = 3 + numLeftOptions; i < 3 + numLeftOptions + numRightOptions; i++) {

					// add it to the list
					matching.addToRightChoices(createMatchingParameters.get(i));
				}

				addQuestion(matching);

				break;
			default:
				isCreatingQuestions = false;
			}
		}
	}

	// get the question based on the question number (not the index)
	public Question getQuestion(int questionNumber) {
		return questions.get(questionNumber - 1);
	}

	// add the question to the end of the list
	public void addQuestion(Question question) {
		questions.add(question);
	}

	// return the list of questions
	public List<Question> getQuestions() {
		return questions;
	}

	public void display() {

		// displaying the questionnaire

		// keep track of the current question number
		int questionNumber = 1;

		// print name of the questionnaire
		System.out.println(name);

		// for each question
		for (Question question : getQuestions()) {

			// display the question
			System.out.printf("%d) ", questionNumber++);
			question.displayQuestion();
			System.out.println();
		}
	}

	public void take() {

		// take the questionnaire

		boolean added; // check whether the user answer is added successfully

		int questionNumber = 1; // keep track of the current question number

		System.out.println(name); // display name of the questionnaire first

		// for each question
		for (Question question : questions) {

			added = false;

			// display question
			System.out.printf("%d) ", questionNumber++);
			question.displayQuestion();

			// keep asking for more answers until the number of answers requirement has been
			// reached
			while (!question.isMaxNumberOfUserAnswersReached()) {

				// keep asking for answer until the user provides a valid answer to the question
				while (!added) {
					String answer = inputParser.getStringInput("");
					added = question.addUserAnswer(answer);
				}
				added = false;
			}

			System.out.println();

		}
	}

	public void modify() {
		// modify the questionnaire

		// display questionnaire name and ask user if they want to change it
		System.out.println(name);

		// if the user says yes to changing the survey name
		if (inputParser.getYesNoInput("Do you wish to change the survey name? (Yes/No)")) {

			// then, prompt the user for a new name and change it
			setName(inputParser.getStringInput("Enter the new name:"));
		}

		// get the question number that the user wants to modify
		int questionNumber = inputParser.getIntegerChoiceWithinRange(
				"What question do you wish to modify? Enter the question number:", 1, getQuestions().size());

		// get the question and then modify the question
		Question question = getQuestion(questionNumber);

		question.modifyQuestion(inputParser);

	}

	// get name of the questionnaire
	public String getName() {
		return name;
	}

	// set the name of the questionnaire
	public void setName(String name) {
		this.name = name;
	}

	// clear all answers from all questions
	public void clearAllAnswers() {
		for (Question question : questions) {
			question.clearAnswer();
		}
	}

	// get all answers for all questions
	public List<Answer> getAnswers() {

		// initialize list to store all answers for each question
		List<Answer> answers = new ArrayList<>();

		// for each question
		for (Question question : questions) {
			// add the answers for that question to the list
			answers.add(question.getUserAnswers());
		}

		return answers;
	}

	// tabulate all responses for the questionnaire
	public void tabulate(List<Survey> surveys) {

		// for each question
		for (int questionNumber = 1; questionNumber <= questions.size(); questionNumber++) {

			// display the question
			System.out.println("Question:");
			getQuestion(questionNumber).displayQuestion();
			System.out.println();

			// display the responses
			System.out.println("Responses:");

			// hashmap to store the different responses with their count
			HashMap<String, Integer> answersTabulator = new HashMap<>();

			// for each survey response
			for (Survey survey : surveys) {

				// get the current survey's response to the question
				String answer = survey.getQuestion(questionNumber).getUserAnswers().getUserAnswers().stream()
						.map(String::trim).sorted(String::compareToIgnoreCase).collect(Collectors.joining("\n"));

				// print the response
				System.out.println(answer);
				System.out.println();

				// accumulate the response in the hashmap to tabulate later
				answersTabulator.compute(answer, (key, value) -> (value == null) ? 1 : value + 1);

			}

			// display the tabulation
			System.out.println("Tabulation:");

			// for each response and count of the response
			for (Map.Entry<String, Integer> answerEntry : answersTabulator.entrySet()) {

				// get the response and the count and print them
				String answer = answerEntry.getKey();
				int count = answerEntry.getValue();

				System.out.println("Count: " + count);
				System.out.println(answer);
				System.out.println();
			}
		}
	}
}
