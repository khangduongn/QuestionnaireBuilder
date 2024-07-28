import java.util.ArrayList;
import java.util.List;

public class Test extends Survey {

	private static final long serialVersionUID = 1L;
	private List<Answer> correctAnswers; // store the correct answers for each question on the test
	private double grade; // store the grade the user got on the test

	public Test(String name, InputParser inputParser) {

		super(name, inputParser);
		this.correctAnswers = new ArrayList<>(); // initialize the correct answers list
		grade = 0; // initialize the grade at 0
	}

	@Override
	public void createQuestions() {

		// create questions in the test

		// keep prompting user to create questions until they are done
		boolean isCreatingQuestions = true;

		while (isCreatingQuestions) {

			// display the create question menu and get user choice
			int createTestChoice = inputParser.getCreateQuestionnaireChoice();

			System.out.println();

			switch (createTestChoice) {
			case 1: // add new true-false question

				TrueFalse trueFalse = new TrueFalse(
						inputParser.getStringInput("Enter the prompt for your True/False question:"));

				// get the correct answer from the user and add it to the correct answer entry
				// of thq question
				List<String> correctAnswer = new ArrayList<>();
				correctAnswer.add(inputParser.getTrueFalse("Enter the correct answer (True or False):"));
				addCorrectAnswer(correctAnswer);

				addQuestion(trueFalse);

				break;
			case 2: // add new multiple-choice question

				List<String> createMultipleChoiceParameters = inputParser.getCreateMultipleChoiceInput();

				int numberOfRequiredChoices = inputParser.getIntegerChoiceWithinRange(
						"Enter the number of choices the taker has to give for this question:", 1, 26);

				MultipleChoice multipleChoice = new MultipleChoice(createMultipleChoiceParameters.get(0),
						numberOfRequiredChoices);

				// counter for the number of choices in the multiple choice question
				int numChoices = 0;

				// add choices to the question
				for (int i = 1; i < createMultipleChoiceParameters.size(); i++) {
					multipleChoice.addChoice(createMultipleChoiceParameters.get(i));
					numChoices++;
				}

				// initialize list to store the correct choices for the question
				List<String> correctChoices = new ArrayList<>();

				// add the correct choices to the list
				for (int i = 0; i < numberOfRequiredChoices; i++) {
					System.out.printf("Enter correct choice #%d:\n", i + 1);
					correctChoices.add(inputParser.getAlphabetChoiceWithinRange(numChoices));
				}

				// add the correct choices to the correct answer entry for this question in the
				// test
				addCorrectAnswer(correctChoices);

				addQuestion(multipleChoice);
				break;
			case 3: // add a new short answer question

				String shortAnswerPrompt = inputParser
						.getStringInput("Enter the prompt for your short answer question:");

				int numberOfRequiredResponses = inputParser
						.getIntegerChoiceWithinRange("Enter the number of responses this question requires:", 1, 26);

				ShortAnswer shortAnswer = new ShortAnswer(shortAnswerPrompt, numberOfRequiredResponses,
						inputParser.getIntegerChoiceWithinRange(
								"Enter the max number of characters a response to this question can have:", 1, 500));

				// initialize list to store the correct responses for the question
				List<String> correctResponses = new ArrayList<>();

				// character limit for this short answer question
				int characterLimit = shortAnswer.getCharacterLimit();

				// add the correct responses to the list
				for (int i = 0; i < numberOfRequiredResponses; i++) {

					// keep prompting the user to enter the correct response until it is valid
					while (true) {

						// get the correct response from user
						String correctResponse = inputParser
								.getStringInput(String.format("Enter the correct response #%d:", i + 1));

						// check if the correct response is longer than the character limit
						if (correctResponse.length() > characterLimit) {

							// if so, print error message and continue prompting user to enter the correct
							// response until it is valid
							System.out.printf(
									"Correct response is too long. Please write your response in no more than %d characters\n",
									characterLimit);
							continue;
						}

						correctResponses.add(correctResponse);
						break;
					}
				}

				// add the correct responses to the correct answer entry for this question in
				// the test
				addCorrectAnswer(correctResponses);

				addQuestion(shortAnswer);
				break;
			case 4: // add a new essay question

				Essay essay = new Essay(inputParser.getStringInput("Enter the prompt for your essay question:"),
						inputParser.getIntegerChoiceWithinRange(
								"Enter the number of paragraphs this question requires:", 1, 26));

				// no correct answers for essay so add an empty list
				addCorrectAnswer(new ArrayList<>());

				// add question to test
				addQuestion(essay);

				break;
			case 5: // add a new date question

				String datePrompt = inputParser.getStringInput("Enter the prompt for your date question:");

				int numberOfRequiredDates = inputParser
						.getIntegerChoiceWithinRange("Enter the number of dates this question requires:", 1, 26);

				ValidDate dateQuestion = new ValidDate(datePrompt, numberOfRequiredDates);

				// initialize list to store the correct dates for the question
				List<String> correctDates = new ArrayList<>();

				// add the correct dates to the list
				for (int i = 0; i < numberOfRequiredDates; i++) {

					// get the correct date and validate the date until the number of correct dates
					// is reached
					while (true) {
						String date = inputParser.getStringInput(String.format("Enter the correct date #%d:", i + 1));

						if (dateQuestion.validateDate(date)) {
							correctDates.add(date);
							break;
						}
					}
				}

				// add the correct dates to the correct answer entry for this question in
				// the test
				addCorrectAnswer(correctDates);

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

				// initialize a list to store the correct answers (pairs) for the matching
				// question
				List<String> correctPairs = new ArrayList<>();

				// for each clue (left option)
				for (int i = 0; i < numLeftOptions; i++) {

					// validate the correct pair to make sure it fits the right formatting and there
					// are no duplicates
					while (true) {

						// get the correct pair
						String correctPair = inputParser
								.getStringInput(String.format("Enter the correct pair #%d:", i + 1));

						correctPair = correctPair.toUpperCase();

						try {
							// split the pair by the space
							String[] correctPairPortions = correctPair.split(" ");

							// get the left choice (alphabet)
							String leftChoice = correctPairPortions[0];

							// get the right choice (number)
							int rightChoice = Integer.parseInt(correctPairPortions[1]);

							// validate that the alphabet and number are valid and within the range
							if ((correctPairPortions.length == 2) && leftChoice.matches("[A-Z]")
									&& (leftChoice.charAt(0) - 'A' <= numLeftOptions - 1)
									&& (rightChoice >= 1 && rightChoice <= numRightOptions)) {

								// check that the clue and match pair doesn't contain a repeat clue since each
								// clue must have a match
								boolean foundPreviousClue = false;

								for (String previousPair : correctPairs) {
									if (previousPair.contains(leftChoice)) {
										foundPreviousClue = true;
										break;
									}
								}

								// if a duplicate clue was found then print error and continue asking for input
								if (foundPreviousClue) {
									System.out.println(
											"This clue already has a match. Please choose a different clue that does not have a match yet.");
								} else {
									// otherwise, add the correct pair to the list
									correctPairs.add(correctPair);
									break;
								}

							} else {
								// print error if the formatting is not correct
								System.out.println(
										"Invalid pair format. Please enter the pair in the form <clueAlphabet> <matchNumber> with a space in between. Make sure the choices you enter are within the available choices.");

							}

						} catch (Exception e) {
							System.out.println(
									"Invalid pair format. Please enter the pair in the form <clueAlphabet> <matchNumber> with a space in between.");
						}
					}

				}

				// add the correct pairs to the correct answer entry for this question
				addCorrectAnswer(correctPairs);

				addQuestion(matching);

				break;
			default:
				isCreatingQuestions = false;
			}
		}
	}

