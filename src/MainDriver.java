import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainDriver {

	// path to the surveys without responses
	String surveysDirectoryPath = "Output" + File.separator + "Surveys";

	// path to the tests without responses
	String testsDirectoryPath = "Output" + File.separator + "Tests";

	// path to the answers of the surveys
	String surveysWithAnswersDirectoryPath = "Output" + File.separator + "SurveysWithAnswers";

	// path to the user answers of the tests
	String testsWithAnswersDirectoryPath = "Output" + File.separator + "TestsWithAnswers";

	InputParser inputParser; // store the input parser
	Survey questionnaire; // store the current questionnaire (either test or survey)

	public MainDriver(InputParser inputParser) {
		this.inputParser = inputParser;
	}

	public static void main(String[] args) {

		// initialize InputParser and MainDriver
		MainDriver driver = new MainDriver(new InputParser());

		while (true) {

			// get the questionnaire type from user
			int questionnaireTypeChoice = driver.inputParser.getQuestionnaireTypeChoice();
			System.out.println();

			if (questionnaireTypeChoice == 3) { // quit
				break;
			}

			while (true) {

				// if the user chooses 1, then they selected survey
				boolean isSurvey = questionnaireTypeChoice == 1;

				int manageQuestionnaireChoice; // store the user's choice on how to manage the questionnaire

				// keep prompt user to manage questionnaire until they choose the quit option
				if (isSurvey) {
					manageQuestionnaireChoice = driver.inputParser.getSurveyMenuChoice();

					if (manageQuestionnaireChoice == 8) {
						break;
					}

				} else {
					manageQuestionnaireChoice = driver.inputParser.getTestMenuChoice();

					if (manageQuestionnaireChoice == 10) {
						break;
					}
				}

				System.out.println();

				switch (manageQuestionnaireChoice) {
				case 1:
					driver.handleCreatingQuestionnaire(isSurvey); // create

					break;
				case 2:
					driver.handleDisplayingQuestionnaire(isSurvey); // display
					break;
				case 3:
					if (isSurvey) {
						driver.handleLoadingQuestionnaire(true); // load survey
					} else {
						driver.handleDisplayingTestWithCorrectAnswers(); // display test with correct answers
					}

					break;
				case 4:

					if (isSurvey) {
						driver.handleSavingQuestionnaire(true); // save survey
					} else {
						driver.handleLoadingQuestionnaire(false); // load test
					}

					break;
				case 5:

					if (isSurvey) {
						driver.handleTakingQuestionnaire(true); // take survey
					} else {
						driver.handleSavingQuestionnaire(false); // save test
					}

					break;
				case 6:

					if (isSurvey) {
						driver.handleModifyingQuestionnaire(true); // modify survey
					} else {
						driver.handleTakingQuestionnaire(false); // take test
					}

					break;
				case 7:

					if (isSurvey) {
						driver.handleTabulatingQuestionnaire(true); // tabulate survey
					} else {
						driver.handleModifyingQuestionnaire(false); // modify test
					}

					break;
				case 8:

					driver.handleTabulatingQuestionnaire(false); // tabulate test

					break;
				case 9:
					driver.handleGradingTest(); // grade test
					break;
				}

			}

		}
	}

	private void handleGradingTest() {

		// verify the directory of the tests and tests with answers and then load the
		// answers to grade

		// directory with the tests files
		File directory = new File(testsDirectoryPath);

		String filePath, testName;
		File[] testsFiles;

		// check if the directory exists
		if (directory.isDirectory()) {

			// get all the tests files in the directory
			testsFiles = directory.listFiles();

			// if no files, print error
			if (testsFiles == null || testsFiles.length == 0) {
				System.out.println("No existing tests found.\n");
				return;
			}

			// display the menu of all the available files and get user choice for the test
			filePath = inputParser.getFileSelection("Please select an existing test to grade:", testsFiles);

			// use the path of the test to get its name
			File testFile = new File(filePath);
			testName = testFile.getName();

		} else {
			System.out.println("The directory containing the tests does not exist.\n");
			return;
		}

		// directory with the test answers
		directory = new File(testsWithAnswersDirectoryPath);

		File[] testWithAnswersFiles;

		// check if the directory exists
		if (directory.isDirectory()) {

			// get all the files in the directory that start with the test name
			testWithAnswersFiles = directory.listFiles((dir, name) -> name.startsWith(testName));

			// if no files with the tests and answers exist, print the error message and
			// return false
			if (testWithAnswersFiles == null || testWithAnswersFiles.length == 0) {
				System.out.println("No available response sets founds.");
				return;
			}

			// display the menu of all the available files and get user choice
			filePath = inputParser.getFileSelection("Please select an existing response set:", testWithAnswersFiles);

			// deserialize the chosen filename to get the questionnaire (test) response
			questionnaire = Output.deserialize(Test.class, filePath);

			// check if the questionnaire (test) response is loaded
			if (questionnaire != null) {

				// grade the test
				((Test) questionnaire).grade();

				// unload the test
				questionnaire = null;

			} else {
				System.out.println("The response set was unable to be loaded.");
			}
		} else {
			System.out.println("The directory containing the response sets does not exist.");
		}

		System.out.println();

	}

	private void handleDisplayingTestWithCorrectAnswers() {
		// display a test with the correct answers for each question

		// check if questionnaire (test) is loaded
		if (questionnaire == null) {
			System.out.println("You must have a test loaded in order to display it with the correct answers.\n");
			return;
		}

		// display the test and all of its questions with the correct answers
		((Test) questionnaire).displayWithCorrectAnswers();
	}

	private void handleLoadingQuestionnaire(boolean isSurvey) {

		// verify the directory of the questionnaires and then load

		File directory;

		// change the directory based on whether the questionnaire is survey or test
		if (isSurvey) {
			directory = new File(surveysDirectoryPath);
		} else {
			directory = new File(testsDirectoryPath);
		}

		String filePath;
		File[] questionnaireFiles;

		// check if the directory exists
		if (directory.isDirectory()) {

			// get all the files in the directory
			questionnaireFiles = directory.listFiles();

			// if no files, print error
			if (questionnaireFiles == null || questionnaireFiles.length == 0) {
				System.out.println("No available files to load.");
				return;
			}

			// display the menu of all the available files and get user choice
			filePath = inputParser.getFileSelection("Please select a file to load:", questionnaireFiles);

			// deserialize the chosen filename to get the questionnaire
			if (isSurvey) {
				questionnaire = Output.deserialize(Survey.class, filePath);
			} else {
				questionnaire = Output.deserialize(Test.class, filePath);
			}

			// check if the questionnaire is loaded
			if (questionnaire != null) {
				System.out.printf("The %s was loaded successfully.\n", isSurvey ? "survey" : "test");
				questionnaire.setInputParser(inputParser);
			} else {
				System.out.printf("The %s was unable to be loaded.\n", isSurvey ? "survey" : "test");
			}
		} else {
			System.out.printf("The directory containing the %s does not exist.\n", isSurvey ? "surveys" : "tests");
		}

		System.out.println();
	}

	private void handleSavingQuestionnaire(boolean isSurvey) {

		// check if questionnaire is loaded
		if (questionnaire == null) {
			System.out.printf("You must have a %s loaded in order to save it.\n\n", isSurvey ? "survey" : "test");
			return;
		}

		// clear all user answers from all questions so that the questionnaire can be
		// saved as empty without responses/answers
		questionnaire.clearAllAnswers();

		// call Output to serialize the questionnaire and use the name of the
		// questionnaire as the filename
		if (isSurvey) {
			Output.serialize(Survey.class, questionnaire, surveysDirectoryPath + File.separator,
					questionnaire.getName().replaceAll("[^a-zA-Z0-9-.]", "_"));
		} else {
			Output.serialize(Test.class, questionnaire, testsDirectoryPath + File.separator,
					questionnaire.getName().replaceAll("[^a-zA-Z0-9-.]", "_"));
		}
	}

	private void handleDisplayingQuestionnaire(boolean isSurvey) {

		// check if questionnaire is loaded
		if (questionnaire == null) {
			System.out.printf("You must have a %s loaded in order to display it.\n\n", isSurvey ? "survey" : "test");
			return;
		}

		// display the questionnaire and all of its questions
		questionnaire.display();
	}

	public void handleCreatingQuestionnaire(boolean isSurvey) {

		// handle creation differently depending on whether the questionnaire should be
		// a survey or test
		if (isSurvey) {
			// create new survey object with the survey name provided by the user
			questionnaire = new Survey(inputParser.getStringInput("Enter the name of the survey:"), inputParser);
		} else {
			// create new test object with the test name provided by the user
			questionnaire = new Test(inputParser.getStringInput("Enter the name of the test:"), inputParser);
		}

		// create the questions in the questionnaire
		questionnaire.createQuestions();
	}

	public void handleTakingQuestionnaire(boolean isSurvey) {

		// check if questionnaire is loaded
		if (questionnaire == null) {
			System.out.printf("You must have a %s loaded in order to take it.\n\n", isSurvey ? "survey" : "test");
			return;
		}

		// clear all answers from all questions from the previous questionnaire taking
		// session
		questionnaire.clearAllAnswers();

		// take the questionnaire
		questionnaire.take();

		// ask if the user wants to save their answers
		if (inputParser.getYesNoInput("Do you want to save your answers? (Yes/No)")) {

			// if the user wants to save their answer, then serialize the appropriate
			// questionnaire object
			if (isSurvey) {
				// serialize the survey object with the answers
				Output.serialize(Survey.class, questionnaire, surveysWithAnswersDirectoryPath + File.separator,
						questionnaire.getName().replaceAll("[^a-zA-Z0-9-.]", "_") + "_Answers_"
								+ new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date()));
			} else {
				// serialize the test object with the answers
				Output.serialize(Test.class, questionnaire, testsWithAnswersDirectoryPath + File.separator,
						questionnaire.getName().replaceAll("[^a-zA-Z0-9-.]", "_") + "_Answers_"
								+ new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date()));
			}

		}

	}

	public void handleModifyingQuestionnaire(boolean isSurvey) {

		// check if questionnaire is loaded
		if (questionnaire == null) {
			System.out.printf("You must have a %s loaded in order to modify it.\n\n", isSurvey ? "survey" : "test");
			return;
		}

		// clear questionnaire answers before modifying questionnaire
		questionnaire.clearAllAnswers();

		// modify the questionnaire
		questionnaire.modify();
	}

	public void handleTabulatingQuestionnaire(boolean isSurvey) {

		// verify the directory of the questionnaires and responses
		// and then load the responses to tabulate them for a survey

		File directory;

		// directory with the questionnaire files
		if (isSurvey) {
			directory = new File(surveysDirectoryPath);
		} else {
			directory = new File(testsDirectoryPath);
		}

		String filePath, questionnaireName;
		File[] questionnaireFiles;

		// check if the directory exists
		if (directory.isDirectory()) {

			// get all the questionnaire files in the directory
			questionnaireFiles = directory.listFiles();

			// if no files, print error
			if (questionnaireFiles == null || questionnaireFiles.length == 0) {
				System.out.printf("No existing %s found.\n\n", isSurvey ? "surveys" : "tests");
				return;
			}

			// display the menu of all the available files and get user choice for the
			// questionnaire
			filePath = inputParser.getFileSelection(
					String.format("Please select an existing %s to tabulate:", isSurvey ? "survey" : "test"),
					questionnaireFiles);

			// use the path of the questionnaire to get its name
			File questionnaireFile = new File(filePath);
			questionnaireName = questionnaireFile.getName();

			// deserialize the chosen filename to get the questionnaire
			if (isSurvey) {
				questionnaire = Output.deserialize(Survey.class, filePath);
			} else {
				questionnaire = Output.deserialize(Test.class, filePath);
			}

			// check if the questionnaire is loaded
			if (questionnaire != null) {
				questionnaire.setInputParser(inputParser);
			} else {
				System.out.printf("The %s was unable to be loaded.\n", isSurvey ? "survey" : "test");
				return;
			}

		} else {
			System.out.printf("The directory containing the %s does not exist.\n\n", isSurvey ? "surveys" : "tests");
			return;
		}

		// directory with the questionnaire responses
		if (isSurvey) {
			directory = new File(surveysWithAnswersDirectoryPath);
		} else {
			directory = new File(testsWithAnswersDirectoryPath);
		}

		File[] questionnaireWithAnswersFiles;

		// check if the directory exists
		if (directory.isDirectory()) {

			// get all the files in the directory that start with the questionnaire name
			questionnaireWithAnswersFiles = directory.listFiles((dir, name) -> name.startsWith(questionnaireName));

			// if no files with the questionnaire's responses exist, print the error
			// message and return false
			if (questionnaireWithAnswersFiles == null || questionnaireWithAnswersFiles.length == 0) {
				System.out.println("No available response sets found. Unable to tabulate.");
				return;
			}

			// store all questionnaire responses for this specific questionnaire that was
			// selected
			List<Survey> allQuestionnaires = new ArrayList<>();
			Survey currentQuestionnaire;

			// for each questionnaire response
			for (File questionnaireWithAnswersFile : questionnaireWithAnswersFiles) {

				// deserialize the questionnaire response set to get the answers
				if (isSurvey) {
					currentQuestionnaire = Output.deserialize(Survey.class, questionnaireWithAnswersFile.getPath());
				} else {
					currentQuestionnaire = Output.deserialize(Test.class, questionnaireWithAnswersFile.getPath());
				}

				// check if the response is loaded
				if (currentQuestionnaire != null) {
					// add response to the list
					allQuestionnaires.add(currentQuestionnaire);
				} else {
					System.out.printf("The response %s was unable to be loaded. Skipping this response.\n",
							questionnaireWithAnswersFile.getName());

				}

			}

			System.out.println();

			// tabulate all the responses for the questionnaire
			questionnaire.tabulate(allQuestionnaires);

			// unload the questionnaire
			questionnaire = null;

		} else {
			System.out.println("The directory containing the response sets does not exist.");
		}

		System.out.println();

	}
}
