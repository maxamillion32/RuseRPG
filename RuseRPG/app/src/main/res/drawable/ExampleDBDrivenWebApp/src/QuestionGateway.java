import java.util.ArrayList;


public class QuestionGateway {
	public static ArrayList<Question> mock;
	static {
		mock = new ArrayList<Question>();
		mock.add(new Question(1,"What experiences have you had with functional programming?"));
		mock.add(new Question(2,"Are you reasonably confident you can complete the assignment now?"));
		mock.add(new Question(3,"What other Java topics would you like to discuss this semester?"));
	}
}