	public void displayWithCorrectAnswers() {
		// displaying the test with correct answers

		// keep track of the current question number
		int questionNumber = 1;

		// print name of the test
		System.out.println(name);

		// for each question
		for (Question question : getQuestions()) {

			// display the question
			System.out.printf("%d) ", questionNumber);
			question.displayQuestion();

			// get the number of correct answers for this question and print the starting
			// line based on the number
			int numCorrectAnswersForQuestion = correctAnswers.get(questionNumber - 1).getUserAnswers().size();
			if (numCorrectAnswersForQuestion == 1) {
				System.out.println("The correct answer:");
			} else if (numCorrectAnswersForQuestion > 1) {
				System.out.println("The correct answers:");
			} else {
				// no correct answers (essay question)
				// continue to the next question without display any correct answers
				System.out.println();
				questionNumber++;
				continue;
			}

			// display the correct answers for the question
			correctAnswers.get(questionNumber - 1).displayAnswer();

			System.out.println();

			questionNumber++;
		}

	}

	public void grade() {

		// initialize the grade to 0
		grade = 0;

		// get the number of points each question is worth
		double pointsPerQuestion = (double) 100 / questions.size();

		// initialize counter to keep track of the number of essay questions
		int numberOfEssayQuestions = 0;

		// for each question
		for (int i = 0; i < questions.size(); i++) {

			// check the response with the correct answer
			if (questions.get(i).getUserAnswers().compare(this.correctAnswers.get(i))) {
				// if the response and the correct answer are the same then add the question
				// point to the grade
				grade += pointsPerQuestion;
			}

			// if there is no correct answer for this question, then this question is an
			// essay question
			if (this.correctAnswers.get(i).getNumberOfUserAnswers() == 0) {
				// add to counter
				numberOfEssayQuestions++;
			}
		}

		// print the grade (depending on whether there were essay questions present in
		// the test
		if (numberOfEssayQuestions > 0) {
			System.out.printf(
					"You received a %.2f on the test. The test was worth 100.00 point, but only %.2f of those points could be auto graded because the test had %d essay question(s).\n",
					Math.min(grade, 100), (questions.size() - numberOfEssayQuestions) * pointsPerQuestion,
					numberOfEssayQuestions);
		} else {
			System.out.printf("You received a %.2f on the test. The test was worth 100.00 points.\n", grade);
		}

	}

