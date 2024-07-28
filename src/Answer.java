import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Answer implements Serializable {
	private static final long serialVersionUID = 1L;

	// string to store the answer
	private List<String> userAnswers;

	public Answer() {
		this.userAnswers = new ArrayList<>();
	}

	// display the user answer
	public void displayAnswer() {
		for (String userAnswer : userAnswers) {
			System.out.println(userAnswer);
		}
	}

	// get the user answer
	public List<String> getUserAnswers() {
		return userAnswers;
	}

	// set the user answer
	public void setUserAnswer(List<String> userAnswers) {
		this.userAnswers = userAnswers;
	}

	// add a user answer
	public void addUserAnswer(String answer) {
		this.userAnswers.add(answer);
	}

	// get the number of responses/answers stored in the list
	public int getNumberOfUserAnswers() {
		return userAnswers.size();
	}

	// clear all answers from the list
	public void clearAnswers() {
		userAnswers.clear();
	}

	// compare two answers to see if they are the same
	public boolean compare(Answer answers) {

		// get the user's answers (make sure it is sorted, trimmed,
		// and lowercase to ensure that the comparison is fair)
		List<String> answers1 = userAnswers.stream().map(String::trim).map(String::toLowerCase)
				.sorted(String::compareToIgnoreCase).collect(Collectors.toList());

		// get the input answers (make sure it is sorted, trimmed,
		// and lowercase to ensure that the comparison is fair)
		List<String> answers2 = answers.getUserAnswers().stream().map(String::trim).map(String::toLowerCase)
				.sorted(String::compareToIgnoreCase).collect(Collectors.toList());

		// compare the two answers and return boolean
		return answers1.equals(answers2);
	}
}