	// set the correct answers for a question by the question number
	public void setCorrectAnswerByQuestionNumber(int questionNumber, List<String> correctAnswer) {
		correctAnswers.get(questionNumber - 1).setUserAnswer(correctAnswer);
	}

	// get the correct answers for all questions
	public List<Answer> getCorrectAnswers() {
		return correctAnswers;
	}

	// set the correct answers for all questions
	public void setCorrectAnswers(List<Answer> correctAnswers) {
		this.correctAnswers = correctAnswers;
	}

	// add the correct answers to the list for a question
	public void addCorrectAnswer(List<String> answer) {

		Answer correctAnswer = new Answer();
		correctAnswer.setUserAnswer(answer);
		correctAnswers.add(correctAnswer);
	}

	// get the grade on the test
	public double getGrade() {
		return grade;
	}

	// set the grade on the test
	public void setGrade(double grade) {
		this.grade = grade;
	}

	@Override
	public void modify() {
		// modify the test

		// display test name and ask user if they want to change it
		System.out.println(name);

		// if the user says yes to changing the test name
		if (inputParser.getYesNoInput("Do you wish to change the test name? (Yes/No)")) {

			// then, prompt the user for a new name and change it
			setName(inputParser.getStringInput("Enter the new name:"));
		}

		// get the question number that the user wants to modify
		int questionNumber = inputParser.getIntegerChoiceWithinRange(
				"What question do you wish to modify? Enter the question number:", 1, getQuestions().size());

		// get the question and then modify the question
		Question question = getQuestion(questionNumber);

		question.modifyQuestion(inputParser);

		// modify the correct answer for the question
		if (question instanceof TrueFalse) {
			// asks user if they want to change the correct answer
			// if the user says yes to changing the correct answer
			if (inputParser.getYesNoInput("Do you wish to change the correct answer? (Yes/No)")) {

				// initialize list to store correct answer
				List<String> correctAnswer = new ArrayList<>();

				// then, prompt the user for the new correct answer and change it
				correctAnswer.add(inputParser.getTrueFalse("Enter the new correct answer (True or False):"));

				// set the new correct answer for the question
				setCorrectAnswerByQuestionNumber(questionNumber, correctAnswer);
			}
		} else if (question instanceof MultipleChoice) {
			// asks user if they want to change the correct choices
			// if the user says yes to changing the correct choices
			if (inputParser.getYesNoInput("Do you wish to change the correct choices? (Yes/No)")) {

				// get the required number of choices for the question and the number of choices
				// total for the question
				int numberOfRequiredChoices = question.getNumberOfUserAnswersRequired();
				int numChoices = ((MultipleChoice) question).getNumberOfChoices();

				// initialize list to store the correct choices for the question
				List<String> correctChoices = new ArrayList<>();

				// add the correct choices to the list
				for (int i = 0; i < numberOfRequiredChoices; i++) {
					System.out.printf("Enter the new correct choice #%d:\n", i + 1);
					correctChoices.add(inputParser.getAlphabetChoiceWithinRange(numChoices));
				}

				// set the new correct choices for the question
				setCorrectAnswerByQuestionNumber(questionNumber, correctChoices);
			}
		} else if (question instanceof Matching) {
			// asks user if they want to change the correct pairs
			// if the user says yes to changing the correct pairs
			if (inputParser.getYesNoInput("Do you wish to change the correct pairs? (Yes/No)")) {

				// get the number of clues (left options)
				int numLeftOptions = ((Matching) question).getLeftChoices().size();

				// get the number of matches (right options)
				int numRightOptions = ((Matching) question).getRightChoices().size();

				// initialize a list to store the correct answers (pairs) for the matching
				// question
				List<String> correctPairs = new ArrayList<>();

				// for each clue (left option)
				for (int i = 0; i < numLeftOptions; i++) {

					// validate the correct pair to make sure it fits the right formatting and there
					// are no duplicates
					while (true) {

						// get the correct pair
						String correctPair = inputParser
								.getStringInput(String.format("Enter the new correct pair #%d:", i + 1));

						correctPair = correctPair.toUpperCase();

						try {
							// split the pair by the space
							String[] correctPairPortions = correctPair.split(" ");

							// get the left choice (alphabet)
							String leftChoice = correctPairPortions[0];

							// get the right choice (number)
							int rightChoice = Integer.parseInt(correctPairPortions[1]);

							// validate that the alphabet and number are valid and within the range
							if ((correctPairPortions.length == 2) && leftChoice.matches("[A-Z]")
									&& (leftChoice.charAt(0) - 'A' <= numLeftOptions - 1)
									&& (rightChoice >= 1 && rightChoice <= numRightOptions)) {

								// check that the clue and match pair doesn't contain a repeat clue since each
								// clue must have a match
								boolean foundPreviousClue = false;

								for (String previousPair : correctPairs) {
									if (previousPair.contains(leftChoice)) {
										foundPreviousClue = true;
										break;
									}
								}

								// if a duplicate clue was found then print error and continue asking for input
								if (foundPreviousClue) {
									System.out.println(
											"This clue already has a match. Please choose a different clue that does not have a match yet.");
								} else {
									// otherwise, add the correct pair to the list
									correctPairs.add(correctPair);
									break;
								}

							} else {
								// print error if the formatting is not correct
								System.out.println(
										"Invalid pair format. Please enter the pair in the form <clueAlphabet> <matchNumber> with a space in between. Make sure the choices you enter are within the available choices.");

							}

						} catch (Exception e) {
							System.out.println(
									"Invalid pair format. Please enter the pair in the form <clueAlphabet> <matchNumber> with a space in between.");
						}
					}

				}

				// set the new correct pairs for the question
				setCorrectAnswerByQuestionNumber(questionNumber, correctPairs);
			}
		} else if (question instanceof ShortAnswer) {
			// asks user if they want to change the correct response
			// if the user says yes to changing the correct response
			if (inputParser.getYesNoInput("Do you wish to change the correct response(s)? (Yes/No)")) {

				// get the character limit for the question
				int characterLimit = ((ShortAnswer) question).getCharacterLimit();

				// get the number of required responses for the question
				int numberOfRequiredResponses = question.getNumberOfUserAnswersRequired();

				// initialize list to store the correct responses for the question
				List<String> correctResponses = new ArrayList<>();

				// add the correct responses to the list
				for (int i = 0; i < numberOfRequiredResponses; i++) {

					// keep prompting the user to enter the correct response until it is valid
					while (true) {

						// get the new correct response from user
						String correctResponse = inputParser
								.getStringInput(String.format("Enter the new correct response #%d:", i + 1));

						// check if the correct response is longer than the character limit
						if (correctResponse.length() > characterLimit) {

							// if so, print error message and continue prompting user to enter the correct
							// response until it is valid
							System.out.printf(
									"Correct response is too long. Please write your response in no more than %d characters\n",
									characterLimit);
							continue;
						}

						correctResponses.add(correctResponse);
						break;
					}
				}

				// set the new correct responses for the question
				setCorrectAnswerByQuestionNumber(questionNumber, correctResponses);
			}
		} else if (question instanceof ValidDate) {
			// asks user if they want to change the correct dates
			// if the user says yes to changing the correct dates
			if (inputParser.getYesNoInput("Do you wish to change the correct date(s)? (Yes/No)")) {

				// get the number of required dates for the question
				int numberOfRequiredDates = question.getNumberOfUserAnswersRequired();

				// initialize list to store the correct dates for the question
				List<String> correctDates = new ArrayList<>();

				// add the correct dates to the list
				for (int i = 0; i < numberOfRequiredDates; i++) {

					// get the correct date and validate the date until the number of correct dates
					// is reached
					while (true) {
						String date = inputParser
								.getStringInput(String.format("Enter the new correct date #%d:", i + 1));

						if (((ValidDate) question).validateDate(date)) {
							correctDates.add(date);
							break;
						}
					}
				}

				// set the new correct dates for the question
				setCorrectAnswerByQuestionNumber(questionNumber, correctDates);
			}
		}
	}
}
